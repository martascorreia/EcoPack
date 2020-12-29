package fcul.cm.g20.ecopack.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class Utils {
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getBitmapFromImageDrawable(Context context, int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    public static Bitmap stringToBitmap(String s){
        if (s == null || s.equals("N/A")) return null;
        else {
            byte[] array = android.util.Base64.decode( s, android.util.Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
            return bitmap;
        }
    }

    public static String bitmapToString(Bitmap b){
        if(b == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }

    public static void showToast(String message, Context ctx) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}
