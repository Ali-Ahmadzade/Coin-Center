package test.example.coincenter.main.coinActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioButton
import test.example.coincenter.databinding.CoinActivityBinding

class CoinActivity : AppCompatActivity() {
    lateinit var binding :CoinActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = CoinActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chartLayoutCoin.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val rb = group.getChildAt(i) as RadioButton
                val isSelected = rb.id == checkedId
                rb.isChecked = isSelected

                val scale = if (isSelected) 1.05f else 1f
                rb.animate()
                    .scaleX(scale)
                    .scaleY(scale)
                    .setDuration(150)
                    .start()
            }
        }

        binding.swipeRefreshCoin.setOnRefreshListener {

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshCoin.isRefreshing = false
            } , 1500 )
        }

    }
}