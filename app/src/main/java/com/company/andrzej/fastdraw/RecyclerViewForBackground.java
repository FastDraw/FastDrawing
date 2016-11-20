package com.company.andrzej.fastdraw;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

class RecyclerViewForBackground extends RecyclerView.Adapter<RecyclerViewForBackground
        .View_Holder> {

    private BackgroundSelectFragment bacground;
    private String[] web;
    private Integer[] img;
    private Context context;

    RecyclerViewForBackground(BackgroundSelectFragment bacground, String[] web,
                              Integer[] img) {
        this.bacground = bacground;
        this.web = web;
        this.img = img;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .list_single, parent, false);
        context = parent.getContext();
        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        holder.txt.setText(web[position]);
        holder.img.setImageResource(img[position]);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).changeBackground(position);
                ((MainActivity) context).hideFragmentBackground();
            }
        });
        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).changeBackground(position);
                ((MainActivity) context).hideFragmentBackground();
            }
        });
    }

    @Override
    public int getItemCount() {
        return web.length;
    }

    class View_Holder extends RecyclerView.ViewHolder {
        private TextView txt;
        private ImageView img;

        View_Holder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
