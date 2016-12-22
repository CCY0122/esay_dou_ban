package com.example.materialdesigntest.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.materialdesigntest.R;
import com.example.materialdesigntest.diy_view.ViewIndicator;
import com.example.materialdesigntest.gsonBean.GMovie;
import com.example.materialdesigntest.util.Mlog;
import com.example.materialdesigntest.util.Mutil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String IMG_URL = "http://guolin.tech/api/bing_pic";
    //杭州天气
    private static final String WEATHER = "http://guolin.tech/api/weather?key=62c9c1cfe4a54a41864234cc148a7b22&cityid=CN101210101";

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView navigationHeader;
    private TextView navigationWeather;
    private ViewIndicator viewIndicator;
    private ViewPager contentViewPager;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private List<Fragment> fragmentList;
    private boolean quitflag = true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            quitflag = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initContentViewPager();
        initMenu();
    }


    private void initView() {
        contentViewPager = (ViewPager) findViewById(R.id.content_view_pager);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        navigationView = (NavigationView) findViewById(R.id.draw_menu);
        navigationHeader = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.navigation_header);
        navigationWeather = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_weather);
        viewIndicator = (ViewIndicator) findViewById(R.id.view_indicator);
    }


    private void initMenu() {
//        try {
//            setNavigationHeader();
//        } catch (IOException e) {
//            navigationHeader.setImageResource(R.drawable.aaz);
//        }
        updateWeather();
        downloadImg();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawer_search:
                        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_set:
                        Mutil.showToast("2");
                        break;
                    case R.id.drawer_update:
                        Mutil.showToast("3");
                        break;
                    case R.id.drawer_clean:
                        SharedPreferences sp = getSharedPreferences("history",0);
                        sp.edit().clear().commit();
                        Mutil.showToast("清理成功");
                        break;
                    case R.id.drawer_support_author:
                        Mutil.showToast("5");
                        break;
                    case R.id.drawer_mianzeshengming:

                        break;
                    case R.id.drawer_feedback:

                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    private void setNavigationHeader() throws IOException {   //算了。。。
        SharedPreferences sp = getSharedPreferences("header_img",0);
        String url = sp.getString("url",null);
        if(TextUtils.isEmpty(url)){
           downloadImg();
        }else {
            Glide.with(this).load(url).into(navigationHeader);
        }
    }
    private void updateWeather() {
        Request request = new Request.Builder().url(WEATHER).build();
        Mutil.getOkHttp().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    parseWeatherJson(response.body().string());
                }
            }

            private void parseWeatherJson(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    JSONArray jsonArr= json.optJSONArray("HeWeather");
                    JSONObject j = jsonArr.optJSONObject(0);
                    JSONObject jnow = j.optJSONObject("now");
                    final StringBuffer sb = new StringBuffer("杭州：");
                    sb.append(jnow.optJSONObject("cond").optString("txt")).append("\t");
                    sb.append(jnow.optString("tmp")).append("\u2103").append("\n");
                    sb.append("相对湿度：").append(jnow.optString("hum")).append("%\n");
                    sb.append("更新于").append(j.optJSONObject("basic").optJSONObject("update").optString("loc"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            navigationWeather.setText(""+sb.toString());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void downloadImg(){
        Request request = new Request.Builder().url(IMG_URL).build();
        Mutil.getOkHttp().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(response.isSuccessful()){
                    final String url = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this).load(url).into(navigationHeader);
                            SharedPreferences sp = getSharedPreferences("header_img",0);
                            sp.edit().putString("url",url).commit();
                        }
                    });
                }
            }
        });
    }


    private void initContentViewPager() {
        fragmentList = new ArrayList<>();

        fragmentList.add(Fragment_1.newInstance(Fragment_1.TYPE_MOVIE));
        fragmentList.add(Fragment_1.newInstance(Fragment_1.TYPE_BOOK));
        fragmentList.add(Fragment_1.newInstance(Fragment_1.TYPE_MOVIE));
        contentViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

        });
        viewIndicator.setViewPager(contentViewPager);
    }

    @Override
    public void onClick(View v) {

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if(quitflag){
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            quitflag = false;
            handler.sendEmptyMessageDelayed(1,2000);
            return;
        }

        super.onBackPressed();
    }
}

