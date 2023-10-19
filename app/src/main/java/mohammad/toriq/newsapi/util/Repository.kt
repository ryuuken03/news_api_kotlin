package mohammad.toriq.newsapi.util

import android.content.Context
import mohammad.toriq.newsapi.models.ResponseArticles
import mohammad.toriq.newsapi.models.ResponseSources
import retrofit2.Call

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
class Repository (context: Context) {
    var service = Util.getAPIHelper(context)

    fun getSources(apikey : String,
          category : String,
          language : String?= null): Call<ResponseSources>? {
        return service?.getSources(
            apikey,
            category,
            language
        )
    }

    fun getSourceArticles(apikey : String,
          sources : String?,
          q : String?,
          page : Int,
          pageSize : Int,
          language : String?= null): Call<ResponseArticles>? {
        return service?.getSourceArticles(
            apikey,
            sources,
            q,
            page,
            pageSize,
            language
        )
    }
}