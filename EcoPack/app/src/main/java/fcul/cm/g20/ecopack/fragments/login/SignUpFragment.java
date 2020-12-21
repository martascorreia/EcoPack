package fcul.cm.g20.ecopack.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fcul.cm.g20.ecopack.LoginActivity;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

// TODO: PERCEBER SE VALE MESMO A PENA TER UMA VERSÃO PARA EMPRESA

public class SignUpFragment extends Fragment {
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View signUpFragment = inflater.inflate(R.layout.fragment_signup, container, false);
        setupFragment(signUpFragment);
        return signUpFragment;
    }

    private void setupFragment(final View signUpFragment) {
        signUpFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        final EditText[] inputs = new EditText[]{
                signUpFragment.findViewById(R.id.create_user_username),
                signUpFragment.findViewById(R.id.create_user_password),
                signUpFragment.findViewById(R.id.create_user_confirm_password)
        };

        final Button registerButton = signUpFragment.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                int empty = 0;
                for (EditText input : inputs) if (input.getText().toString().equals("")) empty++;

                if (empty != 0) showToast("Por favor, preencha todos os campos.");
                else if (!inputs[1].getText().toString().equals(inputs[2].getText().toString())) showToast("Passwords não coincidem. Por favor, tente novamente.");
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A registar utilizador...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    final Map<String, Object> user = new HashMap<>();
                    user.put("username", inputs[0].getText().toString());
                    user.put("password", inputs[1].getText().toString());
                    user.put("picture", "N/A");
                    user.put("name", "N/A");
                    user.put("email", "N/A");
                    user.put("phone", "N/A");
                    user.put("gender", "N/A");
                    user.put("birthday", "N/A");
                    user.put("city", "N/A");
                    user.put("visits", null);
                    user.put("comments", null);
                    user.put("register_date", System.currentTimeMillis());

                    if (isNetworkAvailable(getContext())) {
                        database.collection("users")
                                .whereEqualTo("username", inputs[0].getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().getDocuments().size() == 0) {
                                                database.collection("users")
                                                        .add(user)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                showToast("Utilizador criado com sucesso!");

                                                                SharedPreferences preferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = preferences.edit();
                                                                editor.putString("username", inputs[0].getText().toString());
                                                                editor.putString("password", inputs[1].getText().toString());
                                                                editor.commit();

                                                                Intent mainIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                                                startActivity(mainIntent);
                                                                getActivity().finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                showToast("Não foi possível registar o utilizador. Por favor, tente mais tarde.");
                                                            }
                                                        });
                                            } else {
                                                progressDialog.dismiss();
                                                showToast("Não foi possível registar o utilizador. Já existe um utilizador com este username.");
                                            }
                                        } else {
                                            progressDialog.dismiss();
                                            showToast("Não foi possível registar o utilizador. Por favor, tente mais tarde.");
                                        }
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        showToast("Não foi possível registar o utilizador. Por favor, verifique a sua conexão à Internet.");
                    }
                }

                v.setEnabled(true);
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