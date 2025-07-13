package test.example.coincenter.main.coinActivity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import test.example.coincenter.R
import test.example.coincenter.databinding.CoinActivityBinding
import test.example.coincenter.main.apiManager.ApiManager
import test.example.coincenter.main.models.ChartData
import test.example.coincenter.main.models.CoinData

class CoinActivity : AppCompatActivity() {
    private val apiManager = ApiManager()
    private lateinit var dataCoin :CoinData.Data
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
                loadCoin()
                binding.swipeRefreshCoin.isRefreshing = false
            }, 1500)
        }
        binding.chartLayoutCoin.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {

                R.id.range12H -> {}
                R.id.range1D -> {}
                R.id.range1W -> {}
                R.id.range1M -> {}
                R.id.range3M -> {}
                R.id.range1Y -> {}
                R.id.rangeAll -> {}

            }
        }

    }

    override fun onResume() {
        super.onResume()
        loadData()
        binding.includeToolBarCoin.toolBarMain.title = dataCoin.coinInfo.fullName
        loadCoin()
    }

    private fun loadData() {

        dataCoin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("selectedCoinData", CoinData.Data::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("selectedCoinData") as? CoinData.Data
        }!!


    }

    private fun loadCoin() {
        loadChartInfo()
        loadStatisticInfo()
    }

    private fun loadChartInfo() {

        if (dataCoin.coinInfo.name == "USDC" || dataCoin.coinInfo.name == "USDT") {

            binding.chartLayoutCoin.coinPriceCoin.text = "$ 1.00"
            binding.chartLayoutCoin.changePriceIconCoin.text = "⬤"
            binding.chartLayoutCoin.priceChangePercentCoin.setTextColor(
                ContextCompat.getColor(this, R.color.noChangePrice)
            )
            binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                ContextCompat.getColor(this, R.color.noChangePrice)
            )
            binding.chartLayoutCoin.priceChangeCoin.text = "$0.00"
            binding.chartLayoutCoin.priceChangePercentCoin.text = "0%"
        }else{
            binding.chartLayoutCoin.coinPriceCoin.text = dataCoin.dISPLAY.uSD.pRICE
            binding.chartLayoutCoin.priceChangeCoin.text = dataCoin.dISPLAY.uSD.cHANGE24HOUR

            if (dataCoin.rAW.uSD.cHANGE24HOUR>0){
                binding.chartLayoutCoin.priceChangePercentCoin.text = "+"+dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR+"%"
                binding.chartLayoutCoin.changePriceIconCoin.text = "▲"
                binding.chartLayoutCoin.priceChangePercentCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.incPrice)
                )
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.incPrice)
                )

            }else if (dataCoin.rAW.uSD.cHANGE24HOUR<0){
                binding.chartLayoutCoin.priceChangePercentCoin.text = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR+"%"
                binding.chartLayoutCoin.changePriceIconCoin.text = "▼"
                binding.chartLayoutCoin.priceChangePercentCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.decPrice)
                )
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.decPrice)
                )

            }else{
                binding.chartLayoutCoin.priceChangePercentCoin.text = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR+"%"
                binding.chartLayoutCoin.changePriceIconCoin.text = "⬤"
                binding.chartLayoutCoin.priceChangePercentCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.noChangePrice)
                )
                binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
                    ContextCompat.getColor(this, R.color.noChangePrice)
                )

            }

        }

//        lineChartLoad()

    }

//    private fun lineChartLoad() {
//        binding.chartLayoutCoin.changePriceIconCoin.text = "⬤"
//        binding.chartLayoutCoin.changePriceIconCoin.setTextColor(
//            ContextCompat.getColor(this, R.color.noChangePrice)
//        )
//        val chart = binding.chartLayoutCoin.chartViewCoin
//
//        chart.setTouchEnabled(true)
//        chart.setPinchZoom(true)
//        chart.setDrawGridBackground(false)
//        chart.setBackgroundColor(Color.TRANSPARENT)
//        chart.description.isEnabled = false
//        chart.axisRight.isEnabled = false
//        val xAxis = chart.xAxis
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.setDrawGridLines(false)
//        xAxis.textColor = Color.LTGRAY
//        val yAxis = chart.axisLeft
//        yAxis.setDrawGridLines(true)
//        yAxis.textColor = Color.LTGRAY
//        chart.legend.isEnabled = false
//
//        apiManager.getDataChart(
//            "Bitcoin", "usd", "1", "hourly", object : ApiManager.ApiCallBack<ChartData> {
//                override fun onSuccess(data: ChartData) {
//                    Log.v("errorChart" , data.toString())
//                }
//
//                override fun onFailure(message: String) {
//                    Log.v("errorChart" , message)
//                }
//
//            })
//
//    }

    private fun loadStatisticInfo() {


        if (dataCoin.coinInfo.name == "USDC" || dataCoin.coinInfo.name == "USDT") {
            binding.statisticsLayoutCoin.todayOpen.text = "$ 1.00"
            binding.statisticsLayoutCoin.highToday.text = "$ 1.00"
            binding.statisticsLayoutCoin.todayLow.text = "$ 1.00"
            binding.statisticsLayoutCoin.todayChange.text = "$ 0.00"
            binding.statisticsLayoutCoin.fullName.text = dataCoin.coinInfo.fullName
            binding.statisticsLayoutCoin.launchTime.text = dataCoin.coinInfo.assetLaunchDate
        }else{
            binding.statisticsLayoutCoin.todayOpen.text = dataCoin.dISPLAY.uSD.oPENDAY
            binding.statisticsLayoutCoin.highToday.text = dataCoin.dISPLAY.uSD.hIGHDAY
            binding.statisticsLayoutCoin.todayLow.text = dataCoin.dISPLAY.uSD.lOWDAY
            binding.statisticsLayoutCoin.todayChange.text = dataCoin.dISPLAY.uSD.cHANGEDAY
            binding.statisticsLayoutCoin.fullName.text = dataCoin.coinInfo.fullName
            binding.statisticsLayoutCoin.launchTime.text = dataCoin.coinInfo.assetLaunchDate
        }



    }
}