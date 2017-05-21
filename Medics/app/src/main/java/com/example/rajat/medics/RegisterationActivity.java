package com.example.rajat.medics;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterationActivity extends AppCompatActivity {

    TextView t1,t2,tvlogin;
    EditText uname, upassword;
    Button b;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    // change ip as per your wifi network
    String url = "https://medicsrecords.herokuapp.com/app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        t1 = (TextView) findViewById(R.id.title);
        t2 = (TextView) findViewById(R.id.textView);
        tvlogin = (TextView) findViewById(R.id.textViewlogin);
        uname = (EditText) findViewById(R.id.username);
        upassword = (EditText) findViewById(R.id.password);
        b = (Button) findViewById(R.id.submit);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

    }

    public boolean validate(String name, String password){
        if(name==null || password==null){
            Toast.makeText(this, "74", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Please fill in the details!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.length() < 8 ){
            Toast.makeText(this, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.toLowerCase().contains(name.toLowerCase())){
            Toast.makeText(this, "Password is too similar to the username!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.matches("[0-9]+")){
            Toast.makeText(this, "Password should not be only numeric!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void register(View view) {
        final String username = uname.getText().toString().trim();
        final String password = upassword.getText().toString().trim();
        Log.d("username = ", username);
        Log.d("password = ", password);
        if(!validate(username,password)){
            return;
        }
        if( networkInfo != null) {
            Log.d("**55**", "network not null");
            //new HttpAsyncTask().execute(username, password);

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    //"https://medics.herokuapp.com/app/createUser/"
                    url + "createUser/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(getApplicationContext(), "" + response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if(obj.getString("status").equals("success")){
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent myIntent = new Intent(getApplicationContext(), Profile.class);
                                    myIntent.putExtra("uId", obj.getString("uId"));
                                    startActivity(myIntent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(com.android.volley.error.VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(this, "Internet Required", Toast.LENGTH_SHORT).show();
        }
    }

   /* private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream stream = null;
            OutputStream os = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try{
                URL url = new URL("https://medics.herokuapp.com/app/createUser/"
                        "https://192.168.2.7:8080/app/createUser/");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", params[0]);
                jsonObject.put("password", params[1]);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));

                writer.write(getPostDataString(jsonObject));
                writer.flush();
                writer.close();

                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String input;
                while((input = reader.readLine())!=null){
                    buffer.append(input + "\n");
                }
                if(buffer.length() > 0 ) {
                    return buffer.toString();
                }
                return "check logs";

            }
            catch (Exception e){
                Log.i("Exception", e.getMessage());
                return new String("Exception: " + e.getMessage().toString());

            } finally {
                if(connection!=null) {
                    connection.disconnect();
                }
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }*/

    public void loginscreen(View view) {
        Intent i = new Intent(RegisterationActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
