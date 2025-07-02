package test.example.coincenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import test.example.coincenter.databinding.MarketActivityBinding

class MarketActivity : AppCompatActivity() {
    private lateinit var binding: MarketActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MarketActivityBinding.inflate( layoutInflater )
        setContentView( binding.root )



    }
}