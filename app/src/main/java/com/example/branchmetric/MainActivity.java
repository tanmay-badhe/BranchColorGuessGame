package com.example.branchmetric;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    SharedPreferences settings;SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ColorController(this).start();
        findViewById(R.id.generatelinkbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zzzz", "color index onclick: "+ColorController.index);
                generateLink();
            }
        });
    }

    private void setSavedName() {
        settings= getSharedPreferences("score_pref", 0);
        String name_pref = settings.getString("name", null);
        editName = findViewById(R.id.editName);
        if(name_pref!=null){
            editName.setText(name_pref);
        }
    }

    private void generateLink() {
        String name;
        editName = findViewById(R.id.editName);
        if (editName.getText().toString().length() > 0) {
            name=editName.getText().toString();

        } else {
            name=getString(R.string.your_name);
        }
        editor = settings.edit();
        editor.putString("name",name);
        editor.commit();
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()

                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier("item/12345")
                // This is where you define the open graph structure and how the object will appear on Facebook or in a deepview
                .setTitle(name+" has shared her favorite color")
                .setContentDescription("Guess and Click to view the color")
                .setContentImageUrl("https://is3-ssl.mzstatic.com/image/thumb/Purple114/v4/25/0f/61/250f6186-22db-75d1-0ce8-850cbaecbb33/AppIcon-0-0-1x_U007emarketing-0-0-0-10-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/1200x600wa.png")
//                .setContentImageUrl("https://is3-ssl.mzstatic.com/image/thumb/Purple114/v4/25/0f/61/250f6186-22db-75d1-0ce8-850cbaecbb33/AppIcon-0-0-1x_U007emarketing-0-0-0-10-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/246x0w.webp")
                // You use this to specify whether this content can be discovered publicly - default is public
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)

                // Here is where you can add custom keys/values to the deep link data
                .setContentMetadata(new ContentMetadata()
                        .addCustomMetadata("property1", "blue")
                        .addCustomMetadata("property2", "red"));
        LinkProperties linkProperties = new LinkProperties()
//                .setChannel("facebook")
                .setFeature("referrals")
                .addControlParameter("$activity", "second")
                .addControlParameter("$color",String.valueOf(ColorController.index))
                .addControlParameter("$name",name);

        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                }
            }
        });
        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(MainActivity.this, "Check this out!", "Guess "+name+"'s favourite color to earn points! ")
                .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With");

        branchUniversalObject.showShareSheet(this,
                linkProperties,
                shareSheetStyle,
                new Branch.ExtendedBranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                    }
                    @Override
                    public void onShareLinkDialogDismissed() {
                    }
                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                    }
                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                    @Override
                    public boolean onChannelSelected(String channelName, BranchUniversalObject buo, LinkProperties linkProperties) {
                        return false;
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        Branch.sessionBuilder(this).withCallback(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error != null) {
                    Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.getMessage());
                } else {
                    Log.i("BranchSDK_Tester", "branch init complete!");
                    if (branchUniversalObject != null) {
                        Toast.makeText(getBaseContext()," "+ branchUniversalObject.getTitle(),Toast.LENGTH_LONG).show();
                        Log.i("BranchSDK_Tester", "title " + branchUniversalObject.getTitle());
                        Log.i("BranchSDK_Tester", "CanonicalIdentifier " + branchUniversalObject.getCanonicalIdentifier());
                        Log.i("BranchSDK_Tester", "metadata " + branchUniversalObject.getContentMetadata().convertToJson());
                    }

                    if (linkProperties != null) {
                        Log.i("BranchSDK_Tester", "Channel " + linkProperties.getChannel());
                        Log.i("BranchSDK_Tester", "control params " + linkProperties.getControlParams());
                        Log.i("BranchSDK_Tester",   linkProperties.getControlParams().get("$activity"));
                        if(linkProperties.getControlParams().get("$activity").matches("second") ||
                        linkProperties.getControlParams().get("$android_deeplink_path").matches("second"))
                        {
                            Log.i("BranchSDK_Tester",   "one");
                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            intent.putExtra("color", linkProperties.getControlParams().get("$color"));
                            intent.putExtra("name", linkProperties.getControlParams().get("$name"));
                            startActivity(intent);
//                            finish();
                        }
                        if(linkProperties.getControlParams().get("$activity").matches("third") ||
                                linkProperties.getControlParams().get("$android_deeplink_path").matches("third"))
                        {
                            Log.i("BranchSDK_Tester",   "third");
                            Intent intent = new Intent(MainActivity.this, ThirdAcitivty.class);
                            startActivity(intent);
//                            finish();
                        }
                        else
                            Log.i("BranchSDK_Tester",   "two");

                    }
                }
            }
        }).withData(this.getIntent().getData()).init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        Branch.sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error != null) {
                    Log.e("BranchSDK_Tester", error.getMessage());
                } else if (referringParams != null) {
                    Log.i("BranchSDK_Testerr", referringParams.toString());
                }
            }
        }).reInit();
    }

    @Override
    protected void onResume() {
        setSavedName();
        super.onResume();
    }
}