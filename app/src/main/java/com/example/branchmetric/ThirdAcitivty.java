package com.example.branchmetric;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ThirdAcitivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_acitivty);


        SharedPreferences settings = getSharedPreferences("score_pref", 0);
        int score = settings.getInt("score", 0);
        String name=settings.getString("name", "SomeOne");

        TextView tv3=findViewById(R.id.tv3);
        tv3.setText(name+" your Score so far is: "+score);

    }
}