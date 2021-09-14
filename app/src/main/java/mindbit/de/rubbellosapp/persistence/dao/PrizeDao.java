package mindbit.de.rubbellosapp.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import mindbit.de.rubbellosapp.persistence.model.Prize;

@Dao
public interface PrizeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Prize prize);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Prize> prizes);

    @Query("SELECT * FROM prize WHERE amount > 0 AND enabled")
    LiveData<List<Prize>> getInStock();

    @Query("SELECT * FROM prize")
    LiveData<List<Prize>> getAll();

    @Delete
    void delete(Prize prize);
}
