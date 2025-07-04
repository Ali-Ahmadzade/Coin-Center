package test.example.coincenter.main.coinActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import test.example.coincenter.R
import test.example.coincenter.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {
    lateinit var binding :ActivityCoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}