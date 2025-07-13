package test.example.coincenter.main.apiManager

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import test.example.coincenter.main.models.ChartData
import test.example.coincenter.main.models.CoinData
import test.example.coincenter.main.models.CryptoNewsData

interface ApiService {

    @GET("/data/v2/news/")
    fun getCryptoNews(
        @Query("lang") lang :String = "EN" ,
        @Query("api_key") api :String = API_KEY
    ) :Call<CryptoNewsData>

    @GET("/data/top/totaltoptiervolfull")
    fun getTopCoinMkt(
        @Query("limit") limit :Int = 30 ,
        @Query("tsym") tsym :String = "USD" ,
        @Query("auth_token") auth: String = API_KEY_NEWS
    ) :Call<CoinData>


    @GET("coins/markets/")
    fun getTopCoins(
        @Query("vs_currency") currency :String = "usd" ,
        @Query("order") order :String = "market_cap_desc" ,
        @Query("per_page") quantity :Int = 10 ,
        @Query("page") page :Int = 1 ,
        @Query("sparkline") sparkline :Boolean = false
    ) :Call<List<CoinData>>

    @GET("coins/{id}/market_chart")
    fun getChartData(
        @Path("id") coinId :String,
        @Query("vs_currency") currency :String = "usd",
        @Query("days") days :String = "0.5",
        @Query("interval") interval :String = "hourly" ,
    ) : Call<ChartData>

}