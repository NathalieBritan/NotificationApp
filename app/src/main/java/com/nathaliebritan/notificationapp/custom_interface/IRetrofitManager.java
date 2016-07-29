package com.nathaliebritan.notificationapp.custom_interface;

import com.nathaliebritan.notificationapp.entity.PlayListsEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nathalie Britan on 27.07.2016.
 */
public interface IRetrofitManager {
    String BASE_URL = "https://www.googleapis.com/youtube/v3/";

    @GET("playlists")
    Call<PlayListsEntity> getPlayLists(@Query("part") String part, @Query("channelId") String id, @Query("key") String key);

}
