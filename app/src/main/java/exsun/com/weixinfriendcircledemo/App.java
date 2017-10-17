package exsun.com.weixinfriendcircledemo;

import android.app.Application;
import android.content.Context;

/**
 * @author xiaokun
 * @date 2017/10/17
 */

public class App extends Application
{
    public static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
    }
}
