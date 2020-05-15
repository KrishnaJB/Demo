package com.example.demo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demo.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class DetailedPostView extends AppCompatActivity {

    ImageView detailedImg;
    TextView detailTitle, detailDescription;
    ImageButton shareOption;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post_view);

        //Facebook Initiate
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        detailedImg = findViewById(R.id.detailedImageView);
        detailTitle = findViewById(R.id.detailedViewTitle);
        detailDescription = findViewById(R.id.detailedContentDescription);
        shareOption = findViewById(R.id.sharePostID);

        // get the data from the postAdaptor

        final String detailedImage = getIntent().getExtras().getString("picture");
        Glide.with(this).load(detailedImage).into(detailedImg);

        final String postTitle = getIntent().getExtras().getString("title");
        detailTitle.setText(postTitle);

        String postDescription = getIntent().getExtras().getString("description");
        detailDescription.setText(postDescription);

        //Fb share
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote(postTitle)
                        .setContentUrl(Uri.parse(detailedImage)).build();
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                } else{

                }

            }
        });

    }
}
