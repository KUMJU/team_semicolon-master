package com.semicolon.project.myapplication;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    public static Context context;

    public static Context getAppContext() {
        return MainActivity.context;
    }

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;

    Toolbar myToolbar;

    //json 변수
    private static String TAG = "sql debug";
    private static final String TAG_JSON="webnautes";
    private static final String TAG_NAME = "name";
    private static final String TAG_Value = "value";
    public static String mJsonString;
    public static String j_name;
    public static String j_value;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //날짜 관련 변수
    TextView printDate;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("MM월dd일");
    String Today = sdfNow.format(date);

    //파이차트
    private RelativeLayout mainLayout;
    private PieChart mChart;
    private FrameLayout chartContainer;
    //setBackgroundDrawable(R.drawable.background);
    //메뉴 레이아웃 생성 함수

    //디비
    DBManager db;

    //ui 쓰레드
    public static Thread th;

    //설정
    SharedPreferences prefs;

    //식품명
    private static final String food_name[] = { "유제품", "즉석식품", "가공식품", "제과", "제빵", "과채류", "육류", "해산물", "소스류", "기타" };

    //뒤로가기
    private BackPressCloseHandler backPressCloseHandler;

    //비밀입니다 깔~
    egg easter = new egg();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //DB
        db = new DBManager(this);

        MainActivity.context = getApplicationContext();

        //툴바
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_dehaze_white_24);
        myToolbar.setTitleTextColor(Color.WHITE);

        //날짜출력

        printDate = (TextView) findViewById(R.id.Date_String);
        printDate.setText(Today);

        //네비게이션 메뉴
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        //설정
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //뒤로가기
        backPressCloseHandler = new BackPressCloseHandler(this);


        //파이차트
        // array of graph percentage value

        mainLayout = (RelativeLayout) findViewById(R.id.pieLayout);
        chartContainer =(FrameLayout) findViewById(R.id.chartContainer);

        mChart = new PieChart(this);
        chartContainer.addView(mChart);
        mChart.setUsePercentValues(true);
        mChart.setDescription(new Description());

        //enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(30);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotation(0);
        mChart.setRotationEnabled(true);

        //set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            public void onValueSelected(PieEntry e, int dataSetIndex, Highlight h) {
                //display message when value selected
                if (e == null)
                    return;
            }

            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        //customize legends

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        final TextView talk= findViewById ( R.id.nentalk );
    ///쓰레드 초기화
       th = new Thread(new Runnable() {
            @Override
            public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addData();
                            talk.setText ( talk () );

                            //  Initialize SharedPreferences
                            SharedPreferences getPrefs = PreferenceManager
                                    .getDefaultSharedPreferences(getBaseContext());

                            //  Create a new boolean and preference and set it to true
                            boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                            //  If the activity has never started before...
                            if (isFirstStart) {

                                //  Launch app intro
                                final Intent i = new Intent(MainActivity.this, AppIntroActivity.class);

                                runOnUiThread(new Runnable() {
                                    @Override public void run() {
                                        startActivity(i);
                                    }
                                });

                                //  Make a new preferences editor
                                SharedPreferences.Editor e = getPrefs.edit();

                                //  Edit preference to make it false because we don't want this to run again
                                e.putBoolean("firstStart", false);

                                //  Apply changes
                                e.apply();
                            }

                        }
                    });
            }
        });

        th.start();

    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public String talk(){
        Cursor c = db.sort_Date ();
        String name = null;

        while (c.moveToNext()) {
            name = c.getString ( 1 );
            name = name + "(이)가 안에서 썩고있어요..엉엉...ㅠㅜ";
            break;
        }
        if(name==null){
            name="냉장고가 비었어요 냉장이를 채워주세요~";
        }

        return name;

    }

    private void addData(){
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        int food[] = new int[9];

        for(int i = 0; i<9; i++) {
            food[i] = db.countSelect(food_name[i]);
        }

        for(int i = 0; i<9; i++) {
            if(food[i]!=0){
                yVals1.add(new PieEntry(food[i], food_name[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(7);
        dataSet.setSelectionShift(5);


        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int c: ColorTemplate.VORDIPLOM_COLORS)
        //    colors.add(c);
        //for (int c: ColorTemplate.JOYFUL_COLORS)
        //    colors.add(c);
        for (int c: ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c: ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c: ColorTemplate.PASTEL_COLORS)
        colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        if(prefs.getBoolean("PieChart", true)){
            Log.d("파이차트", "on");
            //data.setDrawValues(true);
        }else{
            Log.d("파이차트", "off");
            data.setDrawValues(false);
        }


        mChart.setData(data);

        //undo all higlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.animateY(2000);
        mChart.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                //Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab1:
                anim();
                //Toast.makeText(this, "Button1", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,InputActivity.class));
                break;
            case R.id.fab2:
                anim();
                //Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                startQRCode();
                break;
        }
    }
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //item.setChecked(true);
        //drawerLayout.closeDrawers();
        int id = item.getItemId();

        // 각 메뉴 클릭시 이뤄지는 이벤트
        FragmentManager manager = getFragmentManager();

        easter.addegg();
        switch (id) {
            case R.id.navigation_item_box:
                Intent intent=new Intent(this, ListActivity.class);
                startActivity(intent);
                break;

            case R.id.navigation_item_help:
                Intent intent1 = new Intent(this, helpActivity.class);
                startActivity(intent1);
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                break;

            case R.id.navigation_item_info:
                Intent intent2=new Intent(this, SettingActivity.class);
                startActivity(intent2);
                break;
                /*
                if(easter.getegg() == 0) {
                    Toast.makeText(MainActivity.this, R.string.Develop_info, Toast.LENGTH_LONG).show();
                }else if(easter.getegg() == 1) {
                    Toast.makeText(MainActivity.this, R.string.school, Toast.LENGTH_LONG).show();
                }else if(easter.getegg() == 2) {
                    Toast.makeText(MainActivity.this, R.string.imfun, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, R.string.last, Toast.LENGTH_LONG).show();
                }
                break;
                */
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startQRCode() { //바코드 리딩 함수
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //바코드 리딩 결과값 출력
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result == null) {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                new GetData().execute(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, null);
        }
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "start");
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result == null){
                Toast.makeText(MainActivity.this, "정보가 없습니다.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,InputActivity.class));
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        protected String doInBackground(String... params) {
            String cdata = params[0];
            Log.d("params", "params - " + params[0]);
            String serverURL = "http://semiserver.iptime.org:80/query.php";
            String postParameters = "code=" + cdata;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, " code - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return null;
            }
        }

        private void showResult(){
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    j_name = item.getString(TAG_NAME);
                    j_value = item.getString(TAG_Value);
                }

                Intent intent = (new Intent(MainActivity.this, InputActivity.class));
                intent.putExtra("Name", j_name);
                intent.putExtra("Value", j_value);
                startActivity(intent);

            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }
}