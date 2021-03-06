package com.mybitcoin.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.mybitcoin.model.CreateWalletModel;
import com.mybitcoin.util.Constants;
import com.mybitcoin.util.Utility;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class QueryManager {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static volatile QueryManager instance = null;

    // private constructor
    private QueryManager() {
    }

    public static QueryManager getInstance() {
        if (instance == null) {
            synchronized (QueryManager.class) {
                // Double check
                if (instance == null) {
                    instance = new QueryManager();
                }
            }
        }
        return instance;
    }

    /**
     * Get response from bock.io
     */
    public void getResponseBlockIO(Activity activity, String json, final CallbackListener callback) {
        if (isNetworkConnected(activity)) {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(json).cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(e, "");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onResult(null, result);
                            } catch (Exception e) {
                                callback.onResult(e, "");
                            }
                        }
                    });
                }
            });
        } else {
            Utility.showToast(activity, "Please check your network connection");
        }
    }

    /**
     * Create wallet
     */
    public void createWallet(CreateWalletModel model, final CallbackListener callback) {
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder();

            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("user_id", model.getUsrId());
            builder.addFormDataPart("name", model.getName());
            builder.addFormDataPart("email", model.getEmail());
            builder.addFormDataPart("password", model.getPassword());
            builder.addFormDataPart("wallet_address", model.getWalletAddress());
            builder.addFormDataPart("wallet_label", model.getWalletLabel());

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(Constants.MY_BASE_URL + Constants.METHOD_CREATE_WALLET)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, final IOException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(e, "");
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String result = response.body().string();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callback.onResult(null, result);
                            } catch (Exception e) {
                                callback.onResult(e, "");
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check internet connection
     */
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm != null && cm.getActiveNetworkInfo() != null;
        } else {
            return false;
        }
    }
}
