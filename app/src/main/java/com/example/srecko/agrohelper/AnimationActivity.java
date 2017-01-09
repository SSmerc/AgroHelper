package com.example.srecko.agrohelper;

import android.content.Intent;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnimationActivity extends AppCompatActivity implements Animation.AnimationListener{

    Button btnStart;
    TextView txt;
    ImageView farmer, corn, carrot, potato, basket,tomato,hay_f;
    // Animation
    Animation animCarrot,animPotato, animCorn, animBasket, animFarmer,animTomato, animHayF,animTxt , animfade,animTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        hay_f=(ImageView) findViewById(R.id.hayFImg);
        farmer= (ImageView) findViewById(R.id.farmerImg);
        corn= (ImageView) findViewById(R.id.cornImg);
        carrot= (ImageView) findViewById(R.id.carrotImg);
        potato= (ImageView) findViewById(R.id.potatoImg);
        basket= (ImageView) findViewById(R.id.basketImg);
        tomato=(ImageView) findViewById(R.id.tomatoImg);
        txt= (TextView) findViewById(R.id.txt);
        txt.setVisibility(View.INVISIBLE);
        farmer.setVisibility(View.INVISIBLE);
        hay_f.setVisibility(View.INVISIBLE);
        corn.setVisibility(View.INVISIBLE);
        potato.setVisibility(View.INVISIBLE);
        carrot.setVisibility(View.INVISIBLE);
        tomato.setVisibility(View.INVISIBLE);
        basket.setVisibility(View.INVISIBLE);
        //load animation
        animCarrot = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_carrot);
        animPotato = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_potato);
        animCorn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_corn);
        animBasket = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFarmer = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.farmer);
        animTomato= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation_tomato);
        animHayF= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.hay_f);
        animTxt = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animfade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        animTemp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        farmer.setImageResource(R.drawable.farmer);
        corn.setImageResource(R.drawable.corn);
        carrot.setImageResource(R.drawable.carrot);
        potato.setImageResource(R.drawable.potato);
        basket.setImageResource(R.drawable.hay_basket);
        tomato.setImageResource(R.drawable.tomato);
        hay_f.setImageResource(R.drawable.hay_f);

        // set animation listener

        animCarrot.setAnimationListener(this);
        animPotato.setAnimationListener(this);
        animBasket.setAnimationListener(this);
        animCorn.setAnimationListener(this);
        animFarmer.setAnimationListener(this);
        animTomato.setAnimationListener(this);
        basket.startAnimation(animBasket);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
            carrot.setVisibility(View.VISIBLE);
            carrot.startAnimation(animCarrot);
            corn.setVisibility(View.VISIBLE);
            corn.startAnimation(animCorn);
            potato.setVisibility(View.VISIBLE);
            potato.startAnimation(animPotato);
            tomato.setVisibility(View.VISIBLE);
            tomato.startAnimation(animTomato);
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    hay_f.setVisibility(View.VISIBLE);
                    hay_f.startAnimation(animHayF);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            farmer.setVisibility(View.VISIBLE);
                            farmer.startAnimation(animFarmer);
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    carrot.startAnimation(animfade);
                                    corn.startAnimation(animfade);
                                    potato.startAnimation(animfade);
                                    tomato.startAnimation(animfade);
                                    hay_f.startAnimation(animfade);
                                    farmer.startAnimation(animfade);
                                    basket.startAnimation(animfade);

                                    new Handler().postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            txt.setVisibility(View.VISIBLE);
                                            txt.startAnimation(animTxt);
                                            new Handler().postDelayed(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    farmer.setVisibility(View.INVISIBLE);
                                                    hay_f.setVisibility(View.INVISIBLE);
                                                    corn.setVisibility(View.INVISIBLE);
                                                    potato.setVisibility(View.INVISIBLE);
                                                    carrot.setVisibility(View.INVISIBLE);
                                                    tomato.setVisibility(View.INVISIBLE);
                                                    basket.setVisibility(View.INVISIBLE);
                                                    new Handler().postDelayed(new Runnable()
                                                    {
                                                        @Override
                                                        public void run()
                                                        {
                                                            txt.startAnimation(animTemp);
                                                            new Handler().postDelayed(new Runnable()
                                                            {
                                                                @Override
                                                                public void run()
                                                                {
                                                                    Intent dva = new Intent(AnimationActivity.this, DataActivity.class);
                                                                    startActivity(dva);
                                                                }
                                                            }, 1000);
                                                        }
                                                    }, 1000);
                                                }
                                            }, 1000);
                                        }
                                    }, 1000);
                                }
                            }, 2500);
                        }
                    }, 500);
                }
            }, 2000);
        }
    }, 1000);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

}
