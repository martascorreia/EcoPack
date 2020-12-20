package fcul.cm.g20.ecopack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fcul.cm.g20.ecopack.fragments.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().replace(R.id.login_content, new LoginFragment()).commit();
    }
}
