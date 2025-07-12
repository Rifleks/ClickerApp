package rifleks.clicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ShopFragment : Fragment() {
    private lateinit var upgradeButton: Button
    private lateinit var cooldownText: TextView
    private lateinit var mangoClickUpgradeButton: Button
    private lateinit var mangoClickInfoText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upgradeButton = view.findViewById(R.id.upgradeButton)
        cooldownText = view.findViewById(R.id.cooldownText)
        mangoClickUpgradeButton = view.findViewById(R.id.mangoClickUpgradeButton)
        mangoClickInfoText = view.findViewById(R.id.mangoClickInfoText)

        val mainActivity = activity as? MainActivity ?: return
        updateViews(mainActivity)

        upgradeButton.setOnClickListener {
            if (mainActivity.clickCooldown <= 0) {
                Toast.makeText(context, "Макс. скорость", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = 25 * Math.pow(2.0, mainActivity.clickUpgradeLevel.toDouble()).toInt()
            if (mainActivity.clicks >= price) {
                mainActivity.buyClickUpgrade()
                updateViews(mainActivity)
            } else {
                Toast.makeText(context, "Нужно больше манго", Toast.LENGTH_SHORT).show()
            }
        }

        mangoClickUpgradeButton.setOnClickListener {
            if (mainActivity.mangoClickLevel >= 25) {
                Toast.makeText(context, "Макс. уровень", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mainActivity.buyMangoClickUpgrade()) {
                updateViews(mainActivity)
            } else {
                Toast.makeText(context, "Нужно больше манго", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateViews(mainActivity: MainActivity) {
        // Обновление информации о кулдауне
        val cooldown = "%.1f".format(mainActivity.clickCooldown / 1000.0)
        cooldownText.text = "Задержка: ${cooldown}сек"

        val price = 25 * Math.pow(2.0, mainActivity.clickUpgradeLevel.toDouble()).toInt()
        upgradeButton.text = "Улучшить ($price🥭)"

        if (mainActivity.clickCooldown <= 0) {
            upgradeButton.text = "МАКСИМУМ"
            upgradeButton.isEnabled = false
        }

        // Обновление информации о "Манго за клик"
        val nextLevel = mainActivity.mangoClickLevel + 1
        val mangoClickPrice = if (nextLevel <= 25) mainActivity.mangoClickPrices[mainActivity.mangoClickLevel] else 0
        mangoClickUpgradeButton.text = "Прокачать ($mangoClickPrice🥭)"

        mangoClickInfoText.text = "Уровень ${mainActivity.mangoClickLevel}, за клик ${mainActivity.mangoPerClick}🥭"

        if (mainActivity.mangoClickLevel >= 25) {
            mangoClickUpgradeButton.text = "МАКСИМУМ"
            mangoClickUpgradeButton.isEnabled = false
        }
    }

    companion object {
        fun newInstance() = ShopFragment()
    }
}