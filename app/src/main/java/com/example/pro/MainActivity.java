package com.example.pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button skin, lung, brain , breast,blood , colon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        skin = findViewById(R.id.skin);
        lung = findViewById(R.id.lung);
        brain = findViewById(R.id.brain);
        breast = findViewById(R.id.breast);
        blood = findViewById(R.id.blood);
        colon=findViewById(R.id.colon);

        skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1=new Intent(MainActivity.this, skin.class);
                startActivity(int1);
            }
        });
        lung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int2=new Intent(MainActivity.this, com.example.pro.lung.class);
                startActivity(int2);
            }
        });
        brain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int3=new Intent(MainActivity.this, brain.class);
                startActivity(int3);

            }
       });
        breast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int4=new Intent(MainActivity.this, com.example.pro.Breastc.class);
                startActivity(int4);
            }
        });
        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int5=new Intent(MainActivity.this, com.example.pro.bloodc.class);
                startActivity(int5);
            }
        });
        colon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int6=new Intent(MainActivity.this, com.example.pro.colonc.class);
                startActivity(int6);
            }
        });


    }
}