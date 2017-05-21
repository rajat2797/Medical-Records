package com.example.rajat.medics;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);

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
            Toast.makeText(this, "Please fill in the details!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void login(View view) {
        final String username = uname.getText().toString().trim();
        final String password = upassword.getText().toString().trim();
        Log.d("username = ", username);
        Log.d("password = ", password);
        if(!validate(username,password)){
            return;
        }
        if( networkInfo != null) {
            Log.d("**51**", "network not null");
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    //"https://medics.herokuapp.com/app/loginUser/",
                    url + "loginUser/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(getApplicationContext(), "" + response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if(obj.getString("status").equals("success")){
                                    finish();
                                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
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
                    }){
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

    public void registerscreen(View view) {
        Intent i = new Intent(LoginActivity.this, RegisterationActivity.class);
        startActivity(i);
    }
}
