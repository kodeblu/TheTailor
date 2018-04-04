package com.bencorp.thetailor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by hp-pc on 3/19/2018.
 */

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    ArrayList<JobPending> arrayList = new ArrayList<>();
    private  OnTapListener onTapListener;
    Context context;

    RecyclerAdapter(ArrayList<JobPending> arrayList,Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final JobPending jobs = arrayList.get(position);
        holder.name.setText(jobs.getName());
        holder.date.setText(jobs.getDate());

        byte[] styleImage = jobs.getImg();
        //Glide.with(context).load(styleImage).into(holder.img);
        Bitmap bitmap = BitmapFactory.decodeByteArray(styleImage,0,styleImage.length);
        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);*/
        holder.img.setImageBitmap(bitmap);
        //Glide.with(context).load(Uri.parse(path)).into(holder.img);
        long id = jobs.getId();

        holder.itemView.setTag(id);
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
