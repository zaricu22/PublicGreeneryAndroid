package com.example.myapplication.fragments.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.fragments.services.gson.Hour;
import com.example.myapplication.fragments.services.gson.WeatherResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

// ZASEBNA KLASA PRAVILNIJE i PREGLEDNIJE
public class PrognozaTask extends AsyncTask<Void, Void, WeatherResult> {
    // slabe reference u slucaju da se sporedna nit izvrsava dugo GC moze nesmetano da obrise njene reference
    private WeakReference<Context> context;
    private WeakReference<View> view;


    public PrognozaTask(View view, Context context) {
        this.view = new WeakReference<View>(view);
        this.context = new WeakReference<Context>(context);
    }

    @Override   // Sporedna nit
    protected WeatherResult doInBackground(Void... voids) {
        try {
            // API zahteva 'kljuc' koji istice u nekom trenutku, besplatno je ali ga treba obnoviti
            URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?q=Novi%20Sad&units=metric&APPID=da4a295196c3545e6af06396366dd621");
            // HTTP bez SSL zahteva neka dodatna bezbedonosna podesavanja na novijim verzijama Androida
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (conn.getResponseCode() == 200) {
                InputStream responseBody = conn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                // GSON bibl za rad sa JSON
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                // Servis vremenske prognoze vraca nezgodno formatiran JSON koji pretvaramo u slozenu strukturu objekata
                // WeatherResult <- Hour[] <- Main & Weahter[], moze visak ali ako nesto fali nece se mapirati
                WeatherResult res = gson.fromJson(responseBodyReader, WeatherResult.class);
                responseBody.close();
                responseBodyReader.close();
                conn.disconnect();

                // Naknadno dobavljanje pravih ikonica na osnovu naziva ikonica
                // iz objekata mapiranog JSON-a i smestanje u iste
                URL url0 = new URL("https://openweathermap.org/img/wn/" + res.getList()[0].getWeather()[0].getIcon() + "@2x.png");
                InputStream is0 = (InputStream) url0.getContent();
                Bitmap bm0 = BitmapFactory.decodeStream(is0);
                res.getList()[0].getWeather()[0].setWeatherIcon(bm0);
                is0.close();
                URL url1 = new URL("https://openweathermap.org/img/wn/" + res.getList()[8].getWeather()[0].getIcon() + "@2x.png");
                InputStream is1 = (InputStream) url1.getContent();
                Bitmap bm1 = BitmapFactory.decodeStream(is1);
                res.getList()[8].getWeather()[0].setWeatherIcon(bm1);
                is1.close();
                URL url2 = new URL("https://openweathermap.org/img/wn/" + res.getList()[16].getWeather()[0].getIcon() + "@2x.png");
                InputStream is2 = (InputStream) url2.getContent();
                Bitmap bm2 = BitmapFactory.decodeStream(is2);
                res.getList()[16].getWeather()[0].setWeatherIcon(bm2);
                is2.close();

                return res;
            } else {
                System.out.println("Nesto nije uredu sa REST servisom za prognozu");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override   // Glavna UI nit
    protected void onPostExecute (WeatherResult res){
        super.onPostExecute(res);
        ArrayList<Hour> trodnevnaPrognoza = new ArrayList<Hour>();
        trodnevnaPrognoza.add(res.getList()[0]); // trenutna
        trodnevnaPrognoza.add(res.getList()[8]); // sutra u isto vreme
        trodnevnaPrognoza.add(res.getList()[16]); // prekosutra u isto vreme

        // Azuriramo UI
        RecyclerView recyclerView = this.view.get().findViewById(R.id.recycler_service);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context.get()));
        recyclerView.setAdapter(new WeatherAdapter(LayoutInflater.from(this.context.get()), trodnevnaPrognoza));
    }
}
