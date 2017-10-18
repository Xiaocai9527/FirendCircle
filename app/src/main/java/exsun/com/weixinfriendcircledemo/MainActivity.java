package exsun.com.weixinfriendcircledemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import exsun.com.weixinfriendcircledemo.adapter.HomeRecyclerViewAdapter;
import exsun.com.weixinfriendcircledemo.entity.CircleData;
import exsun.com.weixinfriendcircledemo.entity.CircleItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    List<CircleItem> mCircleItems;
    private HomeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCircleItems = new ArrayList<>();
        adapter = new HomeRecyclerViewAdapter(R.layout.item_circle_list, mCircleItems);
        recyclerView.setAdapter(adapter);
        loadData();
    }

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
}
