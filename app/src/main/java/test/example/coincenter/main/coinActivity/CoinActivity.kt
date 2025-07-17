package test.example.coincenter.main.coinActivity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var dataCoin: CoinData.Data
    private lateinit var binding: CoinActivityBinding
    private var period = HOUR

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = CoinActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dataCoin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("selectedCoinData", CoinData.Data::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("selectedCoinData") as? CoinData.Data
        }!!

        binding.includeToolBarCoin.toolBarMain.title = dataCoin.coinInfo.fullName

        binding.swipeRefreshCoin.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                loadCoin()
                binding.swipeRefreshCoin.isRefreshing = false
            }, 1500)
        }

        setupChartStyle()

        binding.chartLayoutCoin.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
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

        loadCoin()
    }

    override fun onResume() {
        super.onResume()
        loadCoin()
    }

    private fun loadCoin() {
        updatePriceInfo()
        lineChartLoad()
        loadStatisticInfo()
    }

    private fun updatePriceInfo() {
        val chart = binding.chartLayoutCoin

        if (dataCoin.coinInfo.name == "USDC" || dataCoin.coinInfo.name == "USDT") {
            chart.coinPriceCoin.text = "$ 1.00"
            chart.changePriceIconCoin.text = "⬤"
            chart.priceChangeCoin.text = "$0.00"
            chart.priceChangePercentCoin.text = "0%"

            val color = ContextCompat.getColor(this, R.color.noChangePrice)
            chart.priceChangePercentCoin.setTextColor(color)
            chart.changePriceIconCoin.setTextColor(color)

        } else {
            chart.coinPriceCoin.text = dataCoin.dISPLAY.uSD.pRICE
            chart.priceChangeCoin.text = dataCoin.dISPLAY.uSD.cHANGE24HOUR

            val change = dataCoin.rAW.uSD.cHANGE24HOUR
            val changeText = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR + "%"
            val incColor = ContextCompat.getColor(this, R.color.incPrice)
            val decColor = ContextCompat.getColor(this, R.color.decPrice)
            val noChangeColor = ContextCompat.getColor(this, R.color.noChangePrice)

            when {
                change > 0 -> {
                    chart.priceChangePercentCoin.text = "+$changeText"
                    chart.changePriceIconCoin.text = "▲"
                    chart.priceChangePercentCoin.setTextColor(incColor)
                    chart.changePriceIconCoin.setTextColor(incColor)
                }
                change < 0 -> {
                    chart.priceChangePercentCoin.text = changeText
                    chart.changePriceIconCoin.text = "▼"
                    chart.priceChangePercentCoin.setTextColor(decColor)
                    chart.changePriceIconCoin.setTextColor(decColor)
                }
                else -> {
                    chart.priceChangePercentCoin.text = changeText
                    chart.changePriceIconCoin.text = "⬤"
                    chart.priceChangePercentCoin.setTextColor(noChangeColor)
                    chart.changePriceIconCoin.setTextColor(noChangeColor)
                }
            }
        }
    }

    private fun setupChartStyle() {
        val chart = binding.chartLayoutCoin.chartViewCoin
        chart.apply {
            setDrawGridBackground(false)
            setBackgroundColor(Color.TRANSPARENT)
            description.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false
        }

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = Color.LTGRAY
        }

        chart.axisLeft.apply {
            setDrawGridLines(true)
            textColor = Color.LTGRAY
        }
    }

    private fun lineChartLoad() {
        apiManager.getChartData(period, dataCoin.coinInfo.name, object : ApiManager.ApiCallBack<ChartData> {
            override fun onSuccess(data: ChartData) {
                setupChart(data)
            }

            override fun onFailure(message: String) {
                Toast.makeText(this@CoinActivity, "Error => $message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setupChart(chartData: ChartData) {
        val entries = chartData.data.data.map {
            com.github.mikephil.charting.data.Entry(it.time.toFloat(), it.close.toFloat())
        }

        val dataSet = LineDataSet(entries, "Price").apply {
            color = ContextCompat.getColor( this@CoinActivity , R.color.lineColor )
            setDrawCircles(false)
            setDrawValues(false)
            lineWidth = 2.5f
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        val lineData = LineData(dataSet)
        binding.chartLayoutCoin.chartViewCoin.data = lineData





        binding.chartLayoutCoin.chartViewCoin.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val timeMillis = value.toLong() * 1000
                val sdf = when (period) {
                    HOUR, HOURS24 -> SimpleDateFormat("HH:mm", Locale.getDefault())
                    WEEK, MONTH -> SimpleDateFormat("MM/dd", Locale.getDefault())
                    MONTH3, YEAR, ALL -> SimpleDateFormat("MMM yyyy", Locale.getDefault())
                    else -> SimpleDateFormat("dd/MM", Locale.getDefault())
                }
                return sdf.format(Date(timeMillis))
            }
        }

        binding.chartLayoutCoin.chartViewCoin.invalidate()
    }


    private fun loadStatisticInfo() {
        val stats = binding.statisticsLayoutCoin
        val usdDisplay = dataCoin.dISPLAY.uSD

        stats.fullName.text = dataCoin.coinInfo.fullName
        stats.launchTime.text = dataCoin.coinInfo.assetLaunchDate

        if (dataCoin.coinInfo.name == "USDC" || dataCoin.coinInfo.name == "USDT") {
            stats.todayOpen.text = "$ 1.00"
            stats.highToday.text = "$ 1.00"
            stats.todayLow.text = "$ 1.00"
            stats.todayChange.text = "$ 0.00"
        } else {
            stats.todayOpen.text = usdDisplay.oPENDAY
            stats.highToday.text = usdDisplay.hIGHDAY
            stats.todayLow.text = usdDisplay.lOWDAY
            stats.todayChange.text = usdDisplay.cHANGEDAY
        }
    }
}
