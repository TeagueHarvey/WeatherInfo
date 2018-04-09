package hu.ait.android.weatherinfo.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds {
    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    @SerializedName("all")
    @Expose
    public Integer all;

}