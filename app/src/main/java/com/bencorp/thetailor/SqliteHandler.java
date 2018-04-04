package com.bencorp.thetailor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp-pc on 3/18/2018.
 */

public class SqliteHandler extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "seamstress.db";
    private static final  String TABLE = "jobs";
    private static final String COLUMN_CUSTOMER_ID= "customer_id";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_STYLE = "style";
    public static final String COLUMN_CUSTOMER_MEASUREMENTS= "customer_measurement";
    public static final String COLUMN_DATE = "date";
    private String CREATE_JOB_TABLE ="CREATE TABLE "+TABLE+" (" +
            COLUMN_CUSTOMER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_CUSTOMER_NAME+" TEXT,"+
            COLUMN_PRICE+" INTEGER,"+
            COLUMN_DATE+" TEXT,"+
            COLUMN_CUSTOMER_MEASUREMENTS+" TEXT,"+
            COLUMN_STYLE+" BLOB )";
    private String DROP_JOB_TABLE = "DROP TABLE IF EXISTS "+TABLE;

    public SqliteHandler(Context context){
        super(context,DATABASE_NAME,null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_JOB_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_JOB_TABLE);
        onCreate(db);
    }

    public Boolean addJob(String name,Integer price,String measurements,String date,byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_NAME,name);
        values.put(COLUMN_PRICE,price);
        values.put(COLUMN_CUSTOMER_MEASUREMENTS,measurements);
        values.put(COLUMN_DATE,date);
        values.put(COLUMN_STYLE,img);
        long response = db.insert(TABLE,null,values);
        if(response == -1){
            return  false;
        }
        db.close();
        return true;

    }
    public Cursor getJobs(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE+" ORDER BY "+COLUMN_CUSTOMER_ID+" DESC",null);
        return result;
    }
    public Integer deleteJob(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int remove = db.delete(TABLE,COLUMN_CUSTOMER_ID+" = ?",new String[]{id});
        return  remove;
    }

    public Cursor getJob(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE+" WHERE "+COLUMN_CUSTOMER_ID+" = ?",new String[]{id});
        return result;
    }
    public Cursor getJobmatch(String keyword){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE+" WHERE "+COLUMN_CUSTOMER_NAME+" LIKE ?",new String[]{"%"+keyword+"%"});
        return result;
    }

     public Integer updateJob(String price,String measurements,String id, byte[] img){
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(COLUMN_PRICE,price);
         values.put(COLUMN_CUSTOMER_MEASUREMENTS,measurements);
         values.put(COLUMN_STYLE,img);
         return  db.update(TABLE,values,COLUMN_CUSTOMER_ID + " = ?",new String[]{id});
     }
}

