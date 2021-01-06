package fcul.cm.g20.ecopack.Models;

public class AppSession {

    private User currentUser;

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
