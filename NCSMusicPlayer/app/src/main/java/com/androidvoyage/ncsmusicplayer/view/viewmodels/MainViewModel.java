package com.androidvoyage.ncsmusicplayer.view.viewmodels;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public List<String> getListOfString() {
        List<String> data = new ArrayList<>();
        data.add("AAA");
        data.add("BBB");
        data.add("CCC");
        data.add("DDD");
        return data;

    }
}
