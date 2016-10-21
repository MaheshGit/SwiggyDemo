package example.mks.swiggy;

import android.app.Application;
import android.content.Context;

/**
 * Created by Mahesh on 19/10/16.
 */

public class SwiggyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
