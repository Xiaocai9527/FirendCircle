package exsun.com.weixinfriendcircledemo.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import exsun.com.weixinfriendcircledemo.CircleMovementMethod;
import exsun.com.weixinfriendcircledemo.NameClickable;
import exsun.com.weixinfriendcircledemo.entity.CommentItem;
import exsun.com.weixinfriendcircledemo.NameClickListener;
import exsun.com.weixinfriendcircledemo.R;

/**
 * 评论列表适配器
 *
 * @author xiaokun
 * @date 2017/10/17
 */

public class CommentListAdapter extends CommenAdapter<CommentItem>
{
    public CommentListAdapter(Context context)
    {
        super(context);
    }

    public CommentListAdapter(Context context, List<CommentItem> datas)
    {
        super(context, datas);
    }

    @Override
    public void notifyDataSetChanged()
    {
        if (mListView == null)
        {
            throw new NullPointerException("mListView is null, please bindView first...");
        }
        //这里可以改进,一刀切可以不是一个好办法
        mListView.removeAllViews();
        if (mDatas == null || mDatas.size() == 0)
        {
            return;
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int size = mDatas.size();
        for (int i = 0; i < size; i++)
        {
            View view = getView(i);
            //把子view添加进去,addView内部对child是否为空做了处理
            mListView.addView(view, i, params);
        }
    }

    private View getView(int position)
    {
        View view = View.inflate(mContext, R.layout.item_social_comment, null);
        TextView textView = (TextView) view.findViewById(R.id.commentTv);
        CircleMovementMethod circleMovementMethod = new CircleMovementMethod(R.color.circle_name_selector_color, R.color.circle_name_selector_color);
        CommentItem commentItem = mDatas.get(position);
        //昵称
        String userNickname = commentItem.getUserNickname();
        //被回复人的昵称
        String toReplyName = "";
        if (commentItem.getAppointUserid() != null)
        {
            toReplyName = commentItem.getAppointUserNickname();
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        //添加回复者名字  设置颜色和点击事件通过ClickableSpan SpannableString
        builder.append(setClickableSpan(userNickname, commentItem.getUserId(), position));

        if (!TextUtils.isEmpty(toReplyName))
        {
            //添加回复
            builder.append(" 回复 ");
            //添加被回复者名字  设置颜色和点击事件通过ClickableSpan SpannableString
            builder.append(setClickableSpan(toReplyName, commentItem.getAppointUserNickname(), position));
        }
        //添加冒号：
        builder.append(": ");
        //回复内容
        String content = commentItem.getContent();
        builder.append(content);
        textView.setText(builder);

        return view;
    }

    private SpannableString setClickableSpan(String textStr, String userId, int position)
    {
        SpannableString spannableString = new SpannableString(textStr);
        spannableString.setSpan(new NameClickable(new NameClickListener(spannableString, userId), position),
                0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
