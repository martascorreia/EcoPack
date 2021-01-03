package fcul.cm.g20.ecopack.Models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.Comment;
import fcul.cm.g20.ecopack.utils.Utils;

public class Store {

    private enum CounterTypes { bio, home, paper, plastic, reusable };
    private enum QRCodesTypes { bio, paper, plastic, reusable };

    // firebase path
    private String $Path;

    private String address, email, name, phone, website;
    private List<Comment> comments;
    private Map<String, Integer> counters;
    private long lat, lng;
    private List<Bitmap> photos;
    private long register_date;
    private String schedule;   //TODO: REFACTOR TO OBJECT SCHEDULE
    private Map<String, Bitmap> qrCodes;

    public Store() {
    }

    public Store(DocumentSnapshot snapshot) {
        this(
                snapshot.getReference().getPath(),
                (String) snapshot.get("address"),
                (String) snapshot.get("email"),
                (String) snapshot.get("name"),
                (String) snapshot.get("phone"),
                (String) snapshot.get("website"),
                (List<Map<String, Object>>) snapshot.get("comments"),  //change
                (Map<String, Integer>) snapshot.get("counters"),
                (long) snapshot.get("lat"),
                (long) snapshot.get("lng"),
                (List<String>)snapshot.get("photos"),
                (long) snapshot.get("register_date"),
                (String) snapshot.get("schedule"),
                (Map<String, String>)snapshot.get("qrCodes")
                );
    }

    public Store(String path, String address, String email, String name, String phone, String website, List<Map<String, Object>> comments, Map<String, Integer> counters, long lat, long lng, List<String> photos, long register_date, String schedule, Map<String, String> qrCodes) {
        // When a setter is used is because the type saved in firebase is different from the one saved in "this"
        this.$Path = path;
        this.address = address;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.website = website;
        setComments(comments);
        this.counters = counters;
        this.lat = lat;
        this.lng = lng;
        setPhotos(photos);
        this.register_date = register_date;
        this.schedule = schedule;
        setQrCodes(qrCodes);
    }

    //region Setters For Firebase
    @SuppressLint("NewApi")
    private void setComments(List<Map<String, Object>> comments) {
        LinkedList<Comment> pojo_comments = new LinkedList<>();
        if (comments != null) {
            comments.forEach(commentMap ->
                    pojo_comments.add(
                            new Comment(
                                    commentMap.get("comment").toString(),
                                    commentMap.get("marker").toString(),
                                    commentMap.get("picture").toString(),
                                    commentMap.get("user").toString(),
                                    commentMap.get("date").toString(),
                                    commentMap.get("name").toString()
                            )
                    ));
        }
        this.comments = pojo_comments;
    }

    @SuppressLint("NewApi")
    private void setPhotos(List<String> photos) {
        this.photos = photos.stream().map(Utils::stringToBitmap).collect(Collectors.toList());
    }

    @SuppressLint("NewApi")
    public void setQrCodes(Map<String, String> qrCodes) {
        Map<String, Bitmap> result = new HashMap<>();
        qrCodes.forEach((qrCodeType, qrCodeString) -> result.put(qrCodeType, Utils.stringToBitmap(qrCodeString)));
        this.qrCodes = result;
    }

    public void setPath(DocumentReference ref) {
        this.$Path = ref.getPath();
    }
    //endregion

    //region Getters For Firebase
    public String getAddress() {
        return auxGetString(address);
    }

    public String getEmail() {
        return auxGetString(email);
    }

    public String getName() {
        return auxGetString(name);
    }

    public String getPhone() {
        return auxGetString(phone);
    }

    public String getWebsite() {
        return auxGetString(website);
    }

    @SuppressLint("NewApi")
    public List<Map<String, Object>> getComments() {
        ArrayList<Map<String, Object>> commentsMapList = new ArrayList<>();
        if ( this.comments != null) {
            this.comments.forEach(comment ->{
                        Map<String, Object> commentMap = new HashMap<>();
                        commentMap.put("user", comment.getUsername());
                        commentMap.put("name", comment.getName());
                        commentMap.put("picture", comment.getAvatar());
                        commentMap.put("date", comment.getDate());
                        commentMap.put("comment", comment.getComment());
                        commentMap.put("marker", comment.getMarkerIconId());
                        commentsMapList.add(commentMap);
                    });
        }
        return commentsMapList;
    }

    public Map<String, Integer> getCounters() {
        return counters;
    }

    public long getLat() {
        return lat;
    }

    public long getLng() {
        return lng;
    }

    @SuppressLint("NewApi")
    public List<String> getPhotos() {
        List<String> result = new ArrayList<>();
        if(photos!= null)
            result = photos.stream().map(Utils::bitmapToString).collect(Collectors.toList());
        return result;
    }

    public long getRegister_date() {
        return register_date;
    }

    public String getSchedule() {
        return auxGetString(schedule);
    }

    @SuppressLint("NewApi")
    public Map<String, String> getQrCodes() {
        Map<String, String> result = new HashMap<>();
        if(qrCodes!=null)
            qrCodes.forEach((qrCodeType, qrCodeBitmap) -> result.put(qrCodeType, Utils.bitmapToString(qrCodeBitmap)));
        return result;
    }

    private String auxGetString(String s){
        return (s == null)? "N/A" : s;
    }
    //endregion
}
