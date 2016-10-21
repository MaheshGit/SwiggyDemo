package example.mks.swiggy.network;

import example.mks.swiggy.network.model.response.SwiggyRestaurants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahesh on 19/10/16.
 */

public interface SwiggyApiService {
    @GET("bins/ngcc")
    Call<SwiggyRestaurants> getSwiggyRestaurants(@Query("page") int page);
}
