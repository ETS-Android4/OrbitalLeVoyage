package com.example.levoyage.ui.Accommodation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccommodationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is accommodation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}