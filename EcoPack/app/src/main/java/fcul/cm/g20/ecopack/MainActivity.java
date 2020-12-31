package fcul.cm.g20.ecopack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: userVisits: ID DA LOJA, MARKER, DATA
// TODO: userComments: ID DO COMENTÁRIO (PRESENTE NUMA DADA LOJA), ID DO USER, ID DA LOJA, CONTEÚDO, DATA, MARKER

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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
        super.onSaveInstanceState(outState);
    }
}