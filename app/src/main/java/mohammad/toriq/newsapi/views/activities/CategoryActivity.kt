package mohammad.toriq.newsapi.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
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
import mohammad.toriq.newsapi.util.Util
import java.util.Timer
import java.util.TimerTask

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class CategoryActivity : AppCompatActivity() , InitializerUi {

    lateinit var binding: ActivityCategoryBinding
    lateinit var adapter: ItemAdapter<AdapterCategory>
    lateinit var fastAdapter: FastAdapter<AdapterCategory>
    private var isFirst = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
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
                            showLoading(false)
                        }
                    }
                }, 500)
            }
        }
    }

    override fun initConfig() {
        adapter = ItemAdapter()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        if(isFirst){
            isFirst = false
        }
        binding.toolbar.btnBack.visibility = View.GONE
        binding.toolbar.view.visibility = View.GONE
        binding.toolbar.title.text = "Category "+getString(R.string.app_name)
        binding.toolbar.title.gravity = Gravity.CENTER
        binding.revListData.layoutManager = LinearLayoutManager(this)
        binding.revListData.adapter = fastAdapter
        binding.revListData.animation = null
        binding.revListData.isNestedScrollingEnabled = false

        loadData()

        setListener()
    }

    override fun setListener() {

        fastAdapter.onClickListener = { view, adapter, item, position ->
            var intent = Intent(this@CategoryActivity, SourceActivity::class.java)
            intent.putExtra("category",item.value)
            startActivity(intent)
            false
        }
    }

    fun showLoading(loading : Boolean){
        if(loading){
            binding.progress.visibility = View.VISIBLE
            binding.revListData.visibility = View.GONE
            binding.textDataNotFound.visibility = View.GONE
        }else{
            binding.progress.visibility = View.GONE
            binding.revListData.visibility = View.VISIBLE
            binding.textDataNotFound.visibility = View.GONE
        }
    }

    fun loadData(){
        showLoading(false)
        var categories = resources.getStringArray(R.array.categories)
        categories.forEach {
            var data = AdapterCategory(this)
            data.name = Util.firstCapitalize(it)
            data.value = it
            adapter.add(data)
        }
    }

    fun showDataError(message : String? = null){
        binding.progress.visibility = View.GONE
        binding.revListData.visibility = View.GONE
        binding.textDataNotFound.visibility = View.VISIBLE
        if(message!=null){
            binding.textDataNotFound.text = message
        }
    }

}