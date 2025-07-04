package test.example.coincenter.apimanagers.marketActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import test.example.coincenter.apimanagers.ApiManager
import test.example.coincenter.apimanagers.models.CoinsData
import test.example.coincenter.databinding.MarketActivityBinding


class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallBack {
    private lateinit var binding: MarketActivityBinding
    private val apiManager = ApiManager()
    lateinit var newsData: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MarketActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMarket()
        binding.recyclerLayout.btnMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coingecko.com/"))
            startActivity(intent)
        }
    }

    private fun loadMarket() {
        loadNewsInMkt()
        loadCoinsInMkt()
    }

    private fun loadNewsInMkt() {
        apiManager.getNews(object : ApiManager.ApiCallBack<ArrayList<String>> {
            override fun onSuccess(data: ArrayList<String>) {
                newsData = data
                refreshNews()
            }

            override fun onFailure(message: String) {
                binding.includeNews.txtErrorNews.visibility = View.VISIBLE
            }

        })
    }

    private fun refreshNews() {
        val randomNewsNum: Int = (0..19).random()
        binding.includeNews.txtNews.text = newsData[randomNewsNum]
        binding.includeNews.newsLayout.setOnClickListener {
            refreshNews()
        }
    }

    private fun loadCoinsInMkt() {

        apiManager.getCoins(object : ApiManager.ApiCallBack<List<CoinsData>> {
            override fun onSuccess(data: List<CoinsData>) {
                showDataInRecycler(data)
            }

            override fun onFailure(message: String) {
                Toast.makeText(this@MarketActivity, "Error : $message", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun showDataInRecycler(data: List<CoinsData>) {
        val marketAdapter = MarketAdapter(this, ArrayList(data))
        binding.recyclerLayout.recyclerMain.adapter = marketAdapter
        binding.recyclerLayout.recyclerMain.layoutManager = LinearLayoutManager(this)

    }

    override fun onCoinItemClicked(coinsData: CoinsData) {
        Toast.makeText(this, "HEHE", Toast.LENGTH_SHORT).show()
    }

}