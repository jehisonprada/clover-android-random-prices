package mindbit.de.rubbellosapp.persistence.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @PrimaryKey
    @NonNull
    private String email;
    private String firstName;
    private String lastName;
    private boolean inCloverMarket;

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isInCloverMarket() {
        return inCloverMarket;
    }

    public void setInCloverMarket(boolean inCloverMarket) {
        this.inCloverMarket = inCloverMarket;
    }
}
