package hu.ait.android.weatherinfo;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.android.weatherinfo.Data.City;
import hu.ait.android.weatherinfo.adapter.CityAdapter;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_NEW_ITEM = 101;
    public static final int REQUEST_EDIT_ITEM = 102;
    private CityAdapter cityAdapter;
    private CoordinatorLayout layoutContent;
    private DrawerLayout drawerLayout;
    private int itemToEditPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        RealmResults<City> allCities = getRealm().where(City.class).findAll();
        final City citiesArray[] = new City[allCities.size()];
        List<City> citiesResult = new ArrayList<City>(Arrays.asList(allCities.toArray(citiesArray)));

        cityAdapter = new CityAdapter(citiesResult, this);
        RecyclerView recyclerViewItems = (RecyclerView) findViewById(
                R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(cityAdapter);

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateItemActivity();
            }
        });

        layoutContent = (CoordinatorLayout) findViewById(
                R.id.layoutContent);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.action_add:
                                showCreateItemActivity();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_about:
                                showSnackBarMessage(getString(R.string.about_app));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_help:
                                showSnackBarMessage(getString(R.string.action_help));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                        }

                        return false;
                    }
                });

        setUpToolBar();
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmCityList();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void showCreateItemActivity() {
        Intent intentStart = new Intent(MainActivity.this,
                CreateCityActivity.class);
        startActivityForResult(intentStart, REQUEST_NEW_ITEM);
    }

    public void showEditItemActivity(String itemID, int position) {
        Intent intentStart = new Intent(MainActivity.this,
                CreateCityActivity.class);
        itemToEditPosition = position;

        startActivityForResult(intentStart, REQUEST_EDIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String itemID  = data.getStringExtra(
                        CreateCityActivity.KEY_ITEM);

                City city = getRealm().where(City.class)
                        .equalTo(getString(R.string.itemID), itemID)
                        .findFirst();

                if (requestCode == REQUEST_NEW_ITEM) {
                    cityAdapter.addItem(city);
                    showSnackBarMessage(getString(R.string.city_added));
                }
                break;
            case RESULT_CANCELED:
                showSnackBarMessage(getString(R.string.action_cancelled));
                break;
        }
    }


    public void deleteItem(City city) {
        getRealm().beginTransaction();
        city.deleteFromRealm();
        getRealm().commitTransaction();
    }

    public void deleteAllItem() {
        getRealm().beginTransaction();
        getRealm().deleteAll();
        getRealm().commitTransaction();
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(R.string.hide, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showCreateItemActivity();
                return true;
            case R.id.action_deleteall_from_toolbar:
                cityAdapter.removeAllItems();
                return true;
            default:
                showCreateItemActivity();
                return true;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }

}
