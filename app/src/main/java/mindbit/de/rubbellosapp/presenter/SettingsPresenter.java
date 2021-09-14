package mindbit.de.rubbellosapp.presenter;

import android.text.TextUtils;

import mindbit.de.rubbellosapp.interfaces.presenters.ISettingsPresenter;
import mindbit.de.rubbellosapp.interfaces.views.ISettingsView;
import mindbit.de.rubbellosapp.persistence.model.Prize;

public class SettingsPresenter implements ISettingsPresenter {

    private final ISettingsView settingsView;

    public SettingsPresenter(ISettingsView settingsView) {
        this.settingsView = settingsView;
    }

    public boolean validatePrize(Prize prize) {
        if (TextUtils.isEmpty(prize.getDescription())) {
            settingsView.showDescriptionErrorMessage();
            return false;
        } else if (prize.getAmount() <= 0) {
            settingsView.showAmountErrorMessage();
            return false;
        } else if (prize.getOdds() <= 0) {
            settingsView.showOddsErrorMessage();
            return false;
        }
        return true;
    }

}
