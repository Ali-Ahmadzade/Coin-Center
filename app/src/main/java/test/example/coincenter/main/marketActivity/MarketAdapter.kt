package test.example.coincenter.main.marketActivity


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import test.example.coincenter.R
import test.example.coincenter.main.models.CoinsData
import test.example.coincenter.databinding.CoinItemBinding

class MarketAdapter(
    private val recyclerCallBack: RecyclerCallBack,
    private val data: ArrayList<CoinsData>
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {
    private lateinit var binding: CoinItemBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(coinsData: CoinsData) {
            binding.txtCoinNameMkt.text = coinsData.name
            binding.txtCoinPriceMkt.text = "$"+coinsData.currentPrice.toString()
            val test = coinsData.marketCap.toFloat() / 1000000000
            val index = test.toString().indexOf(".")
            binding.txtMarketCapMkt.text = "$" + test.toString().substring( 0 , index + 3 ) +" B"
            if (coinsData.name == "Tether" || coinsData.name == "USDC"){
                binding.txtCoinPriceMkt.text = "$1.00"
                binding.txtPriceChangeMkt.text = "0.00%"
                binding.txtPriceChangeMkt.setTextColor( ContextCompat.getColor( binding.root.context, R.color.noChangePrice ) )
            }else{
                if (coinsData.priceChange24h > 0){
                    binding.txtPriceChangeMkt.setTextColor( ContextCompat.getColor( binding.root.context, R.color.incPrice ) )
                    binding.txtPriceChangeMkt.text = coinsData.priceChangePercent24h.toString().substring(0,4)+"%"
                }else if (coinsData.priceChange24h < 0){
                    binding.txtPriceChangeMkt.setTextColor( ContextCompat.getColor( binding.root.context, R.color.decPrice ) )
                    binding.txtPriceChangeMkt.text = coinsData.priceChangePercent24h.toString().substring(0,5)+"%"
                }else{
                    binding.txtPriceChangeMkt.setTextColor( ContextCompat.getColor( binding.root.context, R.color.noChangePrice ) )
                    binding.txtPriceChangeMkt.text = "0.00"
                }
            }


            Glide
                .with(itemView)
                .load(coinsData.image)
                .into(binding.imgCoinMarket)
            itemView.setOnClickListener {
                recyclerCallBack.onCoinItemClicked( coinsData )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = CoinItemBinding.inflate(inflater, parent, false)





        return MarketViewHolder(binding.root)
    }

    override fun getItemCount(): Int = 6

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    interface RecyclerCallBack {
        fun onCoinItemClicked(coinsData: CoinsData)
    }

}