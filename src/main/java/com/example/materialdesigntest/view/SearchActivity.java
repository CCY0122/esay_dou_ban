package com.example.materialdesigntest.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.example.materialdesigntest.R;
import com.example.materialdesigntest.gsonBean.DataOverview;
import com.example.materialdesigntest.model.BookListImp;
import com.example.materialdesigntest.model.IDataHandle;
import com.example.materialdesigntest.model.MovieListImp;
import com.example.materialdesigntest.util.Mlog;
import com.example.materialdesigntest.util.Mutil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/15.
 */

public class SearchActivity extends AppCompatActivity{

    private static final String TAG = "search_act";
    private static String SEARCH_BASE_URL = "https://api.douban.com/v2/movie/search";//默认为搜电影基本url
    public static final String SEARCH_MOVIE_BASE_URL = "https://api.douban.com/v2/movie/search";
    public static final String SEARCH_BOOK_BASE_URL = "https://api.douban.com/v2/book/search";
    public static final String SEARCH_MUSIC_BASE_URL = "";

    private static final int OK = 1;
    private static final int ERROR = 0;
    private String url;


    private RadioGroup radioGroup;
    private Button button;
    private AutoCompleteTextView editText;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private ProgressBar pb;
    private SharedPreferences sp;
    private IDataHandle dataHandle = new MovieListImp();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case OK:
                    List<DataOverview> data = (List<DataOverview>) msg.obj;
                    Mlog.d(TAG,"2///"+data.get(0).getTitle());
                    initRecycler(data);

                    break;
                case ERROR:
                    Mutil.showToast("网络出错或请检查输入合法性");
                    break;
            }
            pb.setVisibility(View.INVISIBLE);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        getSupportActionBar().hide();

        initView();
        initHistory();

    }

    private void initHistory() {
        sp = getSharedPreferences("history",MODE_PRIVATE);
        String his = sp.getString("his","ccy,");
        Mlog.d("his","get his:"+his);
        String data[] = his.split(",");
        for (int i = 0; i < data.length; i++) {
            Mlog.d("his","\t--------"+data[i]);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        editText.setAdapter(adapter);

    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.search_radio_group);
        button = (Button) findViewById(R.id.search_btn_do);
        editText = (AutoCompleteTextView) findViewById(R.id.search_edt);
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        pb = (ProgressBar) findViewById(R.id.search_pb);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.search_btn_movie:
                        SEARCH_BASE_URL = SEARCH_MOVIE_BASE_URL;
                        editText.setHint("搜影视、主演、导演");
                        dataHandle = new MovieListImp();
                        if(adapter != null){
                            adapter.setListener(new SearchAdapter.onClickListener() {
                                @Override
                                public Intent getIntent() {
                                    Intent intent = new Intent(SearchActivity.this,Activity_2.class);
                                    return intent;
                                }
                            });
                        }
                        break;
                    case R.id.search_btn_book:
                        SEARCH_BASE_URL = SEARCH_BOOK_BASE_URL;
                        editText.setHint("搜书名、作者、分类");
                        dataHandle = new BookListImp();
                        //// TODO: 2016/12/22 补充book详情页的intent 
                        break;
                    case R.id.search_btn_music:
                        editText.setHint("搜音乐");
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                    url = SEARCH_BASE_URL;
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("q",text);
                    doSearch(url,map);
                    saveHis(text);
                }else {
                    Mutil.showToast("内容不能为空呀");
                }
            }
        });
    }

    private void saveHis(String text) {
        sp = getSharedPreferences("history",MODE_PRIVATE);
        String his = sp.getString("his","ccy,");
        Mlog.d("his",his);
        if(his.contains(text)){
            Mlog.d("his","true");
            return;
        }
        StringBuffer sb = new StringBuffer(his).append(text).append(",");
        Mlog.d("his:","save his:"+sb.toString());
        SharedPreferences.Editor spe = sp.edit();
        spe.putString("his",sb.toString());
        spe.commit();

    }

    private void initRecycler(List<DataOverview> data) {
        adapter = new SearchAdapter(this , data ,SearchActivity.this);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new SearchAdapter.onClickListener() {
            @Override
            public Intent getIntent() {
                Intent intent = new Intent(SearchActivity.this,Activity_2.class);
                return intent;
            }
        });
    }

    private void doSearch(String url , Map<String,String> map) {
        pb.setVisibility(View.VISIBLE);
        Mutil.getJsonFromUrl(url,map,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Mlog.d(TAG,""+response.code()+"---"+response.body().string());
                if(response.isSuccessful()){
                    String json = response.body().string();
//                    Mlog.d(TAG,""+json);
                    List<DataOverview> data;
                    data = dataHandle.parseJsonArray(json);
                    Mlog.d(TAG,"1///"+data.get(0).getTitle());
                    Message msg = handler.obtainMessage(OK);
                    msg.obj = data;
                    handler.sendMessage(msg);
                }else {
                    handler.sendEmptyMessage(ERROR);
                }
            }
        });
    }
}
