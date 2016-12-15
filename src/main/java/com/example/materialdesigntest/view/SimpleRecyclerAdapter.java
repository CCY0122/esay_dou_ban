package com.example.materialdesigntest.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.materialdesigntest.R;
import com.example.materialdesigntest.gsonBean.DataOverview;
import com.example.materialdesigntest.util.Mlog;
import com.example.materialdesigntest.util.Mutil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.mViewHolder>{

    private Context context;
    private List<DataOverview> data;
    private DisplayImageOptions option;

    public SimpleRecyclerAdapter(Context context ,List<DataOverview> data){
        this.context = context;
        this.data = data;
        option = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).
                showImageOnFail(R.drawable.ddd).showImageOnLoading(R.drawable.loading).build();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.a2_recycler_view_item , parent,false);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(data.get(position).getImgUri(),holder.imageView,option);
        holder.title.setText(data.get(position).getTitlle()+"");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mutil.showToast(position+"");
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView title;

        public mViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.a2_item_card_view);
            imageView = (ImageView) itemView.findViewById(R.id.a2_item_image);
            title = (TextView) itemView.findViewById(R.id.a2_item_title);
        }
    }
}
