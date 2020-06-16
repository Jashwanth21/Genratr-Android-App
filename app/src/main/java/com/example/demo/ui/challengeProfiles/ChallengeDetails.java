package com.example.demo.ui.challengeProfiles;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.demo.Challenge;
import com.example.demo.Database;
import com.example.demo.R;
import com.example.demo.User;
import com.example.demo.Utilities;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * Activity that shows the details of a single Challenge
 * the ID of the challenge is passed by the previous activity and it's used to get all the details of a challenge
 */
public class ChallengeDetails extends AppCompatActivity implements OnMapReadyCallback {

    Database db;
    Challenge challenge;
    String userHex;
    String userName;
    String userScore;

    int loggedInUserID;
    String loggedInUserScore;

    //Map variables
    SupportMapFragment supportMapFragment;
    GoogleMap gMap;
    Geocoder geocoder;

    //layout variables
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    AppBarLayout appBarLayout;

    TextView userNameTextView;
    TextView userScoreTextView;
    ImageView userColour;
    TextView challengeTitle;
    TextView challengeTag;
    TextView challengeScore;
    TextView challengeDescription;
    TextView challengeDeadline;
    TextView challengeLevel;
    TextView challengeCoord;
    TextView challengeDaysLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);

        //initialise the database connection
        db = new Database(this);

        //initialise views, get details of a challenge and show details in views
        initialiseViews();
        resolveIntent();

        //initialise geocoder, used to get the local and country level name of a location
        geocoder = new Geocoder(this, Locale.getDefault());


    }

    /**
     * Finds all views used in the activity_challenge_details layout by id and sets the variables
     */
    private void initialiseViews() {
        appBarLayout = findViewById(R.id.app_bar);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userNameTextView = findViewById(R.id.textview_user_name);
        userScoreTextView = findViewById(R.id.textview_user_score);
        userColour = findViewById(R.id.image_hex);
        challengeScore = findViewById(R.id.textview_challenge_score);
        challengeTag = findViewById(R.id.textview_challenge_tag);
        challengeTitle = findViewById(R.id.textview_challenge_title);
        challengeLevel = findViewById(R.id.textview_challenge_level);
        challengeDescription = findViewById(R.id.textview_challenge_desc);
        challengeDeadline = findViewById(R.id.textview_challenge_deadline);
        challengeCoord = findViewById(R.id.textview_challenge_coord);
        challengeDaysLeft = findViewById(R.id.textview_days_left);
    }

    /**
     * Gets the intent sent to this activity, which is the ID of the challenge
     * the id is then used to get the challenge from the database
     */
    private void resolveIntent() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        AsyncTask<Integer, Void, Challenge> loadChallenge = new ChallengeLoaderTask();
        loadChallenge.execute(id);
    }

    /**
     * async task, loads all the information about a challenge from the database and save it in a variable
     */
    private final class ChallengeLoaderTask extends AsyncTask<Integer, Void, Challenge> {

        /**
         * Starts another async task, to load the necessary attributes about the challenge creator.
         *
         * @param newChallenge the challenge that was loaded from the database
         */
        @Override
        protected void onPostExecute(Challenge newChallenge) {
            super.onPostExecute(challenge);
            challenge = newChallenge;
            AsyncTask<Integer, Void, User> loadChallengeCreatorAtttibutes = new ChallengeCreatorAttributeTask();
            loadChallengeCreatorAtttibutes.execute(challenge.getCHALLENGE_CREATOR_ID());
        }

        /**
         * @param integers id of the challenge
         * @return Challenge object containing all the information about the challenge with the selected id
         */
        @Override
        protected Challenge doInBackground(Integer... integers) {
            int id = integers[0];

            return new Challenge(
                    id,
                    db.getChallengeTitle(id),
                    db.getChallengeTag(id),
                    db.getChallengeDescription(id),
                    db.getChallengeCoordinates(id),
                    Utilities.ChallengeLevel.parseChallengeLevel(db.getChallengeLevel(id)),
                    Integer.parseInt(db.getChallengeScore(id)),
                    Integer.parseInt(db.getChallengeCreatorID(id)),
                    LocalDate.parse(db.getChallengeDeadline(id), DateTimeFormatter.ISO_DATE),
                    db.getChallengeHex(id)
            );
        }
    }

    /**
     * Async task, loads some data about the challenge creator
     */
    private final class ChallengeCreatorAttributeTask extends AsyncTask<Integer, Void, User> {

        /**
         * After the user attributes are loaded, all information needed for the activity is collected.
         * SetChallengeDetails is called to show all the information in the activity
         *
         * @param user the user loaded from the database
         */
        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            setChallengeDetails();

        }

        /**
         * @param integers id of the challenge creator
         * @return User object with only the necessary data of the user, rest of the values are null
         */
        @Override
        protected User doInBackground(Integer... integers) {
            userName = db.getUserUsername(integers[0]);
            userScore = db.getUserScore(integers[0]);
            userHex = db.getUserHex(integers[0]);

            return new User(userName, null, null, userScore, userHex, null, userHex);

        }
    }

    /**
     * Shows the challenge attributes using the views in the layout
     */
    private void setChallengeDetails() {
        userNameTextView.setText(userName);
        userScoreTextView.setText(userScore);
        userColour.setBackgroundColor(Color.parseColor(userHex));
        toolbar.setTitle(challenge.getCHALLENGE_TITLE());
        toolbar.setSubtitle("Challenge Score: " + challenge.getCHALLENGE_SCORE());
        toolbarLayout.setTitle(challenge.getCHALLENGE_TITLE());
        challengeTitle.setText(challenge.getCHALLENGE_TITLE());
        challengeTag.setText(challenge.getCHALLENGE_TAG());
        challengeScore.setText(String.valueOf(challenge.getCHALLENGE_SCORE()));
        challengeLevel.setText(challenge.getCHALLENGE_LEVEL().toString());
        challengeDescription.setText(challenge.getCHALLENGE_DESCRIPTION());
        challengeDeadline.setText(challenge.getCHALLENGE_DEADLINE().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        challengeDaysLeft.setText(ChronoUnit.DAYS.between(LocalDate.now(), challenge.getCHALLENGE_DEADLINE()) + " " + getString(R.string.days_left));

        appBarLayout.setBackgroundColor(Color.parseColor(challenge.getCHALLENGE_HEX()));
        toolbarLayout.setBackgroundColor(Color.parseColor(challenge.getCHALLENGE_HEX()));
        toolbar.setBackgroundColor(Color.parseColor(challenge.getCHALLENGE_HEX()));

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.challenge_map);
        supportMapFragment.getMapAsync(this);

    }


    /**
     * Method is called when the camera is loaded,
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        //map is used to show general area of the challenge
        //the map shouldn't be able to move or zoom
        gMap.getUiSettings().setScrollGesturesEnabled(false);
        gMap.getUiSettings().setZoomControlsEnabled(false);
        LatLng latLng;
        CameraUpdate cameraUpdate;
        //If the challenge is global, set the coordinates to 0,0 and zoom out
        // if the challenge is not global, show the saved location in the database
        if (challenge.getCHALLENGE_LEVEL() != Utilities.ChallengeLevel.GLOBAL) {
            latLng = new LatLng(Double.parseDouble(challenge.getCHALLENGE_COORDINATES().split(",")[0]), Double.parseDouble(challenge.getCHALLENGE_COORDINATES().split(",")[1]));
            cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        } else {
            latLng = new LatLng(0, 0);
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 0);
        }
        // move camera to challenge location
        gMap.moveCamera(cameraUpdate);

        //gets the name to be shown based on the challenge level
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList.size() > 0) {
                if (challenge.getCHALLENGE_LEVEL() == Utilities.ChallengeLevel.GLOBAL) {
                    // if the level is global, set the text to global
                    challengeCoord.setText(R.string.global);
                } else if (challenge.getCHALLENGE_LEVEL() == Utilities.ChallengeLevel.COUNTRY) {
                    // if the level is country, return the country the coordinates are located in and zoom out
                    challengeCoord.setText(addressList.get(0).getCountryName());
                    gMap.moveCamera(CameraUpdateFactory.zoomTo(6));
                } else {
                    // if the level is LOCAL, return the county the coordinates are located in and zoom in
                    challengeCoord.setText(addressList.get(0).getAdminArea());
                    gMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the challenge as a completed challenge to the current user
     * and adds the score of the challenge to the userscore.
     */
    public void completeChallenge(View view) {
        AsyncTask<Void, Void, Void> loadLoggedInUserAtttibutes = new LoggedInUserAttributesTask();
        loadLoggedInUserAtttibutes.execute();
        finish();
    }

    /**
     * async task, updates the score of the current user by adding the challenge score to it
     */
    private final class LoggedInUserAttributesTask extends AsyncTask<Void, Void, Void>{
        /**
         * gets the current logged in user from the database and gets their current score
         * the new score is calculated, the score of the logged in user is then updated
         * the challenge is added to the users' completed list
         */
        @Override
        protected Void doInBackground(Void... voids) {
            loggedInUserID = db.getUser();
            loggedInUserScore = db.getUserScore(loggedInUserID);
            int newScore = Integer.parseInt(loggedInUserScore) + challenge.getCHALLENGE_SCORE();
            db.updateUserScore(loggedInUserID, newScore);
            db.completedChallenge(loggedInUserID, challenge.getCHALLENGE_ID());
            return null;
        }
    }

}
