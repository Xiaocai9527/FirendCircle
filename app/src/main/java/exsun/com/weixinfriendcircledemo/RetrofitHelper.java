package exsun.com.weixinfriendcircledemo;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import exsun.com.weixinfriendcircledemo.api.ApiService;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 肖坤 on 2017/9/25.
 * company：exsun
 * email：838494268@qq.com
 */

public class RetrofitHelper
{
    private static final OkHttpClient okHttpClient = null;
    private static ApiService apiService;
    private static Cache cache;
    private static int CONNECT_TIME = 10;
    private static int READ_TIME = 20;
    private static int WRITE_TIME = 20;


    public static ApiService getApiService()
    {
        OkHttpClient okHttpClient = initOkHttp();
        if (apiService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ApiService.HOST)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
            return apiService;
        }
        return apiService;
    }

    /**
     * 初始化okhttp
     */
    private static OkHttpClient initOkHttp()
    {
        if (okHttpClient == null)
        {
            HttpLoggingInterceptor loggingInterceptor = getLoggingInterceptor(HttpLoggingInterceptor.Level.BASIC);
//            Interceptor cacheInterceptor = getCacheInterceptor();
            return new OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
//                    .addNetworkInterceptor(cacheInterceptor)
//                    .addInterceptor(cacheInterceptor)
                    .cache(cache)
                    .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }
        return okHttpClient;
    }

    /**
     * 获取打印拦截器
     *
     * @param level
     * @return
     */
    private static HttpLoggingInterceptor getLoggingInterceptor(HttpLoggingInterceptor.Level level)
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(level);
        return interceptor;
    }

//    /**
//     * 获取缓存拦截器
//     *
//     * @return
//     */
//    private static Interceptor getCacheInterceptor()
//    {
//        File cacheFile = new File(Constants.PATH_CACHE);
//        cache = new Cache(cacheFile, 1024 * 1024 * 50);
//        return new Interceptor()
//        {
//            @Override
//            public Response intercept(Chain chain) throws IOException
//            {
//                Request request = chain.request();
//                if (!SystemUtils.isNetworkConnected())
//                {
//                    //强制请求使用缓存
//                    request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
//                }
//                int tryCount = 0;
//                Response response = chain.proceed(request); //继续新请求，返回response
//                while (!response.isSuccessful() && tryCount < 3)
//                {
//                    tryCount++;
//                    response = chain.proceed(request);
//                }
//                if (SystemUtils.isNetworkConnected())
//                {
//                    int maxTime = 0;//设置缓存时间
//                    response.newBuilder().header("Cache-Control", "public, max-age=" + maxTime)
//                            .removeHeader("Pragma").build();
//                } else
//                {
//                    int maxTime = 60 * 60 * 24 * 7 * 4;//设置缓存时间4周
//                    response.newBuilder().header("Cache-Control", "public, max-age=" + maxTime)
//                            .removeHeader("Pragma").build();
//                }
//                return response;
//            }
//        };
//    }

}
