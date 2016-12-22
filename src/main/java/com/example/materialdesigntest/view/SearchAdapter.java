package com.example.materialdesigntest.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.materialdesigntest.R;
import com.example.materialdesigntest.gsonBean.DataOverview;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<DataOverview> data;
    private Activity activity;
    private DisplayImageOptions options;
    private onClickListener listener;
    public interface onClickListener{
        Intent getIntent();
    }
    public void setListener(onClickListener li){
        this.listener = li;
    }


    public SearchAdapter( Context context,List<DataOverview> data , Activity activity){
        this.context = context;
        this.data =data;
        this.activity = activity;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading).cacheInMemory(true).cacheOnDisk(true).build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_recycler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        ImageLoader.getInstance().displayImage(data.get(pos).getImgUri(),holder.imageView,options);
        holder.title.setText(data.get(pos).getTitle()+"");
        holder.content.setText(data.get(pos).getContent());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgId = data.get(pos).getImgUri();
                String id = data.get(pos).getId();
//                Intent intent = new Intent(context, Activity_2.class);
                if(listener != null) {
                    Intent intent = listener.getIntent();
                    intent.putExtra("imgId", imgId);
                    intent.putExtra("id", id);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                                activity, holder.imageView, "share"
                        ).toBundle());
                    } else {
                        context.startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private CardView card;
        private ImageView imageView;
        private TextView title;
        private TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.search_item_card);
            imageView = (ImageView) itemView.findViewById(R.id.search_item_img);
            title = (TextView) itemView.findViewById(R.id.search_item_title);
            content = (TextView) itemView.findViewById(R.id.search_item_content);
        }
    }
}
