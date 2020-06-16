package com.example.demo.ui.feed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.demo.CompletionEvent;
import com.example.demo.Database;
import com.example.demo.FeedAdapter;
import com.example.demo.Login;
import com.example.demo.R;
import com.example.demo.Utilities;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private Database db;
    private ListView mListView;
    private FeedAdapter adapter;
    private AsyncTask<Void, Void, ArrayList<CompletionEvent>> loading;
    private AuthTask authTask;
    private int currentUserId;
    private boolean authComplete;

    public FeedFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        // initialising database and views
        db = new Database(getActivity());

        // finding id of current user
        authComplete = false;
        authTask = new AuthTask();
        authTask.execute();

        // making database calls and displaying data
        loading = new LoaderTask();
        loading.execute();

        mListView = root.findViewById(R.id.feed_view);

        return root;
    }

    // asynchronous task to fetch data from the database, put in adapter, and attach to the ListView
    private final class LoaderTask extends AsyncTask <Void, Void, ArrayList<CompletionEvent>> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<CompletionEvent> doInBackground(Void... params) {

            // making sure data will not get displayed before we have verified the user is logged in
            // doing this in an AsyncTask avoids hanging the UI during authentication
            while(!authComplete){
                try{
                    Thread.sleep(10);
                } catch (InterruptedException e){
                    String errorMessage = "An error occurred: \n"+e.getMessage();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            // if the user is definitely not logged in, redirect to login activity
            if(!authComplete && currentUserId == 0){
                startActivity(new Intent(getActivity(), Login.class));
            }

            ArrayList<CompletionEvent> dataset = new ArrayList<>();

            // getting all users and challenges to loop through later
            Cursor userCursor = db.getAllUsers();
            Cursor challengeCursor = db.getAllChallenges();
            Cursor linkCursor = db.getAllCompleted();

            // looping through to find only the relevant ones and putting them in a collection
            ArrayList<CompletionEvent> allEvents = Utilities.buildCompletionObjects(userCursor, challengeCursor, linkCursor);

            // finding who the current user follows
            Cursor followingCursor = db.getFollowing(currentUserId);
            ArrayList<Integer> following = new ArrayList<>();
            if (followingCursor.moveToFirst()){
                do{
                    following.add(followingCursor.getInt(0));
                } while (followingCursor.moveToNext());
            }

            for (int i=0; i<allEvents.size(); i++){
                CompletionEvent event = allEvents.get(i);
                Integer id;
                try{
                    id = event.getCompletersUserId();
                } catch(NullPointerException e) {
                    String errorMessage = "One of the events is corrupted\n"+e.getMessage();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    continue;
                }
                if (following.contains(id.intValue())){
                    dataset.add(event);
                }
            }

            return dataset;
        }

        @Override
        protected void onPostExecute(ArrayList<CompletionEvent> result) {
            super.onPostExecute(result);

            adapter = new FeedAdapter(getActivity(), result);
            mListView.setAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    // asynchronous task to find the current user's ID and store in the variable currentUserId
    private final class AuthTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            currentUserId = db.getUser();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result){
            authComplete = true;
        }
    }
}
