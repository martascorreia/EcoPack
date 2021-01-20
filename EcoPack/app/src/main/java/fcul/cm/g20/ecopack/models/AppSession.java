package fcul.cm.g20.ecopack.models;

import java.util.HashMap;
import java.util.Stack;

public class AppSession {

    // User
    public User currentUser;

    public double visibleLocationLatitude;
    public double visibleLocationLongitude;

    // Map
    public HashMap<String, Store> storesMap;

    public Stack<String> currentFragmentTag = new Stack<>();



    // Prizes







    // ???
    //public double createStoreLatitude;
    //public double createStoreLongitude;
    //public String createStoreAddress;
    //public boolean[] createStoreOptions;
    //public ArrayList<String> createStorePhotos = new ArrayList<>();

    //region Singleton pattern
    private static volatile AppSession instance;

    private AppSession() { }

    public static AppSession getInstance() {
        // Lazy initialization
        // thread safe
        if(instance  == null) {
            synchronized (AppSession.class) {
                if (instance == null)
                    instance = new AppSession();
            }
        }
        return instance;
    }
    //endregion
}
