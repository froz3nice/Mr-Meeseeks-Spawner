package com.marcius.opop.mr_meeseeks;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private RelativeLayout layout;
    private float x;
    private float dx;
    private float dy;
    private float y;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, getString(R.string.app_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (getActionBar() != null) {
            getActionBar().hide();
        }

        this.context = getApplicationContext();
        layout = (RelativeLayout) findViewById(R.id.layout);
        final ImageView box = (ImageView) findViewById(R.id.box);
        final ImageView done = (ImageView) findViewById(R.id.all_done);


        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer soundPlayer;
                switch (new Random().nextInt(6)){
                    case 0:
                        soundPlayer = MediaPlayer.create(context, R.raw.meeseek2);
                        break;
                    case 1:
                        soundPlayer = MediaPlayer.create(context, R.raw.meeseek3);
                        break;
                    case 2:
                        soundPlayer = MediaPlayer.create(context, R.raw.meeseek4);
                        break;
                    case 3:
                        soundPlayer = MediaPlayer.create(context, R.raw.meeseek5);
                        break;
                    case 4:
                        soundPlayer = MediaPlayer.create(context, R.raw.meeseek6);
                        break;
                    case 5:
                        soundPlayer = MediaPlayer.create(context, R.raw.meeseek7);
                        break;
                    default:
                        soundPlayer = MediaPlayer.create(context, R.raw.meeseek5);
                        break;
                }
                soundPlayer.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        soundPlayer.release();
                    }
                }, 3000);

                final ImageView imageView = new ImageView(MainActivity.this);
                RelativeLayout.LayoutParams params;
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
                int leftMargin = new Random().nextInt((int) dpWidth);
                int topMargin = new Random().nextInt((int) dpHeight);

                params.setMargins(leftMargin, topMargin, 0, 0);
                imageView.setLayoutParams(params);
                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch (motionEvent.getAction()) {

                            case MotionEvent.ACTION_DOWN:

                                dx = view.getX() - motionEvent.getRawX();
                                dy = view.getY() - motionEvent.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                view.animate()
                                        .x(motionEvent.getRawX() + dx)
                                        .y(motionEvent.getRawY() + dy)
                                        .setDuration(0)
                                        .start();
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
                if (new Random().nextInt(2) == 0) {
                    Picasso.with(context)
                            .load(R.drawable.meeseks12)
                            .resize((int) pxFromDp(context, 80), (int) pxFromDp(context, 120))
                            .into(imageView, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    layout.addView(imageView);
                                }

                                @Override
                                public void onError() {
                                }
                            });
                } else {
                    Picasso.with(context)
                            .load(R.drawable.meeseks22)
                            .resize((int) pxFromDp(context, 45), (int) pxFromDp(context, 180))
                            .into(imageView, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    layout.addView(imageView);
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
                done.setVisibility(View.VISIBLE);
                box.bringToFront();
                done.bringToFront();
                mAdView.bringToFront();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer soundPlayer = MediaPlayer.create(context, R.raw.all_done);
                soundPlayer.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        soundPlayer.release();
                    }
                }, 2000);
                if(layout.getChildCount() == 4){
                    done.setVisibility(View.INVISIBLE);
                }

                for (int i = layout.getChildCount(); i >= 0; i--) {
                    View v = layout.getChildAt(i);
                    if (v instanceof ImageView) {
                        if (v != box && v != done) {
                            layout.removeView(v);
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    private float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
