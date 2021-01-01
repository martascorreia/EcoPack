package fcul.cm.g20.ecopack.fragments.map.store.recyclerview;

import android.graphics.drawable.Drawable;

public class Comment {

    private String avatar, comment, username, date, name;
    private Drawable marker;

    public Comment(String comment, Drawable marker, String uimg, String user, String date, String name) {
        this.comment = comment;
        this.username = user;
        this.date = date;
        this.avatar = uimg;
        this.marker = marker;
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
