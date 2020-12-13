package fcul.cm.g20.ecopack.ui.fragments.points.model;

import android.graphics.Bitmap;

public class Prize {
    private String title;
    private Bitmap image;
    private int cost;

    public Prize(String title, int cost , Bitmap image) {
        this.title = title;
        this.image = image;
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
