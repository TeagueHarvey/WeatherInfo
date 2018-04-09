package hu.ait.android.weatherinfo.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.android.weatherinfo.Data.City;
import hu.ait.android.weatherinfo.MainActivity;
import hu.ait.android.weatherinfo.R;
import hu.ait.android.weatherinfo.WeatherDetailsActivity;

/**
 * Created by teagu_000 on 27/11/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCity;
        public Button btnDelete;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    private List<City> cityList;
    private Context context;
    private int lastPosition = -1;

    public CityAdapter(List<City> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.city_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvCity.setText(cityList.get(position).getName());

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CITY_NAME = "CITY_NAME";
                Intent intentStart = new Intent(context,
                        WeatherDetailsActivity.class);
                intentStart.putExtra(CITY_NAME, viewHolder.tvCity.getText());
                context.startActivity(intentStart);
            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void addItem(City city) {
        cityList.add(city);
        notifyDataSetChanged();
    }

    public void removeAll() {
        cityList.clear();
        notifyDataSetChanged();
    }

    public void updateItem(int index, City city) {
        cityList.set(index, city);
        notifyItemChanged(index);

    }

    public void removeItem(int index) {
        ((MainActivity) context).deleteItem(cityList.get(index));
        cityList.remove(index);
        notifyItemRemoved(index);
    }

    public void removeAllItems() {
        ((MainActivity)context).deleteAllItem();
        notifyItemRangeRemoved(0, cityList.size());
        cityList.clear();
    }


    public City getCity(int i) {
        return cityList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
