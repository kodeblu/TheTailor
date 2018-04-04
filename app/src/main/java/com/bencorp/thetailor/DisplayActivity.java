package com.bencorp.thetailor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity {
    SqliteHandler myDB;
    TextView name,measurement,date,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        dispalyJob();


    }

    private void init(){
        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
        measurement = (TextView) findViewById(R.id.measurement);
        price = (TextView) findViewById(R.id.price);
    }

    private  void dispalyJob(){
        Intent intent = getIntent();
        int jobId = intent.getExtras().getInt("id");
        myDB = new SqliteHandler(this);
        Cursor job = myDB.getJob(Integer.toString(jobId));
        job.moveToNext();
        name.setText("Customer's Name: "+job.getString(1));
        price.setText("Price: "+job.getString(2));
        measurement.setText(job.getString(4));
        date.setText("Created: "+job.getString(3));
    }
}
