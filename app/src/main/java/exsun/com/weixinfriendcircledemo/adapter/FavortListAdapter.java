package exsun.com.weixinfriendcircledemo.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import java.util.List;

import exsun.com.weixinfriendcircledemo.CircleMovementMethod;
import exsun.com.weixinfriendcircledemo.NameClickable;
import exsun.com.weixinfriendcircledemo.R;
import exsun.com.weixinfriendcircledemo.entity.FavortItem;
import exsun.com.weixinfriendcircledemo.widget.FavortListView;

/**
 * 点赞列表适配器
 *
 * @author xiaokun
 * @date 2017/10/18
 */

public class FavortListAdapter extends CommenAdapter<FavortItem>
{
    public FavortListAdapter(Context context)
    {
        super(context);
    }

    public FavortListAdapter(Context context, List<FavortItem> datas)
    {
        super(context, datas);
    }

    @Override
    public void notifyDataSetChanged()
    {
        if (mListView2 == null)
        {
            throw new NullPointerException("mListView2 is null, please bindView first...");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (mDatas != null && mDatas.size() > 0)
        {
            //首先添加一个点赞爱心
            builder.append(setImgSpan());

            int size = mDatas.size();
            FavortItem item = null;
            for (int i = 0; i < size; i++)
            {
                item = mDatas.get(i);
                if (item != null)
                {
                    builder.append(setClickableSpan(item.getUserNickname(), i));
                    if (i != mDatas.size() - 1)
                    {
                        builder.append(", ");
                    }
                }
            }
        }
        ((FavortListView) mListView2).setText(builder);
        //span能点击的关键方法
        ((FavortListView) mListView2).setMovementMethod(new CircleMovementMethod(R.color.circle_name_selector_color));
    }

    private SpannableString setClickableSpan(String textStr, int position)
    {
        SpannableString spannableString = new SpannableString(textStr);
        spannableString.setSpan(new NameClickable(((FavortListView) mListView2).getSpanClickListener(), position),
                0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置图片span
     *
     * @return
     */
    private SpannableString setImgSpan()
    {
        String text = " ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(mContext, R.drawable.dianzansmal, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }
}
