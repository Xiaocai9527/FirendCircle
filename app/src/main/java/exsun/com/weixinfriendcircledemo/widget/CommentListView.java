package exsun.com.weixinfriendcircledemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import exsun.com.weixinfriendcircledemo.adapter.CommenAdapter;

/**
 * Created by 肖坤 on 2017/10/17.
 * company: exsun
 * email: 838494268@qq.com
 */

public class CommentListView extends LinearLayout
{
    private CommenAdapter commenAdapter;

    interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    interface OnItemLongClickListener
    {
        void onItemLongClick();
    }

    public CommentListView(Context context)
    {
        super(context);
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(CommenAdapter adapter)
    {
        this.commenAdapter = adapter;
        commenAdapter.bindListView(this);
    }

}
