package com.example.rajat.medics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TwolistAct extends AppCompatActivity implements View.OnClickListener{

    TextView t1,t2;
    String name;
    static String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twolist);
        t1 = (TextView) findViewById(R.id.tests);
        t2 = (TextView) findViewById(R.id.prescription);

        name = getIntent().getStringExtra("name");
        uId = getIntent().getStringExtra("uId");

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Toast.makeText(getApplicationContext(), "Name = "+name, Toast.LENGTH_SHORT).show();
        if(v.getId() == R.id.tests){
            Intent intent = new Intent(getApplicationContext(), Tests.class);
            intent.putExtra("name", name);
            intent.putExtra("uId", uId);
//            Toast.makeText(getApplicationContext(), "Tests requested", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), Prescribed.class);
            intent.putExtra("name", name);
            intent.putExtra("uId", uId);
//            Toast.makeText(getApplicationContext(), "Prescription requested", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
}
