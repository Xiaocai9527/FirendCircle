package exsun.com.weixinfriendcircledemo;

import android.text.SpannableString;

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

    }
}
