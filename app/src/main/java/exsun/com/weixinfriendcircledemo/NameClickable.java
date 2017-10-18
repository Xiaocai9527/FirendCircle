package exsun.com.weixinfriendcircledemo;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @author xiaokun
 * @date 2017/10/18
 */

public class NameClickable extends ClickableSpan implements View.OnClickListener
{

    private ISpanClick mListener;
    private int mPosition;

    public NameClickable(ISpanClick mListener, int mPosition)
    {
        this.mListener = mListener;
        this.mPosition = mPosition;
    }

    @Override
    public void onClick(View widget)
    {
        mListener.onClick(mPosition);
    }

    @Override
    public void updateDrawState(TextPaint ds)
    {
        super.updateDrawState(ds);
        int colorValue = App.mContext.getResources().getColor(R.color.main_color);
        //设置颜色
        ds.setColor(colorValue);
        //设置不要下划线
        ds.setUnderlineText(false);
        //清除阴影层
        ds.clearShadowLayer();
    }
}
