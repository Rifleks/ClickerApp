package rifleks.clicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class ClickerFragment : Fragment() {
    private lateinit var clickButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_clicker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickButton = view.findViewById(R.id.clickButton)
        setupClickButton()
    }

    private fun setupClickButton() {
        val mainActivity = activity as? MainActivity ?: return

        clickButton.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - mainActivity.lastClickTime >= mainActivity.clickCooldown) {
                mainActivity.handleClick()
                mainActivity.lastClickTime = currentTime

                clickButton.isEnabled = false
                mainActivity.handler.postDelayed({
                    clickButton.isEnabled = true
                }, mainActivity.clickCooldown)
            }
        }
    }

    companion object {
        fun newInstance() = ClickerFragment()
    }
}