package com.example.demo.ui.challenges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.demo.Challenge;
import com.example.demo.ChallengeListAdapter;
import com.example.demo.Database;
import com.example.demo.Profile;
import com.example.demo.R;
import com.example.demo.Utilities;
import com.example.demo.ui.challengeProfiles.CreateChallenge;
import com.example.demo.ui.feed.FeedFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * This activity displays a list of challenges to the user.
 * These can be filtered by geographical level by using a spinner.
 * The floating action button redirects the user to  create a new challenge.
 */
public class ChallengesFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Database db;
    private ArrayList<Challenge> feedData;
    private ListView mListView;
    private ChallengeListAdapter adapter;
    private Spinner spinner;

    public ChallengesFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_challenges, container, false);

        // initialising database and views
        db = new Database(getActivity());
        feedData = new ArrayList<Challenge>();
        mListView = root.findViewById(R.id.challenge_list_view);

        AsyncTask<Void, Void, Cursor> loading = new LoaderTask();
        loading.execute();

        // setting up a floating action button with a listener and colour
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starts activity, if a result is received the list will be updated
                startActivityForResult(new Intent(getActivity(), CreateChallenge.class), 1);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                Snackbar.make(view, "Tap to create a new challenge", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });
        fab.setBackgroundColor(getResources().getColor(R.color.VeryBerry, null));

        // setting up filter spinner
        spinner = root.findViewById(R.id.level_spinner);
        // default spinner layout, could customise
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.challenge_levels_array, android.R.layout.simple_spinner_item);
        // again, could customise
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        // Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);

        return root;
    }

    // AsyncTask to retrieve data from the database and apply it to the ListView using an adapter
    private final class LoaderTask extends AsyncTask <Void, Void, Cursor> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            return db.getAllChallenges();
        }

        // take the returned cursor, parse into objects, apply to adapter and set to the ListView
        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            // parsing the cursor data into an ArrayList of Challenge objects
            // implemented in a the Utilities java file to avoid clutter here
            feedData = Utilities.getChallengesFromCursor(result);

            adapter = new ChallengeListAdapter(getActivity(), feedData);
            mListView.setAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    // spinner methods

    // this uses an enum in the Utilities file to convey which item was selected
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (adapter != null) {
            if (pos == 0){
                adapter.unfilter();
            }
            else if (pos == 1){
                adapter.filter(Utilities.ChallengeLevel.LOCAL);
            }
            else if (pos == 2){
                adapter.filter(Utilities.ChallengeLevel.COUNTRY);
            }
            else if (pos == 3){
                adapter.filter(Utilities.ChallengeLevel.GLOBAL);
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        adapter.unfilter();
    }

    /**
     * when a result is received from the CreateChallenge activity, the list loader is called again to update the fragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if(resultCode == getActivity().RESULT_OK){
                if (data.getBooleanExtra("created", true)){
                    AsyncTask<Void, Void, Cursor> loading = new LoaderTask();
                    loading.execute();
                    spinner.setSelection(0);
                }
            }
        }
    }
}