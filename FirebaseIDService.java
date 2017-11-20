package com.compassgames.androidtest;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import android.os.UserManager;

/**
 * Created by shbli on 2017-08-23.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "Unity";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + firebaseToken);
    }

    public static String firebaseToken;

    public static String FirebaseToken()
    {
        return firebaseToken;
    }

    public static Activity unityActivity;

    public static void GetFirebaseToken(Activity parentActivity)
    {
        unityActivity = parentActivity;

        //run on mainthread
        parentActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d("MAIN", "Thread? " + Thread.currentThread());
                Log.w(TAG, "FirebaseApp.initializeApp");
                FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                        .setApplicationId("*****")
                        .setApiKey("****")
                        .setGcmSenderId("****")
                        .build();
                // Get updated InstanceID token.
                FirebaseApp.initializeApp(unityActivity.getApplicationContext(), firebaseOptions);
                firebaseToken = FirebaseInstanceId.getInstance().getToken();
                Log.w(TAG, "FirebaseToken: " + firebaseToken);
            }
        });
    }

}
