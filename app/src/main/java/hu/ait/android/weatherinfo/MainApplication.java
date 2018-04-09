package hu.ait.android.weatherinfo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {
//this represents our whole application, hence why it's good for it to manage the database, so that every activity can manage the database

    private Realm realmCityList;

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmCityList = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmCityList.close();
    }

    public Realm getRealmCityList() {
        return realmCityList;
    }

}

