package com.example.umesh.omrscanner;
import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    SignInButton googleSignInButton;
    FirebaseDatabase database;
    DatabaseReference myRefUsers;
    String email,organization,userId,CITY,ORGANIZATION,SCHOOL;
    SharedPreferences preferences;
    DataSnapshot userDataSnapshot;
    int userFoundFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database=FirebaseDatabase.getInstance();
        myRefUsers=database.getReference("users");
        preferences=getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);
        userFoundFlag=0;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Sign In Failed. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }  /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignInButton=(SignInButton)findViewById(R.id.google_sign_in_button);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });



    // Example of a call to a native method

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            email=acct.getEmail();



            myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dsp:dataSnapshot.getChildren())
                    {
                        if(dsp.child("email").getValue().toString().equals(email)){
                            userFoundFlag=1;
                            userDataSnapshot=dsp;
                        }


                    }
                    final SharedPreferences.Editor editor=preferences.edit();
                    editor.putString(getString(R.string.sp_email),email); ///has to be replaced with actual email
                    editor.apply();


                    if (userFoundFlag == 1) {
//                        myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for(DataSnapshot dsp:dataSnapshot.getChildren()){
//                                    if(dsp.child("email").equals("fellow1@tfi.org")){
//                                        userId=dsp.getKey();
//                                        editor.putString(getString(R.string.sp_user_id),userId);
//                                        editor.apply();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
                        //to be removed
                        //userId="ghr2rtd8myRefUsers.push().getKey();
                        userId=userDataSnapshot.getKey();
                        CITY=userDataSnapshot.child("city").getValue().toString();
                        ORGANIZATION=userDataSnapshot.child("organization").getValue().toString();
                        SCHOOL=userDataSnapshot.child("school").getValue().toString();
                        editor.putString(getString(R.string.sp_user_id),userId);
                        editor.putString(getString(R.string.sp_city),CITY);
                        editor.putString(getString(R.string.sp_org),ORGANIZATION);
                        editor.putString(getString(R.string.sp_school),SCHOOL);
                        editor.apply();

                        Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                        startActivity(intent);
                    }
                    else{
                        userId=myRefUsers.push().getKey();//"ghr2rtd849ngh";
                        editor.putString(getString(R.string.sp_user_id),userId);
                        editor.apply();
                        myRefUsers.child(userId).child("email").setValue(email);
                        Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//            SharedPreferences.Editor editor=preferences.edit();
//            editor.putString("userEmail",email);
//            editor.apply();
//
//            Toast.makeText(this, userFoundFlag+"", Toast.LENGTH_SHORT).show();
//
//            if (userFoundFlag == 1) {
//                Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
//                startActivity(intent);
//            }
//            else{
//                String id=myRefUsers.push().getKey();
//                editor.putString("userId",id);
//                editor.apply();
//                myRefUsers.child(id).child("email").setValue(email);
//                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
//                startActivity(intent);
//            }

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(LoginActivity.this, "Sign In Failed. Please Try Again.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
//
//    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
}


