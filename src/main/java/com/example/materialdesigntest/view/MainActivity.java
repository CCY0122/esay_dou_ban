package com.example.materialdesigntest.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.materialdesigntest.R;
import com.example.materialdesigntest.diy_view.ViewIndicator;
import com.example.materialdesigntest.gsonBean.GMovie;
import com.example.materialdesigntest.util.Mlog;
import com.example.materialdesigntest.util.Mutil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewIndicator viewIndicator;
    private ViewPager contentViewPager;

    private List<Fragment> fragmentList;

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
        viewIndicator = (ViewIndicator) findViewById(R.id.view_indicator);
    }


    private void initMenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id._1:
//                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                        Mutil.showToast("1");
                        break;
                    case R.id._2:
//                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                        Mutil.showToast("2");
                        break;
                    case R.id._3:
//                        Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
                        Mutil.showToast("3");
                        break;
                    case R.id._4:
//                        Toast.makeText(MainActivity.this, "4", Toast.LENGTH_SHORT).show();
                        Mutil.showToast("4");
                        break;
                    case R.id._5:
//                        Toast.makeText(MainActivity.this, "5", Toast.LENGTH_SHORT).show();
                        Mutil.showToast("5");
                        break;
                }
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                Toast.makeText(MainActivity.this, "open", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                Toast.makeText(MainActivity.this, "close", Toast.LENGTH_SHORT).show();
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }



    private void initContentViewPager() {
        fragmentList = new ArrayList<>();

        fragmentList.add(Fragment_1.newInstance(Fragment_1.TYPE_MOVIE));
        fragmentList.add(Fragment_1.newInstance(Fragment_1.TYPE_MOVIE));
        fragmentList.add(Fragment_1.newInstance(Fragment_1.TYPE_MOVIE));
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
            case android.R.id.home:
                Toast.makeText(this, "222", Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
    }
}


//    LinearLayout linearLayout;
//    TextView t;
//    Button b;
//    TextView t2;
//    ViewOutlineProvider viewOutlineProvider = null;
//    ImageView img;
//    ImageView img2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.abe);
////        linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_pager,null);
//        linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setVisibility(View.INVISIBLE);
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "33", Toast.LENGTH_SHORT).show();
//            }
//        });
//        img = (ImageView) findViewById(R.id.img);
//        img2 = (ImageView) findViewById(R.id.imgg);
//        b = (Button) findViewById(R.id.btn);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "9", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, Activity_2.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent, ActivityOptions.
//                            makeSceneTransitionAnimation(MainActivity.this, img,"share").toBundle());
////                    startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
//                }
//            }
//        });
//        t2 = (TextView) findViewById(R.id.tv_2);
//        t2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
//                Log.d("ccy", "1");
//            }
//        });
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            viewOutlineProvider = new ViewOutlineProvider() {
//                @Override
//                public void getOutline(View view, Outline outline) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        outline.setOval(0, 0, view.getWidth(), view.getHeight());
//                    }
//                }
//            };
//        }
//        t = (TextView) findViewById(R.id.tv);
//        t.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//                    t.setOutlineProvider(viewOutlineProvider);
//                    linearLayout.setVisibility(View.VISIBLE);
//                    Animator animator = ViewAnimationUtils.createCircularReveal(linearLayout, 0, 0, 0, linearLayout.getWidth());
//                    animator.setDuration(400);
////                    animator.setInterpolator(new AccelerateInterpolator());
//                    animator.start();
//                }
//            }
//        });
//    }
//}
