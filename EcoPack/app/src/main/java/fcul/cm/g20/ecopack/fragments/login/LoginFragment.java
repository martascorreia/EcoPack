package fcul.cm.g20.ecopack.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import fcul.cm.g20.ecopack.LoginActivity;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

import static fcul.cm.g20.ecopack.utils.Utils.isNetworkAvailable;
import static fcul.cm.g20.ecopack.utils.Utils.showToast;

public class LoginFragment extends Fragment {
    private LoginActivity loginActivity;
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();
        database = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        setupFragment(loginFragmentView);
        return loginFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (loginActivity.isSignUpFragmentActive) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.login_content, new SignUpFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void setupFragment(final View loginFragment) {
        final EditText[] inputs = new EditText[2];

        EditText loginUsernameEditText = loginFragment.findViewById(R.id.login_username);
        if (loginActivity.loginUsername != null) loginUsernameEditText.setText(loginActivity.loginUsername);
        loginUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginActivity.loginUsername = s.toString();
            }
        });
        inputs[0] = loginUsernameEditText;

        EditText loginPasswordEditText = loginFragment.findViewById(R.id.login_password);
        if (loginActivity.loginPassword != null) loginPasswordEditText.setText(loginActivity.loginPassword);
        loginPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginActivity.loginPassword = s.toString();
            }
        });
        inputs[1] = loginPasswordEditText;

        final Button loginButton = loginFragment.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);

                int empty = 0;
                for (EditText input : inputs) if (input.getText().toString().equals("")) empty++;

                if (empty != 0) showToast("Por favor, preencha todos os campos.", getContext());
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A iniciar sessão...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if (isNetworkAvailable(getContext())) {
                        database.collection("users")
                                .whereEqualTo("username", inputs[0].getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().getDocuments().size() != 0) {
                                                String password = (String) task.getResult().getDocuments().get(0).get("password");
                                                String inputPassword = inputs[1].getText().toString();

                                                if (Objects.requireNonNull(password).equals(inputPassword)) {
                                                    SharedPreferences preferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("username", inputs[0].getText().toString());
                                                    editor.putString("password", inputs[1].getText().toString());
                                                    editor.commit();

                                                    Intent mainIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                                    startActivity(mainIntent);
                                                    getActivity().finish();
                                                } else {
                                                    progressDialog.dismiss();
                                                    showToast("Password errada. Por favor, tente novamente.", getContext());
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                showToast("Não foi possível iniciar sessão. Não existem utilizadores com este username.", getContext());
                                            }
                                        } else {
                                            progressDialog.dismiss();
                                            showToast("Não foi possível iniciar sessão. Por favor, tente mais tarde.", getContext());
                                        }
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        showToast("Não foi possível iniciar sessão. Por favor, verifique a sua conexão à Internet.", getContext());
                    }
                }

                v.setEnabled(true);
            }
        });

        loginFragment.findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_content, new SignUpFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}