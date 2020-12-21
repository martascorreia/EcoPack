package fcul.cm.g20.ecopack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ApplicationState applicationState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (applicationState == null) applicationState = new ApplicationState();

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
        // SERIALIZE THE APPLICATION STATE OBJECT

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // DESERIALIZE THE APPLICATION STATE OBJECT

        super.onRestoreInstanceState(savedInstanceState);
    }

    public ApplicationState getApplicationState() {
        return applicationState;
    }
}