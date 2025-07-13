package test.example.coincenter.main.apiManager

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.example.coincenter.main.models.ChartData
import test.example.coincenter.main.models.CoinData
import test.example.coincenter.main.models.CryptoNewsData


class ApiManager {

    private val apiService: ApiService
    private val apiServiceCoins: ApiService

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)


        val retrofitCoins = Retrofit
            .Builder()
            .baseUrl(BASE_URL_COINS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiServiceCoins = retrofitCoins.create(ApiService::class.java)
    }

    fun getCryptoNews(apiCallBack: ApiCallBack<ArrayList<Pair<String, String>>>) {
        apiService.getCryptoNews().enqueue(object : Callback<CryptoNewsData> {
            override fun onResponse(
                call: Call<CryptoNewsData>?,
                response: Response<CryptoNewsData>?
            ) {
                val data = response?.body()
                val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()
                data?.data?.forEach {
                    dataToSend.add(Pair(it.title, it.url))
                }
                apiCallBack.onSuccess(dataToSend)
            }

            override fun onFailure(call: Call<CryptoNewsData>?, t: Throwable?) {
                apiCallBack.onFailure(t?.message.toString())
            }


        })
    }

    fun getCoinMkt(apiCallBack: ApiCallBack<List<CoinData.Data>>) {
        apiService.getTopCoinMkt().enqueue(object : Callback<CoinData> {
            override fun onResponse(call: Call<CoinData>?, response: Response<CoinData>?) {
                val data = response?.body()
                apiCallBack.onSuccess(data!!.data)
            }

            override fun onFailure(call: Call<CoinData>?, t: Throwable?) {
                apiCallBack.onFailure(t!!.message.toString())
            }
        })
    }

    fun getChartData(period: String, symbol: String, apiCallBack: ApiCallBack<ChartData>) {

        var histoPeriod = ""
        var limit = 30
        var aggregate = 1

        when (period) {
            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }

            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24
            }

            WEEK -> {
                histoPeriod = HISTO_HOUR
                aggregate = 6
            }

            MONTH -> {
                histoPeriod = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {
                histoPeriod = HISTO_DAY
                limit = 90
            }

            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13
            }

            ALL -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000
            }
        }


        apiService.getChartData(histoPeriod, symbol, limit, aggregate)
            .enqueue(object : Callback<ChartData> {
                override fun onResponse(call: Call<ChartData>?, response: Response<ChartData>?) {
                    val dataToSend = response!!.body()
                    apiCallBack.onSuccess(dataToSend)
                }

                override fun onFailure(call: Call<ChartData>?, t: Throwable?) {
                    apiCallBack.onFailure(t!!.message.toString())
                }
            })
    }

    interface ApiCallBack<T> {
        fun onSuccess(data: T)
        fun onFailure(message: String)
    }


}


