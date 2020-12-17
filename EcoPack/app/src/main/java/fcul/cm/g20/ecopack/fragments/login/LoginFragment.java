package fcul.cm.g20.ecopack.fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import fcul.cm.g20.ecopack.LoginActivity;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

public class LoginFragment extends Fragment {
    private final LoginActivity loginActivity;
    private final FirebaseFirestore database;

    public LoginFragment(LoginActivity loginActivity, FirebaseFirestore database) {
        this.loginActivity = loginActivity;
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText[] inputs = new EditText[]{
                loginFragmentView.findViewById(R.id.login_username),
                loginFragmentView.findViewById(R.id.login_password),
        };

        // TODO: MANAGE TO CATCH THE CONNECTIVITY EXCEPTION
        loginFragmentView.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);

                int empty = 0;
                for (EditText input : inputs) if (input.getText().toString().equals("")) empty++;

                // NOTE: IT HAS TO BE IF-ELSE! (IF NOT, AN EXCEPTION IS THROWN FOR SOME STUPID REASON)
                if (empty != 0) showToast("Por favor, preencha todos os campos.");
                else {
                    try {

                        database.collection("users")
                                .whereEqualTo("username", inputs[0].getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful())
                                            if (task.getResult().getDocuments().size() != 0) handleUser(task.getResult().getDocuments().get(0), inputs, v);
                                            else showToast("Não existe nenhum utilizador com este username.");
                                        else showToast(Objects.requireNonNull(task.getException()).toString());
                                    }
                                });
                    } catch (Exception e) {
                        showToast(e.getMessage());
                    }
                }

                v.setClickable(true);
            }
        });

        loginFragmentView.findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpUserFragment signUpUserFragment = new SignUpUserFragment(loginActivity, database);
                loginActivity.getSupportFragmentManager().beginTransaction().replace(R.id.login_content, signUpUserFragment).addToBackStack(null).commit();
            }
        });

        return loginFragmentView;
    }

    private void handleUser(@NonNull DocumentSnapshot user, EditText[] inputs, View v) {
        // TODO: DESENCRIPTAR PASSWORD
        String password = user.getString("password");
        String inputPassword = inputs[1].getText().toString();

        if (Objects.requireNonNull(password).equals(inputPassword)) {
            // TODO: CREATE SESSION

            startActivity(new Intent(loginActivity.getApplicationContext(), MainActivity.class));
            loginActivity.finish();
        } else showToast("Password errada. Tente novamente.");
    }

    // TODO: REPLACE FOR SNACKBAR
    // TODO: ADICIONAR SPINNER ENQUNTO ESTÁ A PROCESSAR A CHAMADA
    private void showToast(String message) {
        Toast.makeText(loginActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}