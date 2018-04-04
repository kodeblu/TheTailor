package com.bencorp.thetailor;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    EditText search;
    SqliteHandler myDB;
    RecyclerView recycleView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    OnTapListener onTapListener;
    ArrayList<JobPending> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_search_activity);
        recycleView = (RecyclerView) findViewById(R.id.search_content);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        set();
        initDb();
        //searchView.setHi
    }
    public void initDb(){
        myDB = new SqliteHandler(getApplicationContext());
    }
    public void set (){
        search = (EditText) findViewById(R.id.search_job);
        search.setHint("Search by name");
        search.setHintTextColor(Color.WHITE);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                findJob(s.toString());
            }
        });

    }
    public void findJob(String keyword){

        arrayList = new ArrayList<>();
        Cursor foundJob  = myDB.getJobmatch(keyword);
        //Toast.makeText(SearchActivity.this,Integer.toString(foundJob.getCount()),Toast.LENGTH_SHORT).show();
        if(keyword.isEmpty()){
            arrayList.clear();
            adapter =  new EmptyAdapter(getApplicationContext());
            recycleView.setAdapter(adapter);
            return;
        }
        if(foundJob.getCount() > 0) {

            while (foundJob.moveToNext()) {
                JobPending jobPending = new JobPending(foundJob.getString(1), foundJob.getInt(2),
                        foundJob.getString(4), foundJob.getString(3), foundJob.getInt(0), foundJob.getBlob(5));

                arrayList.add(jobPending);
            }
            adapter = new SearchRecyclerAdapter(arrayList, getApplicationContext());

            //adapter.setOnTapListener(this);
            recycleView.setAdapter(adapter);
        }else{
            arrayList.clear();

            adapter =  new EmptyAdapter(getApplicationContext());
            recycleView.setAdapter(adapter);

        }
    }
}
