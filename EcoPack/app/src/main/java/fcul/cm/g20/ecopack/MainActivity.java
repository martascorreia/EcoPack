package fcul.cm.g20.ecopack;

import android.os.Bundle;

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

import fcul.cm.g20.ecopack.fragments.profile.ProfileSettingsFragment;

// TODO: userVisits: ID DA LOJA, MARKER, DATA
// TODO: userComments: ID DO COMENTÁRIO (PRESENTE NUMA DADA LOJA), ID DO USER, ID DA LOJA, CONTEÚDO, DATA, MARKER

public class MainActivity extends AppCompatActivity implements ProfileSettingsFragment.OnProfileSettingsFragmentActiveListener {
    public boolean isProfileSettingsFragmentActive = false;

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
    public ArrayList<String> userRedeemedPrizes;
    public ArrayList<HashMap<String, String>> userVisits;
    public ArrayList<HashMap<String, String>> userComments;
    public String userDocumentID;

    public String editPicture;
    public String editName;
    public String editEmail;
    public String editPhone;
    public String editGender;
    public String editBirthday;
    public String editCity;
    public String editPassword;

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
            userVisits = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("userVisits");
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

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (isProfileSettingsFragmentActive) {
            editPicture = null;
            isProfileSettingsFragmentActive = false;
            this.getSupportFragmentManager().popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else super.onBackPressed();
    }
}