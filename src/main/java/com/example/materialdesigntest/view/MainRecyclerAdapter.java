package com.example.materialdesigntest.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.materialdesigntest.R;
import com.example.materialdesigntest.gsonBean.DataOverview;
import com.example.materialdesigntest.util.Mutil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.mViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;

    private Context context;
    private List<DataOverview> data;
    private View headerView;
    private DisplayImageOptions options;
    private Activity activity;
    private View footerView;

    private onFooterClick footClick;
    public interface onFooterClick{
        void footClick(View v , int pos);
    }
    public void setOnFooterClick(onFooterClick oc){
        this.footClick = oc;
    }


    public MainRecyclerAdapter(Context context, List<DataOverview> data) {
        this.context = context;
        this.data = data;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).showImageOnFail(R.drawable.ddd).showImageOnLoading(R.drawable.loading).build();
    }
    public MainRecyclerAdapter(Context context, List<DataOverview> data, Activity activity) {
        this(context,data);
        this.activity = activity;
    }

    public void setHeaderView(View v) {
        this.headerView = v;
        notifyDataSetChanged();
    }
//    public void setFooterView(View v){
//        this.footerView = v;
//        notifyDataSetChanged();
//    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            footerView = LayoutInflater.from(context).inflate(R.layout.footer_view,parent,false);
            return new mViewHolder(headerView);
        }
        if(viewType == TYPE_FOOTER){
            return  new mViewHolder(footerView);
        }

            View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);
            return new mViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView == null && footerView == null){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else if(getItemViewType(position) == TYPE_NORMAL){
            final int pos = holder.getAdapterPosition() -1;
            holder.title.setText(data.get(pos).getTitle()+"");
            holder.content.setText(data.get(pos).getContent()+"");
//            Glide.with(context).load(data.get(pos).getImgUri()).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                    placeholder(R.drawable.aah).error(R.drawable.ddd).into(holder.imageView);
            ImageLoader.getInstance().displayImage(data.get(pos).getImgUri(),holder.imageView,options);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgId = data.get(pos).getImgUri();
                    String id = data.get(pos).getId();
                    Intent intent = new Intent(context, Activity_2.class);
                    intent.putExtra("imgId", imgId);
                    intent.putExtra("id",id);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                                activity,holder.imageView, "share"
                        ).toBundle());
                    } else {
                        context.startActivity(intent);
                    }
                }
            });
        }else{
            holder.footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(footClick != null){
                        footClick.footClick(v,position-2);
                    }
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(mViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.getItemViewType() == 1) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params != null
                    && params instanceof StaggeredGridLayoutManager.LayoutParams
                    && holder.getLayoutPosition() == 0) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) params;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(headerView == null && footerView == null){
            return data.size();
        }else if(headerView == null && footerView != null){
            return data.size() + 1;
        }else if (headerView != null && footerView == null){
            return data.size() + 1;
        }else {
            return data.size() + 2;
        }
    }


    class mViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView title;
        TextView content;

        FrameLayout headerView;
        CardView footerView;


        public mViewHolder(View itemView) {
            super(itemView);

            headerView = (FrameLayout) itemView.findViewById(R.id.linear);

            footerView = (CardView) itemView.findViewById(R.id.footer_view);

            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            title = (TextView) itemView.findViewById(R.id.item_title);
            content = (TextView) itemView.findViewById(R.id.item_content);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }

    }
}
