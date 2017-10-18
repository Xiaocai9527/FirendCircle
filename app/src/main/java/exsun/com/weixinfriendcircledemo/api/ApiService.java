package exsun.com.weixinfriendcircledemo.api;

import exsun.com.weixinfriendcircledemo.entity.CircleData;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author xiaokun
 * @date 2017/10/18
 */

public interface ApiService
{
    String HOST = "http://192.168.1.196:5638/";

    @GET("api/circle_datas")
    Call<CircleData> getCircleItems();

}
