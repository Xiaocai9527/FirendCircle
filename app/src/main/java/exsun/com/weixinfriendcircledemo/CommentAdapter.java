package exsun.com.weixinfriendcircledemo;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * @author xiaokun
 * @date 2017/10/17
 */

public class CommentAdapter extends CommenAdapter<CommentItem>
{
    public CommentAdapter(Context context)
    {
        super(context);
    }

    public CommentAdapter(Context context, List<CommentItem> datas)
    {
        super(context, datas);
    }

    @Override
    public View getView(int position)
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

        return null;
    }

    private SpannableString setClickableSpan(String textStr, String userId, int position)
    {
        SpannableString spannableString = new SpannableString(textStr);
        spannableString.setSpan(new NameClickListener(spannableString, userId), 0, textStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


}
