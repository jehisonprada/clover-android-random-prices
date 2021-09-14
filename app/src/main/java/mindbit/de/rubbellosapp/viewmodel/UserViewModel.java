package mindbit.de.rubbellosapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import mindbit.de.rubbellosapp.persistence.model.User;
import mindbit.de.rubbellosapp.persistence.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository mRepository;
    private final LiveData<List<User>> notInCloverMarket;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository(application);
        notInCloverMarket = mRepository.getNotInCloverMarket();
    }

    public LiveData<List<User>> getNotInCloverMarket() { return notInCloverMarket; }

    public void insert(User user) {
        mRepository.insert(user);
    }

}