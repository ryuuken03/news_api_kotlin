package mohammad.toriq.newsapi.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mohammad.toriq.newsapi.databinding.ActivitySplashBinding
import mohammad.toriq.newsapi.util.InitializerUi
import java.util.Timer
import java.util.TimerTask


class SplashActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initConfig()
    }
    override fun initConfig(){
        initUI()
    }

    override fun initUI(){
        Timer().schedule(object : TimerTask() {
            override fun run() {
                var intent = Intent(this@SplashActivity, CategoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 500)
        setListener()
    }

    override fun setListener(){

    }
}