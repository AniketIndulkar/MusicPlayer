package com.androidvoyage.ncsmusicplayer.view.viewmodels;

import androidx.lifecycle.ViewModel;

import com.androidvoyage.ncsmusicplayer.manager.SongManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SongListViewModel extends ViewModel implements Observer<ArrayList<HashMap<String, String>>> {

    OnSongsLoad onSongsLoad;


    public void setOnSongsLoad(OnSongsLoad onSongsLoad) {
        this.onSongsLoad = onSongsLoad;
    }

    public void getSongsList() {

        SongManager plm = new SongManager();

//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        ObservableOnSubscribe<ArrayList<HashMap<String, String>>> handler = emitter -> {

            emitter.onNext(plm.getPlayList());
            emitter.onComplete();

//            Future<Object> future = executor.schedule(() -> {
//                emitter.onNext(plm.getPlayList());
//                emitter.onComplete();
//                return null;
//            }, 100, TimeUnit.MILLISECONDS);

//            emitter.setCancellable(() -> future.cancel(false));
        };


        Observable.create(handler)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ArrayList<HashMap<String, String>> hashMaps) {
        onSongsLoad.allSongsLoaded(hashMaps);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }


    public interface OnSongsLoad {
        public void allSongsLoaded(ArrayList<HashMap<String, String>> songs);
    }
}

