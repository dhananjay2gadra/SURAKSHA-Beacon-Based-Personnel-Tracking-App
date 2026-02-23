package tata.aut.tatasurksha.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.util.ArrayList;

import tata.aut.tatasurksha.R;
import tata.aut.tatasurksha.SendBeacon;
import tata.aut.tatasurksha.UTILS.SqDB;
import tata.aut.tatasurksha.beacon;


public class frg_ble extends Fragment {
    ListView lstble;
    Context mContext;
    View root;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.blestatus, container, false);
        lstble = root.findViewById(R.id.lstble);
        mContext=root.getContext();
fillData();
        return root;
    }

    void fillData()
    {
        HandlerThread handlerThread = new HandlerThread("call to server");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Do something here!
                try {
                    while(true) {
try {
    frg_ble.this.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            SqDB sql = new SqDB(mContext);
            ArrayList<beacon> lsb = sql.getbeacon();
            String str[] = new String[lsb.size()];
            for (int i = 0; i < lsb.size(); i++) {
                str[i] = lsb.get(i).toString();
            }
            if(lsb.size()==0)
            {
                str = new String[1];
                str[0] = "No Ble Found...... Waiting For Ble\n\n\n\nMake sure AUTO START is turned on ....\nBluetooth is ON \nInternet is ON...\nYou have rebooted your device after installtion";
                ArrayAdapter adapter = new ArrayAdapter<String>(mContext, R.layout.lst_layout, str);
                lstble.setAdapter(adapter);

                           }
            else {
                ArrayAdapter adapter = new ArrayAdapter<String>(mContext, R.layout.lst_layout, str);
                lstble.setAdapter(adapter);
                adapter.notifyDataSetChanged();



            }
        }
    });
}catch (Exception ex){}

                        Thread.sleep(1000*30);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
