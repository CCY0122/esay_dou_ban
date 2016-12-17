package com.example.materialdesigntest.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.materialdesigntest.R;
import com.example.materialdesigntest.diy_view.HeaderViewPager;
import com.example.materialdesigntest.gsonBean.DataOverview;
import com.example.materialdesigntest.model.IDataHandle;
import com.example.materialdesigntest.model.MovieListImp;
import com.example.materialdesigntest.util.Mlog;
import com.example.materialdesigntest.util.Mutil;
import com.example.materialdesigntest.util.MyTransformation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/11.
 */

public class Fragment_1 extends Fragment implements View.OnClickListener {

    public static final String TYPE_MOVIE = "movie";
    public static final String TYPE_BOOK = "book";
    public static final String TYPE_MUSIC = "music";
    private static String DEFAULT_1;
    private static String DEFAULT_2;
    private static String DEFAULT_3;
    public static String MOVIE_1 = "https://api.douban.com/v2/movie/in_theaters"; //正在热映
    public static String MOVIE_2 = "https://api.douban.com/v2/movie/coming_soon";  //即将上映
    public static String MOVIE_3 = "https://api.douban.com/v2/movie/top250";       //top250前20
    private static final int OK = 1;
    private static final int ERROR = 2;
    private String url;
    private String type;
    private DisplayImageOptions options;

    private Context context;
    private MainRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FrameLayout headerView;
    private HeaderViewPager viewPager;
    private LinearLayout viewPagerDot;
    private TextView imgDescribe;
    private FloatingActionButton fab;
    private BottomSheetDialog bottomSheet;

    private List<String> bottomSheetData = new ArrayList<>();
    private List<DataOverview> dataOverviews = new ArrayList<>();
    private List<DataOverview> headerData = new ArrayList<>();
    private IDataHandle dataHandle;

