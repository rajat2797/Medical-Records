package com.example.rajat.medics;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    RecyclerView rcview;
    FolderAdapter mfolderAdapter;
    EditText etName;
    static String uId;
    FrameLayout createFolder;
    List<String> folderNames = new ArrayList<>();
    // change ip as per your wifi network
    String url = "https://medicsrecords.herokuapp.com/app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFolder = (FrameLayout) findViewById(R.id.createFolder);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        rcview = (RecyclerView) findViewById(R.id.rcview);
        mfolderAdapter = new FolderAdapter(this, folderNames);
        rcview.setLayoutManager(new LinearLayoutManager(this));
        rcview.setAdapter(mfolderAdapter);

        etName = (EditText) findViewById(R.id.name);
        uId = getIntent().getStringExtra("uId");

        makeGetRequest();
    }

    public void newFolder(View v){
        rcview.setVisibility(View.GONE);
        createFolder.setVisibility(View.VISIBLE);
        createFolder.setBackgroundColor(Color.parseColor("#a7c68f"));
    }


    public void create(View view) {
        createFolder.setVisibility(View.GONE);
        rcview.setVisibility(View.VISIBLE);
        final String name = etName.getText().toString().trim();
        if(name!=null) {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    url + "createFolder/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("success")) {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                    makeGetRequest();
                                    folderNames.add(name);
                                    int count = mfolderAdapter.getItemCount();
                                    mfolderAdapter.notifyDataSetChanged();
                                    rcview.smoothScrollToPosition(count-1);

                                } else {
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
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("uId", uId);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(this, "Please Enter a Name",Toast.LENGTH_SHORT).show();
        }

    }

    private void makeGetRequest() {
        StringRequest stringrequest = new StringRequest(
                Request.Method.POST,
                url + "userFolder/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("success")){
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                JSONArray data = obj.getJSONArray("folders");
                                for(int i=0;i<data.length();i++) {
                                    folderNames.add(data.getString(i));
                                }
                                int count = mfolderAdapter.getItemCount();
                                mfolderAdapter.notifyDataSetChanged();
                                rcview.smoothScrollToPosition(count-1);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uId",uId);
                return params;
            }
        };
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(stringrequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.refresh:
                folderNames.clear();
                mfolderAdapter = new FolderAdapter(this, folderNames);
                makeGetRequest();
                break;

            case R.id.profile:
                Intent intent = new Intent(this, Profile.class);
                intent.putExtra("uId", uId);
                startActivity(intent);
        }
        return true;
    }

    public void close(View view) {
        createFolder.setVisibility(View.GONE);
        rcview.setVisibility(View.VISIBLE);
    }
}
