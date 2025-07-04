package test.example.coincenter.main.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinsData(
    val name: String, // اسم کامل رمز ارز
    @SerializedName("current_price")
    val currentPrice: Double, // قیمت فعلی
    @SerializedName("market_cap")
    val marketCap: Long, // مارکت کپ
    @SerializedName("price_change_percentage_24h")
    val priceChangePercent24h: Double, // تغییرات ۲۴ ساعته به درصد
    @SerializedName("image")
    val image: String,

    // آمار روزانه:
    @SerializedName("high_24h")
    val high24h: Double, // بالاترین قیمت امروز
    @SerializedName("low_24h")
    val low24h: Double, // پایین‌ترین قیمت امروز
    @SerializedName("price_change_24h")
    val priceChange24h: Double, // تغییرات قیمت مطلق امروز

    // سه آمار اضافی پیشنهادی:
    @SerializedName("total_volume")
    val totalVolume: Long, // حجم معاملات ۲۴ ساعت
    @SerializedName("circulating_supply")
    val circulatingSupply: Double, // مقدار در حال گردش
    @SerializedName("ath")
    val allTimeHigh: Double // بالاترین قیمت تاریخ
) :Parcelable
