package tata.aut.tatasurksha;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


import tata.aut.tatasurksha.UTILS.SendToServer;
import tata.aut.tatasurksha.UTILS.SqDB;
import tata.aut.tatasurksha.UTILS.Utils;
import tata.aut.tatasurksha.UTILS.beaconBean;

public class SendBeacon  extends Service {

    static int i=0;
    int id=0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //this.log.info("Sms Services Is Created");

    }

    private Notification getNotification() {

        NotificationChannel channel = new NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);


        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "channel_01").setAutoCancel(true);
        return builder.build();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(12345678, getNotification());
        handleIntent(intent);
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    private PowerManager.WakeLock mWakeLock;
    TelephonyManager tMgr;
    String Imie;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    private void handleIntent(Intent intent) {
        try {


//            HandlerThread handlerThread = new HandlerThread("HandlerThreadBeacon");
//            handlerThread.start();
//            Looper looper = handlerThread.getLooper();
//            Handler handler = new Handler(looper);
//            handler.post(new Runnable() {
//                @Override

            Thread th=new Thread(){
                public void run() {
                    // Do something here!
                    try {
                         while(true) {

try {
    //id = id + 1;
    //startForeground(id, getNotification());
    // Toast.makeText(getBaseContext(),"Called",Toast.LENGTH_LONG).show();
    SqDB sql = new SqDB(SendBeacon.this.getBaseContext());

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    String loggedInUserID = sp.getString("KEY_USER", null);
    if (loggedInUserID == null) {
        loggedInUserID = "";
    }
    // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    //String loggedInUserID = sp.getString("KEY_USER", null);
    ArrayList<beacon> lbeacon = sql.getbeacon();
    Log.d("SendBeacon", lbeacon.size() + " " + i);
    //Toast.makeText(getBaseContext(),lbeacon.size()+"",Toast.LENGTH_LONG).show();
    if (lbeacon.size() > 0) {
        i = 0;
    } else {
        if (i != 6)
            i = i + 1;

    }
    if (i <= 5)
    //if(lbeacon.size()>0)
    {
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        SendPacket packet = new SendPacket();
        packet.battery = batLevel;
        packet.messageType = "SCHEDULED";
        packet.trackerId = loggedInUserID;
        if (sql.getbeacon().size() > 0) {
            beacon be = Utils.findMaxBeacon(lbeacon);
            beaconBean bean = new beaconBean();
            bean.beaconId = be.beaconId;
            bean.timestamp = be.timestamp;
            packet.scheduled.add(bean);
        } else {
            beaconBean bean = new beaconBean();
            bean.beaconId = "0";
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            ft.setTimeZone(TimeZone.getTimeZone("IST"));
            bean.timestamp = ft.format(dNow);
            packet.scheduled.add(bean);
        }
        Gson gson = new Gson();

        String message = gson.toJson(packet).toString();
        Log.d("SendBeacon", message);
        //Toast.makeText(getBaseContext(),message,Toast.LENGTH_LONG).show();
        SendToServer objsendtosserver = new SendToServer(getBaseContext());
        objsendtosserver.send(message);
    }
}catch (Exception ex){}
                        Log.d("SendBeacon", "STARTED ");
                         Thread.sleep(1000*60*1);
                         }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

th.start();














        }catch (Exception ex){
            //Toast.makeText(SendBeacon.this,ex.toString(), Toast.LENGTH_LONG).show();

        }

    }
}