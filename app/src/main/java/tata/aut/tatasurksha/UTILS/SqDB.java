package tata.aut.tatasurksha.UTILS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import tata.aut.tatasurksha.beacon;


/**
 * Created by Administrator on 7/13/2016.
 */
public class SqDB extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "tatablefine.db";
    public static final int Version=2;


    public SqDB(Context context)
    {
        super(context, DATABASE_NAME , null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //this is used for sms
        db.execSQL(
                "create table beacon " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT ,beaconId text, timestamp text,rssi INTEGER)"
        );







    }

    public void setBeacon(beacon bcon)


    {



        String query="Insert into beacon (beaconId,timestamp,rssi) values ('"+bcon.beaconId+"','"+bcon.timestamp+"',"+bcon.rssi+")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();

        // myDB.execSQL("INSERT INTO "


    }






    public void deletebeacon(int id )
    {
        String query="delete from  beacon where id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();

        // myDB.execSQL("INSERT INTO "


    }


    public void deletebeaconall( )
    {
        String query="delete from  beacon";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();

        // myDB.execSQL("INSERT INTO "


    }


    public ArrayList<beacon> getbeacon()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<beacon> array_list = new ArrayList<beacon>();
        Cursor res = db.rawQuery( "select * from beacon", null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            beacon bea=new beacon();
           // bea.beaconId=res.getInt(res.getColumnIndex("id"));
            bea.beaconId=res.getString(res.getColumnIndex("beaconId")) ;
            bea.timestamp=res.getString(res.getColumnIndex("timestamp")) ;;
            bea.rssi=res.getInt(res.getColumnIndex("rssi"));
            array_list.add(bea);


        res.moveToNext();
    }
    db.close();
        return array_list;

    }

    public ArrayList<beacon> getMaxbeacon() {
    SQLiteDatabase db = this.getReadableDatabase();
    ArrayList<beacon> array_list = new ArrayList<beacon>();
    Cursor res = db.rawQuery("select * from beacon where rssi in (select max(rssi) from beacon) ", null);
    res.moveToFirst();
    while (res.isAfterLast() == false) {
        beacon bea = new beacon();
        // bea.beaconId=res.getInt(res.getColumnIndex("id"));
        bea.beaconId = res.getString(res.getColumnIndex("beaconId"));
        bea.timestamp = res.getString(res.getColumnIndex("timestamp"));
        bea.rssi=res.getInt(res.getColumnIndex("rssi"));

        array_list.add(bea);


        res.moveToNext();
    }
        db.close();
        return array_list;
}





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
               // if(newVersion>oldVersion)
              // {
                    db.execSQL("DROP TABLE IF EXISTS beacon");


                    onCreate(db);
                //}

    }



    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if(newVersion<oldVersion)
       // {
            db.execSQL("DROP TABLE IF EXISTS beacon");

            onCreate(db);
        //}
    }








}
