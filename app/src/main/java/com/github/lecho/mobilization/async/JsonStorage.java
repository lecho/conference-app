package com.github.lecho.mobilization.async;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Leszek on 02.10.2016.
 */

public interface JsonStorage {

    @GET()
    Call<ResponseBody> getFile(@Url String fileUrl);
}