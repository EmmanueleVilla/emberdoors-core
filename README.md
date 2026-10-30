# Emberdoors Core ⚔️🚪

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-purple.svg)](https://kotlinlang.org)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

**Emberdoors Core** è il cuore logico, open-source e multipiattaforma di **Emberdoors**, un dungeon crawler roguelike a turni.

Questo repository contiene l'engine di gioco (modelli, regole di combattimento e generazione dungeon), un client interattivo da terminale e uno strumento di simulazione IA per il bilanciamento matematico delle statistiche.

---

## 📦 Moduli

| Modulo | Tipo | Descrizione |
|---|---|---|
| [`:engine`](engine/) | Kotlin Multiplatform | Regole di combattimento (`CombatEngine`), formule di danno/difesa, modelli di entità (`HeroStats`, `Enemy`, `Door`) e store Redux. Zero dipendenze da Android o UI. |
| [`:terminal`](terminal/) | Kotlin JVM Application | Client console interattivo: esplora le porte, affronta mostri a turni e raccogli tesori direttamente da terminale o Android Studio. |
| [`:balancer`](balancer/) | Kotlin JVM Application | Simulatore headless guidato da un agente IA (`BalancerAiAgent`): simula 1.000+ run per analizzare letalità nemici, durata scontri e win-rate. |

---

## 🚀 Guida Rapida (Quick Start)

### Prerequisiti
- **JDK 21** o superiore
- **Android Studio** (Koala / Ladybug o versioni recenti) oppure **IntelliJ IDEA** / Terminale

### 1. Giocare al gioco da Terminale
Puoi avviare la sessione di gioco interattiva da riga di comando:

```bash
./gradlew :terminal:run -q --console=plain
```

Oppure in **Android Studio**:
1. Apri il file `terminal/src/main/kotlin/com/shadowings/emberdoors/terminal/Main.kt`.
2. Clicca sull'icona **▶ Play** (Run) accanto a `fun main()`.
3. Interagisci tramite il pannello di console con i comandi numerici (`1-3` per scegliere la porta, `1-5` per le azioni di combattimento, `q` per abbandonare).

### 2. Eseguire la Simulazione IA di Bilanciamento
Per testare il bilanciamento matematico ed eseguire 1.000 partite simulate dall'agente IA:

```bash
./gradlew :balancer:run -q --console=plain
```

Output di esempio generato dall'IA:
```text
====================================================
   🤖  E M B E R D O O R S  -  B A L A N C E R  🤖
====================================================
RISULTATI SIMULAZIONE (1000 partite):
----------------------------------------------------
Piano Medio Raggiunto:      12.40
Piano Massimo Raggiunto:    27
Oro Medio Guadagnato:       185.30
Durata Media Combattimento: 3.12 turni

Statistiche Letalità Mostri (Cause di Morte):
 - Orco Furioso              :  412 morti ( 41.2% delle partite)
 - Demone Minore             :  285 morti ( 28.5% delle partite)
 - Guerriero Scheletro       :  180 morti ( 18.0% delle partite)
 - Vipera Velenosa           :   88 morti (  8.8% delle partite)
 - Imp Infernale             :   35 morti (  3.5% delle partite)
----------------------------------------------------
```

---

## 🛠️ Build e Test

Per verificare la compilazione ed eseguire i test di tutti i moduli:

```bash
./gradlew check
```

---

## 🤝 Come Contribuire

I contributi della community sono calorosamente benvenuti!
- Vuoi aggiungere nuovi nemici, elementi magici o abilità? Consulta le linee guida in [CONTRIBUTING.md](CONTRIBUTING.md).
- Per favore rispetta il nostro [Code of Conduct](CODE_OF_CONDUCT.md).
- Per segnalazioni di sicurezza, consulta [SECURITY.md](SECURITY.md).

---

## 📄 Licenza

Questo progetto è rilasciato sotto licenza libera e permissiva **MIT**. Consulta il file [LICENSE](LICENSE) per tutti i dettagli.
