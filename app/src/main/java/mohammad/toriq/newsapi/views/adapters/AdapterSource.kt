package mohammad.toriq.newsapi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mohammad.toriq.newsapi.R
import mohammad.toriq.newsapi.databinding.ItemAdapterCategoryBinding
import mohammad.toriq.newsapi.databinding.ItemAdapterSourceBinding
import mohammad.toriq.newsapi.models.Source
import mohammad.toriq.newsapi.util.InitializerUi
import mohammad.toriq.newsapi.util.Util

/***
 * Created By Mohammad Toriq on 16/02/2023
 */
open class AdapterSource(context: Context) : AbstractBindingItem<ItemAdapterSourceBinding>(), InitializerUi{
    var context: Context = context
    var data: Source? = null

    lateinit var binding: ItemAdapterSourceBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_source

    override fun bindView(binding: ItemAdapterSourceBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterSourceBinding {
        return ItemAdapterSourceBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.source.text = data?.name
        binding.desc.text = data?.description
        binding.category.text = Util.firstCapitalize(data?.category!!)
        setListener()
    }

    override fun setListener() {
    }

}