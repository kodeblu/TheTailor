package com.bencorp.thetailor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp-pc on 4/1/2018.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    ArrayList<JobPending> arrayList = new ArrayList<>();
    private  OnTapListener onTapListener;
    Context context;

    SearchRecyclerAdapter(ArrayList<JobPending> arrayList,Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_layout,parent,false);
        RecyclerAdapter.RecyclerViewHolder recyclerViewHolder = new RecyclerAdapter.RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, final int position) {
        final JobPending jobs = arrayList.get(position);
        holder.name.setText(jobs.getName());
        holder.date.setText(jobs.getDate());

        byte[] styleImage = jobs.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(styleImage,0,styleImage.length);
        holder.img.setImageBitmap(bitmap);
        long id = jobs.getId();

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(new Intent(v.getContext(),DisplayActivity.class));
                intent.putExtra("id",jobs.getId());
                v.getContext().startActivity(intent);
                //context.startActivity(new Intent(context.getApplicationContext(),DisplayActivity.class));
            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void setOnTapListener(OnTapListener onTapListener){
        this.onTapListener = onTapListener;
    }

    public  static  class RecyclerViewHolder extends  RecyclerView.ViewHolder{
        TextView name,price,measurement,date;
        ImageView img;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            img = (ImageView) itemView.findViewById(R.id.cardPic);
        }

    }

}
