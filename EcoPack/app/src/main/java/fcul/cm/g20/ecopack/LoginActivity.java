package fcul.cm.g20.ecopack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import fcul.cm.g20.ecopack.fragments.login.LoginFragment;
import fcul.cm.g20.ecopack.fragments.login.SignUpFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginDialogStateListener, SignUpFragment.OnSignUpDialogStateListener, SignUpFragment.OnSignUpFragmentActiveListener {
    private boolean isLoginDialogOpen = false;
    private boolean isSignUpDialogOpen = false;
    public boolean isSignUpFragmentActive = false;
    private String loginUsername;
    private String loginPassword;
    private String signUpUsername;
    private String signUpPassword;
    private String signUpConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState != null) {
            isSignUpFragmentActive = savedInstanceState.getBoolean("isSignUpFragmentActive");
            loginUsername = savedInstanceState.getString("loginUsername");
            loginPassword = savedInstanceState.getString("loginPassword");
            signUpUsername = savedInstanceState.getString("signUpUsername");
            signUpPassword = savedInstanceState.getString("signUpPassword");
            signUpConfirmPassword = savedInstanceState.getString("signUpConfirmPassword");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_content, new LoginFragment(loginUsername, loginPassword, signUpUsername, signUpPassword, signUpConfirmPassword))
                .commit();
    }

    @Override
    public void onLoginDialogState(boolean isLoginDialogOpen) {
        this.isLoginDialogOpen = isLoginDialogOpen;
    }

    @Override
    public void onSignUpDialogState(boolean isSignUpDialogOpen) {
        this.isSignUpDialogOpen = isSignUpDialogOpen;
    }

    @Override
    public void onSignUpFragmentActive(boolean isSignUpFragmentActive) {
        this.isSignUpFragmentActive = isSignUpFragmentActive;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("isSignUpFragmentActive", isSignUpFragmentActive);
        outState.putString("loginUsername", loginUsername);
        outState.putString("loginPassword", loginPassword);
        outState.putString("signUpUsername", signUpUsername);
        outState.putString("signUpPassword", signUpPassword);
        outState.putString("signUpConfirmPassword", signUpConfirmPassword);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (!isLoginDialogOpen || !isSignUpDialogOpen) {
            isSignUpFragmentActive = false;
            super.onBackPressed();
        }
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public void setSignUpUsername(String signUpUsername) {
        this.signUpUsername = signUpUsername;
    }

    public void setSignUpPassword(String signUpPassword) {
        this.signUpPassword = signUpPassword;
    }

    public void setSignUpConfirmPassword(String signUpConfirmPassword) {
        this.signUpConfirmPassword = signUpConfirmPassword;
    }
}