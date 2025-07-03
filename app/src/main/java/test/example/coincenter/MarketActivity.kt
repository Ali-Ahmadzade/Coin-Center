package test.example.coincenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import test.example.coincenter.apimanagers.ApiManager
import test.example.coincenter.apimanagers.models.NewsData
import test.example.coincenter.databinding.MarketActivityBinding




class MarketActivity : AppCompatActivity() {
    private lateinit var binding: MarketActivityBinding
    val apiManager = ApiManager()
    lateinit var newsData :ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MarketActivityBinding.inflate( layoutInflater )
        setContentView( binding.root )

        loadMarket()

    }

    private fun loadMarket() {
        loadNewsInMkt()
        loadCoinsInMkt()
    }

    private fun loadNewsInMkt() {
        apiManager.getNews( object :ApiManager.ApiCallBack<ArrayList<String>> {
            override fun onSuccess(data: ArrayList<String>) {
                newsData = data
                refreshNews()
            }

            override fun onFailure(message: String) {
                Toast.makeText(this@MarketActivity, "Error : $message", Toast.LENGTH_SHORT).show()
                Log.v("errorNews" , message)
            }

        })
    }
    private fun refreshNews(){
        val randomNewsNum :Int = (0..19).random()
        binding.includeNews.txtNews.text = newsData[randomNewsNum]
        binding.includeNews.newsLayout.setOnClickListener {
            refreshNews()
        }
    }
    private fun loadCoinsInMkt() {

    }




}