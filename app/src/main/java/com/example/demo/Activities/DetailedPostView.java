package com.example.demo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

import java.io.File;
import java.util.ArrayList;

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
//        FacebookSdk.sdkInitialize(this.getApplicationContext());

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
//        callbackManager = CallbackManager.Factory.create();
//        shareDialog = new ShareDialog(this);

        final String imageShare = detailedImage;

        shareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = "Look at my awesome picture";
                Uri pictureUri = Uri.parse(detailedImage);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
//                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
                shareIntent.setPackage("com.whatsapp");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share images..."));


              /*  String text = "Look at my awesome picture";
                Uri pictureUri = Uri.parse(detailedImage);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share images..."));
*/
//                Uri imageToShare = Uri.parse(detailedImage); //MainActivity.this is the context in my app.
//
//                Intent shareIntent = new Intent();
//
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.setType("*/*");
//                shareIntent.putExtra(Intent.EXTRA_STREAM,postTitle);
//                shareIntent.setPackage("com.whatsapp");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageToShare);
//                startActivity(Intent.createChooser(shareIntent, "Share"));

                // Share in Facebook using Facebook SDK
                /*ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote(postTitle)
                        .setContentUrl(Uri.parse(detailedImage)).build();
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                } else{

                }*/

            }
        });

    }
}
