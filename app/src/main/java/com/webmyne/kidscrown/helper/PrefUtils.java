package com.webmyne.kidscrown.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.net.URL;

public class PrefUtils {

    public static Typeface getNexaBold(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "fonts/NexaBold.otf");
        return tf;
    }

    public static Typeface getNexaLight(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "fonts/NexaLight.otf");
        return tf;
    }

    public static String returnBas64Image(Bitmap thumbnail) {
        //complete code to save image on server
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bmp = null;
        try {
            URL url = new URL(path);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmp;
    }
}
