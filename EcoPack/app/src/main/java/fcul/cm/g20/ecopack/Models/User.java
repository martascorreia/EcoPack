package fcul.cm.g20.ecopack.Models;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fcul.cm.g20.ecopack.utils.Utils;

public class User {
    public static final int SCANNER_BLOCK_TIME = 10 * 1000;
    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String gender;
    private String birthday;
    private String city;
    private ArrayList<StoreVisit> visits;
    private ArrayList<HashMap<String, String>> comments;
    private long register_date;
    private Bitmap picture;
    private int points;
    private ArrayList<String> redeemedPrizesIds;

    // firebase
    private final String fireBasePath;

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
                StoreVisit.toVisitsList((ArrayList<HashMap<String, Object>>) snapshot.get("visits")),
                (ArrayList<HashMap<String, String>>) snapshot.get("comments"),
                getLongFrom(snapshot.get("register_date")),
                Utils.stringToBitmap((String) snapshot.get("picture")),
                getIntFrom(snapshot.get("points")),
                (ArrayList<String>) snapshot.get("redeemed_prizes_ids"),
                snapshot.getReference().getPath()
        );
    }

    public User(String username, String email, String password, String name, String phone, String gender, String birthday, String city, ArrayList<StoreVisit> visits, ArrayList<HashMap<String, String>> comments, long register_date, Bitmap picture, int points, ArrayList<String> redeemedPrizesIds, String fireBasePath) {
        this.username = username;
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
        this.redeemedPrizesIds = redeemedPrizesIds;
        this.fireBasePath = fireBasePath;
    }
    //endregion

    public void addPoints(int value) {
        this.points += value;
    }

    public boolean buyPrize(Prize prizeModel) {
        int pointsLeft = this.points - prizeModel.getCost();
        boolean validPurchase = pointsLeft >= 0;
        if (validPurchase) {
            this.points = pointsLeft;
            if(redeemedPrizesIds == null)
                redeemedPrizesIds = new ArrayList<>();
            redeemedPrizesIds.add(prizeModel.getTitle()); //CHANGE TO ID!!!!
        }
        return validPurchase;
    }

    public void addVisit(StoreVisit visit) {
        this.visits.add(visit);
    }

    public boolean canReceivePoints() {
        boolean canReceive = true;
        if(this.visits != null && !this.visits.isEmpty()){
            long blockedTime = SCANNER_BLOCK_TIME;  // Milliseconds
            // get last visit
            StoreVisit lastVisit = visits.get(visits.size()-1);
            Log.d("name123", (System.currentTimeMillis() - lastVisit.getDate() > blockedTime)+"");
            canReceive = System.currentTimeMillis() - lastVisit.getDate() > blockedTime;
        }
        return canReceive;
    }

    @SuppressLint("NewApi")
    public Map<String, Object> getHashMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("username", (this.username == null) ? "N/A" : this.username);
        result.put("email", (this.email == null) ? "N/A" : this.email);
        result.put("password", (this.password == null) ? "N/A" : this.password);
        result.put("name", (this.name == null) ? "N/A" : this.name);
        result.put("phone", (this.phone == null) ? "N/A" : this.phone);
        result.put("gender", (this.gender == null) ? "N/A" : this.gender);
        result.put("birthday", (this.birthday == null) ? "N/A" : this.birthday);
        result.put("city", (this.city == null) ? "N/A" : this.city);
        result.put("visits", this.visits);
        result.put("comments", this.comments);
        result.put("register_date", this.register_date);
        result.put("picture", (this.picture == null) ? "N/A" : Utils.bitmapToString(this.picture));
        result.put("points", this.points);
        result.put("redeemed_prizes_ids", this.redeemedPrizesIds);
        return result;
    }

    //region Static methods
    private static int getIntFrom(Object o) {
        int result = 0;
        if (o != null) {
            if (o instanceof Integer)
                result = (int) o;
            else if (o instanceof Long) {
                result = ((Long) o).intValue();
            }
        }
        return result;
    }

    private static long getLongFrom(Object o) {
        return (o != null) ? (long) o : 0;
    }
    //endregion

    //region Getters

    public String getUserName() {
        return username;
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

    public ArrayList<StoreVisit> getVisits() {
        return visits;
    }

    public ArrayList<HashMap<String, String>> getComments() {
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

    public List<String> getRedeemedPrizesIds() {
        return redeemedPrizesIds;
    }

    public String getFireBasePath() {
        return fireBasePath;
    }
    //endregion

}
