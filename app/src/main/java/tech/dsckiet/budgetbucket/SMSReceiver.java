package tech.dsckiet.budgetbucket;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static tech.dsckiet.budgetbucket.Notification.channel_ID;
public class SMSReceiver extends BroadcastReceiver {

    private Context mContext;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    private String msg, phoneno, recievedMsgTrans, realComapanyPhn, m, debitedMsgText;

    private FirebaseAuth mAuth;
    private NotificationManagerCompat notificationManager;
    private String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }

    String type = "online";
    String URL_POST = "https://tranquil-coast-71727.herokuapp.com/api/v1/add_transaction/" + mail();

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.i(TAG, "Intent Received: " + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {
            //retrieves a map of extended data from the intent
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] mypdu = (Object[]) bundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for (int i = 0; i < mypdu.length; i++) {
                    //for build versions >= API level 23
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        //from pdu we get all object and smsmessage object using following code
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    } else {
                        // for less API VERSIONS than 23
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }
                    msg = message[i].getMessageBody();
                    phoneno = message[i].getOriginatingAddress();
                    if (isDebited(msg)) {
                        recievedMsgTrans = getTransactionAmt(msg);   }
//                    } else recievedMsgTrans = "is a credit message ";
//                    if(isLegalCompanyText(phoneno)){
//                        realComapanyPhn = phoneno;
//                    } else{
//                        realComapanyPhn = "Invalid Sender Code !";
//                        recievedMsgTrans = "NULL";
//                    }


                }

                //TODO:: it helps to show/record messages from company(not local numbers)
//                if(TransacFrom(phoneno))
                if(recievedMsgTrans != null){
//                showNotification(recievedMsgTrans,context);

                    Intent intentService = new Intent(context,BackgroundService.class);
                    intentService.putExtra("inputExtra",recievedMsgTrans);
                    context.startService(intentService);

                }
                //POST METHOD of online transaction recorded in background.
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "onResponse: " + "DATA PUSHED" );
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("amount", recievedMsgTrans);
//                        params.put("type", type);
//                        return params;
//                    }
//                };
//
//                RequestQueue requestQueue = Volley.newRequestQueue(context);
//                requestQueue.add(stringRequest);
            }
        }

    }

//    //todo:
//    private void sendToService(String newData){
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("ServiceToActivityAction");
//        broadcastIntent.putExtra("ServiceToActivityKey", newData);
//        mContext.sendBroadcast(broadcastIntent);
//    }
//
//    public void showNotification(String amount,Context context) {
//        Intent notIntent = new Intent(context,AddCashTransactionActivity.class);
//
//        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0,
//                notIntent, 0);
//
//        Notification mBuilder =
//                new NotificationCompat.Builder(context,channel_ID)
//                        .setSmallIcon(R.drawable.ic_add_red)
//                        .setContentTitle("Budget Bucket")
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                        .setContentIntent(contentIntent)
//                        .setOnlyAlertOnce(true)
//                        .setContentText("Transaction of Rs. " +amount + " has been recorded.")
//                .setAutoCancel(true)
//                .build();
//


//        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        mBuilder.setSound(notificationSound);


//        mBuilder.setDefaults(Notification.ALARM_SER);
//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, mBuilder);
//
//    }

    private String getTransactionAmt(String inputMsg){
        String x = "Rs.";
        String string = "";
        String in = inputMsg;
        char[] y = {0};
        for(int i=0; i<in.length()-2; i++){
            if(in.substring(i,i+3).equals(x)){
                m = "";
                string = in.substring(i+3,i+14);
                for(int j=0; j<string.length(); j++){
                    y[0] = string.substring(j,j+1).charAt(0);
                    if(y[0] >=48 && y[0] <=57 || y[0] == 46) {
                        m = m + y[0];
                    }
                    if(y[0] == 32 && j > 1)
                        break;
                }
                break;
            } else{
                m = "Money string not found";
            }
        }
        System.out.println(m);
        return m;
    }

    //    private Boolean isLegalCompanyText(String inputMsg){
