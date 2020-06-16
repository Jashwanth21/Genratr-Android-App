package com.example.demo.ui.challengeProfiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.demo.Challenge;
import com.example.demo.ChallengeException;
import com.example.demo.Database;
import com.example.demo.R;
import com.example.demo.Utilities;
import com.example.demo.ui.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Activity to create a new challenge, it contains a header preview to show what the challenge profile will look like and editable components that let you customise the application
 */
public class CreateChallenge extends AppCompatActivity {

    Database db;

    //Declaring all the views used in the activity
    //Parent layout and toolbar
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;

    //Views used by the colour picker popup window
    View popupView;
    PopupWindow popupWindow;
    EditText editRed;
    EditText editGreen;
    EditText editBlue;
    ImageView colourPreview;

    //Header preview views
    TextView challengeTitleHeader;
    TextView challengeScoreHeader;
    TextView challengeLevelHeader;
    TextView challengeTagHeader;

    //editable challenge attribute views
    EditText challengeTitle;
    EditText challengeDescription;
    Spinner challengeTagSpinner;
    TextView challengeScore;
    DatePicker challengeDeadline;
    Spinner challengeLevelSpinner;
    ImageButton mapButton;
    TextView challengeLocation;

    //geocoder used to show the name of the county/country
    Geocoder geocoder;

    int colour;
    LatLng coordinates;
    Utilities.ChallengeLevel challengeLevel;
    Utilities.ChallengeType challengeType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        geocoder = new Geocoder(this);

