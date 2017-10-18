package exsun.com.weixinfriendcircledemo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import exsun.com.weixinfriendcircledemo.entity.CircleItem;

/**
 * @author xiaokun
 * @date 2017/10/18
 */

public class HomeRecyclerViewAdapter extends BaseQuickAdapter<CircleItem, BaseViewHolder>
{
    public HomeRecyclerViewAdapter(@LayoutRes int layoutResId, @Nullable List<CircleItem> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CircleItem item)
    {

    }
}
