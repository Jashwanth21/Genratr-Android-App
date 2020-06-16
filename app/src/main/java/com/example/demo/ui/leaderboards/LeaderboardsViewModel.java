package com.example.demo.ui.leaderboards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderboardsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeaderboardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}