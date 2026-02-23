package tata.aut.tatasurksha;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import tata.aut.tatasurksha.UTILS.SendToServer;
import tata.aut.tatasurksha.UTILS.SqDB;
import tata.aut.tatasurksha.UTILS.Utils;

public class BleScannerServices extends Service {

    private static final String TAG = "BGBService";
    public static int  id=1;
    BluetoothLeScanner btScanner;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    private String loggedInUserID;
    private int rssi = -120;
    String address = "";
    Response response;
    private boolean isScanning;
    String CHANNEL_ID="TATASUKHSHA";
    private static PowerManager.WakeLock wakeLock;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BleScanner", "OnCreate ");
        //this.log.info("Sms Services Is Created");


    }
   // @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BleScanner", "OnStart ");
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//
//        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
//                PowerManager.ACQUIRE_CAUSES_WAKEUP |
//                PowerManager.ON_AFTER_RELEASE,"tata:asdf");
//        wakeLock.acquire();
//

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock =pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"BleScannerServices::lock");
        wakeLock.acquire();


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
   // NotificationChannel channel = new NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);


    //@RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification() {

        NotificationChannel channel = new NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);


        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "channel_01").setAutoCancel(true);
        return builder.build();
    }



    public void startScanning() {
        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);

        btAdapter = btManager.getAdapter();
        //btAdapter.disable();
        //btAdapter.enable();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loggedInUserID = sp.getString("KEY_USER", null);
        //if (loggedInUserID != null) {