    private boolean first = true;


    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OK:
                    if (adapter != null && mAdapter != null) {
                        initHeaderView();
                        adapter.setHeaderView(headerView);
                        adapter.notifyDataSetChanged();
                        dimssRef();
                    }
                    break;
                case ERROR:
                    Mlog.d("ccy", "ERROR 数据获取失败");
                    Mutil.showToast("数据获取失败,请检查网络");
                    dimssRef();
                    break;
            }
        }
    };


    public Fragment_1() {
        super();
    }

    public static Fragment_1 newInstance(String type) {
        Fragment_1 fragment_1 = new Fragment_1();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment_1.setArguments(bundle);
        return fragment_1;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).showImageOnFail(R.drawable.ddd).showImageOnLoading(R.drawable.loading).build();
        type = getArguments().getString("type");
        switch (type) {
            case TYPE_MOVIE:
                url = MOVIE_1;
                DEFAULT_1 = MOVIE_1;
                DEFAULT_2 = MOVIE_2;
                DEFAULT_3 = MOVIE_3;

                bottomSheetData.clear();
                bottomSheetData.add("正在热映");
                bottomSheetData.add("即将上映");
                bottomSheetData.add("最高评分");
                dataHandle = new MovieListImp();
                break;
            case TYPE_BOOK:

                break;
            case TYPE_MUSIC:

                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_recycler_view, container, false);
        initView(v);
        initHeaderView();
        initRecyclerView();
        if (first) {
            refresh(null);
            first = false;
        }
        return v;
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean flag = true;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 ){
                    hideFab();
                }
                if(dy < 0){
                    showFab();
                }
            }

            private void showFab() {
                if(!flag) {
                    fab.clearAnimation();
                    fab.animate().alpha(1).scaleX(1).scaleY(1).setDuration(400).
                            setInterpolator(new OvershootInterpolator()).
                            setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    fab.setVisibility(View.VISIBLE);
                                }
                            }).start();
                    flag = !flag;
                }
            }

            private void hideFab() {
                if(flag) {
                    fab.clearAnimation();
                    fab.animate().alpha(0.7f).scaleX(0).scaleY(0).setInterpolator(new LinearInterpolator()).setDuration(150).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fab.setVisibility(View.INVISIBLE);
                        }
                    }).start();
                    flag = !flag;
                }
            }
        });
        headerView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_pager, null);
        viewPagerDot = (LinearLayout) headerView.findViewById(R.id.view_pager_dot);
        imgDescribe = (TextView) headerView.findViewById(R.id.view_pager_text);
        viewPager = (HeaderViewPager) headerView.findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true ,new MyTransformation());
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(0xffFF4040);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(null);
            }
        });
        headerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {         //将父容器的事件传递给viewpager
                return viewPager.dispatchTouchEvent(event);
            }
        });

    }

    private void refresh(@Nullable final Map<String,String> map) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.measure(0, 0);
            swipeRefreshLayout.setRefreshing(true);
        }

        Mutil.getJsonFromUrl(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if(map == null) {
                        dataOverviews.clear();
                    }
                    dataOverviews.addAll(dataHandle.parseJsonArray(json));
                    if (dataOverviews != null && dataOverviews.size() > 0) {
                        if (dataOverviews.size() > 4) {
                            headerData.clear();
                            headerData.addAll( dataOverviews.subList(0, 4));
                        }
                        handler.sendEmptyMessage(OK);
                    }
                }else {
                    Mlog.d("ccy","fragment_1"+"网络错误"+response.code());
                }
            }
        });
    }

    private void dimssRef() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private ArrayAdapter apt;
    private void createBottomSheet(){
        bottomSheet = new BottomSheetDialog(context);
        ListView listView = new ListView(context);
        apt = new ArrayAdapter(context,android.R.layout.simple_list_item_1,bottomSheetData);
        listView.setAdapter(apt);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        url = DEFAULT_1;break;
                    case 1:
                        url = DEFAULT_2;break;
                    case 2:
                        url = DEFAULT_3;break;
                }
                refresh(null);
                bottomSheet.dismiss();
            }
        });
        bottomSheet.setContentView(listView);
        bottomSheet.show();
    }

    private void initRecyclerView() {

        adapter = new MainRecyclerAdapter(context, dataOverviews,getActivity());
        adapter.setHeaderView(headerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapter.setOnFooterClick(new MainRecyclerAdapter.onFooterClick() {
            @Override
            public void footClick(View v, int pos) {
                DataOverview d = dataOverviews.get(pos);
                if(d.getStart()+d.getCount() < d.getTotal()){
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("start",d.getStart()+d.getCount()+"");
                    Mlog.d("test","start="+d.getStart()+"total="+d.getTotal()+"count="+d.getCount());
                    refresh(map);
                }else {
                    Mutil.showToast("没有更多了");
                }
            }
        });
    }

    private MAdapter mAdapter;

    private void initHeaderView() {
        mAdapter = new MAdapter(headerData);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < viewPagerDot.getChildCount(); i++) {
                    View view = viewPagerDot.getChildAt(i );
                    ((ImageView) view).setImageResource(R.drawable.unselect);
                }
                imgDescribe.setText(headerData.get(position).getTitle() + "");
                ((ImageView) viewPagerDot.getChildAt(position)).setImageResource(R.drawable.select);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class MAdapter extends PagerAdapter {

        private List<View> viewPagerList = new ArrayList<>();
        private List<DataOverview> headerData;

        public MAdapter(List<DataOverview> headerData) {
            this.headerData = headerData;
            viewPagerList = initView();
        }

        public List<View> initView() {
            List<View> list = new ArrayList<View>();
            if (headerData != null && headerData.size() > 0) {
                viewPagerDot.removeAllViews();
                for (int i = 0; i < headerData.size(); i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.view_pager_item, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.view_pager_item_img);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    Glide.with(context).load(headerData.get(i).getImgUri()).placeholder(R.drawable.aam).error(R.drawable.ddd).into(imageView);
                    ImageLoader.getInstance().displayImage(headerData.get(i).getImgUri(), imageView, options);
                    imageView.setTag(headerData.get(i));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataOverview d = (DataOverview) v.getTag();
                            Intent intent = new Intent(context, Activity_2.class);
                            intent.putExtra("imgId",d.getImgUri());
                            intent.putExtra("id",d.getId());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                                        getActivity(), v, "share"
                                ).toBundle());
                            } else {
                                startActivity(intent);
                            }
                        }
                    });
                    list.add(view);
                    ImageView dot = new ImageView(context);
                    dot.setImageResource(R.drawable.unselect);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Mutil.dp2px(10), Mutil.dp2px(10));
                    params.gravity = Gravity.CENTER;
                    params.setMargins(Mutil.dp2px(2),0,0, 0);
                    if (i == 0) {
                        params.setMargins(Mutil.dp2px(290),0,0,0);
                        imgDescribe.setText(headerData.get(0).getTitle());
                    }

                    viewPagerDot.addView(dot, params);
                }
            }
            return list;
        }

        @Override
        public int getCount() {
            return viewPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewPagerList.get(position));
            return viewPagerList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewPagerList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "" + position;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.view_pager_item_img:
//                Mutil.showToast("click img");
//                int resId = (int) v.getTag();
//                Intent intent = new Intent(context, Activity_2.class);
//                intent.putExtra("resId", resId);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
//                            getActivity(), v, "share"
//                    ).toBundle());
//                } else {
//                    startActivity(intent);
//                }
//                break;
            case R.id.fab:
                createBottomSheet();
        }
    }
}
