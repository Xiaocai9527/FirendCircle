package exsun.com.weixinfriendcircledemo;

import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exsun.com.weixinfriendcircledemo.adapter.HomeRecyclerViewAdapter;
import exsun.com.weixinfriendcircledemo.adapter.HomeRecyclerViewAdapter.ShowEditListener;
import exsun.com.weixinfriendcircledemo.entity.CircleData;
import exsun.com.weixinfriendcircledemo.entity.CircleItem;
import exsun.com.weixinfriendcircledemo.entity.CommentConfig;
import exsun.com.weixinfriendcircledemo.entity.CommentItem;
import exsun.com.weixinfriendcircledemo.util.DisplayUtil;
import exsun.com.weixinfriendcircledemo.util.KeyBordUtil;
import exsun.com.weixinfriendcircledemo.widget.CommentListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ShowEditListener
{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    List<CircleItem> mCircleItems;
    @Bind(R.id.circleEt)
    EditText circleEt;
    @Bind(R.id.sendIv)
    ImageView sendIv;
    @Bind(R.id.editTextBodyLl)
    LinearLayout editTextBodyLl;
    private HomeRecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private int mSelectCircleItemTop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mCircleItems = new ArrayList<>();
        adapter = new HomeRecyclerViewAdapter(R.layout.item_circle_list, mCircleItems);
        adapter.setShowEditListener(this);
        View headView = View.inflate(this, R.layout.head_view, null);
        adapter.addHeaderView(headView);
        recyclerView.setAdapter(adapter);
        //监听recyclerview滑动
        setViewTreeObserver();
        //加载数据
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData()
    {
        Call<CircleData> circleItems = RetrofitHelper.getApiService().getCircleItems();
        circleItems.enqueue(new Callback<CircleData>()
        {
            @Override
            public void onResponse(Call<CircleData> call, Response<CircleData> response)
            {
                mCircleItems = response.body().getList();
                adapter.setNewData(mCircleItems);
            }

            @Override
            public void onFailure(Call<CircleData> call, Throwable t)
            {

            }
        });
    }

    private CommentConfig mCommentConfig;

    @OnClick(R.id.sendIv)
    public void onViewClicked()
    {
        String content = circleEt.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {

        }

        //添加文字content
        CommentItem commentItem = new CommentItem(mCommentConfig.getName(), mCommentConfig.getId(),
                content, mCommentConfig.getPublishId(), "10000", "依迅");
        adapter.updateComentListView(commentItem, mCommentConfig.getCirclePosition());
    }

    @Override
    public void onShowEdit(int visible, CommentConfig commentConfig)
    {
        mCommentConfig = commentConfig;
        editTextBodyLl.setVisibility(visible);

        measureCircleItemHighAndCommentItemOffset(commentConfig);
        if (commentConfig != null && CommentConfig.Type.REPLY.equals(commentConfig.getCommentType()))
        {
            circleEt.setHint("回复" + commentConfig.getName() + ":");
        } else
        {
            circleEt.setHint("说点什么吧");
        }
        if (View.VISIBLE == visible)
        {
            circleEt.requestFocus();
            //弹出键盘
            KeyBordUtil.showSoftKeyboard(circleEt);
            //隐藏菜单
        } else if (View.GONE == visible)
        {
            //隐藏键盘
            KeyBordUtil.hideSoftKeyboard(circleEt);
            //显示菜单
        }

    }

    private int mSelectCircleItemH;
    private int mSelectCommentItemOffset;


    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig)
    {
        if (commentConfig == null)
        {
            return;
        }

        int headViewCount = adapter.getHeaderLayoutCount();
        //当前选中的view
        int selectPostion = commentConfig.circlePosition;
        View selectCircleItem = linearLayoutManager.findViewByPosition(selectPostion);

        if (selectCircleItem != null)
        {
            mSelectCircleItemTop = selectCircleItem.getTop();
            mSelectCircleItemH = selectCircleItem.getHeight() - DisplayUtil.dip2px(48);
//            mSelectCircleItemH = selectCircleItem.getHeight();
            Log.e("MainActivity", "screenHmSelectCircleItemH：" + mSelectCircleItemH + " $selectPosition:" + selectPostion +
                    " $headViewCount:" + headViewCount);
            //获取评论view,计算出该view距离所属动态底部的距离
            if (commentConfig.commentType == CommentConfig.Type.REPLY)
            {
                //回复评论的情况
                CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
                if (commentLv != null)
                {
                    //找到要回复的评论view,计算出该view距离所属动态底部的距离
                    View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                    if (selectCommentItem != null)
                    {
                        //选择的commentItem距选择的CircleItem底部的距离
                        mSelectCommentItemOffset = 0;
                        View parentView = selectCommentItem;
                        do
                        {
                            int subItemBottom = parentView.getBottom();
                            parentView = (View) parentView.getParent();
                            if (parentView != null)
                            {
                                mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                            }
                        }
                        while (parentView != null && parentView != selectCircleItem);
                    }
                }
            }
        }
    }

    private int mCurrentKeyboardH;
    private int mScreenHeight;
    private int mEditTextBodyHeight;

    private void setViewTreeObserver()
    {
        final ViewTreeObserver swipeRefreshLayoutVTO = recyclerView.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                Rect r = new Rect();
                recyclerView.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = recyclerView.getRootView().getHeight();
                if (r.top != statusBarH)
                {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.e("MainActivity", "screenH＝ " + screenH + " &keyboardH = " + keyboardH +
                        " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);
                if (keyboardH == mCurrentKeyboardH)
                {//有变化时才处理，否则会陷入死循环
                    return;
                }
                mCurrentKeyboardH = keyboardH;
                mScreenHeight = screenH;//应用屏幕的高度
                mEditTextBodyHeight = editTextBodyLl.getHeight();


                //偏移listview
                if (recyclerView != null && mCommentConfig != null)
                {
                    final int index = mCommentConfig.circlePosition;
//                    linearLayoutManager.scrollToPositionWithOffset(index, getListviewOffset(mCommentConfig));
                    int min = mSelectCircleItemTop - getActionBarHeight();

                    if (min < 0)
                    {
                        min = 0;
                    }
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(min, getListviewOffset(mCommentConfig));
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                    {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation)
                        {
                            int animatedValue = (int) animation.getAnimatedValue();
                            linearLayoutManager.scrollToPositionWithOffset(index, animatedValue);
                        }
                    });
                    valueAnimator.start();
                }
            }
        });
    }

    private int getListviewOffset(CommentConfig commentConfig)
    {
        if (commentConfig == null)
        {
            return 0;
        }
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight - getActionBarHeight();
        if (commentConfig.commentType == CommentConfig.Type.REPLY)
        {
            //回复评论的情况
            listviewOffset = listviewOffset + mSelectCommentItemOffset - getActionBarHeight();
        }
        return listviewOffset;
    }

    /**
     * 获取ActionBar高度
     *
     * @return ActionBar高度
     */
    private int getActionBarHeight()
    {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            //方法一
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            Log.d("PopupListView", "tv.data=" + tv.data + ",actionBarHeight=" + actionBarHeight);

            //方法二
            int[] attribute = new int[]{android.R.attr.actionBarSize};
            TypedArray array = obtainStyledAttributes(tv.resourceId, attribute);
            int actionBarHeight1 = array.getDimensionPixelSize(0 /* index */, -1 /* default size */);
            array.recycle();

            //方法三
            TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            float actionBarHeight2 = actionbarSizeTypedArray.getDimension(0, 0);
            actionbarSizeTypedArray.recycle();
        }
        return actionBarHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onBackPressed()
    {
        if (editTextBodyLl.getVisibility() == View.VISIBLE)
        {
            editTextBodyLl.setVisibility(View.GONE);
        } else
        {
            super.onBackPressed();
        }
    }
}
