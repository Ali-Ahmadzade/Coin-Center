package test.example.coincenter.main.apiManager

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.example.coincenter.main.models.ChartData
import test.example.coincenter.main.models.CoinData
import test.example.coincenter.main.models.CryptoNewsData

const val API_KEY_COINS = "CG-cLuXk48fk1r3FYkHkY7utyz3"
const val BASE_URL_COINS = "https://api.coingecko.com/api/v3/"
const val API_KEY_NEWS = "182fd5212e746344c425d65465ad7b8edd36170f"
const val BASE_URL_NEWS = "https://cryptopanic.com/api/developer/v2/"

const val API_KEY = "ac880c17c47e4c0a8abf8ba3a014f47c7c06c3a39b5a500e7bd3821e29ba0a57"
const val BASE_URL = "https://min-api.cryptocompare.com"
const val BASE_URL_IMAGE = "https://cryptocompare.com/"

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

    fun getCryptoNews(apiCallBack :ApiCallBack<ArrayList<Pair<String , String>>>){
        apiService.getCryptoNews().enqueue( object :Callback<CryptoNewsData> {
            override fun onResponse(
                call: Call<CryptoNewsData>?,
                response: Response<CryptoNewsData>?
            ) {
                val data = response?.body()
                val dataToSend :ArrayList<Pair<String , String>> = arrayListOf()
                data?.data?.forEach {
                    dataToSend.add( Pair(it.title , it.url ) )
                }
                apiCallBack.onSuccess( dataToSend )
            }

            override fun onFailure(call: Call<CryptoNewsData>?, t: Throwable?) {
                apiCallBack.onFailure( t?.message.toString() )
            }


        } )
    }

    fun getCoinMkt( apiCallBack: ApiCallBack<List<CoinData.Data>> ){
        apiService.getTopCoinMkt().enqueue( object :Callback<CoinData> {
            override fun onResponse(call: Call<CoinData>?, response: Response<CoinData>?) {
                val data = response?.body()
                apiCallBack.onSuccess(data!!.data)
            }

            override fun onFailure(call: Call<CoinData>?, t: Throwable?) {
                apiCallBack.onFailure( t!!.message.toString() )
            }
        } )
    }


//    fun getCoins( apiCallback: ApiCallBack<List<CoinsData>>) {
//        apiServiceCoins.getTopCoins().enqueue(object : Callback<List<CoinsData>> {
//            override fun onResponse(
//                call: Call<List<CoinsData>>?,
//                response: Response<List<CoinsData>>?
//            ) {
//                val coinsData = response!!.body()
//                apiCallback.onSuccess( coinsData )
//            }
//
//            override fun onFailure(call: Call<List<CoinsData>>?, t: Throwable?) {
//                apiCallback.onFailure( t!!.message.toString() )
//            }
//        })
//    }

    fun getDataChart( coinId :String , currency :String = "usd" , days :String , interval :String = "daily"  ,apiCallBack: ApiCallBack<ChartData> ){
        apiServiceCoins.getChartData(coinId , currency , days , interval ).enqueue( object :Callback<ChartData> {
            override fun onResponse(call: Call<ChartData>?, response: Response<ChartData>?) {
                val returned = response!!.body()
                apiCallBack.onSuccess(returned)
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


