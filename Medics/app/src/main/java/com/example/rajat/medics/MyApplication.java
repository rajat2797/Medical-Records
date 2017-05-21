package com.example.rajat.medics;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by rajat on 24/4/17.
 */

public class MyApplication extends Application{
//    public static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;
    private static Context mCtx;

    private MyApplication(Context ctx){
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance(Context ctx) {
        if(mInstance == null){
            mInstance = new MyApplication(ctx);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
