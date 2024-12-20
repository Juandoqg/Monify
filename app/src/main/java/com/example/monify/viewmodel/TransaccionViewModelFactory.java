package com.example.monify.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TransaccionViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public TransaccionViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TransaccionViewModel.class)) {
            return (T) new TransaccionViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
