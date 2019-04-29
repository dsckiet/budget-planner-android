package tech.dsckiet.budgetbucket;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static tech.dsckiet.budgetbucket.Notification.channel_ID;

public class BackgroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service is starting", Toast.LENGTH_SHORT).show();
        if(intent.getStringExtra("inputExtra") != null) {
            String input = intent.getStringExtra("inputExtra");

            Intent notificationIntent = new Intent(this, AddCashTransactionActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification mBuilder =
                    new NotificationCompat.Builder(this,channel_ID)
                            .setSmallIcon(R.drawable.ic_add_red)
                            .setContentTitle("Budget Bucket")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(pendingIntent)
                            .setOnlyAlertOnce(true)
                            .setContentText("Transaction of Rs. " +input + " has been recorded.")
                            .setAutoCancel(true)
                            .build();
            SharedPreferences prefs = getSharedPreferences("DATA",0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("OnlineAmount", input); //InputString: from the EditText
            editor.commit();

        startForeground(1,mBuilder);
        }
        //   stopSelf();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
