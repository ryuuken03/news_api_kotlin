package mohammad.toriq.newsapi.views.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mohammad.toriq.newsapi.databinding.ActivityArticleBinding
import mohammad.toriq.newsapi.databinding.ActivityWebviewBinding
import mohammad.toriq.newsapi.util.InitializerUi
import mohammad.toriq.newsapi.viewmodels.ArticleViewModel
import mohammad.toriq.newsapi.views.adapters.AdapterArticle

/***
 * Created By Mohammad Toriq on 19/10/2023
 */
class WebviewActivity  : AppCompatActivity() , InitializerUi {
    lateinit var binding: ActivityWebviewBinding
    private var url  = ""
    private var title : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initConfig()
    }

    override fun initConfig() {
        if(intent.getStringExtra("url")!=null){
            url = intent.getStringExtra("url")!!
            title = intent.getStringExtra("title")
            if(title == null){
                title = url
            }
            initUI()
        }else{
            finish()
        }
    }

    override fun initUI() {
        binding.toolbar.title.text = title
        requestData()
        setListener()
    }

    override fun setListener() {
        setupSwipeRefresh()
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                requestData()
                object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        try {
                            binding.swipeRefresh.setRefreshing(false)
                        }catch (e: Exception){}
                    }

                    override fun onFinish() {
                        try {
                            binding.swipeRefresh.setRefreshing(false)
                        }catch (e: Exception){}
                    }
                }.start()
            }
        })
    }

    private fun requestData() {
        binding.webView.getSettings().setJavaScriptEnabled(true)
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        binding.webView.getSettings().setAllowFileAccess(true)
        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true)
        binding.webView.getSettings().setDomStorageEnabled(true)
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON)
        binding.webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                if (progress < 100 && binding.progressBar.getVisibility() === ProgressBar.GONE) {
                    binding.progressBar.setVisibility(ProgressBar.VISIBLE)
                }
                binding.progressBar.setProgress(progress)
                if (progress == 100) {
                    binding.progressBar.setVisibility(ProgressBar.GONE)
                }
            }
        })
        binding.webView.loadUrl(url!!)
    }
}