/**
 * This adapter forms the View Model in the MVVC architecture of this project
 * It defines display logic for individual views in the ListView in ChallengListActivity.java
 */
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


public class FeedAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CompletionEvent> data;

    public FeedAdapter(Context context, ArrayList<CompletionEvent> elements){
        mContext = context;
        data = elements;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CompletionEvent getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get references to the views defined in tile_completed_challenge.xml
        View rowView = mInflater.inflate(R.layout.tile_completed_challenge, parent, false);
        TextView titleTextView = (TextView) rowView.findViewById(R.id.ct_title);
        TextView contentTextView = (TextView) rowView.findViewById(R.id.ct_level);
        TextView cNameTextView = (TextView) rowView.findViewById(R.id.ct_description);

        // Put values into the views and add colours and listeners
        final CompletionEvent item = (CompletionEvent) getItem(position);
        final Challenge challenge = (Challenge) getItem(position).getChallenge();
        String level = "";
        if(challenge.getCHALLENGE_LEVEL().equals("GLOBAL")){
            level = "global level";
        } else if (challenge.getCHALLENGE_LEVEL().equals("COUNTRY")){
            level = "national level";
        } else {
            level = "local";
        }
        titleTextView.setText(item.getCompletersUserName());
        titleTextView.setTextColor(mContext.getResources().getColor(R.color.MediterraneanSea, null));
        titleTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Profile.class);
                intent.putExtra("id", item.getCompletersUserId());
                mContext.startActivity(intent);;
            }
        });
        contentTextView.setText("has completed the "+level+" challenge");
        cNameTextView.setText('"'+challenge.getCHALLENGE_TITLE()+'"');
        cNameTextView.setTextColor(mContext.getResources().getColor(R.color.MediterraneanSea, null));
        cNameTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChallengeDetails.class);
                intent.putExtra("id", challenge.getCHALLENGE_ID());
                mContext.startActivity(intent);
            }
        });

        return rowView;
    }
}