package mindbit.de.rubbellosapp.presenter;

import android.text.TextUtils;
import android.util.Patterns;

import mindbit.de.rubbellosapp.interfaces.presenters.IFormPresenter;
import mindbit.de.rubbellosapp.interfaces.views.IFormView;
import mindbit.de.rubbellosapp.persistence.model.User;

public class FormPresenter implements IFormPresenter {

    private final IFormView formView;

    public FormPresenter(IFormView formView) {
        this.formView = formView;
    }

    @Override
    public boolean validateFields(String firstName, String lastName, String email, boolean dataPolicyAccepted) {
        if (TextUtils.isEmpty(firstName)) {
            formView.showEmptyFirstNameError();
            return false;
        } else if (TextUtils.isEmpty(lastName)){
            formView.showEmptyLastNameError();
            return false;
        } else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            formView.showInvalidEmailError();
            return false;
        } else if(!dataPolicyAccepted){
            formView.showDataPolicyError();
            return false;
        }
        return true;
    }

    @Override
    public User buildUser(String firstName, String lastName, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }

}
