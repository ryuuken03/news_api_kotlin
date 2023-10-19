package mohammad.toriq.newsapi.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/***
 * Created By Mohammad Toriq on 18/10/2023
 */
data class Source (

    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    @SerializedName("category")
    @Expose
    var category: String? = null,

    @SerializedName("language")
    @Expose
    var language: String? = null,

    @SerializedName("country")
    @Expose
    var country: String? = null,

)