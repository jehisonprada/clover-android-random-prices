package mindbit.de.rubbellosapp.util;

import android.support.v4.app.FragmentManager;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.fragments.AboutFragment;
import mindbit.de.rubbellosapp.fragments.BoardFragment;
import mindbit.de.rubbellosapp.fragments.FormFragment;
import mindbit.de.rubbellosapp.fragments.WebViewFragment;
import mindbit.de.rubbellosapp.fragments.SettingsFragment;
import mindbit.de.rubbellosapp.persistence.model.User;

public class FragmentNavigationUtil {

    public static void goToSettingsFragment(FragmentManager fragmentManager) {
        SettingsFragment settingsFragment = SettingsFragment
                .newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.flContainerRubbellos, settingsFragment, "settings_fragment")
                .addToBackStack(null)
                .commit();
    }

    public static void goToImprintFragment(FragmentManager fragmentManager) {
        AboutFragment aboutFragment = AboutFragment
                .newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.flContainerRubbellos, aboutFragment, "about_fragment")
                .addToBackStack(null)
                .commit();
    }

    public static void goToFormFragment(FragmentManager fragmentManager) {
        FormFragment formFragment = FormFragment
                .newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.flContainerRubbellos, formFragment, "form_fragment")
                .addToBackStack(null)
                .commit();
    }

    public static void goToBoardFragment(FragmentManager fragmentManager, User user) {
        BoardFragment boardFragment = BoardFragment
                .newInstance(user);
        fragmentManager.beginTransaction()
                .replace(R.id.flContainerRubbellos, boardFragment, "boardFragment")
                .addToBackStack(null)
                .commit();
    }

    public static void createAndGoToWebViewFragment(FragmentManager fragmentManager, String helpUrl) {
        WebViewFragment webViewFragment = WebViewFragment
                .newInstance(helpUrl);
        fragmentManager.beginTransaction()
                .replace(R.id.flContainerRubbellos, webViewFragment, "webViewFragment")
                .addToBackStack(null)
                .commit();
    }

}
