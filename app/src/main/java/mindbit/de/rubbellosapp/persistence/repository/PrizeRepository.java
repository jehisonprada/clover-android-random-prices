package mindbit.de.rubbellosapp.persistence.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import mindbit.de.rubbellosapp.persistence.AppDatabase;
import mindbit.de.rubbellosapp.persistence.dao.PrizeDao;
import mindbit.de.rubbellosapp.persistence.model.Prize;

@SuppressWarnings("unchecked")
public class PrizeRepository {

    private final PrizeDao prizeDao;
    private final LiveData<List<Prize>> allPrizes;
    private final LiveData<List<Prize>> inStockPrizes;

    public PrizeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        prizeDao = db.prizeDao();
        allPrizes = prizeDao.getAll();
        inStockPrizes = prizeDao.getInStock();
    }

    public LiveData<List<Prize>> getAll(){
        return allPrizes;
    }

    public LiveData<List<Prize>> getInStock(){
        return inStockPrizes;
    }

    public void insert(Prize prize) {
        new InsertAsyncTask(prizeDao).execute(prize);
    }

    public void insertAll(List<Prize> prizes) {
        new InsertAllAsyncTask(prizeDao).execute(prizes);
    }

    public void delete(Prize prize) {
        new DeleteAsyncTask(prizeDao).execute(prize);
    }

    private static class InsertAllAsyncTask extends AsyncTask<List<Prize>, Void, Void> {

        private final PrizeDao mAsyncTaskDao;

        InsertAllAsyncTask(PrizeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(List<Prize>... lists) {
            mAsyncTaskDao.insertAll(lists[0]);
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Prize, Void, Void> {

        private final PrizeDao mAsyncTaskDao;

        InsertAsyncTask(PrizeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Prize... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Prize, Void, Void> {

        private final PrizeDao mAsyncTaskDao;

        DeleteAsyncTask(PrizeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Prize... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
