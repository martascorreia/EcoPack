package fcul.cm.g20.ecopack.fragments.map.store.adapter;

import android.widget.ImageView;

import java.sql.Timestamp;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment {

    private String avatar, marker, comment, username, date, name;

    public Comment(String comment, String marker, String uimg, String user, String date, String name) {
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

    public String getMarker(){
        return marker;
    }

    public void setMarker(String marker){
        this.marker = marker;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
