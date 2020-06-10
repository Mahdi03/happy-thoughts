package com.aymanstudios.happythoughts;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.List;

public class NotificationPopup extends AppCompatActivity {
    View entireScreen;
    TextView quoteOfTheDayTextView;
    AdView bannerAdView;
    WebView backgroundWebView;
    public List<String> listOfBackgroundImages = Arrays.asList("docs/dropping_stars.gif");
    int getRandNum() {
        int randNum = (int) Math.floor(Math.random() * listOfBackgroundImages.size());
        return randNum;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_popup);
        //Get Mobile Banner Ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        bannerAdView = findViewById(R.id.bannerAdView);
        bannerAdView.setAdSize(AdSize.BANNER);
        // <!--Real Ad: ca-app-pub-8495483038077603/1037713608-->
        //    <!--Test Ad: ca-app-pub-3940256099942544/6300978111-->
        bannerAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

        //Set the Background
        backgroundWebView = findViewById(R.id.backgroundWebView);
        backgroundWebView.getSettings().setJavaScriptEnabled(true);
        backgroundWebView.loadUrl("https://mahdi03.github.io/happy-thoughts/background.html");

        //Set the rest of the screen's views
        entireScreen = findViewById(R.id.entireScreen);
        quoteOfTheDayTextView = findViewById(R.id.quoteOfTheDayTextView);
        //String quoteOfTheDay = getIntent().getStringExtra("quoteOfTheDay").replaceAll("\\n", "\n");
        //quoteOfTheDayTextView.setText(quoteOfTheDay);
        //When the constraint layout is clicked, close the activity
        entireScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backgroundWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backgroundWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

    }
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();
        return true;
    }
    public class WebAppInterface {
        Context context;
        WebAppInterface(Context c) {
            context = c;
        }
        @JavascriptInterface
        public void closeActivity() {
            NotificationPopup.this.finish();
        }
    }
}
