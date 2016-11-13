package com.l2minigames.wanderfulworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ///Firebase mRef;
    TextView textHome;
    private String TAG = "Loggar läsning från databasen: ";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userId;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView mRecyclerView;
    String uid;
    Button updateButton;
    NavigationView navigationView;
    FirebaseRecyclerAdapter<CollectedItem, ObjectViewHolder> adapter;
    Firebase myFirebaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        textHome =(TextView)findViewById(R.id.textHome);
        ///myFirebaseRef = new Firebase("https://wanderful-world.firebaseio.com/VlO28d9A4lQLzUJ2mlUikCbnejt1/collectedItems");
        updateButton = (Button)findViewById(R.id.openMap);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUserName();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uid = user.getUid();
            Log.d("TAG","UserID: "+uid);

            myFirebaseRef = new Firebase("https://wanderful-world.firebaseio.com/"+uid+"/collectedItems");

        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(uid);

        Log.d("TAG", "myRef:"+myRef);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

               UserObject object = dataSnapshot.getValue(UserObject.class);
                textHome.setText(object.username+" "+object.email);
                View hView =  navigationView.getHeaderView(0);
                TextView nav_email = (TextView)hView.findViewById(R.id.drawer_email);
                TextView nav_name = (TextView)hView.findViewById(R.id.drawer_name);
                nav_email.setText(object.email);
                nav_name.setText(object.username);
               Log.d("TAG", "Object is: " + object.username);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        ///DatabaseReference myRef = database.getReference(userId);

        textHome.setText("Bengan");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameActivity.this, MapsActivity.class);
                startActivity(intent);
               /// Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                ///        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
           super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showDatabaseInfo(String userId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userId);

    }
    private void writeNewUserName(){
        myRef.child("username").setValue("Nytt användarnamn");
    }
    @Override
    protected void onStart() {
        super.onStart();

        adapter = new FirebaseRecyclerAdapter<CollectedItem, ObjectViewHolder>(CollectedItem.class, R.layout.list_items, ObjectViewHolder.class, myFirebaseRef) {
            @Override
            protected void populateViewHolder(ObjectViewHolder objectViewHolder, CollectedItem collectedItem, int i) {
              /// objectViewHolder.mText.setText(collectedItem.level);
               String key = this.getRef(i).getKey();
              Log.i("TAG", "Key: "+key);
                ///myFirebaseRef.child("uid").setValue(key);
                objectViewHolder.itemName.setText(collectedItem.itemName);
                objectViewHolder.itemType.setText(collectedItem.itemType);

            }


        };
        mRecyclerView.setAdapter(adapter);

    }
    public static class ObjectViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemType;
        TextView mDivider;

        public ObjectViewHolder(View v) {
            super(v);
            itemName = (TextView) v.findViewById(R.id.itemName);
            itemType = (TextView) v.findViewById(R.id.itemType);
            mDivider = (TextView) v.findViewById(R.id.divider);
            itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.i ("TAG", "Item clicked; "+position);
                    String thisItemName = itemName.getText().toString();
                    Log.i ("TAG", "Key clicked; "+thisItemName);

                }
            });
        }
    }
}
