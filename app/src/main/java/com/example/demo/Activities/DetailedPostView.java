package com.example.demo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demo.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
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

//        Fb share
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        final String imageShare = detailedImage;

        shareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BitmapDrawable bitmapDrawable = (BitmapDrawable)detailedImg.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(postTitle, bitmap);




                // Share in Facebook using Facebook SDK
                ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote(postTitle)
                        .setContentUrl(Uri.parse(detailedImage)).build();
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                } else{

                }

            }
        });

    }

    private void shareImageAndText(String postTitle, Bitmap bitmap) {
        String shareBody = postTitle;
        Uri uri = saveImageToShare(bitmap);
        Intent sIntent = new Intent(Intent.ACTION_SEND);
        sIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sIntent.setType("image/png");
        startActivity(Intent.createChooser(sIntent, "Share via"));
    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_images.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();

            uri = FileProvider.getUriForFile(this, "com.example.demo.fileprovider", file);

        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
}
