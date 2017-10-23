package exsun.com.weixinfriendcircledemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import exsun.com.weixinfriendcircledemo.spannable.ISpanClick;
import exsun.com.weixinfriendcircledemo.adapter.FavortListAdapter;

/**
 * @author xiaokun
 * @date 2017/10/18
 */

public class FavortListView extends AppCompatTextView
{
    private ISpanClick mSpanClickListener;

    public FavortListView(Context context)
    {
        super(context);
    }

    public FavortListView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FavortListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setSpanClickListener(ISpanClick listener)
    {
        this.mSpanClickListener = listener;
    }

    public ISpanClick getSpanClickListener()
    {
        return mSpanClickListener;
    }

    public void setAdapter(FavortListAdapter adapter)
    {
        adapter.bindListView(this);
    }
}
