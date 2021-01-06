package fcul.cm.g20.ecopack.fragments.map.store.objects;

import android.graphics.drawable.Drawable;

public class Comment {

    private String avatar, comment, username, date, name;
    private String markerIconId; // Sorry Marta I needed this to be able to create Comment from the Mappers
    private Drawable marker;

    public Comment(String comment, Drawable marker, String uimg, String user, String date, String name) {
        this.comment = comment;
        this.username = user;
        this.date = date;
        this.avatar = uimg;
        this.marker = marker;
        this.name = name;
    }

    public Comment(String comment, String markerIconId, String uimg, String user, String date, String name) {
        // Sorry Marta I needed this to be able to create Comment from the Mappers
        this.comment = comment;
        this.username = user;
        this.date = date;
        this.avatar = uimg;
        this.markerIconId = markerIconId;
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String uimg) {
        this.avatar = uimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String uname) {
        this.username = uname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String timestamp) {
        this.date = timestamp;
    }

    public String getMarkerIconId(){
        // Sorry Marta I needed this to be able to create Comment from the Mappers
        return markerIconId;
    }

    public Drawable getMarker(){
        return marker;
    }

    public void setMarker(Drawable marker){
        this.marker = marker;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
