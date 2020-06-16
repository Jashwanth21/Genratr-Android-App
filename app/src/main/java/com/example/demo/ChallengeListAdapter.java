package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demo.ui.challengeProfiles.ChallengeDetails;

import java.util.ArrayList;

/**
 * This adapter forms the View Model in the MVVC architecture of this project
 * It takes the data passed to it and includes methods to filter and unfilter based on location
 * It also defines display logic for individual views in the ListView in ChallengListActivity.java
 */
public class ChallengeListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Challenge> data;
    private ArrayList<Challenge> dataBackup;

    public ChallengeListAdapter(Context context, ArrayList<Challenge> members) {
        mContext = context;
        data = members;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // allows us to restore values that have been filtered out
        dataBackup = new ArrayList<>();
        dataBackup.addAll(members);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get references to the views defined in tile_challenge.xml
        View rowView = mInflater.inflate(R.layout.tile_challenge, parent, false);
        TextView titleTextView = rowView.findViewById(R.id.ct_title);
        TextView levelTextView = rowView.findViewById(R.id.ct_level);
        TextView descriptionTextView = rowView.findViewById(R.id.ct_description);

        // Put values into the views
        final Challenge challenge = (Challenge) getItem(position);
        String level = "";
        if (challenge.getCHALLENGE_LEVEL().equals("GLOBAL")) {
            level = "Global";
        } else if (challenge.getCHALLENGE_LEVEL().equals("COUNTRY")) {
            level = "National";
        } else {
            level = "Local";
        }
        titleTextView.setText(challenge.getCHALLENGE_TITLE());
        titleTextView.setTextColor(mContext.getResources().getColor(R.color.MediterraneanSea));
        levelTextView.setText("Level: " + level);
        descriptionTextView.setText(challenge.getCHALLENGE_DESCRIPTION());

        // Setting listener to redirect to the ChallengeDetails class
        rowView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChallengeDetails.class);
                intent.putExtra("id", challenge.getCHALLENGE_ID());
                mContext.startActivity(intent);
            }
        });

        return rowView;
    }


    // restore the values to what the database returned instead of a filtered version
    public void unfilter() {
        data.clear();
        data.addAll(dataBackup);
        notifyDataSetChanged();
    }

    // removing every challenge which does not match the geographic level the user has chosen
    public void filter(Utilities.ChallengeLevel level) {

        data.clear();

        ArrayList<Challenge> newData = new ArrayList<>();
        for (int i = 0; i < dataBackup.size(); i++) {
            Challenge c = dataBackup.get(i);
            if (c.getCHALLENGE_LEVEL() == level) {
                newData.add(c);
            }
        }
        data.addAll(newData);

        notifyDataSetChanged();
    }
}