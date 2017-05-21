package com.example.rajat.medics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prescribed extends AppCompatActivity{
    String name, dataType = "prescription";
    private static final int PHOTO_PICK = 100;
    private static final int CAMERA_REQUEST = 200;
    FloatingActionButton fab;
    FrameLayout createFile, updateItem;
    Button media, camera;
    ImageView imgView;
    Uri selectedImage;
    static String uId;
    Bitmap bitmap;
    EditText imgName, imgDesc;
    GridView gridView;
    GridViewAdapter gridViewAdapter;
    List<ImageItem> imgData = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ShareActionProvider shareActionProvider;
    private GoogleApiClient client;
    // change ip as per your wifi network
    String url = "https://medicsrecords.herokuapp.com/app/";
//    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribed);

        /*sharedPreferences = this.getPreferences(MODE_PRIVATE);
        if(!sharedPreferences.contains("firstTime")) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }*/

        name = getIntent().getStringExtra("name");
        uId = getIntent().getStringExtra("uId");
        imgName = (EditText) findViewById(R.id.imgName);
        imgDesc = (EditText) findViewById(R.id.imgDesc);
        fab = (FloatingActionButton) findViewById(R.id.fabFiles);
        createFile = (FrameLayout) findViewById(R.id.createFile);
        media = (Button) findViewById(R.id.media);
        camera = (Button) findViewById(R.id.camera);
        imgView = (ImageView) findViewById(R.id.img);
        gridView = (GridView) findViewById(R.id.gridView);

        /*if(sharedPreferences.getBoolean("firstTime", true)) {
            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, getImageData());
            editor.putBoolean("firstTime", false);
        } else {
            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, imgData);
        }*/

        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, getImageData());
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        imgData.get(position).getTitle()
                                + " " + imgData.get(position).getDate()
                                + " " + imgData.get(position).getDesc(), Toast.LENGTH_SHORT).show();
                gridView.setVisibility(View.GONE);
                imgView.setVisibility(View.VISIBLE);
                imgName.setVisibility(View.VISIBLE);
                imgDesc.setVisibility(View.VISIBLE);
                createFile.setVisibility(View.VISIBLE);
                createFile.setBackgroundColor(Color.parseColor("#5180cc"));
                imgView.setImageBitmap(imgData.get(position).getImage());
                imgName.setText(imgData.get(position).getTitle());
                imgDesc.setText(imgData.get(position).getDesc());
                bitmap = imgData.get(position).getImage();
//                itemId = imgData.get(position).getId();
//                updateTheItem(position);
//                int count = gridViewAdapter.getCount();
//                gridViewAdapter.notifyDataSetChanged();
//                gridView.smoothScrollToPosition(count-1);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*private void updateTheItem(final int position) {
        createFile.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.2.7:8050/app/updateFile/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("success")){
                                Toast.makeText(Prescribed.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                imgName.setVisibility(View.GONE);
                                imgView.setVisibility(View.GONE);
                                imgDesc.setVisibility(View.GONE);
                                imgData.get(position).setImage(bitmap);
                                imgData.get(position).setTitle(imgName.getText().toString());
                                imgData.get(position).setDesc(imgDesc.getText().toString());
                            }
                            else{
                                Toast.makeText(Prescribed.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT)
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uId", uId);
                params.put("folderName", name);
                params.put("imgName", imgName.getText().toString());
                params.put("imgDesc", imgDesc.getText().toString());
                params.put("image", imageToString(bitmap));
                params.put("dataType", dataType);
                params.put("itemId", ""+itemId);
                return params;
            }
        };
        MyApplication.getInstance(Prescribed.this).addToRequestQueue(stringRequest);
    }*/

    public void newFile(View v) {
        gridView.setVisibility(View.GONE);
        createFile.setVisibility(View.VISIBLE);
        createFile.setBackgroundColor(Color.parseColor("#5180cc"));
    }

    public void choosePhoto(View v) {
        if (v.getId() == R.id.media) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_PICK);
        } else {
            Toast.makeText(getApplicationContext(), "Camera", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_PICK && resultCode == RESULT_OK) {
            try {
                selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imgView.setImageBitmap(bitmap);
                imgView.setVisibility(View.VISIBLE);
                imgName.setVisibility(View.VISIBLE);
                imgDesc.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgView.setImageBitmap(bitmap);
                imgView.setVisibility(View.VISIBLE);
                imgName.setVisibility(View.VISIBLE);
                imgDesc.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void uploadImage(View v) {
        createFile.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url + "uploadFile/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("success")) {
                                Toast.makeText(Prescribed.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                imgName.setVisibility(View.GONE);
                                imgView.setVisibility(View.GONE);
                                imgDesc.setVisibility(View.GONE);
                                imgData.add(new ImageItem(bitmap, imgName.getText().toString(), imgDesc.getText().toString(), null));
                                int count = gridViewAdapter.getCount();
                                gridViewAdapter.notifyDataSetChanged();
                                gridView.smoothScrollToPosition(count - 1);
                            } else {
                                Toast.makeText(Prescribed.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT)
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uId", uId);
                params.put("folderName", name);
                params.put("imgName", imgName.getText().toString());
                params.put("imgDesc", imgDesc.getText().toString());
                params.put("image", imageToString(bitmap));
                params.put("dataType", dataType);
                return params;
            }
        };
        MyApplication.getInstance(Prescribed.this).addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private Bitmap stringToImage(String string) {
        InputStream stream = new ByteArrayInputStream(Base64.decode(string.getBytes(), Base64.DEFAULT));
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }

    private List<ImageItem> getImageData() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url + "folderData/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("success")) {
                                JSONArray images = obj.getJSONArray("images");
                                JSONArray names = obj.getJSONArray("names");
                                JSONArray date = obj.getJSONArray("date");
                                JSONArray desc = obj.getJSONArray("desc");
//                                JSONArray id = obj.getJSONArray("id");
                                for (int i = 0; i < images.length(); i++) {
                                    Bitmap imgBitmap = stringToImage(images.getString(i));
                                    ImageItem item = new ImageItem(imgBitmap, names.getString(i), date.getString(i), desc.getString(i));
//                                    id.getInt(i));
                                    imgData.add(item);
                                }

                                int count = gridViewAdapter.getCount();
                                gridViewAdapter.notifyDataSetChanged();
                                if (count > 0) {
                                    gridView.smoothScrollToPosition(count - 1);
                                }

                            } else {
                                Toast.makeText(Prescribed.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT)
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uId", uId);
                params.put("folderName", name);
                params.put("dataType", dataType);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        if (imgData.size() > 0) {
            Log.d("Data", imgData.get(0).getTitle());
        }

        return imgData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

//        MenuItem item = menu.findItem(R.id.menu_item_share);

//        shareActionProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }

    /*private void setShareIntent() {
        if(shareActionProvider!=null){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, "Share Image"));


            shareActionProvider.setShareIntent(shareIntent);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                finish();
                Intent i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;

            case R.id.refresh:
                int count = gridViewAdapter.getCount();
                gridViewAdapter.notifyDataSetChanged();
                gridView.smoothScrollToPosition(count - 1);
                break;

            case R.id.profile:
                Intent intent = new Intent(this, Profile.class);
                intent.putExtra("uId", uId);
                startActivity(intent);
        }
        return true;
    }

    public void close(View view) {
        createFile.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgData.clear();
    }
}
