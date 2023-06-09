package com.example.branchmetric;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * An easy wrapper class for handling color picker functionality for monster views
 */
public class ColorController {

    private Activity activity_;
//    private MonsterImageView monsterImageView_;
    // The buttons
    private Button[] cmdColors;

    /* The color tubs contain one colour button each. They are slightly bigger than the Button
    * that they contain, and the background is the outline that is seen around a selected color. */
    private LinearLayout[] cmdColorTubs;

    public static int index;

//    public ColorController(Activity activity, MonsterImageView monsterImageView){
    public ColorController(Activity activity){
        activity_ = activity;
//        monsterImageView_ = monsterImageView;
    }
    public void start(){
//        final  prefs = MonsterPreferences.getInstance(activity_.getApplicationContext());


        cmdColors = new Button[]{
                activity_.findViewById(R.id.cmdColor0),
                activity_.findViewById(R.id.cmdColor1),
                activity_.findViewById(R.id.cmdColor2),
                activity_.findViewById(R.id.cmdColor3),
                activity_.findViewById(R.id.cmdColor4),
                activity_.findViewById(R.id.cmdColor5),
                activity_.findViewById(R.id.cmdColor6),
                activity_.findViewById(R.id.cmdColor7)
        };

        cmdColorTubs = new LinearLayout[]{
                activity_.findViewById(R.id.cmdColorTub0),
                activity_.findViewById(R.id.cmdColorTub1),
                activity_.findViewById(R.id.cmdColorTub2),
                activity_.findViewById(R.id.cmdColorTub3),
                activity_.findViewById(R.id.cmdColorTub4),
                activity_.findViewById(R.id.cmdColorTub5),
                activity_.findViewById(R.id.cmdColorTub6),
                activity_.findViewById(R.id.cmdColorTub7)
        };


        // Iterate through the color buttons and set their color based on the predefined options.
        for (int i = 0; i < cmdColors.length; i++) {
            Button cmdColor = cmdColors[i];
            TypedArray ta = activity_.getResources().obtainTypedArray(R.array.colors);
            cmdColor.setBackgroundColor(ta.getColor(i, 0xFF24A4DD));
            ta.recycle();

            // Assign a click listener for each.
            cmdColor.setOnClickListener(new Button.OnClickListener() {
                @Override public void onClick(View v) {
                    int idx = Integer.parseInt((String)v.getTag());
//                    prefs.setColorIndex(idx);
//                    monsterImageView_.updateColor(idx);
                    setSelectedColourButton(idx);

                }
            });

            cmdColor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override public void onFocusChange(View v, boolean hasFocus) {
                    int idx = Integer.parseInt((String)v.getTag());
//                    if (idx == prefs.getColorIndex()) {
                    if(idx==1){
                        cmdColorTubs[idx].setBackground(activity_.getResources().getDrawable(R.drawable.colour_button_on));
                    } else {
                        cmdColorTubs[idx].setBackground(activity_.getResources().getDrawable(
                                hasFocus ? R.drawable.colour_button_focused : R.drawable.colour_button_off));
                    }
                }
            });
        }

        setSelectedColourButton(1);
    }

    private void setSelectedColourButton(int idx){
        index=idx;
        // Deselect all colour buttons.
        for (int j = 0; j < cmdColors.length; j++) {
            cmdColorTubs[j].setBackground(activity_.getResources().getDrawable(R.drawable.colour_button_off));
        }
        // Re-select the chosen colour button.
        cmdColorTubs[idx].setBackground(activity_.getResources().getDrawable(R.drawable.colour_button_on));
    }

}
