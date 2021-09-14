package mindbit.de.rubbellosapp.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DrawerLayoutListener implements DrawerLayout.DrawerListener{

    private final Activity drawerLayoutActivity;

    public DrawerLayoutListener(Activity activity){
        drawerLayoutActivity = activity;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        hideKeyboard();
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {

    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) drawerLayoutActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = drawerLayoutActivity.getCurrentFocus();
        if (view == null) {
            view = new View(drawerLayoutActivity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
