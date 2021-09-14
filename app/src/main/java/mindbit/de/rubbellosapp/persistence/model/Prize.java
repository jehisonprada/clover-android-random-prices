package mindbit.de.rubbellosapp.persistence.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

@Entity
public class Prize extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String description;
    private Integer amount;
    private Integer odds;
    private String validity;
    private int initialRange;
    private int finalRange;
    private boolean enabled;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Bindable
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Bindable
    public Integer getOdds() {
        return odds;
    }

    public void setOdds(Integer odds) {
        this.odds = odds;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public int getInitialRange() {
        return initialRange;
    }

    public void setInitialRange(int initialRange) {
        this.initialRange = initialRange;
    }

    public int getFinalRange() {
        return finalRange;
    }

    public void setFinalRange(int finalRange) {
        this.finalRange = finalRange;
    }

    @BindingAdapter("android:text")
    public static void setText(TextView view, Integer value) {
        view.setText(value == null ? "" : String.format("%s",value));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Integer getText(TextView view) {
        try{
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}