package exsun.com.weixinfriendcircledemo.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.stetho.common.LogUtil;

import java.util.List;

import exsun.com.weixinfriendcircledemo.spannable.ISpanClick;
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
    /**
     * 评论实体类一一对应，防止错乱
     */
    private SparseArray<List<CommentItem>> listSparseArray = new SparseArray<>();
    /**
     * 朋友圈实体类一一对应，防止错乱
     */
    private SparseArray<CircleItem> circleSpareArray = new SparseArray<>();
    /**
     * 评论view
     */
    private CommentListView commentListView;
    /**
     * 评论view的adapter
     */
    private CommentListAdapter commentListAdapter;
    /**
     * 是否点赞标志位
     */
    private boolean favortFlag;
    /**
     * 是否评论标志位
     */
    private boolean commentFlag;
    /**
     * 默认并非第一次才有动画
     */
    private boolean isFirst;

    private ShowEditListener mShowEditListener;

    public interface ShowEditListener
    {
        void onShowEdit(int visible, CommentConfig commentConfig);
    }

    public HomeRecyclerViewAdapter(@LayoutRes int layoutResId, @Nullable List<CircleItem> data)
    {
        super(layoutResId, data);
    }

    /**
     * 设置编辑接口
     *
     * @param showEditListener
     */
    public void setShowEditListener(ShowEditListener showEditListener)
    {
        this.mShowEditListener = showEditListener;
    }

    /**
     * 设置是否一次性动画
     *
     * @param isFirst
     */
    public void setFirst(boolean isFirst)
    {
        this.isFirst = isFirst;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CircleItem item)
    {

        if (!isFirst && !favortFlag && !commentFlag)
        {
            LinearLayout llRoot = helper.getView(R.id.ll_root);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(llRoot, "scaleX", 0.1f, 1.0f),
                    ObjectAnimator.ofFloat(llRoot, "scaleY", 0.1f, 1.0f));
            set.setDuration(500).start();
            favortFlag = false;
            commentFlag = false;
        }

        circleSpareArray.append(helper.getLayoutPosition(), item);
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
        LinearLayout llComment = helper.getView(R.id.ll_comment);

        ImageLoaderUtils.displayRound(mContext, avatar, item.getIcon());
        nickName.setText(item.getNickName());
        time.setText(DateUtils.format(item.getCreateTime(), "yyyy-MM-dd"));
        expandableTextView.setText(item.getContent());
        url.setVisibility(View.GONE);
        address.setText(item.getAddress());

        LogUtil.e(System.currentTimeMillis() + "");
        final List<FavortItem> goodjobs = item.getGoodjobs();
        final List<CommentItem> replys = item.getReplys();
        listSparseArray.append(helper.getLayoutPosition(), replys);
        boolean hasFavort = goodjobs.size() > 0 ? true : false;
        boolean hasReply = replys.size() > 0 ? true : false;
        view.setVisibility(hasFavort && hasReply ? View.VISIBLE : View.GONE);
        Log.e("layoutPosision", "position:" + helper.getLayoutPosition() + "; hasFavort:" + hasFavort +
                "; hasComment:" + hasReply + ";size:" + goodjobs.size());
        final FavortListAdapter favortListAdapter = new FavortListAdapter(mContext, goodjobs);
        favortListView.setAdapter(favortListAdapter);
        commentListAdapter = new CommentListAdapter(mContext, replys);
        commentListView.setAdapter(commentListAdapter);
        favortListView.setSpanClickListener(new ISpanClick()
        {
            @Override
            public void onClick(int position)
            {
                Toast.makeText(mContext, "跳转至依迅北斗个人页面", Toast.LENGTH_SHORT).show();
            }
        });
        //有点赞
        if (hasFavort)
        {
            //
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

        //点赞
        favortBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                favortFlag = true;
                FavortItem item1 = new FavortItem();
                item1.setPublishId(item.getId());
                item1.setUserId(item.getUserId());
                item1.setUserNickname("依迅北斗");
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

                LogUtil.e("layoutPosision:" + helper.getLayoutPosition());
                //刷新data数据源时，需要去掉头布局
                setData(helper.getLayoutPosition() - getHeaderLayoutCount(), item);
            }
        });

        //发表评论
        commentBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commentFlag = true;
                CommentConfig config = new CommentConfig();
                config.circlePosition = helper.getLayoutPosition();
                config.commentType = CommentConfig.Type.PUBLIC;
                config.setPublishId(item.getId());
                config.setPublishUserId(item.getUserId());
                config.setName(item.getNickName());
                config.setCommentPosition(helper.getLayoutPosition());
                mShowEditListener.onShowEdit(View.VISIBLE, config);
            }
        });

        //发表评论
        llComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commentFlag = true;
                CommentConfig config = new CommentConfig();
                config.circlePosition = helper.getLayoutPosition();
                config.commentType = CommentConfig.Type.PUBLIC;
                config.setPublishId(item.getId());
                config.setPublishUserId(item.getUserId());
                config.setName(item.getNickName());
                config.setCommentPosition(helper.getLayoutPosition());
                mShowEditListener.onShowEdit(View.VISIBLE, config);
            }
        });

        //单击评论区回复别人，或复制自己评论或删除自己评论
        commentListAdapter.setOnItemClickListener(new CommentListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                if ("10000".equals(replys.get(position).getUserId()))
                { //自己时复制或者删除
                    Toast.makeText(mContext, "单击了自己的评论", Toast.LENGTH_SHORT).show();
                } else
                {//回复别人
                    commentFlag = true;
                    CommentConfig config = new CommentConfig();
                    config.circlePosition = helper.getLayoutPosition();
                    config.commentType = CommentConfig.Type.REPLY;
                    config.setPublishId(item.getId());
                    config.setId(item.getUserId());
                    config.setPublishUserId(item.getUserId());
                    config.setName(replys.get(position).getUserNickname());
                    config.setCommentPosition(position);
                    mShowEditListener.onShowEdit(View.VISIBLE, config);
                }
            }
        });

        //长按 复制或者回复
        commentListAdapter.setOnItemLongClickListener(new CommentListAdapter.OnItemLongClickListener()
        {
            @Override
            public void onItemLongClick(int position)
            {

            }
        });
    }


    /**
     * 更新评论区
     *
     * @param commentItem
     * @param position
     */
    public void updateComentListView(CommentItem commentItem, int position)
    {
        List<CommentItem> commentItems = listSparseArray.get(position);
        CircleItem circleItem = circleSpareArray.get(position);
        commentItems.add(commentItem);
        circleItem.setReplys(commentItems);
        LogUtil.e("layoutPosision:" + position);
        //刷新data数据源时，需要去掉头布局
        setData(position - getHeaderLayoutCount(), circleItem);
        commentListView.setVisibility(View.VISIBLE);

    }

}