        //Initialise the views based on the view IDs in the layout, resolves any intents passed to this activity, and set listeners to update the previews
        InitialiseViews();
        ResolveIntent();
        SetListeners();
    }

    /**
     * If a level and coordinates are passed to the activity, the coordinates and level are set automatically
     */
    private void ResolveIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("level")){
            Utilities.ChallengeLevel level = (Utilities.ChallengeLevel) intent.getSerializableExtra("level");
            challengeLevelSpinner.setSelection(Arrays.asList(Utilities.ChallengeLevel.values()).indexOf(level));
            double lat = intent.getDoubleExtra("lat", 0);
            double lon = intent.getDoubleExtra("long", 0);
            coordinates = new LatLng(lat, lon);
            challengeLocation.setText(getLocationName());
        }
    }

    /**
     * Initialises all views using the view ids in the layout and sets the default values for some components
     */
    private void InitialiseViews() {
        //set a default value for the challenge header colour
        colour = getResources().getColor(R.color.AndroidGreen, null);

        //Set the parent and toolbar components and set the default values
        coordinatorLayout = findViewById(R.id.create_challenge_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbar.setTitle("");
        toolbarLayout.setTitle("");
        toolbar.setBackgroundColor(colour);
        toolbarLayout.setBackgroundColor(colour);

        // initialise header preview components
        challengeTitleHeader = findViewById(R.id.textview_new_challenge_title);
        challengeTagHeader = findViewById(R.id.textview_new_challenge_tag);
        challengeLevelHeader = findViewById(R.id.textview_new_challenge_level);
        challengeScoreHeader = findViewById(R.id.textview_new_challenge_score);

        //initialise editable components and fill spinners based on existing enums
        challengeTitle = findViewById(R.id.new_challenge_title);
        challengeDescription = findViewById(R.id.new_challenge_description);
        challengeTagSpinner = findViewById(R.id.spinner_tag);
        List<Utilities.ChallengeType> challengeTypeList = Arrays.asList(Utilities.ChallengeType.values());
        ArrayAdapter<Utilities.ChallengeType> typesArrayAdapter = new ArrayAdapter<Utilities.ChallengeType>(this, R.layout.support_simple_spinner_dropdown_item, challengeTypeList);
        challengeTagSpinner.setAdapter(typesArrayAdapter);
        challengeScore = findViewById(R.id.textview_score);
        challengeLevelSpinner = findViewById(R.id.new_challenge_level);
        List<Utilities.ChallengeLevel> challengeLevelList = Arrays.asList(Utilities.ChallengeLevel.values());
        ArrayAdapter<Utilities.ChallengeLevel> challengeLevelArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, challengeLevelList);
        challengeLevelSpinner.setAdapter(challengeLevelArrayAdapter);
        challengeDeadline = findViewById(R.id.new_challenge_duration);
        challengeDeadline.updateDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, LocalDate.now().getDayOfMonth());
        challengeDeadline.setMinDate(System.currentTimeMillis());
        challengeLocation = findViewById(R.id.textview_new_location);
        mapButton = findViewById(R.id.new_challenge_location);
    }

    /**
     * Sets listeners for some of the editable components to automatically change the header preview
     */
    private void SetListeners() {
        //change title preview when title is edited
        challengeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                challengeTitleHeader.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //change tag in preview and set score based on the challenge tag
        challengeTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                challengeType = (Utilities.ChallengeType) parent.getItemAtPosition(position);
                challengeScore.setText(String.valueOf(challengeType.getScore()));
                challengeScoreHeader.setText(String.valueOf(challengeType.getScore()));
                challengeTagHeader.setText(challengeType.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                challengeScore.setText(0);
                challengeScoreHeader.setText(0);
                challengeTagHeader.setText("");
            }
        });

        //set challenge level in preview and change location name based on the chosen level
        challengeLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                challengeLevel = (Utilities.ChallengeLevel) parent.getItemAtPosition(position);
                challengeLevelHeader.setText(challengeLevel.toString());
                challengeLocation.setText(getLocationName());
                if (challengeLevel == Utilities.ChallengeLevel.GLOBAL) {
                    mapButton.setEnabled(false);
                    mapButton.setBackgroundColor(getResources().getColor(R.color.BlueMartina, null));
                    mapButton.setAlpha(0.5f);
                } else {
                    mapButton.setEnabled(true);
                    mapButton.setBackgroundColor(getResources().getColor(R.color.MediterraneanSea, null));
                    mapButton.setAlpha(1f);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                challengeLevelHeader.setText("");
            }
        });


    }

    /**
     * uses the geocoder the chosen location to get the name of the location based on the challenge level
     * LOCAL is County Dublin
     * COUNTRY is Ireland
     *
     * @return name of the location based on the level
     */
    private String getLocationName() {
        if (challengeLevel == Utilities.ChallengeLevel.GLOBAL) {
            return getString(R.string.global);
        } else {
            if (coordinates != null) {
                try {
                    List<Address> addressList = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1);


                    if (addressList.size() > 0) {
                        if (challengeLevel == Utilities.ChallengeLevel.LOCAL) {
                            return addressList.get(0).getAdminArea();
                        } else {
                            return addressList.get(0).getCountryName();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            } else {
                return getString(R.string.pick_location);
            }
        }
    }

    /**
     * Opens of the map activity so the user can choose the location of the challenge
     */
    public void getLocation(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("level", challengeLevelSpinner.getSelectedItem().toString());
        startActivityForResult(intent, 1);
    }

    /**
     * Resolves activities that are completed and return a value to this activity
     * In this case we only get the result of the map activity. The map activity returns the coordinates of a chosen location
     * this method gets the coordinates from the resulting intent and set the coordinates.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    coordinates = new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("long", 0));
                    challengeLocation.setText(getLocationName());
                }

            }
            if (resultCode == RESULT_CANCELED) {
                coordinates = null;
            }

        }
    }


    /**
     * If the edit colour button is pressed, a popup is created that lets the user pick the colour of the challenge header
     * This method opens the header and initialises all the views used by popup
     * It also sets listeners for the text changes
     */
    public void getColourPicker(View view) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        popupView = inflater.inflate(R.layout.color_picker, coordinatorLayout);

        popupWindow = new PopupWindow(popupView, CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        editRed = popupView.findViewById(R.id.edit_red);
        editGreen = popupView.findViewById(R.id.edit_green);
        editBlue = popupView.findViewById(R.id.edit_blue);
        colourPreview = popupView.findViewById(R.id.image_preview);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreviewColour();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editRed.addTextChangedListener(textWatcher);
        editGreen.addTextChangedListener(textWatcher);
        editBlue.addTextChangedListener(textWatcher);

    }

    /**
     * Updates the colour preview in the popup based on the rgb EditText components
     */
    private void updatePreviewColour() {
        try {
            int red = Integer.parseInt(editRed.getText().toString());
            int green = Integer.parseInt(editGreen.getText().toString());
            int blue = Integer.parseInt(editBlue.getText().toString());
            colourPreview.setBackgroundColor(Color.rgb(red, green, blue));
        } catch (NumberFormatException e) {
            colourPreview.setColorFilter(Color.BLACK);
        }
    }

    /**
     * There are 4 preset colours, if one of the preset colours is selected, the rgb textfields get updated with the rgb values of the preset colours
     * and the preview is updated
     */
    public void pickColour(View view) {

        switch (view.getId()) {
            case R.id.red_button:
                editRed.setText(String.valueOf(Color.red(getResources().getColor(R.color.BaraRed, null))));
                editGreen.setText(String.valueOf(Color.green(getResources().getColor(R.color.BaraRed, null))));
                editBlue.setText(String.valueOf(Color.blue(getResources().getColor(R.color.BaraRed, null))));
                updatePreviewColour();
                break;
            case R.id.green_button:
                editRed.setText(String.valueOf(Color.red(getResources().getColor(R.color.AndroidGreen, null))));
                editGreen.setText(String.valueOf(Color.green(getResources().getColor(R.color.AndroidGreen, null))));
                editBlue.setText(String.valueOf(Color.blue(getResources().getColor(R.color.AndroidGreen, null))));
                updatePreviewColour();
                break;
            case R.id.blue_button:
                editRed.setText(String.valueOf(Color.red(getResources().getColor(R.color.CircumorbitalRings, null))));
                editGreen.setText(String.valueOf(Color.green(getResources().getColor(R.color.CircumorbitalRings, null))));
                editBlue.setText(String.valueOf(Color.blue(getResources().getColor(R.color.CircumorbitalRings, null))));
                updatePreviewColour();
                break;
            case R.id.yellow_button:
                editRed.setText(String.valueOf(Color.red(getResources().getColor(R.color.Energos, null))));
                editGreen.setText(String.valueOf(Color.green(getResources().getColor(R.color.Energos, null))));
                editBlue.setText(String.valueOf(Color.blue(getResources().getColor(R.color.Energos, null))));
                updatePreviewColour();
                break;
        }


    }

    /**
     * When a colour is selected, the popup is dismissed and the header colour gets changed to the selected colour
     */
    public void setColour(View view) {
        int red = Integer.parseInt(editRed.getText().toString());
        int green = Integer.parseInt(editGreen.getText().toString());
        int blue = Integer.parseInt(editBlue.getText().toString());

        popupWindow.dismiss();
        colour = Color.rgb(red, green, blue);
        toolbar.setBackgroundColor(colour);
        toolbarLayout.setBackgroundColor(colour);

    }

    /**
     * If everything is filled in, the user can press the complete challenge button
     * this method checks if all components without a default value are filled in
     * if something is not complete, ChallengeException will be thrown and a toast will be shown
     * If everything is correct, the challenge will be created
     */
    public void createChallenge(View view) {
        db = new Database(this);
        try {
            String title = challengeTitle.getText().toString();
            if (title.equals("")){
                throw new ChallengeException("Enter a title");
            }
            String description = challengeDescription.getText().toString();
            if (description.equals("")){
                throw new ChallengeException("Enter a description");
            }
            String chosenCoordinates;
            if (challengeLevel != Utilities.ChallengeLevel.GLOBAL && Objects.isNull(coordinates)) {
                throw new ChallengeException("Enter coordinates or set level to Global");
            } else if (challengeLevel != Utilities.ChallengeLevel.GLOBAL) {
                chosenCoordinates = this.coordinates.latitude + "," + this.coordinates.longitude;
            } else {
                chosenCoordinates = "NA";
            }
            LocalDate deadline = LocalDate.of(challengeDeadline.getYear(), challengeDeadline.getMonth()+1, challengeDeadline.getDayOfMonth());
            if (deadline.isBefore(LocalDate.now())){
                throw new ChallengeException("Deadline can't be in the past");
            }

            Challenge newChallenge = new Challenge(title, challengeType.getName(), description, chosenCoordinates, challengeLevel, challengeType.getScore(), -1, deadline, "#" + Integer.toHexString(colour));

            AsyncTask<Challenge, Void, Void> createChallengeTask = new CreateChallengeTask();
            createChallengeTask.execute(newChallenge);
        } catch (ChallengeException e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * async task, creates the challenge in the database using the data entered in the activity
     */
    private final class CreateChallengeTask extends AsyncTask<Challenge, Void, Void> {
        /**
         * gets the current logged in user from the database, using the challenge object a new challenge is inserted into the database
         * When the insert is complete the activity finishes with a success code
         *
         * @param challenges Challenge object, containing all the information needed to create a challenge in the database
         */
        @Override
        protected Void doInBackground(Challenge... challenges) {
            Challenge challenge = challenges[0];
            int currentUser = db.getUser();
            db.insertChallenge(challenge.getCHALLENGE_TITLE(), challenge.getCHALLENGE_TAG(), challenge.getCHALLENGE_DESCRIPTION(), challenge.getCHALLENGE_COORDINATES(), challenge.getCHALLENGE_LEVEL().name(), challenge.getCHALLENGE_SCORE(), currentUser, challenge.getCHALLENGE_DEADLINE().format(DateTimeFormatter.ISO_DATE), challenge.getCHALLENGE_HEX());
            // activity returns an intent, the intent is later used to update the challenge list
            Intent intent = new Intent();
            intent.putExtra("created", true);
            setResult(RESULT_OK, intent);
            finish();
            return null;
        }
    }
}
