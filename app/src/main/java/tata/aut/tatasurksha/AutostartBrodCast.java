package tata.aut.tatasurksha;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;


public class AutostartBrodCast extends BroadcastReceiver {
Context mcontext;
   // private static final long REPEAT_TIME = 5*1000;

   private static final long REPEAT_Send_TIME_BleScanner = 1*60*60*1000;

    private static final long REPEAT_Send_TIME_Data = 1*60*60*1000;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // throw new UnsupportedOperationException("Not yet implemented");
        //SqDB sql=new SqDB(context);
       // sql.deleteSmsall();
        //List<l_t_sms> lsms= sql.getsms();
        //Toast.makeText(context,"Broadcast is calling",Toast.LENGTH_LONG).show();
        mcontext=context;
//        try {
//            BleScanner();
//            Log.d("bleAuto","Scanner");
//        }catch (Exception ex){
//
//           // Log.d("b")
//        }
//        try {
//
//
//            SendBeacon();
//            Log.d("bleAuto","SendMessage");
//        }catch (Exception ex){}
//        Log.d("bleAuto","Start");
        BleScanner();
        SendBeacon();
      // SendBeaconService();
       //BleScannerService();
        //callContactService();
       // SmsSenderService();
    }



    void BleScanner()
    {
        Log.d("bleAuto","In Ble Scanner");
        final Intent intent = new Intent(mcontext, BleScannerServices.class);
        mcontext.startForegroundService(intent);
        //this.mcontext.startService(intent);
       // this.getApplication().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }


    void SendBeacon()
    {
        Log.d("bleAuto","In Send Beacon");
        final Intent intent = new Intent(mcontext, SendBeacon.class);
        mcontext.startForegroundService(intent);
       // this.mcontext.startService(intent);

    }

    void SendBeaconService()
    {
        AlarmManager service = (AlarmManager) mcontext
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(mcontext,SendBeacon.class);
        PendingIntent pending = PendingIntent.getService(mcontext, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        service.cancel(pending);
        service.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+REPEAT_Send_TIME_Data , REPEAT_Send_TIME_Data, pending);
    }


    void BleScannerService()
    {
        AlarmManager service = (AlarmManager) mcontext
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(mcontext,BleScannerServices.class);
        PendingIntent pending = PendingIntent.getService(mcontext, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        service.cancel(pending);
        service.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+REPEAT_Send_TIME_BleScanner , REPEAT_Send_TIME_BleScanner, pending);
    }

// Call GPS Location Service


}
