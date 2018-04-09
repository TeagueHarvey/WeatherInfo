package hu.ait.android.weatherinfo;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

import hu.ait.android.weatherinfo.Data.City;
import io.realm.Realm;



import static android.app.Activity.RESULT_OK;

/**
 * Created by teagu_000 on 29/11/2017.
 */

public class CreateCityActivity extends AppCompatActivity {

    public static final String KEY_ITEM = "KEY_ITEM";
    public static final String API_KEY = "246690bee1f7f1838ace0f6707aa320c";
    public static final String TOTAL = "TOTAL";
    private EditText etName;
    private City cityToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hu.ait.android.weatherinfo.R.layout.activity_add_city);

        setupUI();

        initCreate();
    }

    private void initCreate() {
        getRealm().beginTransaction();
        cityToEdit = getRealm().createObject(City.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }


    private void setupUI() {
        etName = (EditText) findViewById(hu.ait.android.weatherinfo.R.id.etCityName);
        Button btnSave = (Button) findViewById(hu.ait.android.weatherinfo.R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmCityList();
    }

    private void saveItem() {

        if (!TextUtils.isEmpty(etName.getText())) {

            Intent intentResult = new Intent();
            getRealm().beginTransaction();
            cityToEdit.setName(etName.getText().toString());
            getRealm().commitTransaction();
            intentResult.putExtra(KEY_ITEM, cityToEdit.getItemID());
            setResult(RESULT_OK, intentResult);
            finish();

        } else {

            if (TextUtils.isEmpty(etName.getText())) {
                etName.setError("Error! Try again");
            }
        }
    }

}
