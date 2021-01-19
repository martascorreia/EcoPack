package fcul.cm.g20.ecopack.fragments.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

import static fcul.cm.g20.ecopack.utils.Utils.isNetworkAvailable;
import static fcul.cm.g20.ecopack.utils.Utils.showToast;

public class ProfileSettingsFragment extends Fragment {
    public interface OnProfileSettingsFragmentActiveListener {
        void onProfileSettingsFragmentActive(boolean isProfileSettingsFragmentActive);
    }

    private OnProfileSettingsFragmentActiveListener onProfileSettingsFragmentActiveListener;
    private MainActivity mainActivity;
    private FirebaseFirestore database;
    private CircleImageView circleImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = FirebaseFirestore.getInstance();
        onProfileSettingsFragmentActiveListener.onProfileSettingsFragmentActive(true);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileSettingsFragment = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        setupFragment(profileSettingsFragment);
        return profileSettingsFragment;
    }

    private void setupFragment(View profileSettingsFragment) {
        circleImageView = profileSettingsFragment.findViewById(R.id.change_profile_image);

        if (mainActivity.editPicture == null || mainActivity.editPicture.length() == 0) {
            if (mainActivity.userPicture.equals("N/A")) circleImageView.setImageResource(R.drawable.ic_user_empty);
            else {
                byte[] pictureArray = android.util.Base64.decode(mainActivity.userPicture, android.util.Base64.DEFAULT);
                circleImageView.setImageBitmap(BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length));
            }
        } else {
            byte[] pictureArray = android.util.Base64.decode(mainActivity.editPicture, android.util.Base64.DEFAULT);
            circleImageView.setImageBitmap(BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length));
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) loadImageFromGallery();
                    } else loadImageFromGallery();
                } else loadImageFromGallery();
            }
        });

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

        profileSettingsFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.editPicture = null;
                onProfileSettingsFragmentActiveListener.onProfileSettingsFragmentActive(false);
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        profileSettingsFragment.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setMessage("A atualizar perfil...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                final Map<String, Object> user = new HashMap<>();
                user.put("name", inputs[0].getText().toString());
                user.put("email", inputs[1].getText().toString());
                user.put("phone", inputs[2].getText().toString());
                user.put("gender", inputs[3].getText().toString());
                user.put("birthday", inputs[4].getText().toString());
                user.put("city", inputs[5].getText().toString());
                user.put("password", inputs[6].getText().toString());
                if (mainActivity.editPicture != null && mainActivity.editPicture.length() != 0) user.put("picture", mainActivity.editPicture);

                if (isNetworkAvailable(getContext())) {
                    database.document("users/" + mainActivity.userDocumentID)
                            .update(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    SharedPreferences preferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("password", inputs[6].getText().toString());
                                    editor.commit();

                                    progressDialog.dismiss();
                                    showToast("Perfil atualizado com sucesso!", getContext());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    showToast("Não foi possível atualizar o perfil. Por favor, tente mais tarde.", getContext());
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    showToast("Não foi possível atualizar o perfil. Por favor, verifique a sua conexão à Internet.", getContext());
                }
            }
        });
    }

    private void loadImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                Cursor selectedCursor = mainActivity.getContentResolver().query(selectedImage, null, null, null, null);
                int sizeIndex = selectedCursor.getColumnIndex(OpenableColumns.SIZE);
                selectedCursor.moveToFirst();
                long imageSize = selectedCursor.getLong(sizeIndex);

                if (imageSize > 200000) {
                    showToast("A imagem que está a tentar carregar é demasiado grande.", getContext());
                    return;
                }

                int length = 0;
                InputStream inputStream = getContext().getContentResolver().openInputStream(selectedImage);
                byte[] buffer = new byte[(int) imageSize];
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                while ((length = Objects.requireNonNull(inputStream).read(buffer)) != -1) byteBuffer.write(buffer, 0, length);
                mainActivity.editPicture = android.util.Base64.encodeToString(byteBuffer.toByteArray(), android.util.Base64.DEFAULT);
                circleImageView.setImageBitmap(BitmapFactory.decodeByteArray(buffer, 0, buffer.length));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onProfileSettingsFragmentActiveListener = (OnProfileSettingsFragmentActiveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onProfileSettingsFragmentActiveListener = null;
    }
}