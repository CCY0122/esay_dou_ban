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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.mViewHolder> {

    private Context context;
    private List<DataOverview> data;
    private View headerView;
    DisplayImageOptions options;
    private Activity activity;


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

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new mViewHolder(headerView);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);
            return new mViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && headerView != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            return;
        } else {
            holder.title.setText(data.get(position-1).getTitlle()+"");
            holder.content.setText(data.get(position-1).getContent()+"");
//            Glide.with(context).load(data.get(position-1).getImgUri()).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                    placeholder(R.drawable.aah).error(R.drawable.ddd).into(holder.imageView);
            ImageLoader.getInstance().displayImage(data.get(position-1).getImgUri(),holder.imageView,options);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgId = data.get(position-1).getImgUri();
                    String id = data.get(position-1).getId();
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
        if (headerView != null) {
            return data.size() + 1;
        }
        return data.size();
    }


    class mViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView title;
        TextView content;

        FrameLayout headerView;


        public mViewHolder(View itemView) {
            super(itemView);

            headerView = (FrameLayout) itemView.findViewById(R.id.linear);

            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            title = (TextView) itemView.findViewById(R.id.item_title);
            content = (TextView) itemView.findViewById(R.id.item_content);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }

    }
}
