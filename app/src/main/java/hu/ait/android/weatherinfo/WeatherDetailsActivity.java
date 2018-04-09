package hu.ait.android.weatherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.ait.android.weatherinfo.Data.WeatherResult;
import hu.ait.android.weatherinfo.network.WeatherAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.tvTemp)
    TextView tvTemp;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvTempMax)
    TextView tvTempMax;
    @BindView(R.id.tvTempMin)
    TextView tvTempMin;
    @BindView(R.id.tvHumidity)
    TextView tvHumidity;
    @BindView(R.id.tvWind)
    TextView tvWind;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        Call<WeatherResult> call = weatherAPI.getCityByName(getIntent().getStringExtra("CITY_NAME"), "metric",
                CreateCityActivity.API_KEY);

        call.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                if (response.body() != null) {
                    setUiFromData(response);
                } else {
                    Toast.makeText(WeatherDetailsActivity.this, R.string.bad, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                Toast.makeText(WeatherDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        ButterKnife.bind(this);
    }

    private void setUiFromData(Response<WeatherResult> response) {
        tvCityName.setText(response.body().getName());
        tvTemp.setText(Double.toString(response.body().getMain().getTemp()));
        tvTempMax.setText(Double.toString(response.body().getMain().getTempMax())+"°");
        tvTempMin.setText(Double.toString(response.body().getMain().getTempMin())+"°");
        tvDesc.setText(response.body().getWeather().get(0).getDescription());
        tvHumidity.setText(Double.toString(response.body().getMain().getHumidity()));
        tvWind.setText(Double.toString(response.body().getWind().getSpeed()));
        String url = "http://openweathermap.org/img/w/" + response.body().getWeather().get(0).getIcon() + ".png";
        Glide.with(WeatherDetailsActivity.this).load(url).into(ivIcon);
    }

}