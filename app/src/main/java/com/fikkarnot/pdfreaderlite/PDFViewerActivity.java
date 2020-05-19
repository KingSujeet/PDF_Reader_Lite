package com.fikkarnot.pdfreaderlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class PDFViewerActivity extends AppCompatActivity {

    // variables or instances of class
    PDFView pdfView;
    int position =-1;
    boolean darkModeEnables;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pdfviewer);

        // Interestial ad
          mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("Enter your own Interestial ad Unit ID");
        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
        mInterstitialAd.loadAd(adRequestInter);

        // banner Ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pdfView = findViewById(R.id.pdfView);

        position = getIntent().getIntExtra("position",-1);

        // getting value from shareprefernce for darkMode
        SharedPreferences preferences = this.getSharedPreferences("darkMode", Context.MODE_PRIVATE);
         darkModeEnables= preferences.getBoolean("key",false);

        displayPDF();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInterstitialAd.show();
    }

    // this method will display PDF

    private void displayPDF() {


        if (darkModeEnables){

            pdfView.fromFile(MainActivity.fileList.get(position))
                    .enableSwipe(true)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .nightMode(true)
                    .load();
        }else {
            pdfView.fromFile(MainActivity.fileList.get(position))
                    .enableSwipe(true)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        }
    }
}
