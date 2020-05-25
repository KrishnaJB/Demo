package com.example.penBugsBlogApp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.penBugsBlogApp.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.File;
import java.io.FileOutputStream;

public class DetailedPostView extends AppCompatActivity {


    ImageView detailedImg;
    TextView detailTitle, detailDescription;
    ImageButton shareOption;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String title, imageToShare;

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

        title = postTitle;
        imageToShare = detailedImage;
        
        // Create Dynamic links
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(
                this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Log.i("Detailed activity", "We have dynamic link");
                        Uri deepLink = null;
                        if (pendingDynamicLinkData !=null){

                            deepLink = pendingDynamicLinkData.getLink();
                            detailTitle.append("\n nonSuccess called "+ deepLink.toString());


                        }

                        if (deepLink !=null){
                            Log.i("Detailed activity", "We have dynamic link: \n" + deepLink.toString());
                            String currentPage = deepLink.getQueryParameter("curPage");
                            int curPage = Integer.parseInt(currentPage);

                        }
                    }
                }
        ).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                detailTitle.append("\nonFailure");
            }
        });

        //dynamic link share


//        Fb share
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        final String imageShare = detailedImage;

        shareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createLink();


               /* BitmapDrawable bitmapDrawable = (BitmapDrawable)detailedImg.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(postTitle, bitmap);*/




               /* // Share in Facebook using Facebook SDK
                ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote(postTitle)
                        .setContentUrl(Uri.parse(detailedImage)).build();
                shareDialog.show(linkContent);
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                } else{

                }*/

            }
        });

    }

    private void createLink() {



        Log.i("detailed post", "create Link");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.penbugs.com/"))
                .setDomainUriPrefix("https://penbugsblog.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(title)
                                .setImageUrl(Uri.parse(imageToShare))
                                .build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.i("detailed post", "long Link" + dynamicLink.getUri());

        /*// Manual Link sharing
        String linkSharingText = "https://penbugsblog.page.link/?"+
                "link=http://www.penbugs.com/"+
                "&apn="+getPackageName()+
                "&st="+title+
                "&si="+imagetoshare;*/

        // Shorten the Link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
//                .setLongLink(Uri.parse(linkSharingText))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.i("detailed post", "short Link" + shortLink);

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);

                        } else {
                            // Error
                            // ...
                            Log.i("detailed post", "error" + task.getException());
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

            uri = FileProvider.getUriForFile(this, "com.example.penBugsBlogApp.fileprovider", file);

        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.)
    }
}