if (btAdapter.isEnabled()==false)
{
    btAdapter.enable();
}
            if (btAdapter != null && btAdapter.isEnabled()) {
                btScanner = btAdapter.getBluetoothLeScanner();
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            ScanSettings.Builder builderScanSettings = new ScanSettings.Builder();
                            builderScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
                            builderScanSettings.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);

                            builderScanSettings.setReportDelay(0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                builderScanSettings.setMatchMode(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                builderScanSettings.setLegacy(false);
                                builderScanSettings.setPhy(BluetoothDevice.PHY_LE_1M);
                            }

                            ScanFilter.Builder builder = new ScanFilter.Builder();

                            Vector<ScanFilter> filter = new Vector<ScanFilter>();
                            filter.add(builder.build());


                           // List<ScanFilter> filters = new ArrayList<>();
                            btScanner.startScan(filter,builderScanSettings.build(),leScanCallback);
                            //filters.add(new ScanFilter.Builder().setServiceUuid(mUuid).build());
                           // btScanner.startScan(leScanCallback);
                            isScanning = true;
                            Log.d(TAG, "STARTED SCANNING");
                        }
                    });
                }
            }
            else
                {

            }

       // }

    }


    public void stopScanning() {

        if (btScanner != null) {
            try {
                btScanner.stopScan(leScanCallback);
                isScanning=false;
            } catch (Exception ex) {
                // we tried to be good..but still exception... :/
                isScanning=true; //--/???
            }
        }

    }



    private ScanCallback leScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {


            try{
            ScanRecord scanRecord = result.getScanRecord();
            byte[] scanData;
            scanData = scanRecord.getBytes();
           if (scanRecord != null && ((scanData = scanRecord.getBytes())[0] & 255) == 2 && (scanData[1] & 255) == 1 && (scanData[2] & 255) == 6 && (scanData[3] & 255) == 20 && (scanData[4] & 255) == 255 && (scanData[5] & 255) == 84 && (scanData[6] & 255) == 67 && (scanData[7] & 255) == 76 && (result.getRssi() > -120)) {
                BleScannerServices.this.rssi = result.getRssi();
                BleScannerServices.this.address = result.getDevice().getAddress();
                //String address = result.getDevice().getAddress();
                address = address.replace(":", "");
                byte[] mBytes = Utils.hexStringToByteArray(address);
                byte[] mPacket = new byte[25];
                int position = 0;
                mPacket[position++] = mBytes[0];
                mPacket[position++] = scanData[5];
                mPacket[position++] = scanData[6];
                mPacket[position++] = scanData[7];
                mPacket[position++] = mBytes[1];
                mPacket[position++] = scanData[8];
                mPacket[position++] = mBytes[2];
                mPacket[position++] = scanData[9];
                mPacket[position++] = mBytes[3];
                mPacket[position++] = scanData[10];
                mPacket[position++] = scanData[11];
                mPacket[position++] = scanData[12];
                mPacket[position++] = scanData[13];
                mPacket[position++] = scanData[14];
                mPacket[position++] = scanData[15];
                mPacket[position++] = mBytes[4];
                mPacket[position++] = scanData[16];
                mPacket[position++] = scanData[17];
                mPacket[position++] = scanData[18];
                mPacket[position++] = scanData[19];
                mPacket[position++] = scanData[20];
                mPacket[position++] = scanData[21];
                mPacket[position++] = scanData[22];
                mPacket[position++] = scanData[23];
                mPacket[position++] = mBytes[5];

                BleScannerServices.this.response = new Response(true, 5, mPacket);


                String msg = "PayLoad=";
                if (BleScannerServices.this.response != null) {
                    msg = Utils.ByteArrayTohex(response.getPacket());
                }


                BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
                int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);


                //TelephonyManager tMgr = (TelephonyManager)MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
                //String mPhoneNumber = tMgr.getLine1Number();

                // peripheralTextView.append("\nAddress:"+result.getDevice().getAddress()+"Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() +" Battery:"+batLevel+"Payload:"+msg);

                // auto scroll for text view
                // final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
                // if there is no need to scroll, scrollAmount will be <=0
                //if (scrollAmount > 0)
                //   peripheralTextView.scrollTo(0, scrollAmount);


                SendPacket packet = new SendPacket();
                packet.battery = batLevel;
                packet.messageType = "SCHEDULED";

                packet.trackerId = loggedInUserID;
                beacon ble = new beacon();
                ble.beaconId = msg;
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                ft.setTimeZone(TimeZone.getTimeZone("IST"));
                ble.timestamp = ft.format(dNow);
                packet.scheduled.add(ble);
                ble.rssi=result.getRssi();
                SqDB sql=new SqDB(getBaseContext());
                sql.setBeacon(ble);
                // Gson gson = new Gson();

                //String message = gson.toJson(packet).toString();
                //SendToServer objsendtosserver = new SendToServer();
                // objsendtosserver.send(message);
            }

}
catch (Exception ex){
                Log.d(TAG,ex.toString());


}
        }
    };


    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void handleIntent(Intent intent)
    {
        //HandlerThread handlerThread = new HandlerThread("call to server");
        //handlerThread.start();
        //Looper looper = handlerThread.getLooper();
        //Handler handler = new Handler(looper);
       // handler.post(new Runnable() {
        Thread th=new Thread(){
            @Override
            public void run() {
                // Do something here!
                try {
                    while(true) {
                        try {
                            Log.d("BleScanner", "OnLoop ");
                           // id = id + 1;
                            //startForeground(id, getNotification());
//                            try {
//                                stopScanning();
//                                Log.d("BleScanner Stop", "STARTED ");
//                            } catch (Exception ex) {
//                                Log.d("BleScanner Stop err", "STARTED ");
//                            }
                            try {
                                startScanning();
                                Log.d(TAG, "START");
                            } catch (Exception ex) {
                                Log.d(TAG, "STOP");

                            }
                            try {
                                Thread.sleep(1000 * 10);
                            }catch (InterruptedException ex)
                            {
                               Log.d(TAG,"Interupted Exception");
                            }
                            try {
                                stopScanning();
                                Log.d(TAG, "Stoped ");
                            } catch (Exception ex) {
                                Log.d(TAG, "Stoped Error ");

                            }


                            Log.d("BleScanner Recalled", "STARTED ");
                        }catch (Exception ex){}
                        Thread.sleep(1000*60);
                    }

                } catch (Exception e) {
                    Log.d("BleScanner", "Error ");
                }
            }
        };

        th.start();


    }
}