//        Boolean returnValue = false;
//        String inputSubstring = inputMsg.substring(0,2);
//        for(char i = 'A'; i<='Z'; i++){
//            for(char j='A'; j<='Z'; j++){
//                String sampleValue = String.valueOf(i) + String.valueOf(j);
//                if(sampleValue.equals(inputSubstring)){
//                    returnValue = true;
//                    break;
//                } else
//                    returnValue = false;
//            }
//        }
//        return returnValue;
//    }
    private static Boolean isDebited(String inputMsg) {
        Boolean returnValue = false;
        if(isOfType5(inputMsg)) {
            if (isOfType1(inputMsg))
                returnValue = true;
            else if (isOfType2(inputMsg))
                returnValue = true;
            else if (isOfType3(inputMsg))
                returnValue = true;
            else if (isOfType4(inputMsg))
                returnValue = true;
            else returnValue = false;
        }else {
            returnValue = false;
        }

        return returnValue;
    }

    private static Boolean TransacFrom(String inputAddress){
        Boolean returnValue = false;
        if(fromAmazon(inputAddress))
            returnValue = true;
        else if(fromFlipkart(inputAddress))
            returnValue = true;
        else if(fromMyntra(inputAddress))
            returnValue = true;
        else if(fromPayTM(inputAddress))
            returnValue = true;
        else returnValue = false;

        return returnValue;
    }

    private static Boolean isOfType1(String inputMsg) {
        Boolean returnValue = false;
        String type1 = "debited";
        String input = inputMsg.toLowerCase();
        for (int i = 0; i < input.length()-6; i++) {
            if (input.substring(i, i + 7).equals(type1)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

    private static Boolean isOfType2(String inputMsg) {
        Boolean returnValue = false;
        String type1 = "paid";
        String input = inputMsg.toLowerCase();
        for (int i = 0; i < input.length()-3; i++) {
            if (input.substring(i, i + 4).equals(type1)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

    private static Boolean isOfType3(String inputMsg) {
        Boolean returnValue = false;
        String type1 = "transferred";
        String input = inputMsg.toLowerCase();
        for (int i = 0; i < input.length()-10; i++) {
            if (input.substring(i, i + 11).equals(type1)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

    private static Boolean isOfType4(String inputMsg) {
        Boolean returnValue = false;
        String type1 = "received";
        String input = inputMsg.toLowerCase();
        for (int i = 0; i < input.length()-7; i++) {
            if (input.substring(i, i + 8).equals(type1)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

    private static Boolean isOfType5(String inputMsg) {
        Boolean returnValue = true;
        String type1 = "a/c";
        String input = inputMsg.toLowerCase();
        for (int i = 0; i < input.length()-2; i++) {
            if (input.substring(i, i + 3).equals(type1)) {
                returnValue = false;
                break;
            } else
                returnValue = true;
        }
        return returnValue;
    }

    private static Boolean fromPayTM(String inputAddress){
        Boolean returnValue = false;
        String type = "paytm";
        String input = inputAddress.toLowerCase();
        for(int i=0; i < input.length()-4; i++){
            if(input.substring(i,i + 5).equals(type)){
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }
    private static Boolean fromFlipkart(String inputAddress){
        Boolean returnValue = false;
        String type1 = "flpkrt";
        String type2 = "ekartl";
        String input = inputAddress.toLowerCase();
        for(int i=0; i < input.length()-5; i++){
            if(input.substring(i,i + 6).equals(type1) || input.substring(i,i + 6).equals(type2)){
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }
    private static Boolean fromAmazon(String inputAddress){
        Boolean returnValue = false;
        String type = "amazon";
        String input = inputAddress.toLowerCase();
        for(int i=0; i < input.length()-5; i++){
            if(input.substring(i,i + 6).equals(type)){
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }
    private static Boolean fromMyntra(String inputAddress){
        Boolean returnValue = false;
        String type = "myntra";
        String input = inputAddress.toLowerCase();
        for(int i=0; i < input.length()-5; i++){
            if(input.substring(i,i + 6).equals(type)){
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }
}
