package com.androidvoyage.ncsmusicplayer.view.base;

import android.graphics.PixelFormat;
import android.view.Window;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {

        if (getSupportFragmentManager().findFragmentByTag(fragmentTag) != null) {
            getSupportFragmentManager().popBackStack(fragmentTag, 0);
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(containerViewId, fragment, fragmentTag)
                    .addToBackStack(backStackStateName)
                    .commit();
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        // Eliminates color banding
        window.setFormat(PixelFormat.RGBA_8888);
    }

}
