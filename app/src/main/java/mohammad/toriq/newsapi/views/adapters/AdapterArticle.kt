package mohammad.toriq.newsapi.views.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mohammad.toriq.configs.Constants
import mohammad.toriq.newsapi.R
import mohammad.toriq.newsapi.databinding.ItemAdapterArticleBinding
import mohammad.toriq.newsapi.databinding.ItemAdapterCategoryBinding
import mohammad.toriq.newsapi.databinding.ItemAdapterSourceBinding
import mohammad.toriq.newsapi.models.Article
import mohammad.toriq.newsapi.models.Source
import mohammad.toriq.newsapi.util.InitializerUi
import mohammad.toriq.newsapi.util.Util

/***
 * Created By Mohammad Toriq on 16/02/2023
 */
open class AdapterArticle(context: Context) : AbstractBindingItem<ItemAdapterArticleBinding>(), InitializerUi{
    var context: Context = context
    var data: Article? = null

    lateinit var binding: ItemAdapterArticleBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_article

    override fun bindView(binding: ItemAdapterArticleBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterArticleBinding {
        return ItemAdapterArticleBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }
    fun changeFormatDate() : String{
        var inFormat = Constants.DATE_OUT_FORMAT_DEF0
        if(data?.publishedAt!!.contains(".")){
            inFormat = Constants.DATE_OUT_FORMAT_DEF1
        }
        return Util.convertDate(data?.publishedAt, inFormat, Constants.DATE_OUT_FORMAT_DEF3)
    }
    override fun initUI() {
        binding.title.text = data?.title
        var desc = if (data?.content != null) data?.content else data?.description
        binding.content.text = Util.loadHtmlView(desc!!,binding.content)
        if(data?.author!=null){
            binding.author.visibility = View.VISIBLE
            binding.author.text = "Author : "+Util.firstCapitalize(data?.author!!)
        }else{
            binding.author.visibility = View.GONE
        }
        binding.publish.text = "Publish : "+changeFormatDate()
        if(data?.urlToImage!=null){
            binding.image.visibility = View.VISIBLE
            Util.loadImage(
                context,
                data?.urlToImage!!,
                binding.image,
                "",
                1,
                0,
                0,
                1,
                false)
        }else{
            binding.image.visibility = View.GONE
        }
        setListener()
    }

    override fun setListener() {
    }

}