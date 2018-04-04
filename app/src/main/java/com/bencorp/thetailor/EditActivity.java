package com.bencorp.thetailor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditActivity extends AppCompatActivity {
    EditText price,measuremet;
    ImageView imageView;
    Button btn,btnChange;
    SqliteHandler myDB;
    private String jobId;
    final int REQUEST_CODE_GALLERY = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Set up the login form.

        Intent intent = getIntent();
        jobId = intent.getStringExtra("id");
        showMe(jobId);
        btn = (Button) findViewById(R.id.update_btn);
        btnChange = (Button) findViewById(R.id.changePic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    byte[] newStyle = imageViewToByte(imageView);
                    updateJob(price.getText().toString().trim(),
                            measuremet.getText().toString().trim(),
                            jobId,newStyle);
                }

            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EditActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }else{
                Toast.makeText(getApplicationContext(),"You dont have the permission",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(imageView);
            /*try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    public void showMe(String id){
        myDB = new SqliteHandler(getApplicationContext());
        Cursor job = myDB.getJob(id);
        price = (EditText) findViewById(R.id.job_price);
        measuremet = (EditText) findViewById(R.id.customer_measurement);
        imageView = (ImageView) findViewById(R.id.styleChange);

        job.moveToNext();


        price.setText(job.getString(2));
        measuremet.setText(job.getString(4));
        byte[] styleImage = job.getBlob(5);
        Bitmap bitmap = BitmapFactory.decodeByteArray(styleImage,0,styleImage.length);
        imageView.setImageBitmap(bitmap);
    }
    public byte[] imageViewToByte(ImageView img){
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    public boolean validate(){
        boolean validated = true;
        View focusView = null;
        if(price.getText().toString().trim().isEmpty()){
            validated = false;
            focusView = price;
            price.setError("Field cannot be empty");
        }else if(measuremet.getText().toString().trim().isEmpty()){
            validated = false;
            focusView = measuremet;
            measuremet.setError("Field cannot be empty");
        }
        return validated;
    }
    public void updateJob(String price,String measurements,String id, byte[] img){
        int update = myDB.updateJob(price,measurements,id,img);
        if(update > 0){
            Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Job updated successfully",Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },2000);
            //startActivity(new Intent(EditActivity.this,ListActivity.class));
        }
    }

}

