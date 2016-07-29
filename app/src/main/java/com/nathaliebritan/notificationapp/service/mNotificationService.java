package com.nathaliebritan.notificationapp.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nathaliebritan.notificationapp.R;
import com.nathaliebritan.notificationapp.activity.MainActivity;
import com.nathaliebritan.notificationapp.custom_interface.IRetrofitManager;
import com.nathaliebritan.notificationapp.entity.PlayListsEntity;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Nathalie Britan on 29.07.2016.
 */
public class mNotificationService extends Service {
    private static final String KEY_SHARED_PREFERENCES = "SHARED_PREFERENCES";
    private static final String KEY_LISTS_NUMBER= "COUNT";
    private static final String LOG = "Service";
    private static final String URL_PLAY_LISTS = "snippet,contentDetails";
    private static final String CHANNEL_ID = "UCDPM_n1atn2ijUwHd0NNRQw";
    private static final String APP_KEY = "AIzaSyCO5Pv7-2h4x5oh6FoKnWFIbJRnBQD30J4";

    private Handler mHandler;
    private SharedPreferences mSharedPreferences;


    public mNotificationService() {
    }

    @Override
    public void onCreate() {
        mSharedPreferences = getSharedPreferences(KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_LISTS_NUMBER, 0);
        editor.commit();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new Handler();
        ping();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void ping() {
        try {
            getPlaylistInformation();
        } catch (Exception e) {
            Log.e(LOG, "In onStartCommand");
            e.printStackTrace();
        }
        scheduleNext();
    }

    private void scheduleNext() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                ping();
            }
        }, 60000);
    }

    private void Notification(int text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setAutoCancel(false);
        builder.setContentTitle("YouTube Notification");
        builder.setContentText("You have" + String.valueOf(text) + "new video(s)");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setOngoing(true);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());
    }



    private void getPlaylistInformation() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IRetrofitManager.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IRetrofitManager mRetrofitManager = retrofit.create(IRetrofitManager.class);
        Call<PlayListsEntity> chanelPlaylists = mRetrofitManager.getPlayLists(URL_PLAY_LISTS, CHANNEL_ID, APP_KEY);

        chanelPlaylists.enqueue(new Callback<PlayListsEntity>() {
            @Override
            public void onResponse(Call<PlayListsEntity> call, Response<PlayListsEntity> response) {
                int count = 0;
                count = response.body().items.get(0).getContentDetails().getItemCount();
                if (count > mSharedPreferences.getInt(KEY_LISTS_NUMBER, 0)) {
                    Notification(mSharedPreferences.getInt(KEY_LISTS_NUMBER, 0) - count);
                }
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt(KEY_LISTS_NUMBER, count);
                editor.commit();
            }

            @Override
            public void onFailure(Call<PlayListsEntity> call, Throwable t) {
                Log.d(LOG, "FAILURE GET PLAYLIST");
            }
        });
    }
}
