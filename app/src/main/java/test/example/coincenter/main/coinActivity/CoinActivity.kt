package test.example.coincenter.main.coinActivity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import test.example.coincenter.R
import test.example.coincenter.databinding.CoinActivityBinding
import test.example.coincenter.main.models.CoinsData

class CoinActivity : AppCompatActivity() {
    private var coinData: CoinsData? = null
    private lateinit var binding: CoinActivityBinding
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
            }, 1500)
        }

        loadData()
        loadCoin()

    }

    private fun loadData() {
        coinData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("dataToSend", CoinsData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("dataToSend")
        }
    }

    private fun loadCoin() {
        loadChartInfo()
        loadStatisticInfo()
    }

    private fun loadChartInfo() {
        if (coinData!!.name == "Tether" || coinData!!.name == "USDC") {
            binding.chartLayoutCoin.coinPriceCoin.text = "$1.00"
            binding.chartLayoutCoin.changePriceIconCoin.text = "⬤"
            binding.chartLayoutCoin.priceChangePercentCoin.setTextColor(
                ContextCompat.getColor(this , R.color.noChangePrice)
            )
            binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                ContextCompat.getColor(this, R.color.noChangePrice)
            )
            binding.chartLayoutCoin.priceChangeCoin.text = "$0.00"
            binding.chartLayoutCoin.priceChangePercentCoin.text = "0%"
        }else{
            binding.chartLayoutCoin.coinPriceCoin.text = "$"+coinData!!.currentPrice.toString()
            if (coinData!!.priceChange24h > 0){
                binding.chartLayoutCoin.changePriceIconCoin.text = "▲"
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.incPrice)
                )
            }else if(coinData!!.priceChange24h < 0){
                binding.chartLayoutCoin.changePriceIconCoin.text = "▼"
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.decPrice)
                )
            }else{
                binding.chartLayoutCoin.changePriceIconCoin.text = "⬤"
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.noChangePrice)
                )
            }
            binding.chartLayoutCoin.priceChangeCoin.text ="$"+ coinData!!.priceChange24h.toString()

            if (coinData!!.priceChange24h > 0){

                binding.chartLayoutCoin.priceChangePercentCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.incPrice)
                )
            }else if(coinData!!.priceChange24h < 0){
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.decPrice)
                )
            }else{
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.noChangePrice)
                )
            }
            val index = coinData!!.priceChangePercent24h.toString().indexOf(".")
            binding.chartLayoutCoin.priceChangePercentCoin.text = coinData!!.priceChangePercent24h.toString().substring(0 , index + 3)+"%"
        }
    }

    private fun loadStatisticInfo() {



        if (coinData!!.name == "Tether" || coinData!!.name == "USDC") {
            binding.statisticsLayoutCoin.high24H.text = "$1.00"
            binding.statisticsLayoutCoin.low24H.text = "$1.00"
            binding.statisticsLayoutCoin.change24H.text = "0.00%"
            binding.statisticsLayoutCoin.totalVolume.text = coinData!!.totalVolume.toString()
            binding.statisticsLayoutCoin.marketCapRank.text =
                coinData!!.circulatingSupply.toString()
            binding.statisticsLayoutCoin.highAllTime.text = "$1.00"
        } else {
            binding.statisticsLayoutCoin.high24H.text = "$" + coinData!!.high24h.toString()
            binding.statisticsLayoutCoin.low24H.text = "$" + coinData!!.low24h.toString()
            if (coinData!!.priceChange24h > 0) {
                binding.statisticsLayoutCoin.change24H.text =
                    "+" + coinData!!.priceChange24h.toString()+"$"
            } else if (coinData!!.priceChange24h < 0) {
                binding.statisticsLayoutCoin.change24H.text =
                    "-" + coinData!!.priceChange24h.toString()+" $"
            } else {
                binding.statisticsLayoutCoin.change24H.text = "$0"
            }
            val test = coinData!!.totalVolume.toFloat() / 1000000000
            val index = test.toString().indexOf(".")
            binding.statisticsLayoutCoin.totalVolume.text = "$" + test.toString().substring( 0 , index + 3 ) +" B"

            binding.statisticsLayoutCoin.marketCapRank.text = coinData!!.marketCapRank.toString()
            binding.statisticsLayoutCoin.highAllTime.text = "$"+coinData!!.allTimeHigh.toString()
        }


    }
}