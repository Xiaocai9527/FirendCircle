package exsun.com.weixinfriendcircledemo.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.stetho.common.LogUtil;

import java.util.List;

import exsun.com.weixinfriendcircledemo.R;
import exsun.com.weixinfriendcircledemo.entity.CircleItem;
import exsun.com.weixinfriendcircledemo.entity.CommentConfig;
import exsun.com.weixinfriendcircledemo.entity.CommentItem;
import exsun.com.weixinfriendcircledemo.entity.FavortItem;
import exsun.com.weixinfriendcircledemo.util.DateUtils;
import exsun.com.weixinfriendcircledemo.util.ImageLoaderUtils;
import exsun.com.weixinfriendcircledemo.widget.CommentListView;
import exsun.com.weixinfriendcircledemo.widget.ExpandableTextView;
import exsun.com.weixinfriendcircledemo.widget.FavortListView;

/**
 * @author xiaokun
 * @date 2017/10/18
 */

public class HomeRecyclerViewAdapter extends BaseQuickAdapter<CircleItem, BaseViewHolder>
{

    private CommentListAdapter commentListAdapter;
    private List<CommentItem> replys;
    private int layoutPosition;
    private CircleItem mCircleItem;
    private CommentListView commentListView;

    public HomeRecyclerViewAdapter(@LayoutRes int layoutResId, @Nullable List<CircleItem> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CircleItem item)
    {
        this.mCircleItem = item;
        ImageView avatar = helper.getView(R.id.headIv);
        TextView nickName = helper.getView(R.id.nameTv);
        TextView time = helper.getView(R.id.timeTv);
        TextView deleteBtn = helper.getView(R.id.deleteBtn);
        TextView url = helper.getView(R.id.urlTipTv);
        ExpandableTextView expandableTextView = helper.getView(R.id.contentTv);
        final FavortListView favortListView = helper.getView(R.id.favortListTv);
        commentListView = helper.getView(R.id.commentList);
        TextView favortBtn = helper.getView(R.id.favortBtn);
        TextView address = helper.getView(R.id.tv_address_or_distance);
        View view = helper.getView(R.id.lin_dig);
        TextView commentBtn = helper.getView(R.id.commentBtn);

        ImageLoaderUtils.displayRound(mContext, avatar, item.getIcon());
        nickName.setText(item.getNickName());
        time.setText(DateUtils.format(item.getCreateTime(), "yyyy-MM-dd"));
        expandableTextView.setText(item.getContent());
        url.setVisibility(View.GONE);
        address.setText(item.getAddress());

        LogUtil.e(System.currentTimeMillis() + "");
        final List<FavortItem> goodjobs = item.getGoodjobs();
        replys = item.getReplys();

        boolean hasFavort = goodjobs.size() > 0 ? true : false;
        boolean hasReply = replys.size() > 0 ? true : false;
        view.setVisibility(hasFavort && hasReply ? View.VISIBLE : View.GONE);
        Log.e("layoutPosision", "position:" + helper.getLayoutPosition() + "; hasFavort:" + hasFavort +
                "; hasComment:" + hasReply + ";size:" + goodjobs.size());
        final FavortListAdapter favortListAdapter = new FavortListAdapter(mContext, goodjobs);
        favortListView.setAdapter(favortListAdapter);
        commentListAdapter = new CommentListAdapter(mContext, replys);
        commentListView.setAdapter(commentListAdapter);
        //有点赞
        if (hasFavort)
        {
            favortListView.setVisibility(View.VISIBLE);
            favortListView.setAdapter(favortListAdapter);
            favortListAdapter.setDatas(goodjobs);
        } else
        {
            favortListView.setVisibility(View.GONE);
        }
        //有评论
        if (hasReply)
        {
            commentListView.setVisibility(View.VISIBLE);
            commentListView.setAdapter(commentListAdapter);
            commentListAdapter.setDatas(replys);
        } else
        {
            commentListView.setVisibility(View.GONE);
        }
        layoutPosition = helper.getLayoutPosition();
        //点赞
        favortBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FavortItem item1 = new FavortItem();
                item1.setPublishId(item.getId());
                item1.setUserId(item.getUserId());
                item1.setUserNickname(item.getNickName());
                int size = goodjobs.size();
                if (size == 0)
                {
                    goodjobs.add(item1);
                    favortListView.setVisibility(View.VISIBLE);
                } else
                {
                    for (int i = 0; i < size; i++)
                    {

                    }
                }
                item.setGoodjobs(goodjobs);
                favortListAdapter.setDatas(goodjobs);

                LogUtil.e("layoutPosision:" + layoutPosition);
                setData(layoutPosition, item);
            }
        });

        //发表评论
        commentBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CommentConfig config = new CommentConfig();
                config.circlePosition = helper.getLayoutPosition();
                config.commentType = CommentConfig.Type.PUBLIC;
                config.setPublishId(item.getId());
                config.setPublishUserId(item.getUserId());
                config.setName(item.getNickName());
                config.setCommentPosition(layoutPosition);
                mShowEditListener.onShowEdit(View.VISIBLE, config);
            }
        });
    }

    private ShowEditListener mShowEditListener;

    public void setShowEditListener(ShowEditListener showEditListener)
    {
        this.mShowEditListener = showEditListener;
    }

    public interface ShowEditListener
    {
        void onShowEdit(int visible, CommentConfig commentConfig);
    }

    public void updateComentListView(CommentItem commentItem, int position)
    {
        replys.add(commentItem);
        mCircleItem.setReplys(replys);
//        commentListAdapter.setDatas(replys);
        setData(position - 1, mCircleItem);
        commentListView.setVisibility(View.VISIBLE);
    }

}
