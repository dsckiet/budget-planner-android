package tech.dsckiet.budgetbucket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SMSReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    private String msg, phoneno, recievedMsgTrans, m;

    private FirebaseAuth mAuth;
    private NotificationManagerCompat notificationManager;

    private static Boolean isDebited(String inputMsg) {
        Boolean returnValue = false;
        if (isOfType5(inputMsg)) {
            if (isOfType1(inputMsg))
                returnValue = true;
            else if (isOfType2(inputMsg))
                returnValue = true;
            else if (isOfType3(inputMsg))
                returnValue = true;
            else if (isOfType4(inputMsg))
                returnValue = true;
            else returnValue = false;
        } else {
            returnValue = false;
        }

        return returnValue;
    }

    private static Boolean TransacFrom(String inputAddress) {
        Boolean returnValue = false;
        if (fromAmazon(inputAddress))
            returnValue = true;
        else if (fromFlipkart(inputAddress))
            returnValue = true;
        else if (fromMyntra(inputAddress))
            returnValue = true;
        else if (fromPayTM(inputAddress))
            returnValue = true;
        else returnValue = false;

        return returnValue;
    }

    private static Boolean isOfType1(String inputMsg) {
        Boolean returnValue = false;
        String type1 = "debited";
        String input = inputMsg.toLowerCase();
        for (int i = 0; i < input.length() - 6; i++) {
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
        for (int i = 0; i < input.length() - 3; i++) {
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
        for (int i = 0; i < input.length() - 10; i++) {
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
        for (int i = 0; i < input.length() - 7; i++) {
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
        for (int i = 0; i < input.length() - 2; i++) {
            if (input.substring(i, i + 3).equals(type1)) {
                returnValue = false;
                break;
            } else
                returnValue = true;
        }
        return returnValue;
    }

    private static Boolean fromPayTM(String inputAddress) {
        Boolean returnValue = false;
        String type = "paytm";
        String input = inputAddress.toLowerCase();
        for (int i = 0; i < input.length() - 4; i++) {
            if (input.substring(i, i + 5).equals(type)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

    private static Boolean fromFlipkart(String inputAddress) {
        Boolean returnValue = false;
        String type1 = "flpkrt";
        String type2 = "ekartl";
        String input = inputAddress.toLowerCase();
        for (int i = 0; i < input.length() - 5; i++) {
            if (input.substring(i, i + 6).equals(type1) || input.substring(i, i + 6).equals(type2)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

    private static Boolean fromAmazon(String inputAddress) {
        Boolean returnValue = false;
        String type = "amazon";
        String input = inputAddress.toLowerCase();
        for (int i = 0; i < input.length() - 5; i++) {
            if (input.substring(i, i + 6).equals(type)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

    private static Boolean fromMyntra(String inputAddress) {
        Boolean returnValue = false;
        String type = "myntra";
        String input = inputAddress.toLowerCase();
        for (int i = 0; i < input.length() - 5; i++) {
            if (input.substring(i, i + 6).equals(type)) {
                returnValue = true;
                break;
            } else
                returnValue = false;
        }
        return returnValue;
    }

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
                        recievedMsgTrans = getTransactionAmt(msg);
                    }
                }

                //TODO:: it helps to show/record messages from company(not local numbers)
                if (TransacFrom(phoneno))
                    if (recievedMsgTrans != null) {
                        Intent intentService = new Intent(context, BackgroundService.class);
                        intentService.putExtra("inputExtra", recievedMsgTrans);
                        context.startService(intentService);
                    }
            }
        }

    }

    private String getTransactionAmt(String inputMsg) {
        String x = "Rs.";
        String string = "";
        String in = inputMsg;
        char[] y = {0};
        for (int i = 0; i < in.length() - 2; i++) {
            if (in.substring(i, i + 3).equals(x)) {
                m = "";
                string = in.substring(i + 3, i + 14);
                for (int j = 0; j < string.length(); j++) {
                    y[0] = string.substring(j, j + 1).charAt(0);
                    if (y[0] >= 48 && y[0] <= 57 || y[0] == 46) {
                        m = m + y[0];
                    }
                    if (y[0] == 32 && j > 1)
                        break;
                }
                break;
            } else {
                m = "Money string not found";
            }
        }
        System.out.println(m);
        return m;
    }
}
