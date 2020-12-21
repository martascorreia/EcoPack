package fcul.cm.g20.ecopack.fragments.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

// TODO: OPTIMIZE BY PASSING THE WHOLE DOCUMENT IN THE INTENT

public class LoginFragment extends Fragment {
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        setupFragment(loginFragmentView);
        return loginFragmentView;
    }

    private void setupFragment(final View loginFragment) {
        final EditText[] inputs = new EditText[]{
                loginFragment.findViewById(R.id.login_username),
                loginFragment.findViewById(R.id.login_password),
        };

        final Button loginButton = loginFragment.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);

                int empty = 0;
                for (EditText input : inputs) if (input.getText().toString().equals("")) empty++;

                if (empty != 0) showToast("Por favor, preencha todos os campos.");
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A iniciar sessão...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
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
                                                } else showToast("Password errada. Por favor, tente novamente.");
                                            } else {
                                                progressDialog.dismiss();
                                                showToast("Não foi possível iniciar sessão. Não existem utilizadores com este username.");
                                            }
                                        } else {
                                            progressDialog.dismiss();
                                            showToast("Não foi possível iniciar sessão. Por favor, tente mais tarde.");
                                        }
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        showToast("Não foi possível iniciar sessão. Por favor, verifique a sua conexão à Internet.");
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

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) return false;
        else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info)
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) return true;
        }

        return false;
    }
}