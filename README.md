# Emberdoors Core ⚔️🚪

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-purple.svg)](https://kotlinlang.org)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

**Emberdoors Core** is the open-source, multiplatform core logic and game engine for **Emberdoors**.

---

## 📦 Modules

| Module | Type | Description |
|---|---|---|
| [`:engine`](engine/) | Kotlin Multiplatform | Core game engine, domain models, and state management. Zero Android or UI dependencies. |
| [`:terminal`](terminal/) | Kotlin JVM Application | Console / terminal client for local testing and gameplay. |
| [`:balancer`](balancer/) | Kotlin JVM Application | Headless simulation tool for statistical balancing. |

---

## 🚀 Quick Start

### Prerequisites
- **JDK 21** or higher
- **Android Studio** or **IntelliJ IDEA** / Terminal

### Build & Test

To verify compilation and run tests across all modules:

```bash
./gradlew check
```

---

## 🤝 Contributing

Community contributions are warmly welcome!
- Check the guidelines in [CONTRIBUTING.md](CONTRIBUTING.md).
- Please adhere to our [Code of Conduct](CODE_OF_CONDUCT.md).
- For security issues, refer to [SECURITY.md](SECURITY.md).

---

## 📄 License

This project is licensed under the permissive **MIT** license. See the [LICENSE](LICENSE) file for full details.
