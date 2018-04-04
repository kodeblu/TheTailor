package com.bencorp.thetailor;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements OnTapListener{
    SqliteHandler myDB;
    RecyclerView recycleView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    OnTapListener onTapListener;
    ArrayList<JobPending> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycleView = (RecyclerView) findViewById(R.id.job_content);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);


        initDb();
        viewRecentJob();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ListActivity.this,"okay",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ListActivity.this,Test.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }
    public void initDb(){
        myDB = new SqliteHandler(getApplicationContext());
    }
    @Override
    public void onRestart(){
        super.onRestart();

        viewRecentJob();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_search){
            //Toast.makeText(ListActivity.this,"hello",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ListActivity.this,SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewRecentJob(){

        arrayList = new ArrayList<>();
        Cursor data = myDB.getJobs();
        if(data.getCount() > 0){
            //StringBuffer buffer = new StringBuffer();
            //GridLayout grid = (GridLayout) findViewById(R.id.my_card);
            while(data.moveToNext()){
                JobPending jobPending = new JobPending(data.getString(1),data.getInt(2),
                        data.getString(4),data.getString(3),data.getInt(0),data.getBlob(5));

                arrayList.add(jobPending);
            }
            adapter = (RecyclerAdapter) new RecyclerAdapter(arrayList,getApplicationContext());

            //adapter.setOnTapListener(this);
            recycleView.setAdapter(adapter);

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    confirmDialog((long) viewHolder.itemView.getTag());
                }
            }).attachToRecyclerView(recycleView);
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    Intent intent = new Intent(ListActivity.this,EditActivity.class);
                    intent.putExtra("id",viewHolder.itemView.getTag().toString());
                    startActivity(intent);
                }
            }).attachToRecyclerView(recycleView);

        }else{
            //Toast.makeText(getApplicationContext(),"notin 2 see",Toast.LENGTH_LONG).show();
            //Snackbar.make(g,"Details have been saved successfully",Snackbar.LENGTH_LONG).show();
            //displayData("None");
        }

    }
    public void removeJob(long id){
        int delete = myDB.deleteJob(Long.toString(id));
        if(delete > 0){

            Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Job deleted successfully",Snackbar.LENGTH_LONG).show();
            viewRecentJob();

        }
    }
    public void confirmDialog(long id){
        final long identity = id;
        new AlertDialog.Builder(ListActivity.this)
        .setCancelable(true)
        .setTitle("Delete Job")
        .setMessage("Are you sure you want to delete this job")
        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeJob(identity);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewRecentJob();
            }
        }).show();

    }
    @Override
    public void onTapView(int position) {

    }
}
