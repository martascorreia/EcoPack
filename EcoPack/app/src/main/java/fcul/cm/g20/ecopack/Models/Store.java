package fcul.cm.g20.ecopack.Models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fcul.cm.g20.ecopack.fragments.map.store.objects.Comment;
import fcul.cm.g20.ecopack.utils.Utils;

public class Store {
    private enum PackageTypes { bio, home, paper, plastic, reusable };
    private enum QRCodesTypes { bio, paper, plastic, reusable, home };

    // firebase path
    private String $Path;

    private String owner, address, email, name, phone, website;
    private List<Comment> comments;
    private Map<String, Integer> counters;
    private double lat, lng;
    private List<Bitmap> photos;
    private long register_date;
    private String schedule;   //TODO: REFACTOR TO OBJECT SCHEDULE
    private Map<String, Bitmap> qrCodes;

    public Store() {
        this.comments = new ArrayList<>();
        this.counters = new HashMap<>();
        this.photos = new ArrayList<>();
        this.qrCodes = new HashMap<>();
    }

    public Store(DocumentSnapshot snapshot) {
        this(
                snapshot.getReference().getPath(),
                (String) snapshot.get("address"),
                (String) snapshot.get("email"),
                (String) snapshot.get("name"),
                (String) snapshot.get("phone"),
                (String) snapshot.get("website"),
                (List<Map<String, Object>>) snapshot.get("comments"),
                (Map<String, Long>) snapshot.get("counters"),
                (double) snapshot.get("lat"),
                (double) snapshot.get("lng"),
                (List<String>)snapshot.get("photos"),
                (long) snapshot.get("register_date"),
                (String) snapshot.get("schedule"),
                (Map<String, String>)snapshot.get("qrCodes")
        );
    }

    public Store(String path, String address, String email, String name, String phone, String website, List<Map<String, Object>> comments, Map<String, Long> counters, double lat, double lng, List<String> photos, long register_date, String schedule, Map<String, String> qrCodes) {
        // When a setter is used is because the type saved in firebase is different from the one saved in "this"
        this.$Path = path;
        this.address = address;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.website = website;
        setComments(comments);
        this.counters = convertMapToInt(counters);
        this.lat = lat;
        this.lng = lng;
        setPhotos(photos);
        this.register_date = register_date;
        this.schedule = schedule;
        setQrCodes(qrCodes);
    }

    public int getPackageTypePoints(String type) {
        int result = 0;
        if (type.equals(PackageTypes.bio.toString())) {
            result = 4;
        } else if (type.equals(PackageTypes.home.toString())) {
            result = 3;
        } else if (type.equals(PackageTypes.paper.toString())) {
            result = 2;
        } else if (type.equals(PackageTypes.plastic.toString())) {
            result = 1;
        } else if (type.equals(PackageTypes.reusable.toString())) {
            result = 5;
        }

        return result;
    }

    public void incrementCounter(String type, int i) {
        int newValue = i;
        if(counters != null){
            if(counters.containsKey(type)){
                newValue = counters.get(type) + 1;
            }
        } else {
            counters = new HashMap<>();
        }
        counters.put(type, newValue);
    }

    @SuppressLint("NewApi")
    public void generateQrCodes(DocumentReference ref) {
        this.qrCodes = new HashMap<>();
        String storePath = ref.getPath();
        if(this.counters != null && !this.counters.isEmpty()){
            Arrays.stream(QRCodesTypes.values())
                    .forEach( qrType -> {
                        if(this.counters.containsKey(qrType.toString()) && this.counters.get(qrType.toString()) > 0){
                            // means the user choose this as type in there store
                            String type = qrType.toString();
                            String code = type + '\u0000' + storePath; //'\u0000' -> null Char
                            Bitmap myBitmap = QRCode.from(code).withColor(0xFFFFFFFF, pickQrCodeColour(qrType)).bitmap();
                            this.qrCodes.put(type, myBitmap);
                        }
                    });
        }
    }

    private int pickQrCodeColour(QRCodesTypes qrType){
        int result = 0xFFFFFFFF;
        switch (qrType){
            case bio:
                result = 0xFF9C693C;
                break;
            case paper:
                result = 0xFF547FCA;
                break;
            case plastic:
                result = 0xFFDAA948;
                break;
            case reusable:
                result = 0xFF66B16F;
                break;
            case home:
                result = 0xFFDA5D44;
                break;
            default:
                result = 0xFFFFFFFF;
                break;
        }
        return result;
    }

    @SuppressLint("NewApi")
    private Map<String, Integer> convertMapToInt(Map<String, Long> counters) {
        Map<String, Integer> temp = new HashMap<>();
        counters.forEach((k,v) -> temp.put(k, Math.toIntExact(v)));
        return temp;
    }

    public String getFirebasePath() {
        return this.$Path;
    }

    //region Setters For Firebase
    @SuppressLint("NewApi")
    public void setComments(List<Map<String, Object>> comments) {
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
    public void setPhotos(List<String> photos) {
        this.photos = (photos != null)?
                photos.stream().map(Utils::stringToBitmap).collect(Collectors.toList())
                :
                new ArrayList<>();
    }

    @SuppressLint("NewApi")
    public void setQrCodes(Map<String, String> qrCodes) {
        Map<String, Bitmap> result = new HashMap<>();
        qrCodes.forEach((qrCodeType, qrCodeString) -> result.put(qrCodeType, Utils.stringToBitmap(qrCodeString)));
        this.qrCodes = result;
    }

    //endregion

    //region Setters
    public void setPath(DocumentReference ref) {
        this.$Path = ref.getPath();
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setCommentsList(List<Comment> comments) {
        this.comments = (comments!=null)? comments : new ArrayList<>();
    }

    public void setCounters(Map<String, Integer> counters) {
        this.counters = (counters!=null)? counters : new HashMap<>();
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setRegister_date(long register_date) {
        this.register_date = register_date;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    //endregion

    //region Getters For Firebase
    public String getOwner() {
        return owner;
    }

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

    public double getLat() {
        return lat;
    }

    public double getLng() {
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
        return (s == null || s.isEmpty())? "N/A" : s;
    }
    //endregion
}
