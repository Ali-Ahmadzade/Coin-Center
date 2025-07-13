package test.example.coincenter.main.coinActivity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import test.example.coincenter.R
import test.example.coincenter.databinding.CoinActivityBinding
import test.example.coincenter.main.apiManager.ALL
import test.example.coincenter.main.apiManager.ApiManager
import test.example.coincenter.main.apiManager.HOUR
import test.example.coincenter.main.apiManager.HOURS24
import test.example.coincenter.main.apiManager.MONTH
import test.example.coincenter.main.apiManager.MONTH3
import test.example.coincenter.main.apiManager.WEEK
import test.example.coincenter.main.apiManager.YEAR
import test.example.coincenter.main.models.ChartData
import test.example.coincenter.main.models.CoinData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoinActivity : AppCompatActivity() {
    private val apiManager = ApiManager()
    private lateinit var dataCoin :CoinData.Data
    private lateinit var binding: CoinActivityBinding
    private var period = HOUR

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = CoinActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.swipeRefreshCoin.setOnRefreshListener {

            Handler(Looper.getMainLooper()).postDelayed({
                loadCoin()
                binding.swipeRefreshCoin.isRefreshing = false
            }, 1500)
        }
        binding.chartLayoutCoin.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.range12H -> {
                    period = HOUR
                    lineChartLoad()
                }
                R.id.range1D -> {
                    period = HOURS24
                    lineChartLoad()
                }
                R.id.range1W -> {
                    period = WEEK
                    lineChartLoad()
                }
                R.id.range1M -> {
                    period = MONTH
                    lineChartLoad()
                }
                R.id.range3M -> {
                    period = MONTH3
                    lineChartLoad()
                }
                R.id.range1Y -> {
                    period = YEAR
                    lineChartLoad()
                }
                R.id.rangeAll -> {
                    period = ALL
                    lineChartLoad()
                }
            }
        }

        initializeChart()


    }

    private fun initializeChart() {

        val chart = binding.chartLayoutCoin.chartViewCoin

        chart.setDrawGridBackground(false)
        chart.setBackgroundColor(Color.TRANSPARENT)
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.LTGRAY
        val yAxis = chart.axisLeft
        yAxis.setDrawGridLines(true)
        yAxis.textColor = Color.LTGRAY
        chart.legend.isEnabled = false
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

        lineChartLoad()

    }

    private fun lineChartLoad() {


        apiManager.getChartData(period , dataCoin.coinInfo.name , object  :ApiManager.ApiCallBack<ChartData> {
            override fun onSuccess(data: ChartData) {
                setupChart( data )
            }

            override fun onFailure(message: String) {
                Toast.makeText(this@CoinActivity, "Error => $message", Toast.LENGTH_SHORT).show()
            }
        } )

    }

    fun setupChart( chartData: ChartData ) {
        val entries = chartData.data.data.map {
            com.github.mikephil.charting.data.Entry(it.time.toFloat() , it.close.toFloat())

        }
        val dataSet = LineDataSet(entries , "Price").apply {
            color = Color.GREEN
            setDrawCircles(false)
            setDrawValues(false)
            lineWidth = 2f
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        val lineData = LineData(dataSet)
        binding.chartLayoutCoin.chartViewCoin.data = lineData
        binding.chartLayoutCoin.chartViewCoin.xAxis.valueFormatter = object  :ValueFormatter(){
            private val sdf = SimpleDateFormat("HH:mm" , Locale.getDefault())
            override fun getFormattedValue(value: Float): String {
                return sdf.format(Date(value.toLong() * 1000))
            }
        }
        binding.chartLayoutCoin.chartViewCoin.invalidate()
    }

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