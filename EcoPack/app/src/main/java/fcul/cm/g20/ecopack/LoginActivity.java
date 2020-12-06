package fcul.cm.g20.ecopack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import fcul.cm.g20.ecopack.ui.fragments.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        LoginFragment loginFragment = new LoginFragment(this, database);
        getSupportFragmentManager().beginTransaction().replace(R.id.login_content, loginFragment).commit();
    }
}
