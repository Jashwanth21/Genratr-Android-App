package com.example.demo.ui.leaderboards;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.demo.Challenge;
import com.example.demo.Database;
import com.example.demo.MainActivity;
import com.example.demo.R;

import java.util.ArrayList;

public class LeaderboardsFragment extends Fragment {

    Database db;

    String test;

    private ArrayList<String> leadersArray = new ArrayList<String>();

    // CREATE AN ARRAY OF CHALLENGE OBJECTS
    public Challenge[] challenge;

    // CREATE ARRAY LISTS FOR FOLLOWERS AND FOLLOWING
    private ArrayList<Integer> followers = new ArrayList<>();
    private ArrayList<Integer> following = new ArrayList<>();

    // CREATE AN ARRAY LIST OF ALL CHALLENGES COMPLETED BY FOLLOWING
    private ArrayList<String> followingChallenges = new ArrayList<>();

    public LeaderboardsFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboards, container, false);
        db = new Database(getActivity());
        challenge = new Challenge[db.getChallengeCount()];

        // IMPORT THE TOP (INPUT) USERS FROM THE DATABASE
        importLeadersFromDB();

        // CREATE A LIST AND APPEND THE RELEVANT DATA TO THE LIST
        ListView leaderboard = (ListView) view.findViewById(R.id.leaderboardView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, leadersArray);

        leaderboard.setAdapter(adapter);
        return view;
    }

    // IMPORT ALL LEADERS FROM THE DATABASE
    public void importLeadersFromDB(){
        // RETRIEVE THE TOP 10 USERS FROM THE DATABASE
        Cursor res = db.getTop(10);

        if(res.getCount() == 0){
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
            return;
        }

        while(res.moveToNext()){
            // ADD EACH RESULT ITEM TO THE LEADERS ARRAY
            leadersArray.add(res.getString(1) + "    -   " + res.getInt(4));
        }
    }

}