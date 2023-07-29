package kr.co.fastcampus.co.kr.coroutines.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kr.co.fastcampus.co.kr.coroutines.api.NaverImageSearchService
import kr.co.fastcampus.co.kr.coroutines.model.Item
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NaverImageSearchRepository {
    private val service: NaverImageSearchService

    init {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

//        curl "https://openapi.naver.com/v1/search/image?query=%EC%A3%BC%EC%8B%9D&display=10&start=1&sort=sim" \
//        -H "X-Naver-Client-Id: XXXX" \
//        -H "X-Naver-Client-Secret: YYYY" -v
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
//                -H "X-Naver-Client-Id: {애플리케이션 등록 시 발급받은 클라이언트 아이디 값}" \
//                -H "X-Naver-Client-Secret: {애플리케이션 등록 시 발급받은 클라이언트 시크릿 값}"-v
                val request = chain.request().newBuilder()
                    .addHeader("X-Naver-Client-Id", " TbJZsFesw0UbqtpdGMs9")
                    .addHeader("X-Naver-Client-Secret", "Lt1jwVfvQB")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logger)
            .build()

        service = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverImageSearchService::class.java)
    }

    fun getImageSearch(query: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = NaverImageSearchDataSource.defaultDisplay,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NaverImageSearchDataSource(query, service)
            }
        ).flow
    }
}