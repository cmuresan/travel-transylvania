package com.cristianmmuresan.traveltransylvania.ui.place;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class PlaceViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application application;
    private final int placeId;

    PlaceViewModelFactory(Application application, int placeId) {
        this.application = application;
        this.placeId = placeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PlaceViewModel(application, placeId);
    }
}
