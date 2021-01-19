package fcul.cm.g20.ecopack.Models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fcul.cm.g20.ecopack.utils.Utils;

public class Prize {
    private String title;
    private Bitmap image;
    private int cost;
    private boolean isDisable;
    Random random;

    @SuppressLint("NewApi")
    public Prize(DocumentSnapshot snapshot) {
        this(
                (String) snapshot.get("title"),
                Math.toIntExact((long) snapshot.get("cost")),  // não sei porque que isto vem como um long ??
                Utils.stringToBitmap((String)snapshot.get("image"))
        );
    }

    public Prize(String title, int cost , Bitmap image) {
        this.title = title;
        this.image = image;
        this.cost = cost;
        this.isDisable = false;
        random = new Random();
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

    public Map<String, Object> getHashMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("cost", cost);
        result.put("image", Utils.bitmapToString(image));

        return result;
    }

    public Map<String, String> generateCode() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "\n" + random.nextInt(10000) + "\n");
        map.put("company", "Company: " + this.title);
        map.put("date", "ValidationDate: 01/02/21");
        return map;
    }

    public boolean isDisabled() {
        return this.isDisable;
    }

    public void setIsDisable(boolean value) {
        this.isDisable = value;
    }
}
