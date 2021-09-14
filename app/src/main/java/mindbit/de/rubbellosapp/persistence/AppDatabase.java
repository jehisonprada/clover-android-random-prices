package mindbit.de.rubbellosapp.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import mindbit.de.rubbellosapp.persistence.dao.PrizeDao;
import mindbit.de.rubbellosapp.persistence.dao.UserDao;
import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.persistence.model.User;

@Database(entities = {User.class, Prize.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase sInstance;

    @VisibleForTesting
    private static final String DATABASE_NAME = "rubbellos-db";

    public abstract PrizeDao prizeDao();
    public abstract UserDao userDao();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

}