package hu.ait.android.weatherinfo.Data;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by teagu_000 on 27/11/2017.
 */

public class City extends RealmObject {
    @PrimaryKey
    private String itemID;

    private String name;

    public City(){ //for object relation mapping

    }

    public City(String city) {
        this.name = city;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }


}
