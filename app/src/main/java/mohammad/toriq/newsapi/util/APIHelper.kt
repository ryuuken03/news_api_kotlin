package mohammad.toriq.newsapi.util

import mohammad.toriq.newsapi.models.ResponseArticles
import mohammad.toriq.newsapi.models.ResponseSources
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/***
 * Created By Mohammad Toriq on 18/10/2023
 */
interface APIHelper {

    @GET("top-headlines/sources")
    fun getSources(
        @Query(value = "apikey", encoded = true) apikey: String,
        @Query(value = "category", encoded = true) category: String,
        @Query(value = "language", encoded = true) language: String? = null,
    ): Call<ResponseSources>

    @GET("everything")
    fun getSourceArticles(
        @Query(value = "apikey", encoded = true) apikey: String,
        @Query(value = "sources", encoded = true) sources: String? = null,
        @Query(value = "q", encoded = true) q: String? = null,
        @Query(value = "page", encoded = true) page: Int,
        @Query(value = "pageSize", encoded = true) pageSize: Int,
        @Query(value = "language", encoded = true) language: String? = null,
    ): Call<ResponseArticles>
}