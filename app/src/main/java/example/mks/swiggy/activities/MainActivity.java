package example.mks.swiggy.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import example.mks.swiggy.R;
import example.mks.swiggy.adapters.RestaurantListAdapter;
import example.mks.swiggy.network.SwiggyApiManager;
import example.mks.swiggy.network.model.response.Restaurant;
import example.mks.swiggy.network.model.response.SwiggyRestaurants;
import example.mks.swiggy.utility.CommonUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static String TAG = MainActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    private ProgressDialog progress;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeLayout;
    private int page = 0;
    private boolean loading = false;
    private boolean end_of_page = false;
    private ArrayList<Restaurant> restaurantsList = new ArrayList<>();
    private RestaurantListAdapter adapter;
    private int visibleThreshold = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    if (end_of_page || loading)
                        return;

                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                    if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        requestNextPage();
                    }
                }
            }
        });
        adapter = new RestaurantListAdapter(restaurantsList);
        mRecyclerView.setAdapter(adapter);
        requestNextPage();
        showLoading();
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "on refresh");
        if (loading) {
            Log.i(TAG, "already getting Restaurants ");
        } else {
            end_of_page = false;
            page = 0;
            adapter.clearDataSource();
            adapter.notifyDataSetChanged();
            requestNextPage();
        }
    }

    private void showLoading() {
        try {
            progress = ProgressDialog.show(this, null, "Please wait...");
            progress.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissLoading() {
        try {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestNextPage() {
        page++;
        getRestaurants(page);
        loading = true;
        swipeLayout.setRefreshing(true);
    }

    private void setEndOfPage() {
        loading = false;
        swipeLayout.setRefreshing(false);
        end_of_page = true;
    }


    public void getRestaurants(final int pageNumber) {
        Call<SwiggyRestaurants> call = SwiggyApiManager.getApiManager().getSwiggyRestaurants(pageNumber);
        call.enqueue(new Callback<SwiggyRestaurants>() {
            @Override
            public void onResponse(Call<SwiggyRestaurants> call, Response<SwiggyRestaurants> response) {
                dismissLoading();
                Log.i("Restaurants", response.body().getRestaurants().get(0).getAvg_rating());
                if (200 == response.code()) {
                    if (response.body().getRestaurants().size() == 0) {
                        setEndOfPage();
                        if (page == 1) {
                            CommonUtilities.showToastMessage(MainActivity.this, "No Restaurants available in your area");
                        } else {
                            CommonUtilities.showToastMessage(MainActivity.this, "No more Restaurants available");
                        }
                    } else {
                        adapter.addToDataSrc((ArrayList<Restaurant>) response.body().getRestaurants());
                        adapter.notifyDataSetChanged();
                        loading = false;
                        swipeLayout.setRefreshing(false);
                    }
                } else {
                    setEndOfPage();
                }
            }

            @Override
            public void onFailure(Call<SwiggyRestaurants> call, Throwable t) {
                dismissLoading();
                setEndOfPage();
            }
        });
    }
}
