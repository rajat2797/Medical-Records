package com.example.rajat.medics;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rajat on 7/3/17.
 */

public class MyProgressTask extends AsyncTask<String, Integer, Bitmap> {

    Context ctx;
    ProgressDialog pd;

    public MyProgressTask(Context ct){
        ctx = ct;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String u = params[0];
        InputStream stream;

        try{
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            stream = conn.getInputStream();
            Bitmap myMap = BitmapFactory.decodeStream(stream);

            return myMap;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(ctx);
        pd.setTitle("Fetching Data");
        pd.setMessage("Please Wait...");
        pd.setMax(100);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                cancel(true);
            }
        });

    }

}
