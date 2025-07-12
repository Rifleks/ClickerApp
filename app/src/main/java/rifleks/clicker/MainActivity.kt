package rifleks.clicker

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {
    // Основные переменные
    internal var clicks = 0
    internal var clickCooldown = 1000L
    internal var clickUpgradeLevel = 0
    internal var lastClickTime = 0L

    private fun calculateMangoPerClick(level: Int): Int {
        return when(level) {
            1 -> 1
            2 -> 2
            3 -> 4
            4 -> 6
            5 -> 8
            6 -> 10
            7 -> 12
            8 -> 15
            9 -> 20
            10 -> 25
            11 -> 30
            12 -> 40
            13 -> 50
            14 -> 65
            15 -> 85
            16 -> 110
            17 -> 150
            18 -> 200
            19 -> 300
            20 -> 400
            21 -> 600
            22 -> 850
            23 -> 1200
            24 -> 1500
            25 -> 2500
            else -> 1
        }
    }

    internal var mangoPerClick = 1
    internal var mangoClickLevel = 1
    internal val mangoClickPrices = listOf<Long>(
        0L,      // Уровень 0 (не используется)
        100L,     // Уровень 1 -> 2
        400L,     // Уровень 2 -> 3
        1000L,    // Уровень 3 -> 4
        1800L,    // Уровень 4 -> 5
        3600L,    // Уровень 5 -> 6
        5200L,   // Уровень 6 -> 7
        8000L,   // Уровень 7 -> 8
        12000L,   // Уровень 8 -> 9
        17000L,   // Уровень 9 -> 10
        25000L,  // Уровень 10 -> 11
        35000L,  // Уровень 11 -> 12
        45000L,  // Уровень 12 -> 13
        55000L, // Уровень 13 -> 14
        70000L, // Уровень 14 -> 15
        100000L, // Уровень 15 -> 16
        125000L, // Уровень 16 -> 17
        150000L, // Уровень 17 -> 18
        200000L, // Уровень 18 -> 19
        250000L, // Уровень 19 -> 20
        300000L, // Уровень 20 -> 21
        350000L, // Уровень 21 -> 22
        400000L, // Уровень 22 -> 23
        450000L, // Уровень 23 -> 24
        500000L  // Уровень 24 -> 25
    )

    private lateinit var mangoCounter: TextView
    private lateinit var prefs: SharedPreferences
    internal val handler = Handler(Looper.getMainLooper())
    private lateinit var clickerTab: ImageButton
    private lateinit var shopTab: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("ClickerPrefs", MODE_PRIVATE)
        loadGame()

        initViews()
        setupTabs()
        showClickerFragment()
    }

    private fun initViews() {
        mangoCounter = findViewById(R.id.mangoCounter)
        clickerTab = findViewById(R.id.clickerTab)
        shopTab = findViewById(R.id.shopTab)
        updateMangoCounter()
    }

    private fun setupTabs() {
        clickerTab.setOnClickListener {
            showClickerFragment()
            setActiveTab(true)
        }

        shopTab.setOnClickListener {
            showShopFragment()
            setActiveTab(false)
        }
    }

    private fun showClickerFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, ClickerFragment.newInstance())
            .commit()

        clickerTab.setBackgroundColor(getColor(android.R.color.holo_green_dark))
        shopTab.setBackgroundColor(getColor(android.R.color.transparent))
    }

    private fun showShopFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, ShopFragment.newInstance())
            .commit()

        shopTab.setBackgroundColor(getColor(android.R.color.holo_green_dark))
        clickerTab.setBackgroundColor(getColor(android.R.color.transparent))
    }

    private fun setActiveTab(isClickerTabActive: Boolean) {
        clickerTab.alpha = if (isClickerTabActive) 1f else 0.5f
        shopTab.alpha = if (!isClickerTabActive) 1f else 0.5f
    }

    internal fun updateMangoCounter() {
        mangoCounter.text = "$clicks🥭"
    }

    internal fun buyClickUpgrade() {
        val price = 25 * Math.pow(2.5, clickUpgradeLevel.toDouble()).toInt()
        if (clicks >= price && clickCooldown > 0) {
            clicks -= price
            clickUpgradeLevel++
            clickCooldown = (clickCooldown - 200).coerceAtLeast(0)
            updateMangoCounter()
            saveGame()
        }
    }

    internal fun handleClick() {
        clicks += mangoPerClick
        updateMangoCounter()
        saveGame()
    }

    internal fun buyMangoClickUpgrade(): Boolean {
        if (mangoClickLevel >= 25) return false

        val price = mangoClickPrices[mangoClickLevel]
        if (clicks >= price) {
            clicks -= price.toInt() // Важное исправление: преобразование Long в Int
            mangoClickLevel++
            mangoPerClick = calculateMangoPerClick(mangoClickLevel)
            updateMangoCounter()
            saveGame()
            return true
        }
        return false
    }

    private fun loadGame() {
        clicks = prefs.getInt("clicks", 0)
        clickCooldown = prefs.getLong("clickCooldown", 1000L)
        clickUpgradeLevel = prefs.getInt("clickUpgradeLevel", 0)
        mangoPerClick = prefs.getInt("mangoPerClick", 1)
        mangoClickLevel = prefs.getInt("mangoClickLevel", 1)
    }

    internal fun saveGame() {
        prefs.edit()
            .putInt("clicks", clicks)
            .putLong("clickCooldown", clickCooldown)
            .putInt("clickUpgradeLevel", clickUpgradeLevel)
            .putInt("mangoPerClick", mangoPerClick)
            .putInt("mangoClickLevel", mangoClickLevel)
            .apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}