package tata.aut.tatasurksha.login.data;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tata.aut.tatasurksha.UTILS.Constants;
import tata.aut.tatasurksha.login.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            //----- network call goes here!
            String wsResult = callLoginWebservice(username, password);
            if (wsResult != null && wsResult.equals("TRUE")) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                username,
                                username);
                return new Result.Success<>(fakeUser);
            } else if (wsResult.equals("FALSE")) {
                return new Result.Error(new IOException("Invalid Credentials"));
            } else {
                return new Result.Error(new IOException(wsResult));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }


    private String callLoginWebservice(String username, String password) {
       // OutputStream os = null;
       //InputStream is = null;
       // HttpURLConnection conn = null;
        try {

            //  private static final String METHOD_NAME = "TopGoalScorers";
            //  private static final String SOAP_ACTION = "http://footballpool.data";
            //  private static final String NAMESPACE = "http://footballpool.dataaccess.eu";
            //   private static final String URL = "http://footballpool.dataaccess.eu/data/info.wso?WSDL&quot;;";
            //constants

            //SoapObject resp= callSOAP(Constants.LOGIN_METHOD_NAME, Constants.SOAP_ACTION, Constants.SOAP_URI_NS, Constants.LOGIN_URL);


            String APPPACKAGENAME = Constants.APPPACKAGENAME;
            String APPGROUP = Constants.APPGROUP;
            String inputJson = "{\"appGroup\":\"" + APPGROUP + "\",\"appPackageName\":\"" + APPPACKAGENAME + "\",\"deviceId\":\"123456789\",\"requestType\":\"M\",\"userIdHintText\":\"Enter User ID\",\"userIdLabel\":\"User ID Web\",\"userIdValue\":\" - \",\"userIdvalueFlag\":\"N\",\"userPasswordHintText\":\"Enter ADID Password Web\",\"userPasswordLabel\":\"Password\",\"authenticateButtonText\":\"Sign In\",\"loginTitle\":\"Tata Steel Web Login Portal\",\"userId\":\"%1$s\",\"password\":\"%2$s\"}";
            inputJson = String.format(inputJson, username, password);

            SoapObject request = new SoapObject(Constants.SOAP_URI_NS, Constants.LOGIN_METHOD_NAME); //set up request
            request.addProperty("jsonVal", inputJson); //passs the soap input parameters.
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope
            envelope.setOutputSoapObject(request);  //prepare request
            envelope.dotNet = true;
            HttpTransportSE httpTransport = new HttpTransportSE(Constants.LOGIN_URL);
            httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)
            httpTransport.call(Constants.SOAP_ACTION, envelope); //send request
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse(); //get response
            if (result != null) {
                if (result.toString().startsWith("S#[{")) {
                    return "TRUE";
                } else {
                    return "FALSE";
                }
            } else {
                return "ERROR - No response from server";
            }

//            URL url = new URL(Constants.LOGIN_URL);
//
//            conn = (HttpURLConnection) url.openConnection();
//
//            conn.setDoOutput(true);
//
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            //conn.setRequestProperty("Accept-Charset", "application/json;charset=utf-8");
//
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//
//            conn.connect();
//
//            os = conn.getOutputStream();
//            String POST_PARAMS = "jsonVal=" + inputJson;
//            os.write(POST_PARAMS.getBytes());
//            os.flush();
//            os.close();
//            int responseCode = conn.getResponseCode();
//            System.out.println("POST Response Code :: " + responseCode);
//            if (responseCode == HttpURLConnection.HTTP_OK) { //success
//                BufferedReader in = new BufferedReader(new InputStreamReader(
//                        conn.getInputStream()));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                // print result
//                System.out.println(response.toString());
//                if(response.toString().contains("S#[{")){
//                    return "TRUE";
//                }
//                else{
//                    return "FALSE";
//                }
//            } else {
//                System.out.println("POST request not worked");
//                return "ERROR - No response from server";
//            }
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR - " + e.getMessage();
        } catch (Exception ex) {
            return "ERROR - " + ex.getMessage();
        } finally {
            //clean up
            //conn.disconnect();
        }
    }


//    public SoapObject callSOAP(String METHOD_NAME, String SOAP_ACTION, String NAMESPACE, String URL) throws IOException, XmlPullParserException {
//        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //set up request
//        request.addProperty("iTopN", "5"); //variable name, value. I got the variable name, from the wsdl file!
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope
//        envelope.setOutputSoapObject(request);  //prepare request
//        HttpTransportSE httpTransport = new HttpTransportSE(URL);
//        httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)
//        httpTransport.call(SOAP_ACTION, envelope); //send request
//        SoapObject result = (SoapObject) envelope.getResponse(); //get response
//        return result;
//    }
}
