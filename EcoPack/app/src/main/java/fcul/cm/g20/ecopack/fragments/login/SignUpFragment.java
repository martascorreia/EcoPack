package fcul.cm.g20.ecopack.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import fcul.cm.g20.ecopack.LoginActivity;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.Models.StoreVisit;
import fcul.cm.g20.ecopack.R;

import static fcul.cm.g20.ecopack.utils.Utils.isNetworkAvailable;
import static fcul.cm.g20.ecopack.utils.Utils.showToast;

public class SignUpFragment extends Fragment {
    public interface OnSignUpFragmentActiveListener {
        void onSignUpFragmentActive(boolean isSignUpFragmentActive);
    }

    private OnSignUpFragmentActiveListener onSignUpFragmentActiveListener;
    private LoginActivity loginActivity;
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();
        database = FirebaseFirestore.getInstance();
        onSignUpFragmentActiveListener.onSignUpFragmentActive(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View signUpFragment = inflater.inflate(R.layout.fragment_login_signup, container, false);
        setupFragment(signUpFragment);
        return signUpFragment;
    }

    private void setupFragment(final View signUpFragment) {
        final EditText[] inputs = new EditText[3];

        EditText signUpUsernameEditText = signUpFragment.findViewById(R.id.create_user_username);
        if (loginActivity.signUpUsername != null) signUpUsernameEditText.setText(loginActivity.signUpUsername);
        signUpUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginActivity.signUpUsername = s.toString();
            }
        });
        inputs[0] = signUpUsernameEditText;

        EditText signUpPasswordEditText = signUpFragment.findViewById(R.id.create_user_password);
        if (loginActivity.signUpPassword != null) signUpPasswordEditText.setText(loginActivity.signUpPassword);
        signUpPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginActivity.signUpPassword = s.toString();
            }
        });
        inputs[1] = signUpPasswordEditText;

        EditText signUpConfirmPasswordEditText = signUpFragment.findViewById(R.id.create_user_confirm_password);
        if (loginActivity.signUpConfirmPassword != null) signUpConfirmPasswordEditText.setText(loginActivity.signUpConfirmPassword);
        signUpConfirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginActivity.signUpConfirmPassword = s.toString();
            }
        });
        inputs[2] = signUpConfirmPasswordEditText;

        final Button registerButton = signUpFragment.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                int empty = 0;
                for (EditText input : inputs) if (input.getText().toString().equals("")) empty++;

                if (empty != 0) showToast("Por favor, preencha todos os campos.", getContext());
                else if (!inputs[1].getText().toString().equals(inputs[2].getText().toString())) showToast("Passwords não coincidem. Por favor, tente novamente.", getContext());
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A registar utilizador...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
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
                    user.put("points", 0);
                    user.put("register_date", System.currentTimeMillis());
                    user.put("redeemed_prizes_ids", new ArrayList<String>());
                    user.put("visits", new ArrayList<StoreVisit>());
                    user.put("comments", new ArrayList<HashMap<String, Object>>());

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
                                                                showToast("Utilizador criado com sucesso!", getContext());

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
                                                                progressDialog.dismiss();
                                                                showToast("Não foi possível registar o utilizador. Por favor, tente mais tarde.", getContext());
                                                            }
                                                        });
                                            } else {
                                                progressDialog.dismiss();
                                                showToast("Não foi possível registar o utilizador. Já existe um utilizador com este username.", getContext());
                                            }
                                        } else {
                                            progressDialog.dismiss();
                                            showToast("Não foi possível registar o utilizador. Por favor, tente mais tarde.", getContext());
                                        }
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        showToast("Não foi possível registar o utilizador. Por favor, verifique a sua conexão à Internet.", getContext());
                    }
                }

                v.setEnabled(true);
            }
        });

        signUpFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpFragmentActiveListener.onSignUpFragmentActive(false);
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onSignUpFragmentActiveListener = (OnSignUpFragmentActiveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSignUpFragmentActiveListener = null;
    }
}