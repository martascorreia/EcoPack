package fcul.cm.g20.ecopack.fragments.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

import static fcul.cm.g20.ecopack.utils.Utils.isNetworkAvailable;
import static fcul.cm.g20.ecopack.utils.Utils.showToast;

// TODO: UPDATE PICTURE

public class ProfileSettingsFragment extends Fragment {
    public interface OnEditProfileDialogStateListener {
        void onEditProfileDialogState(boolean isEditProfileDialogOpen);
    }

    public interface OnProfileSettingsFragmentActiveListener {
        void onProfileSettingsFragmentActive(boolean isProfileSettingsFragmentActive);
    }

    private OnEditProfileDialogStateListener onEditProfileDialogStateListener;
    private OnProfileSettingsFragmentActiveListener onProfileSettingsFragmentActiveListener;
    private MainActivity mainActivity;
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = FirebaseFirestore.getInstance();
        onProfileSettingsFragmentActiveListener.onProfileSettingsFragmentActive(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileSettingsFragment = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        setupFragment(profileSettingsFragment);
        return profileSettingsFragment;
    }

    private void setupFragment(View profileSettingsFragment) {
        profileSettingsFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.editPicture = null;
                mainActivity.editName = null;
                mainActivity.editEmail = null;
                mainActivity.editPhone = null;
                mainActivity.editGender = null;
                mainActivity.editBirthday = null;
                mainActivity.editCity = null;
                mainActivity.editPassword = null;
                onProfileSettingsFragmentActiveListener.onProfileSettingsFragmentActive(false);
                getActivity().getSupportFragmentManager().popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        /*
        String picture = mainActivity.userPicture;
        CircleImageView circleImageView = profileSettingsFragment.findViewById(R.id.change_profile_image);
        if (picture.equals("N/A")) circleImageView.setImageResource(R.drawable.ic_user_empty);
        else {
            byte[] pictureArray = android.util.Base64.decode(picture, android.util.Base64.DEFAULT);
            Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);
            circleImageView.setImageBitmap(pictureBitmap);
        }
         */

        final EditText[] inputs = new EditText[7];

        EditText editUsernameEditText = profileSettingsFragment.findViewById(R.id.edit_user_name);
        editUsernameEditText.setText(mainActivity.editName);
        editUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainActivity.editName = s.toString();
            }
        });
        inputs[0] = editUsernameEditText;

        EditText editEmailEditText = profileSettingsFragment.findViewById(R.id.edit_user_email);
        editEmailEditText.setText(mainActivity.editEmail);
        editEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainActivity.editEmail = s.toString();
            }
        });
        inputs[1] = editEmailEditText;

        EditText editPhoneEditText = profileSettingsFragment.findViewById(R.id.edit_user_phone);
        editPhoneEditText.setText(mainActivity.editPhone);
        editPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainActivity.editPhone = s.toString();
            }
        });
        inputs[2] = editPhoneEditText;

        EditText editGenderEditText = profileSettingsFragment.findViewById(R.id.edit_user_gender);
        editGenderEditText.setText(mainActivity.editGender);
        editGenderEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainActivity.editGender = s.toString();
            }
        });
        inputs[3] = editGenderEditText;

        EditText editBirthdayEditText = profileSettingsFragment.findViewById(R.id.edit_user_birthday);
        editBirthdayEditText.setText(mainActivity.editBirthday);
        editBirthdayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainActivity.editBirthday = s.toString();
            }
        });
        inputs[4] = editBirthdayEditText;

        EditText editCityEditText = profileSettingsFragment.findViewById(R.id.edit_user_city);
        editCityEditText.setText(mainActivity.editCity);
        editCityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainActivity.editCity = s.toString();
            }
        });
        inputs[5] = editCityEditText;

        EditText editPasswordEditText = profileSettingsFragment.findViewById(R.id.edit_user_password);
        editPasswordEditText.setText(mainActivity.editPassword);
        editPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainActivity.editPassword = s.toString();
            }
        });
        inputs[6] = editPasswordEditText;

        profileSettingsFragment.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setMessage("A atualizar perfil...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                onEditProfileDialogStateListener.onEditProfileDialogState(true);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                final Map<String, Object> user = new HashMap<>();
                user.put("name", inputs[0].getText().toString());
                user.put("email", inputs[1].getText().toString());
                user.put("phone", inputs[2].getText().toString());
                user.put("gender", inputs[3].getText().toString());
                user.put("birthday", inputs[4].getText().toString());
                user.put("city", inputs[5].getText().toString());
                user.put("password", inputs[6].getText().toString());

                if (isNetworkAvailable(getContext())) {
                    database.document(mainActivity.userDocumentID)
                            .update(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    SharedPreferences preferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("password", inputs[6].getText().toString());
                                    editor.commit();

                                    progressDialog.dismiss();
                                    onEditProfileDialogStateListener.onEditProfileDialogState(false);
                                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                    showToast("Perfil atualizado com sucesso!", getContext());

                                    onProfileSettingsFragmentActiveListener.onProfileSettingsFragmentActive(false);
                                    mainActivity.onBackPressed();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    onEditProfileDialogStateListener.onEditProfileDialogState(false);
                                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                    showToast("Não foi possível atualizar o perfil. Por favor, tente mais tarde.", getContext());
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    onEditProfileDialogStateListener.onEditProfileDialogState(false);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    showToast("Não foi possível atualizar o perfil. Por favor, verifique a sua conexão à Internet.", getContext());
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onEditProfileDialogStateListener = (OnEditProfileDialogStateListener) context;
            onProfileSettingsFragmentActiveListener = (OnProfileSettingsFragmentActiveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onEditProfileDialogStateListener = null;
        onProfileSettingsFragmentActiveListener = null;
    }
}