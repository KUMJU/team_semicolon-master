package com.semicolon.project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_intro_action);

        showSkipButton ( false );

        addSlide(AppIntroFragment.newInstance("모든 음식을 맛있게 먹을수 있도록", "저희는 신선한 음식을 드실수 있도록 도와드립니다.",
                R.drawable.intro_image1, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("내가 원하는 시간에 언제든지", "어떤 음식이든 잊지 않도록 알려드립니다.",
                R.drawable.intro_image2, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("지금부터 시작해볼까요?", "저희와 함께 첫 식품을 등록해보세요!",
                R.drawable.intro_image3, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
/*
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
*/
    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
