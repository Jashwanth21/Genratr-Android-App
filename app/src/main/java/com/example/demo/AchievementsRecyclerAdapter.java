package com.example.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Recycler adapter used to fill the achievements RecyclerView in the user profile
 */
public class AchievementsRecyclerAdapter extends RecyclerView.Adapter<AchievementsRecyclerAdapter.ViewHolder>{
    public List<String> list;

    public AchievementsRecyclerAdapter(List<String> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public AchievementsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementsRecyclerAdapter.ViewHolder holder, int position) {
        String desc = list.get(position);
        holder.desc.setText(desc);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView desc;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            desc = mView.findViewById(R.id.label);
        }
    }
}
