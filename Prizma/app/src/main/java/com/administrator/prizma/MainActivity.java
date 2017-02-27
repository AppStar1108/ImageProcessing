package com.administrator.prizma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.administrator.prizma.R;
import com.google.android.gms.ads.AdListener;
import com.zomato.photofilters.geometry.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.prizma.effects.AllEffects;
import com.administrator.prizma.utility.Utility;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
//
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.xyzxqs.xlowpoly.LowPoly;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    Bitmap bImage, b1Image;
    Boolean _isOpenGallery = false;
    SeekBar seekBar1, seekBar3;
    int widthX = 0,heightY = 0;
    ImageView mainImage, upperImage;

    private static final int REQUEST_CAMERA = 1;
    private static final int PICK_IMAGE_REQUEST = 0;

    RelativeLayout _categoryRelativeLayout2, _seekbarCatRelative3, _seekbarAlphaRelative4, _selectPicRelativelayout,_progressRound;


    LinearLayout _linearCat1, _linearCat2, _linearCat3, _linearCat4, _linearCat5 , _linearCat6, _linearCat7, _linearCat8, _linearCat9, _linearCat10,
            _linearCat11, _linearCat12, _linearCat13, _linearCat14, _linearCat15, _seekbarLayout, _closeLinearLayout, _saveShareLinearLayout,_clickLayer;
    ImageView _image1selected, _image2selected, _image3selected, _image4selected,_container,
            _image5selected,_image6selected,_image7selected,_image8selected,_image9selected,_image10selected,
            _image11selected,_image12selected,_image13selected,_image14selected,_image15selected, btnCamera, btnGallery, _closeBtn, _downloadBtn, _shareBtn,
            _image1,_image2,_image3,_image4,_image5,_image6,_image7,_image8,_image9,_image10,_image11,_image12,_image13,_image14,_image15;
    ImageButton _menuBtn;

    TextView mTxvSeekBarValue, mTxvSeekBarValue3, textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11, textView12, textView13, textView14, textView15;

    HorizontalScrollView _horizontalScrollView;

    AdView mAdView;
    InterstitialAd mInterstitialAd;

    AllEffects allEffects = new AllEffects();
    final Handler handler = new Handler();

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    public void onBackPressed()
    {
        if(_progressRound.getVisibility() == View.GONE)
        {
            if(_closeLinearLayout.getVisibility() == View.VISIBLE)
            {
                _closeLinearLayout.setVisibility(View.GONE);
                _seekbarCatRelative3.setVisibility(View.GONE);
                _saveShareLinearLayout.setVisibility(View.GONE);
                _seekbarAlphaRelative4.setVisibility(View.GONE);
                _selectPicRelativelayout.setVisibility(View.VISIBLE);
                _categoryRelativeLayout2.setVisibility(View.VISIBLE);
            }
            else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_feedback, null);
                dialogBuilder.setView(dialogView);

                RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBar);

                dialogView.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.finish();
                    }
                });

                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.finish();
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please wait, It will take a moment",Toast.LENGTH_LONG).show();
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        onGetDeviceSize();


        mainImage = ((ImageView) findViewById(R.id.imageView));
        upperImage = ((ImageView) findViewById(R.id.imageView2));
        upperImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(_seekbarLayout.getVisibility() == View.VISIBLE)
                    _seekbarLayout.setVisibility(View.GONE);
                else
                    _seekbarLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });


        _linearCat1 = (LinearLayout) findViewById(R.id.linearCat1);
        _linearCat1.setOnClickListener(this);
        _linearCat2 = (LinearLayout) findViewById(R.id.linearCat2);
        _linearCat2.setOnClickListener(this);
        _linearCat3 = (LinearLayout) findViewById(R.id.linearCat3);
        _linearCat3.setOnClickListener(this);
        _linearCat4 = (LinearLayout) findViewById(R.id.linearCat4);
        _linearCat4.setOnClickListener(this);
        _linearCat5 = (LinearLayout) findViewById(R.id.linearCat5);
        _linearCat5.setOnClickListener(this);
        _linearCat6 = (LinearLayout) findViewById(R.id.linearCat6);
        _linearCat6.setOnClickListener(this);
        _linearCat7 = (LinearLayout) findViewById(R.id.linearCat7);
        _linearCat7.setOnClickListener(this);
        _linearCat8 = (LinearLayout) findViewById(R.id.linearCat8);
        _linearCat8.setOnClickListener(this);
        _linearCat9 = (LinearLayout) findViewById(R.id.linearCat9);
        _linearCat9.setOnClickListener(this);
        _linearCat10 = (LinearLayout) findViewById(R.id.linearCat10);
        _linearCat10.setOnClickListener(this);
        _linearCat11 = (LinearLayout) findViewById(R.id.linearCat11);
        _linearCat11.setOnClickListener(this);
        _linearCat12 = (LinearLayout) findViewById(R.id.linearCat12);
        _linearCat12.setOnClickListener(this);
        _linearCat13 = (LinearLayout) findViewById(R.id.linearCat13);
        _linearCat13.setOnClickListener(this);
        _linearCat14 = (LinearLayout) findViewById(R.id.linearCat14);
        _linearCat14.setOnClickListener(this);
        _linearCat15 = (LinearLayout) findViewById(R.id.linearCat15);
        _linearCat15.setOnClickListener(this);

        _closeLinearLayout = (LinearLayout) findViewById(R.id.closeLinearLayout);
        _saveShareLinearLayout = (LinearLayout) findViewById(R.id.saveShareLinearLayout);

        _closeBtn = (ImageView) findViewById(R.id.closeBtn);
        _closeBtn.setOnClickListener(this);

        _downloadBtn = (ImageView) findViewById(R.id.downloadBtn);
        _downloadBtn.setOnClickListener(this);

        _shareBtn = (ImageView) findViewById(R.id.shareBtn);
        _shareBtn.setOnClickListener(this);

        _selectPicRelativelayout = (RelativeLayout) findViewById(R.id.selectPicRelativelayout);
        _selectPicRelativelayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(_categoryRelativeLayout2.getVisibility() == View.VISIBLE)
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                else
                    _categoryRelativeLayout2.setVisibility(View.VISIBLE);

                return false;
            }
        });

        _categoryRelativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        _seekbarLayout =(LinearLayout) findViewById(R.id.seekbarLayout);

        _horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);

        _image1selected = (ImageView) findViewById(R.id.image1selected);
        _image2selected = (ImageView) findViewById(R.id.image2selected);
        _image3selected = (ImageView) findViewById(R.id.image3selected);
        _image4selected = (ImageView) findViewById(R.id.image4selected);
        _image5selected = (ImageView) findViewById(R.id.image5selected);

        _image6selected = (ImageView) findViewById(R.id.image6selected);
        _image7selected = (ImageView) findViewById(R.id.image7selected);
        _image8selected = (ImageView) findViewById(R.id.image8selected);
        _image9selected = (ImageView) findViewById(R.id.image9selected);
        _image10selected = (ImageView) findViewById(R.id.image10selected);

        _image11selected = (ImageView) findViewById(R.id.image11selected);
        _image12selected = (ImageView) findViewById(R.id.image12selected);
        _image13selected = (ImageView) findViewById(R.id.image13selected);
        _image14selected = (ImageView) findViewById(R.id.image14selected);
        _image15selected = (ImageView) findViewById(R.id.image15selected);


        _image1 = (ImageView) findViewById(R.id.image1);
        _image2 = (ImageView) findViewById(R.id.image2);
        _image3 = (ImageView) findViewById(R.id.image3);
        _image4 = (ImageView) findViewById(R.id.image4);
        _image5 = (ImageView) findViewById(R.id.image5);

        _image6 = (ImageView) findViewById(R.id.image6);
        _image7 = (ImageView) findViewById(R.id.image7);
        _image8 = (ImageView) findViewById(R.id.image8);
        _image9 = (ImageView) findViewById(R.id.image9);
        _image10 = (ImageView) findViewById(R.id.image10);

        _image11 = (ImageView) findViewById(R.id.image11);
        _image12 = (ImageView) findViewById(R.id.image12);
        _image13 = (ImageView) findViewById(R.id.image13);
        _image14 = (ImageView) findViewById(R.id.image14);
        _image15 = (ImageView) findViewById(R.id.image15);

        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView11 = (TextView) findViewById(R.id.textView11);
        textView12 = (TextView) findViewById(R.id.textView12);
        textView13 = (TextView) findViewById(R.id.textView13);
        textView14 = (TextView) findViewById(R.id.textView14);
        textView15 = (TextView) findViewById(R.id.textView15);

        _menuBtn = (ImageButton) findViewById(R.id.menuBtn);
        _menuBtn.setOnClickListener(this);

        _progressRound = (RelativeLayout) findViewById(R.id.progress);

        btnCamera = (ImageView) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);
        btnGallery = (ImageView) findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);

        _seekbarAlphaRelative4 =(RelativeLayout) findViewById(R.id.seekbarAlphaRelative4);
        mTxvSeekBarValue = (TextView) this.findViewById(R.id.txtSeekbarAlpha);
        seekBar1 = (SeekBar) findViewById(R.id.seekBar);

        seekBar1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    int progress = seekBar1.getProgress();

                    mTxvSeekBarValue.setVisibility(View.VISIBLE);
                    mTxvSeekBarValue.setText(String.valueOf(progress).toCharArray(), 0, String.valueOf(progress).length());
                    mTxvSeekBarValue.setText(" "+ mTxvSeekBarValue.getText() + " % ");

                } else if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    int progress = seekBar1.getProgress();
                    upperImage.setAlpha((float) (progress*0.01));

                    mTxvSeekBarValue.setVisibility(View.VISIBLE);
                    mTxvSeekBarValue.setText(String.valueOf(progress).toCharArray(), 0, String.valueOf(progress).length());
                    mTxvSeekBarValue.setText(" "+ mTxvSeekBarValue.getText() + " % ");
                }
                return false;
            }
        });

        _container = (ImageView)findViewById(R.id.container);
        _container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // NOTE: This prevents the touches from propagating through the view and incorrectly invoking the button behind it
                return true;
            }
        });

        _seekbarCatRelative3 =(RelativeLayout) findViewById(R.id.seekbarCatRelative3);

        mTxvSeekBarValue3 = (TextView) this.findViewById(R.id.txtSeekBarValue3);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);

        deSelectall();
        _image1selected.setVisibility(View.VISIBLE);
        textView1.setTextColor(getResources().getColor(R.color.colorSelected));

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        getInitialLayOut();
    }

    private void getInitialLayOut()
    {
        int wD = ((widthX * 200) / 1080);
        int hD = wD;
        int mT = ((heightY * 20) / 1920);
        int mR = ((widthX * 20) / 1080);

        RelativeLayout.LayoutParams dp = new RelativeLayout.LayoutParams(wD,hD);
        dp.setMargins(0,mT,mR,0);
        dp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnCamera.setLayoutParams(dp);
        btnGallery.setLayoutParams(dp);
        _menuBtn.setLayoutParams(dp);

        hD = ((heightY * 400) / 1920);
        dp = new RelativeLayout.LayoutParams(_categoryRelativeLayout2.getLayoutParams().width,hD);
        dp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        _categoryRelativeLayout2.setLayoutParams(dp);

        hD = ((heightY * 350) / 1920);
        int mL = ((widthX * 50) / 1080);
        mR = ((widthX * 50) / 1080);
        dp = new RelativeLayout.LayoutParams(_horizontalScrollView.getLayoutParams().width,hD);
        dp.setMargins(mL,0,mR,0);
        dp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        _horizontalScrollView.setLayoutParams(dp);


        wD = ((widthX * 205) / 1080);
        hD = ((heightY * 200) / 1920);
        mT = ((heightY * 20) / 1920);
        mL = ((widthX * 20) / 1080);
        dp = new RelativeLayout.LayoutParams(wD,hD);
        dp.setMargins(mL,mT,0,0);
        _image1.setLayoutParams(dp);
        _image2.setLayoutParams(dp);
        _image3.setLayoutParams(dp);
        _image4.setLayoutParams(dp);
        _image5.setLayoutParams(dp);
        _image6.setLayoutParams(dp);
        _image7.setLayoutParams(dp);
        _image8.setLayoutParams(dp);
        _image9.setLayoutParams(dp);
        _image10.setLayoutParams(dp);
        _image11.setLayoutParams(dp);
        _image12.setLayoutParams(dp);
        _image13.setLayoutParams(dp);
        _image14.setLayoutParams(dp);
        _image15.setLayoutParams(dp);


        wD = ((widthX * 250) / 1080);
        hD = ((heightY * 250) / 1920);
        dp = new RelativeLayout.LayoutParams(wD,hD);
        _image1selected.setLayoutParams(dp);
        _image2selected.setLayoutParams(dp);
        _image3selected.setLayoutParams(dp);
        _image4selected.setLayoutParams(dp);
        _image5selected.setLayoutParams(dp);
        _image6selected.setLayoutParams(dp);
        _image7selected.setLayoutParams(dp);
        _image8selected.setLayoutParams(dp);
        _image9selected.setLayoutParams(dp);
        _image10selected.setLayoutParams(dp);
        _image11selected.setLayoutParams(dp);
        _image12selected.setLayoutParams(dp);
        _image13selected.setLayoutParams(dp);
        _image14selected.setLayoutParams(dp);
        _image15selected.setLayoutParams(dp);

        wD = ((widthX * 250) / 1080);
        LinearLayout.LayoutParams dp1 = new LinearLayout.LayoutParams(wD,textView1.getLayoutParams().height);
        int pT = ((heightY * -10) / 1920);
        textView1.setGravity(Gravity.CENTER);
        textView1.setPadding(0,pT,0,0);
        textView1.setLayoutParams(dp1);

        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(0,pT,0,0);
        textView2.setLayoutParams(dp1);

        textView3.setGravity(Gravity.CENTER);
        textView3.setPadding(0,pT,0,0);
        textView3.setLayoutParams(dp1);

        textView4.setGravity(Gravity.CENTER);
        textView4.setPadding(0,pT,0,0);
        textView4.setLayoutParams(dp1);

        textView5.setGravity(Gravity.CENTER);
        textView5.setPadding(0,pT,0,0);
        textView5.setLayoutParams(dp1);

        textView6.setGravity(Gravity.CENTER);
        textView6.setPadding(0,pT,0,0);
        textView6.setLayoutParams(dp1);

        textView7.setGravity(Gravity.CENTER);
        textView7.setPadding(0,pT,0,0);
        textView7.setLayoutParams(dp1);

        textView8.setGravity(Gravity.CENTER);
        textView8.setPadding(0,pT,0,0);
        textView8.setLayoutParams(dp1);

        textView9.setGravity(Gravity.CENTER);
        textView9.setPadding(0,pT,0,0);
        textView9.setLayoutParams(dp1);

        textView10.setGravity(Gravity.CENTER);
        textView10.setPadding(0,pT,0,0);
        textView10.setLayoutParams(dp1);

        textView11.setGravity(Gravity.CENTER);
        textView11.setPadding(0,pT,0,0);
        textView11.setLayoutParams(dp1);

        textView12.setGravity(Gravity.CENTER);
        textView12.setPadding(0,pT,0,0);
        textView12.setLayoutParams(dp1);

        textView13.setGravity(Gravity.CENTER);
        textView13.setPadding(0,pT,0,0);
        textView13.setLayoutParams(dp1);

        textView14.setGravity(Gravity.CENTER);
        textView14.setPadding(0,pT,0,0);
        textView14.setLayoutParams(dp1);

        textView15.setGravity(Gravity.CENTER);
        textView15.setPadding(0,pT,0,0);
        textView15.setLayoutParams(dp1);

        dp1 = (LinearLayout.LayoutParams) _linearCat2.getLayoutParams();
        mL = ((widthX * 50) / 1080);
        dp1.setMargins(mL,0,0,0);
        _linearCat2.setLayoutParams(dp1);
        _linearCat3.setLayoutParams(dp1);
        _linearCat4.setLayoutParams(dp1);
        _linearCat5.setLayoutParams(dp1);
        _linearCat6.setLayoutParams(dp1);
        _linearCat7.setLayoutParams(dp1);
        _linearCat8.setLayoutParams(dp1);
        _linearCat9.setLayoutParams(dp1);
        _linearCat10.setLayoutParams(dp1);
        _linearCat11.setLayoutParams(dp1);
        _linearCat12.setLayoutParams(dp1);
        _linearCat13.setLayoutParams(dp1);
        _linearCat14.setLayoutParams(dp1);
        _linearCat15.setLayoutParams(dp1);

        hD = ((heightY * 250) / 1920);
        dp1 = new LinearLayout.LayoutParams(_seekbarAlphaRelative4.getLayoutParams().width,hD);
        _seekbarAlphaRelative4.setLayoutParams(dp1);

        dp1 = new LinearLayout.LayoutParams(_seekbarCatRelative3.getLayoutParams().width,hD);
        _seekbarCatRelative3.setLayoutParams(dp1);

        mT = ((heightY * 50) / 1920);
        mL = ((widthX * 50) / 1080);
        dp = (RelativeLayout.LayoutParams)_closeLinearLayout.getLayoutParams();
        dp.setMargins(mL,mT,0,0);
        _closeLinearLayout.setLayoutParams(dp);

        wD = ((widthX * 150) / 1080);
        hD = ((heightY * 150) / 1920);
        dp1 = new LinearLayout.LayoutParams(wD,hD);
        _closeBtn.setLayoutParams(dp1);

        mT = ((heightY * 50) / 1920);
        mL = ((widthX * 50) / 1080);
        dp = (RelativeLayout.LayoutParams)_saveShareLinearLayout.getLayoutParams();
        dp.setMargins(mL,mT,0,0);
        _saveShareLinearLayout.setGravity(Gravity.RIGHT);
        _saveShareLinearLayout.setLayoutParams(dp);

        wD = ((widthX * 150) / 1080);
        hD = ((heightY * 150) / 1920);
        dp1 = new LinearLayout.LayoutParams(wD,hD);
        _downloadBtn.setLayoutParams(dp1);
        _shareBtn.setLayoutParams(dp1);
    }

    int tempDep = 0;

    private void setSecondSeekBar(int initialVal, int maxValue, final int incrementVal, final int initialStartVal, final int totalIncVal, final String effect)
    {
        tempDep = 0;
        seekBar3.setProgress(initialVal);
        seekBar3.setMax(maxValue);

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                int yourStep = incrementVal;
                progress = ((int) Math.round(seekBar.getProgress() / yourStep)) * yourStep;
                mTxvSeekBarValue3.setText(String.valueOf(progress).toCharArray(), 0, String.valueOf(progress).length());
                mTxvSeekBarValue3.setText(" " + mTxvSeekBarValue3.getText() + " % ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                int progress = 0;
                int yourStep = incrementVal;
                progress = ((int) Math.round(seekBar.getProgress() / yourStep)) * yourStep;
//                mTxvSeekBarValue3.setText(String.valueOf(progress).toCharArray(), 0, String.valueOf(progress).length());
//                mTxvSeekBarValue3.setText(" " + mTxvSeekBarValue3.getText() + " % ");

                if(effect.equals("Shade"))
                {
                    shadeEffect(initialStartVal + ((progress / incrementVal) * totalIncVal));
                    mTxvSeekBarValue3.setText(String.valueOf(progress).toCharArray(), 0, String.valueOf(progress).length());
                    mTxvSeekBarValue3.setText(" " + mTxvSeekBarValue3.getText() + " % ");
                }
                else if(effect.equals("Depth"))
                {
                    //32+((32/32)*32)
                    final int dep = (initialStartVal + ((progress / incrementVal) * totalIncVal));

                    if(dep != tempDep)
                    {
                        tempDep = dep;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                depthEffect();
                            }
                        }, 500);
                    }
                    mTxvSeekBarValue3.setText(" " + dep + " % ");
                }
                else if(effect.equals("Tint"))
                {
                    System.out.println("Progress : "+ progress);
                    tintEffect(progress);
                }
                else if(effect.equals("Hue"))
                {
                    final int hue = progress;
                    System.out.println("Progress 1 : "+ hue);
                    if(hue != tempDep)
                    {
                        System.out.println("Hello");
                        tempDep = hue;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HueEffect(hue);
                            }
                        }, 100);
                    }

                }
                else if(effect.equals("Boost1"))
                {
                    System.out.println("Boost1 : " + progress);
                    final int per = progress;
                    System.out.println("Progress 1 : "+ per);
                    if(per != tempDep)
                    {
                        System.out.println("Hello");
                        tempDep = per;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                applyBoostEffect(1,per);
                            }
                        }, 100);
                    }
                }
                else if(effect.equals("Boost2"))
                {
                    System.out.println("Boost2 : " + progress);
                    final int per = progress;
                    System.out.println("Progress 1 : "+ per);
                    if(per != tempDep)
                    {
                        System.out.println("Hello");
                        tempDep = per;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                applyBoostEffect(2,per);
                            }
                        }, 100);
                    }
                }
                else if(effect.equals("Boost3"))
                {
                    System.out.println("Boost3 : " + progress);
                    final int per = progress;
                    System.out.println("Progress 1 : "+ per);
                    if(per != tempDep)
                    {
                        System.out.println("Hello");
                        tempDep = per;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                applyBoostEffect(3,per);
                            }
                        }, 100);
                    }
                }
            }
        });
    }

    private void ShowSeekValue(int x, int y)
    {
        if(x > 0 && x < seekBar1.getWidth())
        {
            AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, x, y);
            mTxvSeekBarValue.setLayoutParams(lp);
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                break;
            case R.id.menuBtn:
                AnimateImageButton();
                break;
            case R.id.btnCamera:
                takePhotoFromCamera();
                break;
            case R.id.btnGallery:
                takePicturefromGallery();
                break;
            case R.id.linearCat1:
                deSelectall();
                selectCat(1);
                if(bImage != null)
                {
                    setAlphaSeekBar(100);
                    mainImage.setVisibility(View.VISIBLE);

                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
                    setPolyEffect();
                }
                else
                    AnimateImageButton();
                break;
            case R.id.linearCat2:
                deSelectall();
                selectCat(2);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);
                    setEffectBackground();
                    setSecondSeekBar(50,100,10,0,10,"Boost3");
                    applyBoostEffect(3,50);
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat3:
                deSelectall();
                selectCat(3);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);
                    setEffectBackground();
                    setSecondSeekBar(20, 100, 20, -33000, 500, "Shade");
                    shadeEffect(-32500);
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat4:
                deSelectall();
                selectCat(4);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);
                    setEffectBackground();
                    setSecondSeekBar(50, 300, 10, 0, 10, "Tint");
                    tintEffect(50);
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat5:
                deSelectall();
                selectCat(5);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);
                    setEffectBackground();
                    setSecondSeekBar(80,100,20,-20000,1000,"Shade");
                    shadeEffect(-16000);
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat6:
                deSelectall();
                selectCat(6);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.GONE);
                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);
                    setRectPolyEffect();
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat7:
                deSelectall();
                selectCat(7);
                if(bImage != null)
                {
                    setAlphaSeekBar(100);
                    mainImage.setVisibility(View.VISIBLE);
                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
                    showSnowEffect();

                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat8:
                deSelectall();
                selectCat(8);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);
                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
