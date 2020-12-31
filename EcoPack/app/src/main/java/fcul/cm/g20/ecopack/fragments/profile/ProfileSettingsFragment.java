package fcul.cm.g20.ecopack.fragments.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

// TODO: UPDATE USERNAME
// TODO: UPDATE PICTURE
// TODO: REMOVE PROFILE FROM STACK ON SUCCESSFUL UPDATE

public class ProfileSettingsFragment extends Fragment {
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileSettingsFragment = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        setupFragment(profileSettingsFragment);
        return profileSettingsFragment;
    }

    private void setupFragment(View profileSettingsFragment) {
        /*
        String picture = (String) userDocument.get("picture");
        CircleImageView circleImageView = profileSettingsFragment.findViewById(R.id.edit_user_picture);
        if (picture.equals("N/A")) circleImageView.setImageResource(R.drawable.ic_user_empty);
        else {
            byte[] pictureArray = android.util.Base64.decode((String) userDocument.get("picture"), android.util.Base64.DEFAULT);
            Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);
            circleImageView.setImageBitmap(pictureBitmap);
        }

        final EditText[] inputs = new EditText[7];

        EditText name = profileSettingsFragment.findViewById(R.id.edit_user_name);
        name.setText((String) userDocument.get("name"));
        inputs[0] = name;

        EditText email = profileSettingsFragment.findViewById(R.id.edit_user_email);
        email.setText((String) userDocument.get("email"));
        inputs[1] = email;

        EditText phone = profileSettingsFragment.findViewById(R.id.edit_user_phone);
        phone.setText((String) userDocument.get("phone"));
        inputs[2] = phone;

        EditText gender = profileSettingsFragment.findViewById(R.id.edit_user_gender);
        gender.setText((String) userDocument.get("gender"));
        inputs[3] = gender;

        EditText birthday = profileSettingsFragment.findViewById(R.id.edit_user_birthday);
        birthday.setText((String) userDocument.get("birthday"));
        inputs[4] = birthday;

        EditText city = profileSettingsFragment.findViewById(R.id.edit_user_city);
        city.setText((String) userDocument.get("city"));
        inputs[5] = city;

        EditText password = profileSettingsFragment.findViewById(R.id.edit_user_password);
        password.setText((String) userDocument.get("password"));
        inputs[6] = password;

        profileSettingsFragment.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setMessage("A atualizar perfil...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                final Map<String, Object> user = new HashMap<>();
                user.put("name", inputs[0].getText().toString());
                user.put("email", inputs[1].getText().toString());
                user.put("phone", inputs[2].getText().toString());
                user.put("gender", inputs[3].getText().toString());
                user.put("birthday", inputs[4].getText().toString());
                user.put("city", inputs[5].getText().toString());
                user.put("password", inputs[6].getText().toString());

                if (isNetworkAvailable(getContext())) {
                    database.document(userDocument.getReference().getPath())
                            .update(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    showToast("Perfil atualizado com sucesso!");

                                    SharedPreferences preferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("password", inputs[6].getText().toString());
                                    editor.commit();

                                    getActivity()
                                            .getSupportFragmentManager()
                                            .popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    showToast("Não foi possível atualizar o perfil. Por favor, tente mais tarde.");
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    showToast("Não foi possível atualizar o perfil. Por favor, verifique a sua conexão à Internet.");
                }
            }
        });

        profileSettingsFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
         */
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