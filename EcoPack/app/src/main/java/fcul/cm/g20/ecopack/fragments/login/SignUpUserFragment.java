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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fcul.cm.g20.ecopack.LoginActivity;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

public class SignUpUserFragment extends Fragment {
    private final LoginActivity loginActivity;
    private final FirebaseFirestore database;

    public SignUpUserFragment(LoginActivity loginActivity, FirebaseFirestore database) {
        this.loginActivity = loginActivity;
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View signUpFragment = inflater.inflate(R.layout.fragment_signup_user, container, false);

        final EditText[] inputs = new EditText[]{
                signUpFragment.findViewById(R.id.sign_up_username),
                signUpFragment.findViewById(R.id.sign_up_name),
                signUpFragment.findViewById(R.id.sign_up_email),
                signUpFragment.findViewById(R.id.sign_up_password),
                signUpFragment.findViewById(R.id.sign_up_confirm_password)
        };

        // TODO: MANAGE TO CATCH THE CONNECTIVITY EXCEPTION
        // TODO (CONT.): PROBLEM!!! QUANDO NÃO HÁ INTERNET, ELE CRIA NA DATABASE A ENTRADA ASSIM QUE VOLTA HAVER CONEXÃO. TEMOS MESMO QUE VERIFICAR CONEXÃO!
        // https://stackoverflow.com/questions/50543290/how-to-catch-error-in-firestore-when-no-internet
        // TODO (CONT.): SE O UTILIZADOR SAIR DO FRAGMENTO E RETOMAR A CONEXÃO, É LANÇADA A EXCEÇÃO DE "NOT ATTACHED TO ACTIVITY"!
        signUpFragment.findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);

                int empty = 0;
                for (EditText input : inputs) if (input.getText().toString().equals("")) empty++;

                // NOTE: IT HAS TO BE IF-ELSE! (IF NOT, AN EXCEPTION IS THROWN FOR SOME STUPID REASON)
                if (empty != 0) showToast("Por favor, preencha todos os campos.");
                else if (!inputs[3].getText().toString().equals(inputs[4].getText().toString())) showToast("Passwords não coincidem.");
                else {
                    database.collection("users")
                            .whereEqualTo("username", inputs[0].getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful())
                                        if (task.getResult().getDocuments().size() == 0) createUser(inputs);
                                        else showToast("Já existe um utilizador com este username.");
                                    else showToast(Objects.requireNonNull(task.getException()).toString());
                                }
                            });
                }

                v.setClickable(true);
            }
        });

        return signUpFragment;
    }

    // TODO: REPLACE FOR SNACKBAR
    // TODO: ADICIONAR SPINNER ENQUNTO ESTÁ A PROCESSAR A CHAMADA
    private void showToast(String message) {
        Toast.makeText(loginActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void createUser(EditText[] inputs) {
        Map<String, Object> user = new HashMap<>();
        user.put("picture", "");
        user.put("username", inputs[0].getText().toString());
        user.put("name", inputs[1].getText().toString());
        user.put("city", "");
        user.put("birthday", "");
        user.put("gender", "");
        user.put("email", inputs[2].getText().toString());
        // TODO: ENCRYPT PASSWORD
        user.put("password", inputs[3].getText().toString());
        user.put("register_date", System.currentTimeMillis());

        database.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showToast("Utilizador criado com sucesso");

                        // TODO: GUARDAR SESSÃO NO TELEMÓVEL

                        startActivity(new Intent(loginActivity.getApplicationContext(), MainActivity.class));
                        loginActivity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.toString());
                    }
                });
    }
}