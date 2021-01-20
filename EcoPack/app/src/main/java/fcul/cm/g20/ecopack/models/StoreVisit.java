package fcul.cm.g20.ecopack.models;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StoreVisit implements Serializable {
    private String storeId;
    private String storeName;
    private MarkerTypes markerTag;
    private long date;

    public StoreVisit(String storeId, String storeName, MarkerTypes markerTag, long date) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.markerTag = markerTag;
        this.date = date;
    }

    public StoreVisit(HashMap<String, Object> map) {
        this.storeId = (String) map.get("storeId");
        this.storeName = (String) map.get("storeName");
        this.markerTag = convertToMarkerType((String) map.get("markerTag"));
        this.date = (long) map.get("date");
    }

    @SuppressLint("NewApi")
    private MarkerTypes convertToMarkerType(String markerTag) {
        return Arrays.stream(MarkerTypes.values())
                                .filter(type -> type.toString().equals(markerTag))
                                .findAny()
                                .orElse(null);
    }

    @SuppressLint("NewApi")
    public static ArrayList<StoreVisit> toVisitsList(ArrayList<HashMap<String, Object>> visits) {
        ArrayList<StoreVisit> result = new ArrayList<>();
        visits.forEach(map -> result.add(new StoreVisit(map)));
        return result;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public MarkerTypes getMarkerTag() {
        return markerTag;
    }

    public void setMarkerTag(MarkerTypes markerTag) {
        this.markerTag = markerTag;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
