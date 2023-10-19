package mohammad.toriq.newsapi.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mohammad.toriq.newsapi.R
import mohammad.toriq.newsapi.models.Article
import mohammad.toriq.newsapi.models.ResponseArticles
import mohammad.toriq.newsapi.models.ResponseSources
import mohammad.toriq.newsapi.models.Source
import mohammad.toriq.newsapi.util.Repository
import mohammad.toriq.newsapi.util.Util
import mohammad.toriq.views.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * Created By Mohammad Toriq on 19/10/2023
 */
class ArticleViewModel(
    context : Context
): ViewModel() {
    private val context = context
    private val repository =  Repository(context)
    private val apikey  = context.getString(R.string.apikey)
    val _articles = MutableLiveData<Resource<ResponseArticles>>()
    val articles : LiveData<Resource<ResponseArticles>>
        get() = _articles

    fun getSourceArticles(sources:String, q:String?= null, page:Int = 1, pageSize:Int = 20) = viewModelScope.launch {
        _articles.postValue(Resource.Loading(true))
        try {
            repository.getSourceArticles(apikey,sources,q, page, pageSize)?.enqueue(
                object : Callback<ResponseArticles> {
                    override fun onResponse(
                        call: Call<ResponseArticles>,
                        response: Response<ResponseArticles>
                    ) {
                        _articles.postValue(Resource.Loading(false))
                        if (response.isSuccessful) {
                            var tmp = response.body()!!
                            _articles.postValue(Resource.Success(tmp))
                        }else{
                            _articles.postValue(Resource.Failure(Util.parseError(response).toString()))
                        }
                    }
                    @SuppressLint("ResourceType")
                    override fun onFailure(call: Call<ResponseArticles>, t: Throwable) {
                        _articles.postValue(Resource.Loading(false))
                        var errorMessage = Util.parseThrowable(context,t)
                        _articles.postValue(Resource.Failure(errorMessage))
                    }
                }
            )
        } catch (e: Exception) {
            _articles.postValue(Resource.Failure(e.message.toString()))
        } finally {
            _articles.postValue(Resource.Loading(false))
        }
    }
}