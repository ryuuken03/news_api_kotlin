package mohammad.toriq.newsapi.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
data class ResponseArticles (
    @SerializedName("totalResults")
    @Expose
    var totalResults : Long = 0,

    @SerializedName("articles")
    @Expose
    var articles : ArrayList<Article> = ArrayList<Article>(),
)