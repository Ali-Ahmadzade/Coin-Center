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

    @GET("/data/v2/{period}")
    fun getChartData(
        @Path("period") period :String ,
        @Query("fsym") fsym :String ,
        @Query("limit") limit :Int ,
        @Query("aggregate") aggregate :Int ,
        @Query("tsym") tsym :String = "USD"
    ):Call<ChartData>

}