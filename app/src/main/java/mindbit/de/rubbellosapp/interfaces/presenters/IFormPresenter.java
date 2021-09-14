package mindbit.de.rubbellosapp.interfaces.presenters;

import mindbit.de.rubbellosapp.persistence.model.User;

public interface IFormPresenter {

    boolean validateFields(String firstName, String lastName, String email, boolean dataPolicyAccepted);

    User buildUser(String name, String lastName, String email);

}
