package com.example.branchmetric;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;

public class SecondActivity extends AppCompatActivity {

    EditText editName;
    String name, receivername;
    String colorIndex;
    SharedPreferences settings;SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        new NewController(this).start();
        settings= getSharedPreferences("score_pref", 0);
        editor = settings.edit();
        String name_pref = settings.getString("name", null);
        editName = findViewById(R.id.editName);
        if(name_pref!=null){
            editName.setText(name_pref);
        }

        findViewById(R.id.generatelinkbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              afterFirstClick();
            }
        });

        colorIndex = getIntent().getExtras().getString("color","1");
        name = getIntent().getExtras().getString("name","SomeOne");
        TextView textViewFavColor=findViewById(R.id.TextViewFavColor);
        textViewFavColor.setText("Guess "+name+"'s favourite color");

    }
    void afterFirstClick(){

        if (editName.getText().toString().length() > 0) {
            receivername=editName.getText().toString();
            editor.putString("name",receivername);
            int score = settings.getInt("score", 0);

            if(NewController.index==Integer.valueOf(colorIndex)){
                TextView tv=findViewById(R.id.tv);
                tv.setText("Right answer! \n"+name+ "'s favourite color is");
                new BranchEvent(BRANCH_STANDARD_EVENT.ACHIEVE_LEVEL)
                        .setCustomerEventAlias("Right Answer!")
//                        .setDescription("tanmay")
//                        .setSearchQuery("product name")
                        .addCustomDataProperty(name, receivername)
                        .logEvent(this);
                editor.putInt("score",++score);
            }
            else {
                TextView tv=findViewById(R.id.tv);
                tv.setText("Wrong answer! \n"+name+ "'s favourite color is");
                new BranchEvent("LOST")
                        .setCustomerEventAlias("Wrong Answer!")
                        .addCustomDataProperty(name, receivername)
                        .logEvent(this);
            }
            editor.commit();
            TextView tv_score=findViewById(R.id.tv_score);
            tv_score.setText("Your Score so far is: "+score);
            View cmdColor;
            cmdColor=findViewById(R.id.viewcolor);
            TypedArray ta = getResources().obtainTypedArray(R.array.colors);
            cmdColor.setBackgroundColor(ta.getColor(Integer.valueOf(colorIndex) , 0xFF24A4DD));
            findViewById(R.id.lowerconstraitLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.generatelinkbutton).setVisibility(View.INVISIBLE);
            Button generateyourownlink=findViewById(R.id.generateyourownlinkbutton);
            generateyourownlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            Toast.makeText(getBaseContext(),"Please Enter your name first!",Toast.LENGTH_LONG).show();
        }

    }
}