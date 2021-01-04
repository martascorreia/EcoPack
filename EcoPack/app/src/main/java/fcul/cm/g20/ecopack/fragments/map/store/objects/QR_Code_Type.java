package fcul.cm.g20.ecopack.fragments.map.store.objects;

import android.graphics.drawable.Drawable;

public class QR_Code_Type {

    private String title;
    private Drawable marker;

    public QR_Code_Type(String title, Drawable marker) {
        this.title = title;
        this.marker = marker;
    }

    public Drawable getMarker() {
        return marker;
    }

    public void setMarker(Drawable marker) {
        this.marker = marker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}