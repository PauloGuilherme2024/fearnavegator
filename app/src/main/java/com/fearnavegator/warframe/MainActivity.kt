package com.fearnavegator.warframe

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = darkColorScheme()) { AnalyzerScreen() } }
    }
}

@Composable
fun AnalyzerScreen() {
    var current by remember { mutableStateOf<Pair<WeaponStats, DamageResult>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val history = remember { mutableStateListOf<Pair<WeaponStats, DamageResult>>() }
    val context = androidx.compose.ui.platform.LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val image = InputImage.fromFilePath(context, uri)
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(image)
                .addOnSuccessListener { vision ->
                    val stats = WarframeOcrParser.parse(vision.text)
                    if (stats == null) error = "Não consegui ler todos os atributos. Tente uma foto mais reta e próxima."
                    else {
                        val result = DamageEngine.calculate(stats)
                        current = stats to result
                        history.removeAll { it.first.name == stats.name }
                        history.add(stats to result)
                        error = null
                    }
                }.addOnFailureListener { error = "Falha ao analisar a imagem." }
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("FEAR NAVEGATOR", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("WARFRAME • DANO/MIN", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(28.dp))
            Button(onClick = { picker.launch("image/*") }, modifier = Modifier.fillMaxWidth()) { Text("📷  ANALISAR FOTO") }
            Spacer(Modifier.height(24.dp))
            current?.let { (weapon, result) ->
                Text(weapon.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("DANO / MINUTO", style = MaterialTheme.typography.labelLarge)
                Text(NumberFormat.getIntegerInstance(Locale("pt", "BR")).format(result.damagePerMinute), fontSize = 48.sp, fontWeight = FontWeight.Black)
                result.heavyExpectedDamage?.let { Text("Ataque pesado: ${NumberFormat.getIntegerInstance(Locale("pt", "BR")).format(it)}") }
                result.note?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (history.isNotEmpty()) {
                Spacer(Modifier.height(28.dp)); Text("RANKING", fontWeight = FontWeight.Bold)
                LazyColumn(Modifier.fillMaxWidth()) {
                    itemsIndexed(history.sortedByDescending { it.second.damagePerMinute }) { index, item ->
                        ListItem(headlineContent = { Text("${index + 1}º  ${item.first.name}") }, trailingContent = { Text(NumberFormat.getIntegerInstance(Locale("pt", "BR")).format(item.second.damagePerMinute), fontWeight = FontWeight.Bold) })
                    }
                }
            }
        }
    }
}
