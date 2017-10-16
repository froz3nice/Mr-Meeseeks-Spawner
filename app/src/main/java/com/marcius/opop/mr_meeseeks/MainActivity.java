package com.marcius.opop.mr_meeseeks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private RelativeLayout layout;
    private float x;
    private float dx;
    private float dy;
    private float y;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences prefs;
    private ArrayList<Integer> allSounds;
    private ImageView box;
    private ImageView done;
    private ImageView musicSelector;

    private void addSounds() {
        allSounds = new ArrayList<>();
        allSounds.add(R.raw.meeseek3);
        allSounds.add(R.raw.meeseek4);
        allSounds.add(R.raw.meeseek5);
        allSounds.add(R.raw.meeseek6);
        allSounds.add(R.raw.meeseek7);
        allSounds.add(R.raw.pickle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, getString(R.string.app_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        this.context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        addSounds();
        layout = (RelativeLayout) findViewById(R.id.layout);
        box = (ImageView) findViewById(R.id.box);
        done = (ImageView) findViewById(R.id.all_done);
        musicSelector = (ImageView) findViewById(R.id.music);

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer soundPlayer;
                ArrayList<Integer> sounds = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    if (prefs.getInt(String.valueOf(i), i) != -1) {
                        sounds.add(allSounds.get(i));
                    }
                }
                int randomPickle = new Random().nextInt(150);
                if (randomPickle == 69) {
                    if (prefs.getInt(String.valueOf(5), -1) == 5) {
                        soundPlayer = MediaPlayer.create(context, R.raw.pickle);
                        soundPlayer.start();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                soundPlayer.release();
                            }
                        }, 3000);
                        final ImageView imageView = new ImageView(MainActivity.this);
                        enableMeeseeksDragging(imageView);
                        loadPickleRick(imageView);
                    } else {
                        setUpMeeseeks(sounds);
                    }
                } else {
                    setUpMeeseeks(sounds);
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMeeseeks();
            }
        });
        showDialog();
    }

    private void setUpMeeseeks(ArrayList<Integer> sounds) {
        if (sounds.size() > 0) {
            int randomNumber = new Random().nextInt(sounds.size());
            final MediaPlayer soundPlayer = MediaPlayer.create(context, sounds.get(randomNumber));
            soundPlayer.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    soundPlayer.release();
                }
            }, 3000);
        }
        final ImageView imageView = new ImageView(MainActivity.this);
        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels - pxFromDp(context, 180);
        float dpWidth = displayMetrics.widthPixels - pxFromDp(context, 80);
        int leftMargin = new Random().nextInt((int) dpWidth);
        int topMargin = new Random().nextInt((int) dpHeight);

        params.setMargins(leftMargin, topMargin, 0, 0);
        imageView.setLayoutParams(params);
        enableMeeseeksDragging(imageView);
        loadMeeseeksToScreen(imageView);
    }

    private void loadPickleRick(final ImageView imageView) {
        Picasso.with(context)
                .load(R.drawable.pickle)
                .resize((int) pxFromDp(context, 120), (int) pxFromDp(context, 160))
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

    private void loadMeeseeksToScreen(final ImageView imageView) {
        if (new Random().nextInt(2) == 0) {
            Picasso.with(context)
                    .load(R.drawable.meeseks12)
                    .resize((int) pxFromDp(context, 80), (int) pxFromDp(context, 120))
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            layout.addView(imageView);
                            done.setVisibility(View.VISIBLE);
                            box.bringToFront();
                            done.bringToFront();
                            mAdView.bringToFront();
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
                            done.setVisibility(View.VISIBLE);
                            box.bringToFront();
                            done.bringToFront();
                            mAdView.bringToFront();
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
    }

    private void enableMeeseeksDragging(ImageView imageView) {
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
    }

    private void deleteMeeseeks() {
        final MediaPlayer soundPlayer = MediaPlayer.create(context, R.raw.all_done);
        soundPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPlayer.release();
            }
        }, 2000);
        if (layout.getChildCount() == 5) {
            done.setVisibility(View.INVISIBLE);
        }

        for (int i = layout.getChildCount(); i >= 0; i--) {
            View v = layout.getChildAt(i);
            if (v instanceof ImageView) {
                if (v != box && v != done && v != musicSelector) {
                    layout.removeView(v);
                    break;
                }
            }
        }
    }

    private void showDialog() {
        final CharSequence[] items = {" oh yeah yes maam ",
                " hey there i'm mr meeseeks ",
                " oh i'm mr meeseeks ",
                " i'm mr meeseeks",
                " I'm mr meeseeks look at me ",
                " Random ultra rare Pickle Rick"};
        boolean[] checkedValues = new boolean[6];
        checkedValues[0] = prefs.getInt(String.valueOf(0), 0) == 0;
        checkedValues[1] = prefs.getInt(String.valueOf(1), 1) == 1;
        checkedValues[2] = prefs.getInt(String.valueOf(2), 2) == 2;
        checkedValues[3] = prefs.getInt(String.valueOf(3), 3) == 3;
        checkedValues[4] = prefs.getInt(String.valueOf(4), 4) == 4;
        checkedValues[5] = prefs.getInt(String.valueOf(5), -1) == 5;

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(" Select desired sounds ")
                .setMultiChoiceItems(items, checkedValues, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            playSound(indexSelected);
                            prefs.edit().putInt(String.valueOf(indexSelected), indexSelected).apply();
                        } else {
                            prefs.edit().putInt(String.valueOf(indexSelected), -1).apply();
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();

        musicSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void playSound(int indexSelected) {
        final MediaPlayer soundPlayer = MediaPlayer.create(context, allSounds.get(indexSelected));
        soundPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPlayer.release();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
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