//                    setSecondSeekBar(32,64,32,32,32,"Depth");
                    depthEffect();
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat9:
                System.out.println("Hello");
                deSelectall();
                selectCat(9);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);

                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);

                    setCartoonEffect();
//                    tempImage1  = BitmapFilter.changeStyle(bImage, BitmapFilter.OIL_STYLE, 5);
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat10:
                deSelectall();
                selectCat(10);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);

                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);

                    setToneEffect();
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat11:
                deSelectall();
                selectCat(11);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(100);

                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
                    saturationEffect();
                }
                else
                    AnimateImageButton();
                break;

            case R.id.linearCat12:
                deSelectall();
                selectCat(12);
                if(bImage != null)
                {
                    mainImage.setVisibility(View.VISIBLE);
                    setAlphaSeekBar(50);
                    setEffectBackground();
                    setSecondSeekBar(20,100,10,0,10,"Boost2");
                    applyBoostEffect(2,20);
                }
                else
                    AnimateImageButton();
                break;
            case R.id.linearCat13:
                deSelectall();
                selectCat(13);
                if(bImage != null)
                {
                    setAlphaSeekBar(100);
                    upperImage.setVisibility(View.GONE);
                    mainImage.setVisibility(View.VISIBLE);
                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
                    blackFilter();
                }
                break;
            case R.id.linearCat14:
                deSelectall();
                selectCat(14);
                if(bImage != null)
                {
                    setAlphaSeekBar(100);
                    mainImage.setVisibility(View.VISIBLE);
                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
                    applySepiaEffect(.7, 0.3, 0.9);
                }
                else
                    AnimateImageButton();
                break;
            case R.id.linearCat15:
                deSelectall();
                selectCat(15);
                if(bImage != null)
                {
                    setAlphaSeekBar(100);
                    mainImage.setVisibility(View.VISIBLE);
                    _closeLinearLayout.setVisibility(View.VISIBLE);
                    _categoryRelativeLayout2.setVisibility(View.GONE);
                    _saveShareLinearLayout.setVisibility(View.VISIBLE);
                    _selectPicRelativelayout.setVisibility(View.GONE);
                    _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
                    applySepiaEffect(.7, 0.3, 0.2);
                }
                else
                    AnimateImageButton();
                break;
            case R.id.closeBtn:
                showInterstitialAd();
                upperImage.setVisibility(View.GONE);
                upperImage.setImageResource(android.R.color.transparent);
                _closeLinearLayout.setVisibility(View.GONE);
                _seekbarCatRelative3.setVisibility(View.GONE);
                _saveShareLinearLayout.setVisibility(View.GONE);
                _seekbarAlphaRelative4.setVisibility(View.GONE);
                _selectPicRelativelayout.setVisibility(View.VISIBLE);
                _categoryRelativeLayout2.setVisibility(View.VISIBLE);
                break;
            case R.id.downloadBtn:
                saveImage(takeScreenshot());
                break;
            case R.id.shareBtn:
                sharePicture(takeScreenshot());
                break;
        }
    }

    private void setToneEffect()
    {
        Bitmap workingBitmap = Bitmap.createBitmap(bImage);
        final Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                Filter myFilter = new Filter();
                Point[] rgbKnots;
                rgbKnots = new Point[3];
                rgbKnots[0] = new Point(0, 0);
                rgbKnots[1] = new Point(175, 139);
                rgbKnots[2] = new Point(255, 255);

                Point[] redKnots;
                redKnots = new Point[3];
                redKnots[0] = new Point(0, 0);
                redKnots[1] = new Point(175, 139);
                redKnots[2] = new Point(255, 255);

                myFilter.addSubFilter(new ToneCurveSubfilter(rgbKnots, redKnots, null, null));
                tempImage1 = myFilter.processFilter(mutableBitmap);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                closeProgress();
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
            }
        }.execute();

    }

    private void showInterstitialAd()
    {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener()
        {
            public void onAdLoaded()
            {
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
            }
        });
    }

    private void setCartoonEffect()
    {
        Bitmap workingBitmap = Bitmap.createBitmap(bImage);
        final Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                Filter myFilter = new Filter();
                myFilter.addSubFilter(new VignetteSubfilter(getApplicationContext(), 255));
                tempImage1 = myFilter.processFilter(mutableBitmap);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
                closeProgress();
            }
        }.execute();


    }

    private void setAlphaSeekBar(int val)
    {
        seekBar1.setProgress(val);
        upperImage.setAlpha((float) (val*0.01));
        mTxvSeekBarValue.setText(" "+ val + " % ");
    }

    private void blackFilter()
    {
        bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        Bitmap tempImage = allEffects.applyBlackFilter(bImage);
        upperImage.setVisibility(View.VISIBLE);
        upperImage.setImageBitmap(tempImage);
    }

    private void setRectPolyEffect()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                tempImage1 = LowPoly.lowPoly(bImage, 10000, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
                closeProgress();
            }
        }.execute();
    }



    private void applySepiaEffect(final double red, final double green, final double blue)
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                tempImage1 = allEffects.applySepiaToningEffect(bImage,150,red, green, blue);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
                closeProgress();
            }
        }.execute();
    }
    private void applyBoostEffect(final int type, final int per)
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();

            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                tempImage1 = allEffects.applyBoostEffect(bImage,type,per);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
                closeProgress();
            }
        }.execute();
    }

    private void showSnowEffect()
    {
        bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        Bitmap tempImage = allEffects.applySnowEffect(bImage);
        upperImage.setImageBitmap(tempImage);
        upperImage.setVisibility(View.VISIBLE);
    }

    private void setPolyWithTint()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                tempImage1 = LowPoly.lowPoly(bImage, 10000, true);
                tempImage1 = allEffects.tintImage(tempImage1,200);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                upperImage.setImageBitmap(tempImage1);
                closeProgress();
            }
        }.execute();
    }

    private void setPolyEffect()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                System.out.println("Hello : "+ bImage.getWidth());
                System.out.println("Hello : "+ bImage.getHeight());
                bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                tempImage1 = LowPoly.lowPoly(bImage, 10000, true);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
                closeProgress();
            }
        }.execute();


    }

    private void setEffectBackground()
    {
        _closeLinearLayout.setVisibility(View.VISIBLE);
        _categoryRelativeLayout2.setVisibility(View.GONE);
        _saveShareLinearLayout.setVisibility(View.VISIBLE);
        _selectPicRelativelayout.setVisibility(View.GONE);
        _seekbarAlphaRelative4.setVisibility(View.VISIBLE);
        _seekbarCatRelative3.setVisibility(View.VISIBLE);
    }


    private void showProgress()
    {
        _progressRound.setVisibility(View.VISIBLE);
    }

    private void closeProgress()
    {
        _progressRound.setVisibility(View.GONE);
    }

    View rootView;


    private Bitmap takeScreenshot()
    {
//        System.out.println("Check this  : "+ rootView);
//        rootView = null;
//        rootView = getWindow().getDecorView().findViewById(R.id.relativeCapture);
//        rootView.setDrawingCacheEnabled(true);
//        return rootView.getDrawingCache();
        View z = null;
        z = (View) findViewById(R.id.relativeCapture);
        z.setDrawingCacheEnabled(false);
        z.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(z.getDrawingCache());
        return  z.getDrawingCache();
    }

    private Bitmap combineTwoBitmaps(Bitmap background, Bitmap foreground)
    {
        Bitmap combinedBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(foreground, 0, 0, paint);
        return combinedBitmap;
    }

    Bitmap tempImage1;

    private void saturationEffect()
    {
        Bitmap workingBitmap = Bitmap.createBitmap(bImage);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(5f));
        Bitmap outputImage = myFilter.processFilter(mutableBitmap);
        upperImage.setImageBitmap(outputImage);
        upperImage.setVisibility(View.VISIBLE);

