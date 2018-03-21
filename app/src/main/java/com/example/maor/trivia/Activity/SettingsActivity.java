package com.example.maor.trivia.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.maor.trivia.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends BaseActivity implements
        View.OnClickListener{

    private static final SettingsActivity ourInstance = new SettingsActivity();
    private Button LogOutWithGoogle;
    private Button LogOutManual;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient check;
    public int flagForSound; // if flag is 0 no sound
                            // if flag is 1 sound
                            ToggleButton simpleToggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences settings = getSharedPreferences("gameSound", 0);
        boolean silent = settings.getBoolean("switchkey", false);
        findViewById(R.id.logOutManual).setOnClickListener(this);
        findViewById(R.id.logOutForGoolge).setOnClickListener(this);
    }




    public static SettingsActivity getInstance() {
        return ourInstance;
    }
    public void onStart() {
        super.onStart();


    }
    @Override
    public void onResume() {
        super.onResume();

    }
    private void signOut() {
        // Firebase sign out

    }
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
           // findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.logOutForGoolge).setVisibility(View.VISIBLE);
        } else {
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.logOutForGoolge).setVisibility(View.GONE);
        }
    }
    public void onClick(View v) {


            if (v.getId() == R.id.logOutManual) {

                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                finish();
                                ourInstance.finish();

                            }
                        });

        }
        ///Log out with Google start
            if(v.getId() == R.id.logOutForGoolge) {

                    mAuth = FirebaseAuth.getInstance();
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

                    Button LogOutWithGoogle = (Button) findViewById(R.id.logOutForGoolge);
                mAuth.signOut();
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                updateUI(null);
                                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                ourInstance.finish();

                            }
                        });
                    ///Log out with Google End

            }
    }


}
