package tata.aut.tatasurksha.UTILS;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tata.aut.tatasurksha.BeansParameter.Para_con;
import tata.aut.tatasurksha.Listener.AsyncListener;
import tata.aut.tatasurksha.Parser.Parser;

public class SendToServer {

Context context=null;
    public SendToServer(Context context)
    {
        this.context=context;
    }
   public SendToServer()
    {

    }

    public void sendMiCloud(final String message)
    {
        final Para_con par_con=new Para_con();
        par_con.data=message;


        AsyncListener<Para_con> listener=new AsyncListener<Para_con>() {
            @Override
            public Para_con onPreDownload() {

                return par_con;
            }

            @Override
            public void onPostDownload(JSONObject result) {


                try{
                    Gson gson = new Gson();
                    Log.d("jsonData",result.toString());
                    //JSONObject object = result.getJSONObject("Result");
                    //SqDB sql=new SqDB(SmsReciver.this.getBaseContext());
                    //Toast.makeText(SmsReciver.this.getBaseContext(),result.toString(),Toast.LENGTH_LONG).show();
                    //Log.e("Dhan Meassaagae",result.toString());
                    // JSONArray objzone = result.getJSONArray("Zone");
                    //final Result finalresult = gson.fromJson(object.toString(), Result.class);
                    //if (finalresult.status == 0) {




                    //}
                }catch (Exception ex){


                }
                //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }
        };

        Parser<Para_con> devParser = new Parser<Para_con>();

        devParser.setListener(listener);
        devParser.execute();
    }



    public  void send(final String message)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    sendMiCloud(message);
                }catch (Exception ex){}
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                try {
//                    //constants
//                    //URL url = new URL("http://emptracker.iot.tatacommunications.com/api/v1/deviceuplinks/uplinks");
//                    URL url = new URL("https://demo.emptracker.iot.tatacommunications.com/api/v1/deviceuplinks/uplinks");
//                    //String message = "{}";//jsonObject.toString();
//
//                    conn = (HttpURLConnection) url.openConnection();
//
//                    conn.setDoOutput(true);
//
//                    conn.setRequestMethod("POST");
//
//                    conn.setRequestProperty("Accept-Charset", "application/json;charset=utf-8");
//
//                    conn.setReadTimeout(10000);
//                    conn.setConnectTimeout(15000);
//
//                    conn.connect();
//
//                    // paramsString = sbParams.toString();
//
//                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//                    wr.writeBytes(message);
//                    wr.flush();
//                    wr.close();

                   // URL url = new URL("https://demo.emptracker.iot.tatacommunications.com/api/v1/deviceuplinks/uplinks");
                    //String message = "{}";//jsonObject.toString();
                   // https://emptracker.iot.tatacommunications.com/api/v1/deviceuplinks/uplinks
                    //URL url = new URL("https://demo.emptracker.iot.tatacommunications.com/api/v1/deviceuplinks/uplinks");
                    URL url = new URL("https://emptracker.iot.tatacommunications.com/api/v1/deviceuplinks/uplinks");
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setDoOutput(true);

                    conn.setRequestMethod("POST");

                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");


                    Log.d("JSON Parser", "result: " +"https://emptracker.iot.tatacommunications.com/api/v1/deviceuplinks/uplinks");


                    conn.setReadTimeout(80000);
                    conn.setConnectTimeout(85000);

                    conn.connect();

                    // paramsString = sbParams.toString();

                    OutputStream wr = conn.getOutputStream();
                    wr.write(message.getBytes("UTF-8"));
                    wr.flush();
                    wr.close();


                    StringBuilder result = new StringBuilder();
                    try {
                        //Receive the response from the server
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        JSONObject jsonObject=new JSONObject(result.toString());
                        SqDB sql = new SqDB(context);
                        sql.deletebeaconall();


//                        if(jsonObject.getInt("status")==200)
//                        {
//                            if(context!=null) {
//                                SqDB sql = new SqDB(context);
//                                sql.deletebeaconall();
//                            }
//                        }

                        Log.d("JSON Parser", "result: " + result.toString());

                    } catch (IOException e) {
                        Log.d("Connection", e.toString());
                        e.printStackTrace();
                    }



                } catch (IOException e) {
                    Log.d("Connection", e.toString());
                    e.printStackTrace();
                } catch (Exception ex){

                    Log.d("Connection", ex.toString());
                }finally {
                    //clean up


                    conn.disconnect();
                }
            }
        }).start();
    }

}
