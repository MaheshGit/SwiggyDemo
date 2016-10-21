package example.mks.swiggy.network.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh on 19/10/16.
 */

public class SwiggyRestaurants {
    private List<Restaurant> restaurants = new ArrayList<Restaurant>();

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
