package tata.aut.tatasurksha;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_IGNORE_BATTERY_OPTIMIZATIONS=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);


        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    checkPermission();
                }
            });
            builder.show();
        }
        else{
            //--we already have permission!
            //canStartScanning=true;

            HandlerThread handlerThread = new HandlerThread("HandlerThreadName");
            handlerThread.start();
             Looper looper = handlerThread.getLooper();
             Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Do something here!
                                try {
                    Thread.sleep(5000);
                    Intent i=new Intent(getBaseContext(),homescreen.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        }






    }


    private void checkPermission(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                    HandlerThread handlerThread = new HandlerThread("HandlerThreadName");
                    handlerThread.start();
                    Looper looper = handlerThread.getLooper();
                    Handler handler = new Handler(looper);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Do something here!
                            try {
                                Thread.sleep(5000);
                                Intent i=new Intent(getBaseContext(),homescreen.class);
                                startActivity(i);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            checkPermission();
                        }

                    });
                    builder.show();
                }
                return;
            }

        }
    }
}
