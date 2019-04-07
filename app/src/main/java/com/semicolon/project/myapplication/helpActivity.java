package com.semicolon.project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class helpActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_intro_action);

        addSlide(AppIntroFragment.newInstance("식품 등록하기", "식품을 등록하기 위해 하단 + 버튼을 누르세요",
                R.drawable.help_img1, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("세부메뉴 들어가기", "메뉴 버튼을 통해 보관함, 설정에 들어가보세요",
                R.drawable.help_img2, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("식품 보관함", "등록한 식품들을 확인할 수 있습니다.",
                R.drawable.help_img3, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("식품 보관함", "등록한 식품들을 취향에 따라 정렬할 수 있습니다.",
                R.drawable.help_img4, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("심화기능", "삭제와 레시피를 검색해 볼 수 있습니다.",
                R.drawable.help_img5, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
