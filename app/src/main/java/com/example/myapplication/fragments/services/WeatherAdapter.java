package com.example.myapplication.fragments.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.fragments.services.gson.Hour;
import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private LayoutInflater myInflater;
    private ArrayList<Hour> trodnevnaPrognoza;

    public WeatherAdapter(LayoutInflater inflater, ArrayList<Hour> prognoza) {
        this.myInflater = inflater;
        this.trodnevnaPrognoza = prognoza;
    }

    // Provide a reference to the views for each data item
    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView icon;
        TextView temp;
        TextView hum;
        TextView press;
        WeatherViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.weatherIcon);
            temp = itemView.findViewById(R.id.txtTemp);
            hum = itemView.findViewById(R.id.txtHumidity);
            press = itemView.findViewById(R.id.txtPressure);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // U layout item-a, visina ne sme biti match_parent jer se nece videti ostali!!!
        View view = myInflater.inflate(R.layout.recycler_service_row, parent, false);
        return new WeatherViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.icon.setImageBitmap(trodnevnaPrognoza.get(position).getWeather()[0].getWeatherIcon());
        holder.temp.setText(trodnevnaPrognoza.get(position).getMain().getTemp().intValue()+"°C");
        holder.hum.setText(trodnevnaPrognoza.get(position).getMain().getHumidity().toString()+"%");
        holder.press.setText(trodnevnaPrognoza.get(position).getMain().getPressure().toString()+"mb");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return trodnevnaPrognoza.size();
    }
}

