To develop an Android equivalent of the **"Crypto Trading v3"** spreadsheet, you must replicate its three-tier architecture: **Data Ingestion**, **Strategic Configuration**, and **Portfolio Rebalancing Logic**.

### 1. Analysis of Current Features

* **Data Sheet (Backend/API Layer):**

	* **Functionality:** Acts as a real-time database. It stores hundreds of cryptocurrency pairs (e.g., ETHBTC, BTCUSDT) with their current prices and sync timestamps.

	* **Logic:** It tracks "Free" vs. "Locked" balances for assets like ADA, BNB, and ETH, likely imported via an exchange API (like Binance).

* **Setup Sheet (Configuration Layer):**

	* **Functionality:** Maps specific assets to their base and quote prices.

	* **Precision Management:** Uses "R¹", "R²", and "R³" columns, which represent decimal precision requirements for different trading pairs (essential for order execution).

* **Balance Sheet (Analytical/UI Layer):**

	* **Rebalancing Logic:** This is the "brain" of the app. It calculates the "Slice" (actual % of portfolio) against a target (e.g., 10% per asset).

	* **Trade Generation:** It calculates the "Delta" (difference) and outputs specific "BUY" or "SELL" recommendations and exact quantities (e.g., "Buy 18 XRP" or "Sell 6 ADA") to return the portfolio to its target 10% allocation.

### 2. Analysis of Scripts, Macros, and Triggers

While the raw data doesn't show the code, the UI elements (like the gear icon "⚙" in the Balance sheet) and the automated timestamping indicate the following underlying AppScript components:

* **API Fetcher (Trigger):** A time-driven trigger (likely every 1–5 minutes) that calls a crypto exchange API to update the **Data** sheet prices.

* **Trade Executor (Macro):** Clicking the gear icon "⚙" likely triggers a script that prepares or executes an API `POST` request to an exchange to perform the suggested trade.

* **Custom Functions:** Macros that calculate the total portfolio value in BTC/USD and distribute the "Slice" percentages dynamically.

### 3. Android App Development Specifications

To replicate this as a native Android app, you will need:

* **Architecture:** MVVM (Model-View-ViewModel).

* **Data Layer:** Retrofit for API calls (Binance/CoinGecko) and Room Database for local storage of "Setup" parameters.

* **Logic Layer:** A rebalancing engine class that performs the "Delta" calculations found in the **Balance** sheet.

* **UI Layer:** A `RecyclerView` to show holdings, with color-coded indicators for "BUY" (Green) and "SELL" (Red).

---

### 4. Complete Development Prompt

**Subject:** Technical Specification for "CryptoRebalance" Android Application

**Project Objective:**
Develop a native Android application (Kotlin/Jetpack Compose) that replicates the logic of the "Crypto Trading v3" spreadsheet. The app must automate portfolio rebalancing across 10+ assets based on user-defined target percentages.

**Key Functional Requirements:**

1. **Dashboard (The "Balance" View):**

	* Display a list of assets (BTC, ETH, LTC, etc.).

	* Calculate "Current Slice" vs. "Target Slice" (default 10%).

	* Display a "Trade Recommendation" column that shows "BUY" or "SELL" with exact quantities based on the deviation (Delta) from the target.

2. **Configuration (The "Setup" View):**

	* Allow users to set trading pairs and decimal precision for orders.

	* Input/Edit target allocation percentages (Total must equal 100%).

3. **Real-Time Sync (The "Data" View):**

	* Integrate a Crypto Exchange API (e.g., Binance) to fetch live prices and wallet balances.

	* Implement a Background Worker (WorkManager) to refresh data every 60 seconds.

4. **Execution Engine:**

	* Provide an "Execute" button (equivalent to the spreadsheet's ⚙ icon) that triggers an API call to place the suggested rebalancing trades.

**Technical Stack:**

* **Language:** Kotlin.
* **UI:** Jetpack Compose.
* **Networking:** Retrofit + OkHttp.
* **Local DB:** Room (to store portfolio history and setup).
* **API Integration:** Binance API (for price and trade execution).
