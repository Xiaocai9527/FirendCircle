package exsun.com.weixinfriendcircledemo.spannable;

import android.text.SpannableString;
import android.widget.Toast;

import exsun.com.weixinfriendcircledemo.App;

/**
 * @author xiaokun
 * @date 2017/10/17
 */

public class NameClickListener implements ISpanClick
{

    private SpannableString userName;
    private String userId;

    public NameClickListener(SpannableString userName, String userId)
    {
        this.userName = userName;
        this.userId = userId;
    }

    @Override
    public void onClick(int position)
    {
        Toast.makeText(App.mContext, "触发NameClickListener类中onClick方法", Toast.LENGTH_SHORT).show();
    }
}
