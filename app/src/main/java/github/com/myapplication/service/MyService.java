package github.com.myapplication.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import github.com.myapplication.R;
import github.com.myapplication.activity.MainActivity;
import github.com.myapplication.firebase.helper.ShopCrud;
import github.com.myapplication.model.Shop;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MyService extends Service{

    private Context context = this;
    private static final double lat= 53.813887;
    private static final double lon= 23.847896;
    private static List<Shop> shops = new ArrayList<>();
    private Thread thread;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ShopCrud.getAllShops(shops);
        someTask();
        return super.onStartCommand(intent, flags, startId);
    }

    private void someTask(){

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
            startLocationListener();
            }
        });

        thread.start();

    }


    private static double taylorCos(double x) {
        double yy = x * x;
        double zz = yy;
        return 1 - (yy) / 2 + (zz *= yy) / 24;
    }

    private static double taylorSin(double x) {
        double yy = x * x;
        double zz = x;
        return zz - (zz *= yy) / 6 + (zz *= yy) / 120;
    }


    private static double distance(double a, double b, double c, double d) {
        int earthRadius = 6372795;
        double pi180 = 0.017453;
        double zz = 1, yy = 1;
        double lat1 = a * pi180;
        double lat2 = c * pi180;
        double long1 = b * pi180;
        double long2 = d * pi180;
        double cl1 = taylorCos(lat1);
        double cl2 = taylorCos(lat2);
        double sl1 = taylorSin(lat1);
        double sl2 = taylorSin(lat2);
        double delta = long2 - long1;
        double cdelta = taylorCos(delta);
        double sdelta = taylorSin(delta);
        double tmp = cl2 * cdelta;
        double y = Math.sqrt(cl2 * cl2 * sdelta * sdelta + (cl1 * sl2 - sl1 * tmp) * (cl1 * sl2 - sl1 * tmp));
        double x = sl1 * sl2 + cl1 * tmp;
        double ad = Math.atan2(y, x);

        return (double) (int)Math.round(ad * earthRadius);
    }

    private void startLocationListener() {

        long mLocTrackingInterval = 1000 * 10;
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(context)
                .location()
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        for (int i = 0; i < shops.size(); i++) {
                            github.com.myapplication.model.Location locationShop = shops.get(i).getLocation();
                            int radius = 200;
                            if(distance(location.getLatitude(),location.getLongitude(),locationShop.getLat(), locationShop.getLon())< radius)showMessage();
                        }
                    }
                });
    }

    private void showMessage(){
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle("My notification")
                        .setContentText("Купи продукты :)");
        final Intent resultIntent = new Intent(this, MainActivity.class);
        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert mNotificationManager != null;
        mNotificationManager.notify(1, mBuilder.build());
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SmartLocation.with(context).location().stop();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
