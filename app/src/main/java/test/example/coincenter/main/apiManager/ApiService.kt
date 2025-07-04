package test.example.coincenter.main.apiManager

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import test.example.coincenter.main.models.CoinsData
import test.example.coincenter.main.models.NewsData

interface ApiService {

    @GET("posts/")
    fun getNews(
        @Query("auth_token") auth: String = API_KEY_NEWS,
        @Query("filter") filter :String = "hot",
        @Query("kind") kind :String = "news"
    ) :Call<NewsData>

    @GET("coins/markets/")
    fun getTopCoins(
        @Query("vs_currency") currency :String = "usd" ,
        @Query("order") order :String = "market_cap_desc" ,
        @Query("per_page") quantity :Int = 10 ,
        @Query("page") page :Int = 1 ,
        @Query("sparkline") sparkline :Boolean = false
    ) :Call<List<CoinsData>>

}