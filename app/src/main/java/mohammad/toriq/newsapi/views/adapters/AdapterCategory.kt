package mohammad.toriq.newsapi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mohammad.toriq.newsapi.R
import mohammad.toriq.newsapi.databinding.ItemAdapterCategoryBinding
import mohammad.toriq.newsapi.util.InitializerUi

/***
 * Created By Mohammad Toriq on 16/02/2023
 */
open class AdapterCategory(context: Context) : AbstractBindingItem<ItemAdapterCategoryBinding>(), InitializerUi{
    var context: Context = context
    var id: Long? = null
    var name: String? = null
    var value: String? = null

    lateinit var binding: ItemAdapterCategoryBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_category

    override fun bindView(binding: ItemAdapterCategoryBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterCategoryBinding {
        return ItemAdapterCategoryBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.category.text = name
        setListener()
    }

    override fun setListener() {
    }

}