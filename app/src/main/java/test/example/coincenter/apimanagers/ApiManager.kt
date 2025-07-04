package test.example.coincenter.apimanagers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.example.coincenter.apimanagers.models.CoinsData
import test.example.coincenter.apimanagers.models.NewsData

const val API_KEY_COINS = "CG-cLuXk48fk1r3FYkHkY7utyz3"
const val BASE_URL_COINS = "https://api.coingecko.com/api/v3/"
const val API_KEY_NEWS = "182fd5212e746344c425d65465ad7b8edd36170f"
const val BASE_URL_NEWS = "https://cryptopanic.com/api/developer/v2/"

class ApiManager {

    private val apiServiceNews: ApiService
    private val apiServiceCoins: ApiService

    init {
        val retrofitNews = Retrofit
            .Builder()
            .baseUrl(BASE_URL_NEWS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiServiceNews = retrofitNews.create(ApiService::class.java)

        val retrofitCoins = Retrofit
            .Builder()
            .baseUrl(BASE_URL_COINS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiServiceCoins = retrofitCoins.create(ApiService::class.java)
    }

    fun getNews(apiCallback: ApiCallBack<ArrayList<String>>) {
        apiServiceNews.getNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>?, response: Response<NewsData>?) {

                val news = response!!.body()

                val dataToSend: ArrayList<String> = arrayListOf()

                news.results.forEach {
                    dataToSend.add(it.title)
                }
                apiCallback.onSuccess(dataToSend)
            }

            override fun onFailure(call: Call<NewsData>?, t: Throwable?) {
                apiCallback.onFailure(t!!.message.toString())
            }

        })
    }

    fun getCoins( apiCallback: ApiCallBack<List<CoinsData>> ) {
        apiServiceCoins.getTopCoins().enqueue(object : Callback<List<CoinsData>> {
            override fun onResponse(
                call: Call<List<CoinsData>>?,
                response: Response<List<CoinsData>>?
            ) {
                val coinsData = response!!.body()
                apiCallback.onSuccess( coinsData )
            }

            override fun onFailure(call: Call<List<CoinsData>>?, t: Throwable?) {
                apiCallback.onFailure( t!!.message.toString() )
            }
        })
    }

    interface ApiCallBack<T> {
        fun onSuccess(data: T)
        fun onFailure(message: String)
    }


}


