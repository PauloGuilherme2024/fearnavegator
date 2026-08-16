# Fear Navegator — Warframe Damage Analyzer

Aplicativo mobile Android para analisar fotos da tela de atributos de armas do Warframe e transformar os dados em uma comparação simples de **DANO/MINUTO**.

## Objetivo

Fotografar ou selecionar uma imagem da tela de atributos da arma. O aplicativo reconhece os atributos visíveis, calcula um valor comparável de dano sustentado e salva a build para formar um ranking pessoal.

## Resultado principal

- Nome da arma
- DANO/MINUTO (resultado principal)
- Ataque pesado (secundário)
- Uma observação curta quando crítico/status merecer destaque
- Posição no ranking das armas analisadas

## Arquitetura do cálculo

O motor mantém separados os dados reconhecidos da foto, dados públicos da arma, regras/fórmulas e o resultado mostrado ao usuário. Isso permite atualizar dados e fórmulas sem complicar a interface.

Para melee, não trataremos o número de Velocidade de Ataque do Arsenal ingenuamente como golpes por segundo: o cálculo poderá usar informação da classe/stance/animações quando necessária para produzir um número comparável.

## Dados do Warframe

A base será sincronizável a partir dos dados públicos oficiais do Warframe (incluindo ExportWeapons) e poderá receber metadados complementares necessários para melee/stances.

## Stack

- Android / Kotlin
- Jetpack Compose
- CameraX
- Google ML Kit Text Recognition
- Room/SQLite
- Retrofit/OkHttp para atualização opcional da base

## Privacidade

A análise das fotos será local sempre que possível. O aplicativo não lê memória do jogo, não automatiza comandos e não interfere no Warframe.

## Status

Projeto iniciado em 16/08/2026.