//        tempImage1 = allEffects.applySaturationFilter(bImage, saturation);

    }

    private void depthEffect()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                tempImage1 =  allEffects.decreaseColorDepth(bImage,64);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                closeProgress();
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
            }
        }.execute();


    }

    private void shadeEffect(int shade)
    {
        System.out.println("Shade : " + shade);
        Bitmap tempImage = allEffects.applyShadingFilter(bImage, shade);
        bImage.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        upperImage.setImageBitmap(tempImage);
        upperImage.setVisibility(View.VISIBLE);
    }

    private void HueEffect(final int hue)
    {
        System.out.println("Hello : " + hue);
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                System.out.println("Hello>>>>>>");
                bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                tempImage1 =  allEffects.applyHueFilter(bImage, hue);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                closeProgress();
                upperImage.setImageBitmap(tempImage1);
                upperImage.setVisibility(View.VISIBLE);
            }
        }.execute();


    }

    private void tintEffect(int tint)
    {
        bImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        Bitmap tempImage = allEffects.tintImage(bImage, tint);
        upperImage.setImageBitmap(tempImage);
        upperImage.setVisibility(View.VISIBLE);
    }

    Boolean _isRotated = false;

    private void AnimateImageButton()
    {
        if(!_isRotated) {
            RotateAnimation ra = new RotateAnimation(0, 405, _menuBtn.getWidth() / 2, _menuBtn.getHeight() / 2);
            ra.setFillAfter(true);
            ra.setDuration(500);
            _isRotated = true;
            _menuBtn.startAnimation(ra);

            showRound();
        }
        else
        {
            RotateAnimation ra = new RotateAnimation(405, 0, _menuBtn.getWidth() / 2, _menuBtn.getHeight() / 2);
            ra.setFillAfter(true);
            ra.setDuration(500);
            _isRotated = false;
            _menuBtn.startAnimation(ra);

            hideRound();
        }
    }

    private void hideRound()
    {
        btnCamera.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -((200*heightY)/1920));
        animation.setDuration(500); // duartion in ms
        animation.setFillAfter(true);
        btnCamera.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                btnCamera.clearAnimation();
                btnCamera.setY(((20*heightY)/1920));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        btnGallery.setVisibility(View.VISIBLE);
        TranslateAnimation animation1 = new TranslateAnimation(0, 0, 0, -((400*heightY)/1920));
        animation1.setDuration(500); // duartion in ms
        animation1.setFillAfter(true);
        btnGallery.startAnimation(animation1);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                btnGallery.clearAnimation();
                btnGallery.setY(((20*heightY)/1920));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showRound()
    {
        btnCamera.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, ((200*heightY)/1920));
        animation.setDuration(500); // duartion in ms
        animation.setFillAfter(true);
        btnCamera.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                btnCamera.clearAnimation();
                btnCamera.setY(((220*heightY)/1920));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        btnGallery.setVisibility(View.VISIBLE);
        TranslateAnimation animation1 = new TranslateAnimation(0, 0, 0, ((400*heightY)/1920));
        animation1.setDuration(500); // duartion in ms
        btnGallery.startAnimation(animation1);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                btnGallery.clearAnimation();
                btnGallery.setY(((420*heightY)/1920));
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

    private void selectCat(int value)
    {
        switch (value)
        {
            case 1:
                _image1selected.setVisibility(View.VISIBLE);
                textView1.setTextColor(Color.WHITE);
                break;
            case 2:
                _image2selected.setVisibility(View.VISIBLE);
                textView2.setTextColor(Color.WHITE);
                break;
            case 3:
                _image3selected.setVisibility(View.VISIBLE);
                textView3.setTextColor(Color.WHITE);
                break;
            case 4:
                _image4selected.setVisibility(View.VISIBLE);
                textView4.setTextColor(Color.WHITE);
                break;
            case 5:
                _image5selected.setVisibility(View.VISIBLE);
                textView5.setTextColor(Color.WHITE);
                break;
            case 6:
                _image6selected.setVisibility(View.VISIBLE);
                textView6.setTextColor(Color.WHITE);
                break;
            case 7:
                _image7selected.setVisibility(View.VISIBLE);
                textView7.setTextColor(Color.WHITE);
                break;
            case 8:
                _image8selected.setVisibility(View.VISIBLE);
                textView8.setTextColor(Color.WHITE);
                break;
            case 9:
                _image9selected.setVisibility(View.VISIBLE);
                textView9.setTextColor(Color.WHITE);
                break;
            case 10:
                _image10selected.setVisibility(View.VISIBLE);
                textView10.setTextColor(Color.WHITE);
                break;
            case 11:
                _image11selected.setVisibility(View.VISIBLE);
                textView11.setTextColor(Color.WHITE);
                break;
            case 12:
                _image12selected.setVisibility(View.VISIBLE);
                textView12.setTextColor(Color.WHITE);
                break;
            case 13:
                _image13selected.setVisibility(View.VISIBLE);
                textView13.setTextColor(Color.WHITE);
                break;
            case 14:
                _image14selected.setVisibility(View.VISIBLE);
                textView14.setTextColor(Color.WHITE);
                break;
            case 15:
                _image15selected.setVisibility(View.VISIBLE);
                textView15.setTextColor(Color.WHITE);
                break;
        }
    }

    private void deSelectall()
    {
        _image1selected.setVisibility(View.INVISIBLE);
        _image2selected.setVisibility(View.INVISIBLE);
        _image3selected.setVisibility(View.INVISIBLE);
        _image4selected.setVisibility(View.INVISIBLE);
        _image5selected.setVisibility(View.INVISIBLE);

        _image6selected.setVisibility(View.INVISIBLE);
        _image7selected.setVisibility(View.INVISIBLE);
        _image8selected.setVisibility(View.INVISIBLE);
        _image9selected.setVisibility(View.INVISIBLE);
        _image10selected.setVisibility(View.INVISIBLE);

        _image11selected.setVisibility(View.INVISIBLE);
        _image12selected.setVisibility(View.INVISIBLE);
        _image13selected.setVisibility(View.INVISIBLE);
        _image14selected.setVisibility(View.INVISIBLE);
        _image15selected.setVisibility(View.INVISIBLE);

        textView1.setTextColor(getResources().getColor(R.color.colorDefault));
        textView2.setTextColor(getResources().getColor(R.color.colorDefault));
        textView3.setTextColor(getResources().getColor(R.color.colorDefault));
        textView4.setTextColor(getResources().getColor(R.color.colorDefault));
        textView5.setTextColor(getResources().getColor(R.color.colorDefault));
        textView6.setTextColor(getResources().getColor(R.color.colorDefault));
        textView7.setTextColor(getResources().getColor(R.color.colorDefault));
        textView8.setTextColor(getResources().getColor(R.color.colorDefault));
        textView9.setTextColor(getResources().getColor(R.color.colorDefault));
        textView10.setTextColor(getResources().getColor(R.color.colorDefault));
        textView11.setTextColor(getResources().getColor(R.color.colorDefault));
        textView12.setTextColor(getResources().getColor(R.color.colorDefault));
        textView13.setTextColor(getResources().getColor(R.color.colorDefault));
        textView14.setTextColor(getResources().getColor(R.color.colorDefault));
        textView15.setTextColor(getResources().getColor(R.color.colorDefault));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("hello requestCode >>>>>>> : "+ resultCode);
        upperImage.setImageResource(android.R.color.transparent);

        if (requestCode == PICK_IMAGE_REQUEST)
            onSelectFromGalleryResult(data);
        else if (requestCode == REQUEST_CAMERA)
            onCaptureImageResult(data);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {
        // BEST QUALITY MATCH
        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                bImage = bm;
                showSelectedImage(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onGetDeviceSize()
    {
        Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        widthX = size.x;
        heightY = size.y;
        System.out.println("widthX : " + widthX);
        System.out.println("heightY : " + heightY);
    }

    private void onCaptureImageResult(Intent data)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File file = new File(Environment.getExternalStorageDirectory() + File.separator +timeStamp+"image.jpg");
        System.out.println("Helllloooo >>>>>>>>>>> : " + file.getAbsolutePath());
        Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), widthX, heightY);
        System.out.println("Bitmap : "+ bitmap);
        if(bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            showSelectedImage(bitmap);
            bImage = bitmap;
        }

//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        bImage = thumbnail;
//        System.out.println("Hello >>>> : "+ thumbnail.getWidth() + " && " + thumbnail.getHeight());
//        showSelectedImage(thumbnail);
    }

    private void showSelectedImage(Bitmap bitmap)
    {
        mainImage.setImageBitmap(bitmap);
//        mainImage.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));


//        mainImage.setScaleType(ImageView.ScaleType.CENTER);
//        mainImage.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
    }

    private void takePicturefromGallery()
    {
        AnimateImageButton();
        boolean result = Utility.checkPermission(MainActivity.this);
        if (result) {
            _isOpenGallery = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    private File imageUri;
    String timeStamp;

    private void takePhotoFromCamera()
    {
        AnimateImageButton();
        boolean result = Utility.checkPermission(MainActivity.this);
        if (result) {
            _isOpenGallery = false;

//            imageUri = getOutputMediaFile();
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            startActivityForResult(intent, REQUEST_CAMERA);

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + timeStamp +"image.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, REQUEST_CAMERA);
            System.out.println("Hello >>>>>>>> : "+ file.getAbsolutePath());

//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (_isOpenGallery)
                        takePicturefromGallery();
                    else
                        takePhotoFromCamera();
                } else {
                    //code for deny
                }
                break;
        }
    }

    public void sharePicture(Bitmap bitmap)
    {
        Bitmap icon = bitmap;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Photo Prisma");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_hhmmss").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    File pictureFile;

    private void saveImage(final Bitmap image)
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                pictureFile = getOutputMediaFile();
                if (pictureFile == null)
                {
                    Log.d(TAG,
                            "Error creating media file, check storage permissions: ");// e.getMessage());
                    return null;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    fos.close();
//
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                closeProgress();
                Toast.makeText(getApplicationContext(),"Image save at - "+pictureFile.toString(),Toast.LENGTH_LONG).show();
            }
        }.execute();

    }


    private void saveToInternalStorage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(getApplicationContext(),"Image save at - "+pictureFile.toString(),Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

//    private String saveToInternalStorage(Bitmap bitmapImage) {
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        File mypath = new File(directory, "profile.jpg");
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return directory.getAbsolutePath();
//    }


//    class OilFilter {
//         {
//            System.loadLibrary("AndroidImageFilter");
//        }
//

    private static int calculateHeight(int originalWidth, int originalHeight, int width) {
        return (int) Math.ceil(originalHeight / ((double) originalWidth/width));
    }
}
