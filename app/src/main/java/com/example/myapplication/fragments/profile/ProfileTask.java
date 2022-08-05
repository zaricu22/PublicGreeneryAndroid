package com.example.myapplication.fragments.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import com.example.myapplication.R;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class ProfileTask extends AsyncTask<Void, Void, Bitmap> {
    // slabe reference u slucaju da se sporedna nit izvrsava dugo GC moze nesmetano da obrise njene reference
    private WeakReference<View> view;

    public ProfileTask(View view) {
        this.view = new WeakReference<View>(view);
    }

    @Override   // Dobavljanje rezultata, izvrsava na sporednoj pozadinskoj niti
    protected Bitmap doInBackground(Void... voids) {
        try {
            // Radi sa oba HTTP/HTTPS, samo HTTP zahteva dodatna bezbednosna podesavanja na novijim Androidima
            // Novije verzije Androida podrazumevaju dozvolu za Internet
            URL url0 = new URL("https://icon-library.net//images/account-icon/account-icon-3.jpg");
            InputStream is0 = (InputStream) url0.getContent();
            Bitmap bmp0 = BitmapFactory.decodeStream(is0);
            if(bmp0 != null)
                System.out.println("USPESNAAAA");
            is0.close();
            return bmp0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override   // Obrada rezultata, izvrsava na glavnoj UI niti
    protected void onPostExecute(Bitmap bmp) {
        super.onPostExecute(bmp);
        Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,  128 ,128, true);
        ((ImageView)this.view.get().findViewById(R.id.imgKorisnik)).setImageBitmap(bmp);
    }
}