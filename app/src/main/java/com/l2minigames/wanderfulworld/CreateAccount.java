package com.l2minigames.wanderfulworld;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "Loggar i CreateAccount: ";
    private EditText mEditEmail;
    private EditText mEditPassword;
    private Button mButtonCreate;
    private String email;
    private String password;
    public String uid;
    private TextView itemName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        mEditEmail = (EditText) findViewById(R.id.editEmail);
        mEditPassword = (EditText) findViewById(R.id.editPassword);
        mButtonCreate = (Button) findViewById(R.id.buttonCreate);
        itemName = (TextView) findViewById(R.id.itemName);


        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditEmail.getText().toString();
                password = mEditPassword.getText().toString();
                if (isEmailValid(email) && password.length() > 5) {
                    createUser();
                } else if (!isEmailValid(email)) {
                    Toast.makeText(CreateAccount.this, R.string.mail_format_error,
                            Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(CreateAccount.this, R.string.password_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                uid=user.getUid();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    public void createUser() {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        ArrayList<MyMarker> tmpMarkerList = new ArrayList<>();
                        HashMap<String, CollectedItem> tmpCollectedItems = new HashMap<>();
                        MyMarker myMarker = new MyMarker(0,0,"hallon");
                        tmpMarkerList.add(myMarker);
                        Wand myWand = new Wand(10,10,10,10);
                        String default_name = getResources().getString(R.string.default_name);

                        ///CollectedItem tmpCollectedItem = new CollectedItem();
                       /// CollectedItem tmpCollectedItem = new CollectedItem("hallon", "berry","earth", "imageRef", 0, 1, 10, 10);
                        ///tmpCollectedItems.put("collectedItems", tmpCollectedItem);
                        UserObject mUser = new UserObject(default_name, email, 10, 0, 1,0, 0, myWand, tmpMarkerList, tmpCollectedItems);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference myRef = database.getReference(uid);
                        myRef.setValue(mUser);

                        CollectedItem tmpCollectedItem2 = new CollectedItem("hallon", "berry","earth", "imageRef", 0, 1, 10, 10, "uid");
                        myRef.child("collectedItems").push().setValue(tmpCollectedItem2);


                        CollectedItem tmpCollectedItem3 = new CollectedItem("jordgubbe", "berry","earth", "imageRef", 0, 1, 10, 10, "uid");
                        myRef.child("collectedItems").push().setValue(tmpCollectedItem3);
                        ///DatabaseReference collectedRef = myRef.child("collectedItems");
                        ///CollectedItem tmpCollectedItem2 = new CollectedItem("jordgubbe", "berry","earth", "imageRef", 0, 1, 10, 10);
                        ///myRef.child(collectedpush().
                       /// collectedUpdates.put("new items", tmpCollectedItem2);
                       /// collectedRef.push().setValue(collectedUpdates);
                        Intent intent = new Intent(CreateAccount.this, GameActivity.class);
                        startActivity(intent);
                        finish();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }



                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

