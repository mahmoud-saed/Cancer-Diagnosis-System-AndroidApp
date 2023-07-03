package com.example.pro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView txtv=(TextView)findViewById(R.id.explane);
        TextView txtv2=(TextView)findViewById(R.id.rec);
        Bundle b=getIntent().getExtras();
        String explane=b.getString("Explanation");
        String rec=b.getString("Reccomendation");
        txtv.setText(explane);
        txtv2.setText(rec);




    }
}