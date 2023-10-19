package mohammad.toriq.newsapi.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mohammad.toriq.newsapi.databinding.ActivityCategoryBinding
import mohammad.toriq.newsapi.util.InitializerUi
import mohammad.toriq.newsapi.views.adapters.AdapterCategory
import mohammad.toriq.newsapi.R
import mohammad.toriq.newsapi.databinding.ActivityArticleBinding
import mohammad.toriq.newsapi.models.Article
import mohammad.toriq.newsapi.models.ResponseArticles
import mohammad.toriq.newsapi.models.Source
import mohammad.toriq.newsapi.util.Util
import mohammad.toriq.newsapi.viewmodels.ArticleViewModel
import mohammad.toriq.newsapi.viewmodels.ArticleViewModelFactory
import mohammad.toriq.newsapi.viewmodels.SourceViewModel
import mohammad.toriq.newsapi.viewmodels.SourceViewModelFactory
import mohammad.toriq.newsapi.views.adapters.AdapterArticle
import mohammad.toriq.newsapi.views.adapters.AdapterSource
import mohammad.toriq.views.util.Resource
import java.util.Timer
import java.util.TimerTask

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class ArticleActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivityArticleBinding
    lateinit var adapter: ItemAdapter<AdapterArticle>
    lateinit var fastAdapter: FastAdapter<AdapterArticle>
    private lateinit var viewModel: ArticleViewModel
    private var isFirst = true
    private var category: String? =null
    private var source  = ""
    private var sourceName  = ""
    private var page = 1
    private var isMax = false
    private var search: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        if(isFirst){
            initConfig()
        }else{
            runOnUiThread {
                showLoading(true)
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            binding.progress.visibility = View.GONE
                            binding.revListData.visibility = View.VISIBLE
                            binding.textDataNotFound.visibility = View.GONE
                        }
                    }
                }, 500)
            }
        }
    }

    override fun initConfig() {
        val factory = ArticleViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[ArticleViewModel::class.java]
        if(intent.getStringExtra("source")!=null){
            source = intent.getStringExtra("source")!!
            sourceName = intent.getStringExtra("sourceName")!!
            category = intent.getStringExtra("category")

            adapter = ItemAdapter()
            fastAdapter = FastAdapter.with(adapter)
            initUI()
        }else{
            finish()
        }
    }

    override fun initUI() {
        if(isFirst){
            isFirst = false
        }
        binding.toolbar.title.text = sourceName
        binding.revListData.layoutManager = LinearLayoutManager(this)
        binding.revListData.adapter = fastAdapter
        binding.revListData.animation = null
        binding.revListData.isNestedScrollingEnabled = false

        viewModel.articles.observe(this){ resource ->
            when (resource) {
                is Resource.Loading -> showLoading(resource.state)
                is Resource.Success -> loadData(resource.data)
                is Resource.Failure -> showDataError(resource.errorMessage)
            }
        }

        viewModel.getSourceArticles(source,search)

        setListener()
    }

    override fun setListener() {

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            var intent = Intent(this@ArticleActivity, WebviewActivity::class.java)
            intent.putExtra("url",item.data?.url)
            intent.putExtra("title",item.data?.title)
            startActivity(intent)
            false
        }
        binding.revListData.setOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                var mLayoutManager = binding.revListData.layoutManager as LinearLayoutManager
                val firstPos: Int = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                val lastPost: Int = mLayoutManager.findLastCompletelyVisibleItemPosition()
                Log.d("OkTag",adapter.adapterItemCount.toString())
                Log.d("OkTag","lastPost : "+(lastPost+1).toString())
                if(adapter.adapterItemCount == lastPost+1){
                    if(!isMax){
                        page++
                        viewModel.getSourceArticles(source,search, page)
                    }
                }
            }
        })
        binding.search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(binding.search.text.toString().trim().length != 0){
                    search = binding.search.text.toString()
                }
                if(search != null){
                    if(Util.isTextEmpty(search)){
                        search = null
                    }
                }
//                adapter.filter(search)
                resetData()
                Util.hideKeyboard(this@ArticleActivity)
                true
            }
            false
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().length != 0) {
                    binding.btnClear.visibility = View.VISIBLE
                } else {
                    binding.btnClear.visibility = View.GONE
                }
            }
        })

        binding.btnClear.setOnClickListener {
            search = null
            binding.search.setText("")
            binding.btnClear.visibility = View.GONE
//            adapter.filter("")
            resetData()
            Util.hideKeyboard(this@ArticleActivity)
        }
    }

    fun resetData(){
        adapter.clear()
        page = 1
        viewModel.getSourceArticles(source,search, page)
    }

    fun showLoading(loading : Boolean){
        if(loading){
            binding.progress.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
            binding.revListData.visibility = View.GONE
            binding.textDataNotFound.visibility = View.GONE
        }else{
        }
    }

    fun loadData(response : ResponseArticles){
        var data = response.articles
        binding.progress.visibility = View.GONE
        binding.searchLayout.visibility = View.VISIBLE
        binding.revListData.visibility = View.VISIBLE
        binding.textDataNotFound.visibility = View.GONE
        var isEmpty = false
        if(page == 1){
            if(data.size == 0){
                isEmpty = true
            }
        }
        if(!isEmpty){
            data.forEach {
                var data = AdapterArticle(this)
                data.data = it
                adapter.add(data)
            }
            if(adapter.adapterItemCount.toLong() == response.totalResults){
                isMax = true
            }
        }else{
            showDataError()
            binding.searchLayout.visibility = View.VISIBLE
        }
    }

    fun showDataError(message : String? = null){
        binding.progress.visibility = View.GONE
        binding.searchLayout.visibility = View.GONE
        binding.revListData.visibility = View.GONE
        binding.textDataNotFound.visibility = View.VISIBLE
        if(message!=null){
            binding.textDataNotFound.text = message
        }
    }

}