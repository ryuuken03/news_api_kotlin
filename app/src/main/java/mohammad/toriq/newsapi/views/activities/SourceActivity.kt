package mohammad.toriq.newsapi.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mohammad.toriq.newsapi.databinding.ActivityCategoryBinding
import mohammad.toriq.newsapi.util.InitializerUi
import mohammad.toriq.newsapi.views.adapters.AdapterCategory
import mohammad.toriq.newsapi.R
import mohammad.toriq.newsapi.databinding.ActivitySourceBinding
import mohammad.toriq.newsapi.models.Source
import mohammad.toriq.newsapi.util.Util
import mohammad.toriq.newsapi.viewmodels.SourceViewModel
import mohammad.toriq.newsapi.viewmodels.SourceViewModelFactory
import mohammad.toriq.newsapi.views.adapters.AdapterSource
import mohammad.toriq.views.util.Resource
import java.util.Timer
import java.util.TimerTask

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class SourceActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivitySourceBinding
    lateinit var adapter: ItemAdapter<AdapterSource>
    lateinit var fastAdapter: FastAdapter<AdapterSource>
    private lateinit var viewModel: SourceViewModel
    private var isFirst = true
    private var category  = ""
    private var search: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySourceBinding.inflate(layoutInflater)
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
        val factory = SourceViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[SourceViewModel::class.java]
        if(intent.getStringExtra("category")!=null){
            category = intent.getStringExtra("category")!!
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
        binding.toolbar.title.text = Util.firstCapitalize(category)
        binding.revListData.layoutManager = LinearLayoutManager(this)
        binding.revListData.adapter = fastAdapter
        binding.revListData.animation = null
        binding.revListData.isNestedScrollingEnabled = false

        viewModel.sources.observe(this){ resource ->
            when (resource) {
                is Resource.Loading -> showLoading(resource.state)
                is Resource.Success -> loadData(resource.data)
                is Resource.Failure -> showDataError(resource.errorMessage)
            }
        }

        viewModel.getSources(category)

        setListener()
    }

    override fun setListener() {

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
        //configure the itemAdapter
        adapter.itemFilter.filterPredicate = { item: AdapterSource, constraint: CharSequence? ->
            if(constraint.toString().trim().length == 0){
                true
            }else{
                var sentence = constraint.toString().toLowerCase()
                var isFilter = false
                if(item.data!!.name!!.toLowerCase().contains(sentence)){
                    isFilter = true
                }else if(item.data!!.description!!.toLowerCase().contains(sentence)){
                    isFilter = true
                }

                isFilter
            }
        }
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
                adapter.filter(search)
                Util.hideKeyboard(this@SourceActivity)
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
            adapter.filter("")
            Util.hideKeyboard(this@SourceActivity)
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            var intent = Intent(this@SourceActivity, ArticleActivity::class.java)
            intent.putExtra("source",item.data?.id)
            intent.putExtra("sourceName",item.data?.name)
            startActivity(intent)
            false
        }
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

    fun loadData(data : ArrayList<Source>){
        binding.progress.visibility = View.GONE
        binding.searchLayout.visibility = View.VISIBLE
        binding.revListData.visibility = View.VISIBLE
        binding.textDataNotFound.visibility = View.GONE
        data.forEach {
            var data = AdapterSource(this)
            data.data = it
            adapter.add(data)
        }
    }

    fun showDataError(message : String? = null){
        binding.progress.visibility = View.GONE
        binding.revListData.visibility = View.GONE
        binding.searchLayout.visibility = View.GONE
        binding.textDataNotFound.visibility = View.VISIBLE
        if(message!=null){
            binding.textDataNotFound.text = message
        }
    }

}