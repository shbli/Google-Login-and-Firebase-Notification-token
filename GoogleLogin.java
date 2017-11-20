/**
 * Created by shbli on 8/16/16.
 */

package com.compassgames.androidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.analytics.FirebaseAnalytics;


public class GoogleLogin extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private static String token = "";
    private static boolean isActivityDone = false;
    public static boolean isSignOutActivity = false;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9002;
    private static final String TAG = "Unity";
    private static final String CLIENT_ID = "333770720019-8v5kmpkrs64dtoi531pockpj79bpe6bk.apps.googleusercontent.com";

    private static GoogleLogin instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        instance = this;
        Log.w(TAG,"[GoogleLogin][onCreate]");
        isActivityDone = false;
        this.SignInWithGooglePlay();

    }

    public static boolean IsActivityDone()
    {
        return isActivityDone;
    }

    public static String Token()
    {
        return token;
    }

    public static void CreateActivity(Activity parentActivity, boolean p_isSignOutActivity)
    {
        isSignOutActivity = p_isSignOutActivity;
        if (instance == null)
        {
            Intent i = new Intent(parentActivity, GoogleLogin.class);
            Bundle b = new Bundle();
            i.putExtras(b);

            parentActivity.startActivity(i);
        }
    }

    public static GoogleLogin Instance()
    {
        return instance;
    }

    private void SignInWithGooglePlay()
    {
        Log.w(TAG,"SignInWithGooglePlay");
// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.w(TAG,"onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (isSignOutActivity == true)
        {
            if (token != null && token.length() > 0) {
                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.w(TAG, "revokeAccessCompleted");
                    }
                });
            }
        }
        isActivityDone = true;
        finish();
        instance = null;
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int
                                                  i)
    {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d(TAG, "onConnectionFailed");
        isActivityDone = true;
        finish();
        instance = null;
    }

    private void handleSignInResult(GoogleSignInResult result)
    {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null)
            {
                Log.w(TAG, "Token is " + acct.getIdToken());
                token = acct.getIdToken();
            }
        }
    }
}
