package fcul.cm.g20.ecopack;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import fcul.cm.g20.ecopack.Models.AppSession;
import fcul.cm.g20.ecopack.Models.StoreVisit;
import fcul.cm.g20.ecopack.fragments.points.PointsFragment;
import fcul.cm.g20.ecopack.fragments.points.PrizeCodeFragment;
import fcul.cm.g20.ecopack.fragments.profile.ProfileSettingsFragment;

// TODO: RESOLVER PROBLEMAS GRAVES DE NAVEGAÇÃO! (ON BACK PRESSED)

public class MainActivity extends AppCompatActivity implements ProfileSettingsFragment.OnProfileSettingsFragmentActiveListener {
    public boolean isProfileSettingsFragmentActive = false;

    AppSession appSession = AppSession.getInstance();

    public String userPicture;
    public String userUsername;
    public String userPassword;
    public String userName;
    public String userEmail;
    public String userPhone;
    public String userGender;
    public String userBirthday;
    public String userCity;
    public long userRegisterDate;
    public long userPoints;
    public ArrayList<String> userRedeemedPrizes = new ArrayList<>();
    public ArrayList<StoreVisit> userVisits = new ArrayList<>();   // ID DA LOJA, MARKER, DATA
    public ArrayList<HashMap<String, String>> userComments = new ArrayList<>(); // ID DO COMENTÁRIO (PRESENTE NUMA DADA LOJA), ID DO USER, ID DA LOJA, CONTEÚDO, DATA, MARKER
    public String userDocumentID;
    public String editPicture;
    public String editName;
    public String editEmail;
    public String editPhone;
    public String editGender;
    public String editBirthday;
    public String editCity;
    public String editPassword;
    public double visibleLocationLatitude;
    public double visibleLocationLongitude;
    public double createStoreLatitude;
    public double createStoreLongitude;
    public String createStoreAddress;
    public boolean[] createStoreOptions;
    public ArrayList<String> createStorePhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            isProfileSettingsFragmentActive = savedInstanceState.getBoolean("isProfileSettingsFragmentActive");
            userPicture = savedInstanceState.getString("userPicture");
            userUsername = savedInstanceState.getString("userUsername");
            userPassword = savedInstanceState.getString("userPassword");
            userName = savedInstanceState.getString("userName");
            userEmail = savedInstanceState.getString("userEmail");
            userPhone = savedInstanceState.getString("userPhone");
            userGender = savedInstanceState.getString("userGender");
            userBirthday = savedInstanceState.getString("userBirthday");
            userCity = savedInstanceState.getString("userCity");
            userRegisterDate = savedInstanceState.getLong("userRegisterDate");
            userPoints = savedInstanceState.getLong("userPoints");
            userRedeemedPrizes = (ArrayList<String>) savedInstanceState.getSerializable("userRedeemedPrizes");
            userVisits = (ArrayList<StoreVisit>) savedInstanceState.getSerializable("userVisits");
            userComments = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("userComments");
            userDocumentID = savedInstanceState.getString("userDocumentID");
            editPicture = savedInstanceState.getString("editPicture");
            editName = savedInstanceState.getString("editName");
            editEmail = savedInstanceState.getString("editEmail");
            editPhone = savedInstanceState.getString("editPhone");
            editGender = savedInstanceState.getString("editGender");
            editBirthday = savedInstanceState.getString("editBirthday");
            editCity = savedInstanceState.getString("editCity");
            editPassword = savedInstanceState.getString("editPassword");
            visibleLocationLatitude = savedInstanceState.getDouble("visibleLocationLatitude");
            visibleLocationLongitude = savedInstanceState.getDouble("visibleLocationLongitude");
            createStoreAddress = savedInstanceState.getString("createStoreAddress");
            createStoreLatitude = savedInstanceState.getDouble("createStoreLatitude");
            createStoreLongitude = savedInstanceState.getDouble("createStoreLongitude");
            createStoreOptions = savedInstanceState.getBooleanArray("createStoreOptions");
            createStorePhotos = savedInstanceState.getStringArrayList("createStorePhotos");
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_points,
                R.id.navigation_tree,
                R.id.navigation_map,
                R.id.navigation_profile
        ).build();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                // empty onBackPressed stack
                if(fm.getBackStackEntryCount() > 0) {
                    // get top frag
                    String prev = fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName();
                    switch (item.getItemId()) {
                        case R.id.navigation_map:
                            //empty everything
                            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }
                            break;
                        case R.id.navigation_points:
                            if(prev.equals("info") || prev.equals("tree")){
                                fm.popBackStack("tree", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            } else if (prev.equals("profile")) {
                                fm.popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                            fm.popBackStack("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            break;
                        case R.id.navigation_profile:
                            if(prev.equals("info") || prev.equals("tree")){
                                fm.popBackStack("tree", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            } else if (prev.equals("points")) {
                                fm.popBackStack("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                            fm.popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            break;
                        case R.id.navigation_tree:
                            if(prev.equals("profile")){
                                fm.popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            } else if (prev.equals("points")) {
                                fm.popBackStack("points", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                            fm.popBackStack("tree", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            break;
                    }
                }
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });

        getSupportActionBar().hide();
    }

    @Override
    public void onProfileSettingsFragmentActive(boolean isProfileSettingsFragmentActive) {
        this.isProfileSettingsFragmentActive = isProfileSettingsFragmentActive;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("isProfileSettingsFragmentActive", isProfileSettingsFragmentActive);
        outState.putString("userPicture", userPicture);
        outState.putString("userUsername", userUsername);
        outState.putString("userPassword", userPassword);
        outState.putString("userName", userName);
        outState.putString("userEmail", userEmail);
        outState.putString("userPhone", userPhone);
        outState.putString("userGender", userGender);
        outState.putString("userBirthday", userBirthday);
        outState.putString("userCity", userCity);
        outState.putLong("userRegisterDate", userRegisterDate);
        outState.putLong("userPoints", userPoints);
        outState.putSerializable("userRedeemedPrizes", userRedeemedPrizes);
        outState.putSerializable("userVisits", userVisits);
        outState.putSerializable("userComments", userComments);
        outState.putString("userDocumentID", userDocumentID);
        outState.putString("editPicture", editPicture);
        outState.putString("editName", editName);
        outState.putString("editEmail", editEmail);
        outState.putString("editPhone", editPhone);
        outState.putString("editGender", editGender);
        outState.putString("editBirthday", editBirthday);
        outState.putString("editCity", editCity);
        outState.putString("editPassword", editPassword);
        outState.putDouble("visibleLocationLatitude", visibleLocationLatitude);
        outState.putDouble("visibleLocationLongitude", visibleLocationLongitude);
        outState.putString("createStoreAddress", createStoreAddress);
        outState.putDouble("createStoreLatitude", createStoreLatitude);
        outState.putDouble("createStoreLongitude", createStoreLongitude);
        outState.putBooleanArray("createStoreOptions", createStoreOptions);
        outState.putStringArrayList("createStorePhotos", createStorePhotos);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        AppSession.getInstance().currentFragmentTag = new Stack<>();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        createStoreOptions = null;
        createStorePhotos = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() >0)
            Log.d("TAG123","Found fragment: " + fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName());

        super.onBackPressed();

//        if (!isProfileSettingsFragmentActive) super.onBackPressed();
//        else {
//            editPicture = null;
//            isProfileSettingsFragmentActive = false;
//            this
//                    .getSupportFragmentManager()
//                    .popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        }
    }
}