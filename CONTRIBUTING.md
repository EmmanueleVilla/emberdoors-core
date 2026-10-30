# Linee Guida per la Contribuzione (Contributing Guide)

Grazie per l'interesse nel contribuire a **Emberdoors Core**! 🎉
Questo progetto è open source e accoglie contributi per espandere il motore di gioco, ottimizzare l'algoritmo di bilanciamento o arricchire l'interfaccia da terminale.

---

## 🧭 Principi di Progettazione di Emberdoors Core

1. **Zero UI nell'Engine**: Il modulo `:engine` deve rimanere privo di dipendenze verso Android SDK, Jetpack Compose o librerie grafiche.
2. **Determinismo e Testabilità**: Le regole di calcolo (combattimento, statistiche) devono essere coperte da test unitari.
3. **Licenza Permissiva**: Tutto il codice inviato sarà rilasciato sotto licenza MIT.

---

## 🛠️ Come Iniziare

1. Fai il **Fork** del repository su GitHub.
2. Clona il tuo fork in locale:
   ```bash
   git clone https://github.com/<tuo-username>/emberdoors-core.git
   cd emberdoors-core
   ```
3. Crea un branch semantico per le tue modifiche:
   ```bash
   git checkout -b feature/nuova-abilita-fulmine
   # oppure
   git checkout -b fix/correzione-parry
   ```

---

## 📐 Standard di Codice e Convenzioni

- Utilizziamo **Kotlin idiomatico** (immutabilità di default con `val` e data class, `when` esaustivi, sigilli `sealed interface`).
- Formattazione standard ufficiale Kotlin (`ktlint` / Android Studio formatting default).
- Messaggi di commit chiari e in formato Conventional Commits:
  - `feat: aggiunta supporto elemento Fulmine nelle statistiche`
  - `fix: corretto calcolo della riduzione danni con Difesa attiva`
  - `docs: aggiornato il README con le nuove statistiche del balancer`
  - `test: aggiunti unit test per il combat engine`

---

## 🧪 Esecuzione dei Test

Prima di aprire una Pull Request, assicurati che la build e i test passino senza errori:

```bash
./gradlew check
./gradlew :balancer:run -q --console=plain
```

---

## 📬 Invio di una Pull Request (PR)

1. Esegui il push delle modifiche sul tuo fork:
   ```bash
   git push origin feature/nome-feature
   ```
2. Apri una Pull Request verso il branch `main` del repository principale.
3. Compila il template della PR spiegando chiaramente le modifiche apportate e la motivazione.
4. I maintainer revisioneranno la PR e forniranno feedback.
