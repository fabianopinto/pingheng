# Pínghéng (平衡, CryptoRebalance)

Pínghéng, 平衡 in Chinese, is a native Android application designed to automate portfolio rebalancing across multiple cryptocurrency assets. It replicates the logic of the [**"Crypto Trading v3"**](https://docs.google.com/spreadsheets/d/1TPZpIFP1YSo7a7DeSaFFYITx1WHFc7Ic8Sk8rNJpfEY/edit?usp=sharing&usp=drivesdk) spreadsheet, allowing users to maintain a target allocation for their crypto holdings.

## Features

- **Portfolio Dashboard:** View your current assets, their "Current Slice" vs. "Target Slice", and real-time valuation.
- **Trade Recommendations:** Automatically calculates "BUY" or "SELL" actions with exact quantities based on deviations (Delta) from your target allocation.
- **Dynamic Configuration:** Easily set up trading pairs, decimal precision, and target percentages (totaling 100%).
- **Real-Time Sync:** Integrates with the Binance API to fetch live prices and wallet balances.
- **Automated Updates:** Uses Android WorkManager to refresh data every 60 seconds in the background.
- **One-Click Execution:** Trigger rebalancing trades directly from the app.

## Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/)
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room)
- **Background Tasks:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- **API Integration:** Binance API

## Getting Started

### Prerequisites

- Android Studio Ladybug or newer.
- JDK 17+.
- A Binance API Key and Secret (with appropriate permissions for reading balances and placing trades).

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/fabianopinto/pingheng.git
   ```
2. Open the project in Android Studio.
3. (Coming Soon) Configure your Binance API credentials in the app settings.
4. Build and run the application on your device or emulator.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.
