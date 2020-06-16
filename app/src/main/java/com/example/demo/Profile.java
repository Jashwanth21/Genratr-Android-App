/**
 * Activity file for the User Profile page.
 */
package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.ui.feed.FeedFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Profile extends AppCompatActivity {

    // Variables for the profile activity (text views, recycler view, view, Database, image button, button, shared preferences)
    List<String> achievementsArray;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_CODE = 1;
    //private static final int RESULT_SETTINGS = 1;
    private RecyclerView rvAchievements;
    private TextView tvUserName, tvBio, tvScoreValue;
    private View viewCover;
    private Database db;
    private int user;
    private Button btLogout, btEditProfile;
    private ImageButton btUserProfile, ibProfile;
    private String ImagePath;
    private SharedPreferences sharedPrefs;
    private boolean flagLoggedInUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = new Database(this);

        if (db.getStatus() == 0) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            user = intent.getIntExtra("id", 0);
            flagLoggedInUser = false;
        } else {
            user = db.getUser();
            flagLoggedInUser = true;
        }
        init();
        showUserSettings();
    }

    // Method to initialise the variables which were defined earlier for this activity
    private void init() {
        achievementsArray = new ArrayList<>();
        achievementsArray.add("Running Champ");
        achievementsArray.add("Waste Disposal Pro");
        achievementsArray.add("Minimal Plastic Usage");
        achievementsArray.add("Environmentalist");
        achievementsArray.add("Public Transport Advisor");
        achievementsArray.add("Resource Sharing Expert");
        achievementsArray.add("Food Sharing");
        achievementsArray.add("Caregiver");
        achievementsArray.add("Plogger");
        achievementsArray.add("Awareness Champ");
        achievementsArray.add("Sustainable Champion");
        rvAchievements = (RecyclerView) findViewById(R.id.recyclerViewAchievements);
        rvAchievements.setLayoutManager(new LinearLayoutManager(this));
        AchievementsRecyclerAdapter mAdapter = new AchievementsRecyclerAdapter(achievementsArray);
        rvAchievements.setAdapter(mAdapter);
        rvAchievements.setItemAnimator(new DefaultItemAnimator());

        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        tvUserName = (TextView) findViewById(R.id.textViewUsername);
        tvBio = (TextView) findViewById(R.id.textViewBio);
        tvScoreValue = (TextView) findViewById(R.id.textViewScoreValue);
        viewCover = (View) findViewById(R.id.viewCover);
        btLogout = (Button) findViewById(R.id.button_logout);
        btEditProfile = (Button) findViewById(R.id.button_edit_profile);
        btUserProfile = (ImageButton) findViewById(R.id.imageButtonUserProfile);
        ibProfile = (ImageButton) findViewById(R.id.imageButtonProfile);
        // Any other user apart from the one logged in, should not be able to change profile image
        if (!flagLoggedInUser) {
            ibProfile.setEnabled(false);
            ibProfile.setClickable(false);
        }
    }

    /* Method to set the user defined settings in the profile like the cover colour, profile image, username, a short bio and current score
       It also handles visibility of the buttons according to the user who is seeing this page
       If the logged in user is seeing this page, Edit Profile and Logout buttons would be visible.
       If any other user is seeing this page by clicking on the user from the feed page, User Profile image button would be visible.
     */
    private void showUserSettings() {
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        if (!flagLoggedInUser) {
            btLogout.setVisibility(View.INVISIBLE);
            btEditProfile.setVisibility(View.INVISIBLE);
            btUserProfile.setVisibility(View.VISIBLE);
            tvBio.setVisibility(View.INVISIBLE);
        } else {
            btLogout.setVisibility(View.VISIBLE);
            btEditProfile.setVisibility(View.VISIBLE);
            btUserProfile.setVisibility(View.INVISIBLE);
        }

        String colour = db.getUserHex(user);
        viewCover.setBackgroundColor(Color.parseColor(colour));
        tvUserName.setText(db.getUserUsername(user));
        tvBio.setText(sharedPrefs.getString("bio", null));
        tvScoreValue.setText(db.getUserScore(user));
        if (db.getUserImagePath(user) != null)
            loadImageFromStorage(db.getUserImagePath(user));
    }

    /* Method to verify if the permissions required - 'WRITE_EXTERNAL_STORAGE' and 'CAMERA' are already there or not
       If permissions are granted, a boolean flag is set to true which is used in openCamera() method, otherwise false
     */
    public boolean verifyPermissions() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            return false;
        }
    }

    /* Method to handle the functionality once permission dialog box comes up
       It checks whether all the three permissions required are granted or not.
       If all permissions are granted, it calls the verifyPermissions() method again.
       If all permissions are not granted, it will generate a Toast and set profile image as the cover colour.
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    clickPicture();
                else {
                    Toast.makeText(this, "To set the profile image, both Camera and External Storage permissions are needed. Since they are not granted, setting the image as the cover colour.", Toast.LENGTH_LONG).show();
                    String colour = db.getUserHex(user);
                    ibProfile.setBackgroundColor(Color.parseColor(colour));
                    //flagPermission = false;
                }

        }
    }

    /* Method to handle on click of the Profile Image button.
       It calls another method clickPicture() based on the result of verifyPermissions()
     */
    public void openCamera(View view) {
        if (verifyPermissions()) {
            clickPicture();
        }
    }

    /* Method to handle camera functionality
     */
    public void clickPicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    /* Method to handle the functionality once a picture has been clicked
       It calls another method saveToInternalStorage and sets the image of the button as the clicked picture.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImagePath = saveToInternalStorage(imageBitmap);
            System.out.println(ImagePath);
            ibProfile.setImageBitmap(imageBitmap);
        }
    }

    /* Method to handle on click of the Edit Profile button.
       It will open the Profile Settings page.
     */
    public void openSettings(View view) {
        Intent itSettings = new Intent(this, ProfileSettingsActivity.class);
        startActivity(itSettings);
    }

    /* Method to handle on click of the Logout button.
       It will log out the user and open the Login page.
     */
    public void logout(View view) {
        db.setStatus(false);
        Intent itLogout = new Intent(this, Login.class);
        startActivity(itLogout);
    }

    /* Method to handle on click of the User Profile Image button.
       It will open the Profile page.
     */
    public void openUserProfile(View view) {
        Intent itSettings = new Intent(this, Profile.class);
        startActivity(itSettings);
    }

    /* Method to handle changes in the shared preferences from the Profile Settings activity.
       It updates the database with these modified values too.
     */
    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("username")) {
                db.updateUserName(user, sharedPreferences.getString("username", null));
                tvUserName.setText(db.getUserUsername(user));

            }
            if (key.equals("cover_colour")) {
                String colour = sharedPreferences.getString("cover_colour", null);
                db.updateUserHex(user, colour);
                viewCover.setBackgroundColor(Color.parseColor(db.getUserHex(user)));

            }
            if (key.equals("bio")) {
                tvBio.setText(sharedPreferences.getString("bio", null));
            }
        }
    };

    /* Method to save the picture clicked in the internal storage.
       The files will be saved in a folder 'genratr_images' inside the 'Pictures' directory.
       The file will be named as Image_num.jpg where num will be a random number between 1-10000.
       The path of the image will be saved to the database.
       The path of the image is returned so that it can be set as the profile image.
     */
    // Referenced from
    private String saveToInternalStorage(Bitmap bitmapImage) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/genratr_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tells the media scanner about the new file so that it is immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        sharedPrefs.edit().putString("imageFile", path).commit();
                        db.updateUserImagePath(user, sharedPrefs.getString("imageFile", null));
                    }
                });
        return "path";
    }

    /* Method to load the clicked picture at the time of creation of the page.
       It will find the image saved and set the Profile image.
     */
    private void loadImageFromStorage(String path) {
        if (path != null) {
            try {
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                ibProfile.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
