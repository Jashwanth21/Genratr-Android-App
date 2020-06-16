/**
 * Activity file for the home page of the application once a authorised user logs in.
 * It includes bottom navigation for navigating between different activities of the application.
*/
package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.demo.ui.challenges.ChallengesFragment;
import com.example.demo.ui.feed.FeedFragment;
import com.example.demo.ui.leaderboards.LeaderboardsFragment;
import com.example.demo.ui.map.MapActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

// References
// 1) Official Android Documentation
// https://developer.android.com/reference/android/app/Fragment
// https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView
// 2) Displaying fragments
// https://medium.com/@oluwabukunmi.aluko/bottom-navigation-view-with-fragments-a074bfd08711
public class MainActivity extends AppCompatActivity {

    // Variables for the main activity (fragments, bottom navigation view)
    final Fragment fragment1 = new FeedFragment();
    final Fragment fragment2 = new ChallengesFragment();
    final Fragment fragment3 = new LeaderboardsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    private BottomNavigationView bottomNavigationView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this);
        if (db.getStatus() == 0){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        init();
    }

    // Method to initialise the variables which were defined earlier for this activity
    private void init(){
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // Method to handle item selection on the bottom navigation view
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // Display fragment for feed
                case R.id.action_feed:
                    fm.beginTransaction().hide(active).commit();
                    fm.beginTransaction().hide(fragment1).commit();
                    fm.beginTransaction().show(fragment1).commit();
                    active = fragment1;
                    return true;

                // Display fragment for challenges
                case R.id.action_challenge:
                    fm.beginTransaction().hide(active).commit();
                    fm.beginTransaction().hide(fragment1).commit();
                    fm.beginTransaction().show(fragment2).commit();
                    active = fragment2;
                    return true;

                // Display fragment for map
                case R.id.action_map:
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivityForResult(intent, 1);
                    return true;

                // Display fragment for leaderboard
                case R.id.action_leaderboard:
                    fm.beginTransaction().hide(active).commit();
                    fm.beginTransaction().hide(fragment1).commit();
                    fm.beginTransaction().show(fragment3).commit();
                    active = fragment3;
                    return true;
            }
            return false;
        }
    };

    /* Method to handle on click of the User Profile Image button.
           It will open the Profile page.
         */
    public void openUserProfile(View view){
        Intent itProfile = new Intent(this, Profile.class);
        startActivity(itProfile);
    }

    /**
     * If a result is received, it means that a new challenge is created
     * The fragment is changed to the challenge list and the intent is passed to the onActivityResult of the challengeList
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                fm.beginTransaction().hide(active).commit();
                fm.beginTransaction().hide(fragment1).commit();
                fm.beginTransaction().show(fragment2).commit();
                active = fragment2;
                fragment2.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
