package example.mks.swiggy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.mks.swiggy.R;
import example.mks.swiggy.SwiggyApplication;
import example.mks.swiggy.network.model.response.Chain;
import example.mks.swiggy.network.model.response.Restaurant;
import example.mks.swiggy.utility.CommonUtilities;

/**
 * Created by Mahesh on 20/10/16.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Restaurant> mRestaurantList;
    String imageUploadUrl = "https://res.cloudinary.com/swiggy/image/upload/";
    boolean isListVisible = false;

    public RestaurantListAdapter(ArrayList<Restaurant> restaurantList) {
        mRestaurantList = restaurantList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RestaurantListAdapter.ViewHolderItem(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RestaurantListAdapter.ViewHolderItem holderItem = (RestaurantListAdapter.ViewHolderItem) holder;
        Restaurant restaurant = mRestaurantList.get(position);
        StringBuilder imageUrlBuilder = new StringBuilder(imageUploadUrl);
        imageUrlBuilder.append(restaurant.getCid());
        CommonUtilities.loadImage(SwiggyApplication.context, imageUrlBuilder.toString(), holderItem.imageView);
        holderItem.tvName.setText(restaurant.getName());
        holderItem.tvCasuine.setText(CommonUtilities.getCommaSeperatedString((ArrayList<String>) restaurant.getCuisine()));
        holderItem.tvRating.setText(restaurant.getAvg_rating());
        if ((restaurant.getChain() != null) && (restaurant.getChain().size() > 0)) {
            holderItem.outletLayout.setVisibility(View.VISIBLE);
            holderItem.tvOutletHeader.setText("" + restaurant.getChain().size() + " outlets for you");
        } else {
            holderItem.outletLayout.setVisibility(View.GONE);
        }
        if (restaurant.getDeliveryTime() > 0) {
            holderItem.tvDeliveryTime.setVisibility(View.VISIBLE);
            holderItem.watchItemImage.setVisibility(View.GONE);
            holderItem.tvDeliveryTime.setText(restaurant.getDeliveryTime());

        } else {
            holderItem.tvDeliveryTime.setVisibility(View.GONE);
            holderItem.watchItemImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mRestaurantList == null || mRestaurantList.size() == 0)
            return 0;
        return mRestaurantList.size();
    }

    public void addToDataSrc(ArrayList<Restaurant> items) {
        this.mRestaurantList.addAll(items);
    }

    public void clearDataSource() {
        this.mRestaurantList.clear();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView, downArrowImage, watchItemImage;
        TextView tvName, tvCasuine, tvAmount, tvRating, tvOutletHeader, tvDeliveryTime;
        RelativeLayout outletHeaderLayout;
        LinearLayout outerListLayout, outletLayout;

        ViewHolderItem(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            tvName = (TextView) v.findViewById(R.id.name);
            tvAmount = (TextView) v.findViewById(R.id.amount);
            tvRating = (TextView) v.findViewById(R.id.rating);
            tvCasuine = (TextView) v.findViewById(R.id.casuine);
            outletHeaderLayout = (RelativeLayout) v.findViewById(R.id.outletHeaderLayout);
            outletHeaderLayout.setOnClickListener(this);
            outerListLayout = (LinearLayout) v.findViewById(R.id.listLayout);
            tvOutletHeader = (TextView) v.findViewById(R.id.outletHeading);
            outletLayout = (LinearLayout) v.findViewById(R.id.outletLayout);
            downArrowImage = (ImageView) v.findViewById(R.id.down_arrow);
            tvDeliveryTime = (TextView) v.findViewById(R.id.deliveryTime);
            watchItemImage = (ImageView) v.findViewById(R.id.time);
        }

        @Override
        public void onClick(View v) {
            isListVisible = !isListVisible;
            float deg = (downArrowImage.getRotation() == 180F) ? 0F : 180F;
            downArrowImage.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            if (isListVisible) {
                outerListLayout.setVisibility(View.VISIBLE);
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    outerListLayout.removeAllViews();
                    Restaurant clickedDataItem = mRestaurantList.get(pos);
                    List<Chain> chains = clickedDataItem.getChain();
                    for (Chain chain : chains) {
                        View view = LayoutInflater.from(SwiggyApplication.context).inflate(R.layout.outlet_list_item, null);
                        TextView name = (TextView) view.findViewById(R.id.name);
                        name.setText(chain.getName());
                        TextView rating = (TextView) view.findViewById(R.id.rating);
                        rating.setText(chain.getAvg_rating());
                        TextView deliveryTime = (TextView) view.findViewById(R.id.deliveryTime);
                        ImageView timerImage = (ImageView) view.findViewById(R.id.time);
                        if(chain.getDeliveryTime()>0){
                            deliveryTime.setVisibility(View.VISIBLE);
                            deliveryTime.setText(chain.getDeliveryTime().toString());
                            timerImage.setVisibility(View.GONE);
                        }else{
                            deliveryTime.setVisibility(View.GONE);
                            timerImage.setVisibility(View.VISIBLE);
                        }
                        outerListLayout.addView(view);
                    }
                }
            } else {
                outerListLayout.setVisibility(View.GONE);
            }
        }
    }
}
