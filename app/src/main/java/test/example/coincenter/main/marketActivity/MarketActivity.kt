package test.example.coincenter.main.marketActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import test.example.coincenter.main.apiManager.ApiManager
import test.example.coincenter.main.models.CoinData
import test.example.coincenter.databinding.MarketActivityBinding
import test.example.coincenter.main.coinActivity.CoinActivity


class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallBack {

    private lateinit var binding: MarketActivityBinding
    private val apiManager = ApiManager()
    private lateinit var newsData: ArrayList<Pair<String, String>>
    private lateinit var coinData: List<CoinData.Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MarketActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerLayout.btnMore.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW , Uri.parse("https://www.cryptocompare.com/coins/list/all/USD/1")))
        }
        binding.swipeRefreshMarket.setOnRefreshListener {
            loadMarket()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMarket.isRefreshing = false
            }, 1500)
        }
    }

    private fun loadMarket() {
        loadNewsInMkt()
        loadCoinsInMkt()
    }

    private fun loadNewsInMkt() {
        apiManager.getCryptoNews(object : ApiManager.ApiCallBack<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                newsData = data
                refreshNews()
            }

            override fun onFailure(message: String) {
                Toast.makeText(this@MarketActivity, "Error => $message", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun refreshNews() {
        val randomNewsNum: Int = (0..49).random()
        binding.includeNews.txtNews.text = newsData[randomNewsNum].first
        binding.includeNews.newsLayout.setOnClickListener {
            refreshNews()
        }
        binding.includeNews.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsData[randomNewsNum].second))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadMarket()
    }

    private fun loadCoinsInMkt() {

        apiManager.getCoinMkt(object : ApiManager.ApiCallBack<List<CoinData.Data>> {
            override fun onSuccess(data: List<CoinData.Data>) {
                showDataInRecycler(data)
            }

            override fun onFailure(message: String) {
                Toast.makeText(this@MarketActivity, "Error => $message", Toast.LENGTH_SHORT).show()

            }
        })

    }

    private fun showDataInRecycler(data: List<CoinData.Data>) {

        val myAdapter = MarketAdapter(ArrayList(data), this)
        binding.recyclerLayout.recyclerMain.adapter = myAdapter
        binding.recyclerLayout.recyclerMain.layoutManager = LinearLayoutManager(this)
    }

    override fun onCoinClicked(dataCoin: CoinData.Data) {
        val intent = Intent(this, CoinActivity::class.java)
        intent.putExtra("selectedCoinData", dataCoin)
        startActivity(intent)
    }


}