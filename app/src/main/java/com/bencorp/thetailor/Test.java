package com.bencorp.thetailor;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
//import java.util.jar.Manifest;
import android.Manifest;

import com.bumptech.glide.Glide;

public class Test extends AppCompatActivity {
    Button btn,open;
    LinearLayout job;
    SqliteHandler myDB;
    ProgressDialog progressDialog;
    int[] viewIds = new int[]{R.id.customer_name,R.id.job_price,R.id.customer_measurement};
    // UI references.
    EditText name;
    EditText price;
    EditText measuremnts;
    ImageView style;
    final int REQUEST_CODE_GALLERY = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    byte[] newStyle = imageViewToByte(style);
                    insertData(name.getText().toString().trim(),
                            Integer.parseInt(price.getText().toString().trim()),
                            measuremnts.getText().toString().trim(),view,newStyle);
                }
                //Toast.makeText(getApplicationContext(),"btn clicked",Toast.LENGTH_SHORT).show();
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Test.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
                //Toast.makeText(getApplicationContext(),"hhhhh",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void init(){
        btn = (Button) findViewById(R.id.save_btn);
        style = (ImageView) findViewById(R.id.style);

        open = (Button) findViewById(R.id.open);
        name = (EditText)findViewById(R.id.customer_name);
        price = (EditText)findViewById(R.id.job_price);
        measuremnts = (EditText)findViewById(R.id.customer_measurement);
        myDB = new SqliteHandler(getApplicationContext());
        //job = (LinearLayout) findViewById(R.id.customer_form);
    }
    public Boolean validate(){
        View focusView = null;
        Boolean allow = true;
        for(int i = 0; i < viewIds.length;i++){
            EditText formInput = (EditText)findViewById(viewIds[i]);
            if(formInput.getText().toString().trim().isEmpty()){
                formInput.setError(getString(R.string.error_field_required));
                focusView = formInput;
                allow = false;
                break;
            }

        }
        return allow;
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
            //Glide.with(this).load(uri).into(style);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                style.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    public void insertData(String name,Integer price,String measurement,View view,byte[] img){
        btn.setEnabled(false);
        loading();
        String date = new SimpleDateFormat("dd MMMM, yyyy | EEEE", Locale.getDefault()).format(new Date());

        Boolean inserting = myDB.addJob(name,price,measurement,date,img);
        if(inserting){
            progressDialog.dismiss();
            Snackbar.make(view,"Details have been saved successfully",Snackbar.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"successful creation",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setEnabled(true);
                    finish();
                }
            },2000);

        }else{
            //Snackbar snackbar = Snackbar.make(view,"S",Snackbar.LENGTH_LONG);
            Snackbar snackbar = Snackbar.make(view,"Something went wrong, please resart app and try again",Snackbar.LENGTH_LONG);
            View sub = snackbar.getView();
            TextView textView = (TextView) sub.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }

    public void loading(){

        progressDialog = new ProgressDialog(Test.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Job creation in progress");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public byte[] imageViewToByte(ImageView img){
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
