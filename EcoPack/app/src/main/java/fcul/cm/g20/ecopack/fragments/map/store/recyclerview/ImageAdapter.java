package fcul.cm.g20.ecopack.fragments.map.store.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Map;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private Map<String, Object> images;

    public ImageAdapter(Context context, Map<String, Object> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        byte[] photo = android.util.Base64.decode((String) images.get("photo" + position), android.util.Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}