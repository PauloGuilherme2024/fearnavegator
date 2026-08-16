# Métrica DANO/MINUTO

O objetivo do aplicativo é comparação rápida, não simular vida/armadura/resistência de um inimigo específico.

## Fórmula inicial

`dano_esperado_golpe = dano_total_arsenal × (1 + chance_critica × (multiplicador_critico - 1))`

`golpes_segundo = fator_ciclo_stance × velocidade_ataque`

`dano_minuto = dano_esperado_golpe × golpes_segundo × 60`

Quando houver metadados de stance:

`fator_ciclo_stance = impactos_do_ciclo / duração_base_do_ciclo`

Sem metadados de stance, o fator é 1,0 e o resultado deve ser entendido como índice comparativo baseado no Arsenal, não como medição física de golpes reais.

## Status

Status não recebe um valor universal artificial. Slash, Heat, Toxin etc. têm efeitos diferentes e dependem do alvo/tempo. A versão inicial apenas sinaliza status elevado.

## Ataque pesado

O app mostra o dano pesado esperado com crítico separadamente. Combo/Tennokai serão incorporados somente quando puderem ser determinados de maneira confiável pela build ou por uma opção explícita do usuário.

## Referências de implementação

As notas oficiais da Digital Extremes confirmam que Attack Speed aumenta a animação de ataque melee e que o Arsenal expõe Attack Speed, Critical Chance, Critical Multiplier, Status, tipos de dano e Total Damage. A base de regras deve ser versionada porque atualizações do Warframe alteram sistemas e balanceamento.
