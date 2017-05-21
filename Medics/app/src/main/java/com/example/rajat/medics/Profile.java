package com.example.rajat.medics;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    EditText Name,adhaar;
    Button bSubmit,bProceed;
    RadioGroup rg;
    RadioButton rb;
    DatePicker dp;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    String name,dob,adhaarId,gender,uId;
    int day,month,year,selectId;
    // change ip as per your wifi network
    String url = "https://medicsrecords.herokuapp.com/app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Name = (EditText)findViewById(R.id.name);
        adhaar = (EditText)findViewById(R.id.adhaarId);
        dp = (DatePicker) findViewById(R.id.datePicker);
        rg = (RadioGroup) findViewById(R.id.rg);
        bSubmit = (Button) findViewById(R.id.submit);
        bProceed = (Button) findViewById(R.id.proceed);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        uId = getIntent().getStringExtra("uId");
        bProceed.setVisibility(View.INVISIBLE);

        makePostrequest();

    }

    public void submit(View v){
        name = Name.getText().toString();
        day = dp.getDayOfMonth();
        month = dp.getMonth()+1;
        year = dp.getYear();
        selectId = rg.getCheckedRadioButtonId();
        if(selectId == R.id.male){
            gender="male";
        }
        else{
            gender="female";
        }
        dob = "" + day + "/" + month + "/" + year;
        adhaarId = adhaar.getText().toString();
        if(networkInfo!=null){

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    url + "userProfile/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if(obj.getString("status").equals("success")){
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    bProceed.setVisibility(View.VISIBLE);
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
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("uId", uId);
                    params.put("name",name);
                    params.put("gender",gender);
                    params.put("dob",dob);
                    params.put("adhaarId",adhaarId);
                    params.put("type", "update");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(this, "Internet Required", Toast.LENGTH_SHORT).show();
        }
    }

    public void makePostrequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url + "userProfile/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("success")){
                                Name.setText(obj.getString("name"));
                                adhaar.setText(obj.getString("adhaarId"));
                                if(obj.getString("gender").equals("male")) {
                                    rg.check(R.id.male);
                                } else {
                                    rg.check(R.id.female);
                                }

                                String dobFromData = obj.getString("dob");
                                Integer Date = Integer.parseInt("" + dobFromData.charAt(0) + dobFromData.charAt(1));
                                Integer Month = Integer.parseInt("" + dobFromData.charAt(3) + dobFromData.charAt(4));
                                Integer Year = Integer.parseInt("" + dobFromData.charAt(6) + dobFromData.charAt(7) + dobFromData.charAt(8) + dobFromData.charAt(9));
                                dp.updateDate(Date, Month, Year);
                                Toast.makeText(getApplicationContext(), "" + Date, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT)
                            .show();
                }
            }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uId", uId);
                params.put("type", "get");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void gotomain(View view) {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("uId", uId);
        startActivity(myIntent);
    }
}
