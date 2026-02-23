package tata.aut.tatasurksha;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import tata.aut.tatasurksha.ui.main.SectionsPagerAdapter;

public class homescreen extends AppCompatActivity {
    private  static final int REQUEST_IGNORE_BATTERY_OPTIMIZATIONS=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        checkPermission();
//        if (this.checkSelfPermission(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("This app needs Battery Optimization");
//            builder.setMessage("Please grant Battery Optimization.");
//            builder.setPositiveButton(android.R.string.ok, null);
//            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    checkPermission();
//                }
//            });
//            builder.show();
//        }




       //boolean b=   AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(homescreen.this);
      //  Toast.makeText(homescreen.this,b+"",Toast.LENGTH_LONG).show();

      //  boolean c=   AutoStartPermissionHelper.getInstance().getAutoStartPermission(homescreen.this);
      //  Toast.makeText(homescreen.this,"p"+c+"",Toast.LENGTH_LONG).show();

//        try
//        {
//            //Open the specific App Info page:
//            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(Uri.parse("package:" +homescreen.this.getPackageName()));
//            homescreen.this.startActivity(intent);
//        }
//        catch ( ActivityNotFoundException e )
//        {
//            //Open the generic Apps page:
//            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
//            homescreen.this.startActivity(intent);
//        }

//
//        if(Build.BRAND.equalsIgnoreCase("xiaomi") ){
//
//            Intent intent = new Intent();
//            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
//            startActivity(intent);
//
//
//        }else if(Build.BRAND.equalsIgnoreCase("Letv")){
//
//            Intent intent = new Intent();
//            intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
//            startActivity(intent);
//
//        }
//        else if(Build.BRAND.equalsIgnoreCase("Honor")){
//
//            Intent intent = new Intent();
//            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
//            startActivity(intent);
//
//        }

        //FloatingActionButton fab = findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    private void checkPermission(){


            Intent intent=new Intent();
            String packageName=homescreen.this.getPackageName();
        PowerManager pm= (PowerManager) homescreen.this.getSystemService(Context.POWER_SERVICE);
        if(pm.isIgnoringBatteryOptimizations(packageName))
        {
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        }
        else
        {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+packageName));
        }
        homescreen.this.startActivity(intent);

    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IGNORE_BATTERY_OPTIMIZATIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since Battery Optimization access has not been granted, this app will not be able to discover beacons when in the background.");
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