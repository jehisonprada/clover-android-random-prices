package mindbit.de.rubbellosapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.persistence.repository.PrizeRepository;

public class PrizeViewModel extends AndroidViewModel {

    private final PrizeRepository mRepository;

    private final LiveData<List<Prize>> mAllPrizes;

    private final LiveData<List<Prize>> inStockPrizes;

    public PrizeViewModel (Application application) {
        super(application);
        mRepository = new PrizeRepository(application);
        mAllPrizes = mRepository.getAll();
        inStockPrizes = mRepository.getInStock();
    }

    public LiveData<List<Prize>> getAllPrizes() { return mAllPrizes; }

    public LiveData<List<Prize>> getInStockPrizes() { return inStockPrizes; }

    public void insert(Prize prize) {
        mRepository.insert(prize);
    }

    public void insertAll(List<Prize> prizes) {
        mRepository.insertAll(prizes);
    }

    public void delete(Prize prize) {
        mRepository.delete(prize);
    }
}