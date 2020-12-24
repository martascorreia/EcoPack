package fcul.cm.g20.ecopack.Models;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fcul.cm.g20.ecopack.utils.Utils;

public class User {
    private String userName;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String gender;
    private String birthday;
    private String city;
    private int visits;
    private int comments;
    private long register_date; // SHOULD BE LOCALDATE
    private Bitmap picture;
    private int points;
    private List<Prize> redeemedPrizes;

    //region Constructors
    public User(DocumentSnapshot snapshot) {

        //List<Prize> list = snapshot.get("redeemed_prizes_ids");

        this(
                (String) snapshot.get("username"),
                (String) snapshot.get("email"),
                (String) snapshot.get("password"),
                (String) snapshot.get("name"),
                (String) snapshot.get("phone"),
                (String) snapshot.get("gender"),
                (String) snapshot.get("birthday"),
                (String) snapshot.get("city"),
                getIntFrom(snapshot.get("visits")),
                getIntFrom(snapshot.get("comments")),
                getLongFrom(snapshot.get("register_date")),
                Utils.stringToBitmap((String) snapshot.get("points")),
                getIntFrom(snapshot.get("points")),
                new ArrayList<>()
        );
    }

    public User(String userName, String email, String password, String name, String phone, String gender, String birthday, String city, int visits, int comments, long register_date, Bitmap picture, int points, List<Prize> redeemedPrizes) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
        this.city = city;
        this.visits = visits;
        this.comments = comments;
        this.register_date = register_date;
        this.picture = picture;
        this.points = points;
        this.redeemedPrizes = redeemedPrizes;
    }
    //endregion

    public void addPoints(int value) {
        this.points += value;
    }

    public boolean buyPrize(Prize prizeModel) {
        int pointsLeft = this.points - prizeModel.getCost();
        boolean validPurchase = pointsLeft >= 0;
        if(validPurchase){
            this.points = pointsLeft;
            redeemedPrizes.add(prizeModel);
        }
        return validPurchase;
    }


    //region Static methods
    private static int getIntFrom(Object o){
        return (o != null)? (int)o : 0;
    }

    private static long getLongFrom(Object o){
        return (o != null)? (long)o : 0;
    }
    //endregion

    //region Getters

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCity() {
        return city;
    }

    public int getVisits() {
        return visits;
    }

    public int getComments() {
        return comments;
    }

    public long getRegister_date() {
        return register_date;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public int getPoints() {
        return points;
    }

    public List<Prize> getRedeemedPrizes() {
        return redeemedPrizes;
    }
    //endregion

}
