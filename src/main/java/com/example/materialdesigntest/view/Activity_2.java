package com.example.materialdesigntest.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.materialdesigntest.R;
import com.example.materialdesigntest.diy_view.RoundIndicatorView;
import com.example.materialdesigntest.gsonBean.CastsOverview;
import com.example.materialdesigntest.gsonBean.DataOverview;
import com.example.materialdesigntest.gsonBean.GMovie;
import com.example.materialdesigntest.model.IDataHandle;
import com.example.materialdesigntest.model.MovieImp;
import com.example.materialdesigntest.util.Mlog;
import com.example.materialdesigntest.util.Mutil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/4.
 */

public class Activity_2 extends AppCompatActivity {

    private static final String TAG = "activity2";
    private static final int OK = 1;
    private static final int ERROR = 2;
    public static final String MOVIE_BASE_URL = "https://api.douban.com/v2/movie/subject/";
    public static final String ACTOR_BASE_URL = "https://api.douban.com/v2/movie/celebrity/";

    private Intent intent;
    private IDataHandle dataHandle = new MovieImp();
    private DisplayImageOptions option;
    private String id;
    private String imgId;
    private String url;

    private ProgressBar pb;
    private SimpleRecyclerAdapter adapter;
    private ImageView imageView;
    private TextView title;
    private TextView rating;
    private TextView content;
    private RoundIndicatorView roundIndicatorView;
    private RecyclerView recyclerView;
    private TextView summary;
    private TextView to_web;

    private GMovie gMovie;
//    private List<CastsOverview> castsOverviewData = new ArrayList<>();  //原始演员列表
    private List<DataOverview> data = new ArrayList<>(); //适配演员recyclerview列表

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OK:
                    wrapData(gMovie);
                    adapter.notifyDataSetChanged();
                    break;
                case ERROR:
                    Mutil.showToast("出问题了T_T  请检查网络");
                    break;
            }
            pb.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        option = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).
                showImageOnLoading(R.drawable.loading).build();
        initData();
        initView();
        initRecyclerView();
        refresh();

    }

    private void initData() {
        intent = getIntent();
        imgId = intent.getStringExtra("imgId");
        id = intent.getStringExtra("id");
        url = MOVIE_BASE_URL + id;
    }

    private void refresh() {
        pb.setVisibility(View.VISIBLE);
        Mutil.getJsonFromUrl(url, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    gMovie = (GMovie) dataHandle.parseJsonObject(json);
//                    Mlog.d(TAG, "" + gMovie.ratings_count + gMovie.year + gMovie.title);
                    handler.sendEmptyMessage(OK);
                } else {
                    Mlog.d(TAG, "activity_2" + "网络错误" + response.code());
                    handler.sendEmptyMessage(ERROR);
                }
            }
        });
    }

    private void initRecyclerView() {
        adapter = new SimpleRecyclerAdapter(this, data);
        LinearLayoutManager lin = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lin);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new SimpleRecyclerAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(Activity_2.this,WebActivity.class);
                intent.putExtra("url",gMovie.casts.get(pos).alt);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        pb = (ProgressBar) findViewById(R.id.a2_progress_bar);
        imageView = (ImageView) findViewById(R.id.a2_img);
        title = (TextView) findViewById(R.id.a2_title);
        rating = (TextView) findViewById(R.id.a2_rating);
        content = (TextView) findViewById(R.id.a2_content);
        roundIndicatorView = (RoundIndicatorView) findViewById(R.id.a2_round_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.a2_recycler_view);
        summary = (TextView) findViewById(R.id.a2_summary);
        to_web = (TextView) findViewById(R.id.a2_to_web_view);

        if (imgId != null) {
            ImageLoader.getInstance().displayImage(imgId, imageView, option);//提前加载本意是为了能过渡动画，UIL里应该有缓存
        }

    }

    private void wrapData(final GMovie gMovie) {
        if (imageView.getDrawable() == null) {
            ImageLoader.getInstance().displayImage(gMovie.images.large, imageView, option);
        }
//        Mlog.d(TAG, "豆瓣评分" + gMovie.rating.average);
        title.setText(gMovie.title + "");
        if (gMovie.rating.average == 0.0) {
            rating.setText("暂无");
        } else {
            rating.setText(gMovie.rating.average + "");
        }
        roundIndicatorView.setCurrentNumAnim(gMovie.ratings_count);
        summary.setText("简介：" + gMovie.summary + "");

        StringBuffer con = new StringBuffer();
        con.append("类型:");
        for (int i = 0; i < gMovie.genres.size(); i++) {
            con.append(gMovie.genres.get(i)+"\t\t");
        }
        con.append("。").append("上映时间:").append(gMovie.year).append("。\t\t");
        con.append("演员表:");
        for (int i = 0; i < gMovie.casts.size(); i++) {
            con.append(gMovie.casts.get(i).name+"\t\t");
        }
        content.setText(con.toString() + "");

        to_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_2.this,WebActivity.class);
                intent.putExtra("url",gMovie.alt);
                startActivity(intent);
            }
        });

        new DataChange().changeToDataOverView(gMovie.casts, data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class DataChange implements DataOverviewAdapter<CastsOverview> {
        @Override
        public void changeToDataOverView(CastsOverview castsOverview, DataOverview dataOverview) {

        }

        @Override
        public void changeToDataOverView(List<CastsOverview> t, List<DataOverview> dataOverviews) {
            dataOverviews.clear();
            for (int i = 0; i < t.size(); i++) {
                DataOverview d = new DataOverview();
                CastsOverview c = t.get(i);
                d.setId(c.id);
                d.setImgUri(c.avatars.large);
                d.setTitle(c.name);
                d.setContent("");
                dataOverviews.add(d);
            }
        }
    }

}
