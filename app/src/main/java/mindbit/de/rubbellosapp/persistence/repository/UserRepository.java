package mindbit.de.rubbellosapp.persistence.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import mindbit.de.rubbellosapp.persistence.AppDatabase;
import mindbit.de.rubbellosapp.persistence.dao.UserDao;
import mindbit.de.rubbellosapp.persistence.model.User;

public class UserRepository {

    private final UserDao userDao;
    private final LiveData<List<User>> notInCloverMarket;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        notInCloverMarket = userDao.getNotInCloverMarket();
    }

    public LiveData<List<User>> getNotInCloverMarket(){
        return notInCloverMarket;
    }

    public void insert(User user) {
        new UserRepository.InsertAsyncTask(userDao).execute(user);
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {

        private final UserDao mAsyncTaskDao;

        InsertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
