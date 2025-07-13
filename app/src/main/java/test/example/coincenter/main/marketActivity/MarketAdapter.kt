package test.example.coincenter.main.marketActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import test.example.coincenter.R
import test.example.coincenter.databinding.CoinItemBinding
import test.example.coincenter.main.apiManager.BASE_URL_IMAGE
import test.example.coincenter.main.models.CoinData

class MarketAdapter(
    private val data: ArrayList<CoinData.Data>,
    private val recyclerCallBack: RecyclerCallBack
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    private lateinit var binding: CoinItemBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(dataCoin: CoinData.Data) {

            Glide
                .with(itemView)
                .load(BASE_URL_IMAGE + dataCoin.coinInfo.imageUrl)
                .into(binding.imgCoinMarket)
            itemView.setOnClickListener {
                recyclerCallBack.onCoinClicked(dataCoin)
            }

            if (dataCoin.coinInfo.name == "USDC" || dataCoin.coinInfo.name == "USDT") {
                binding.txtCoinNameMkt.text = dataCoin.coinInfo.fullName
                binding.txtPriceChangeMkt.text = "0.00%"
                binding.txtPriceChangeMkt.setTextColor(
                    ContextCompat.getColor( itemView.context , R.color.noChangePrice )
                )
                binding.txtCoinPriceMkt.text = "$ 1.00"
                binding.txtMarketCapMkt.text = dataCoin.dISPLAY.uSD.mKTCAP

            } else {
                binding.txtCoinNameMkt.text = dataCoin.coinInfo.fullName
                binding.txtCoinPriceMkt.text = dataCoin.dISPLAY.uSD.pRICE
                binding.txtMarketCapMkt.text = dataCoin.dISPLAY.uSD.mKTCAP
                if (dataCoin.rAW.uSD.cHANGEPCT24HOUR > 0){
                    binding.txtPriceChangeMkt.text = "+"+dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR+"%"
                    binding.txtPriceChangeMkt.setTextColor(
                        ContextCompat.getColor( itemView.context , R.color.incPrice )
                    )
                }else if (dataCoin.rAW.uSD.cHANGEPCT24HOUR < 0){
                    binding.txtPriceChangeMkt.text = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR+"%"
                    binding.txtPriceChangeMkt.setTextColor(
                        ContextCompat.getColor( itemView.context , R.color.decPrice )
                    )
                }else{
                    binding.txtPriceChangeMkt.text = dataCoin.dISPLAY.uSD.cHANGEPCT24HOUR+"%"
                    binding.txtPriceChangeMkt.setTextColor(
                        ContextCompat.getColor( itemView.context , R.color.noChangePrice )
                    )
                }

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = CoinItemBinding.inflate(layoutInflater, parent, false)
        return MarketViewHolder(binding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    interface RecyclerCallBack {
        fun onCoinClicked(dataCoin: CoinData.Data)
    }
}