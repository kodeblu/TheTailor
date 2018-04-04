package com.bencorp.thetailor;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by hp-pc on 4/2/2018.
 */

public class EmptyAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    Context context;

    EmptyAdapter(Context context){

        this.context = context;
    }

    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout,parent,false);
        RecyclerAdapter.RecyclerViewHolder recyclerViewHolder = new RecyclerAdapter.RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, final int position) {


    }


    @Override
    public int getItemCount() {
        return 1;
    }



    public  static  class RecyclerViewHolder extends  RecyclerView.ViewHolder{

        public RecyclerViewHolder(View itemView) {
            super(itemView);

        }

    }

}
