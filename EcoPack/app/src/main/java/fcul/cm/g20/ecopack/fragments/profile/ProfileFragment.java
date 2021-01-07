package fcul.cm.g20.ecopack.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.LoginActivity;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.Models.StoreVisit;
import fcul.cm.g20.ecopack.R;

import static fcul.cm.g20.ecopack.utils.Utils.showToast;

public class ProfileFragment extends Fragment {
    private MainActivity mainActivity;
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileFragment = inflater.inflate(R.layout.fragment_profile, container, false);
        setupFragment(profileFragment);
        return profileFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // IMPORTANTE!
        mainActivity.createStoreLatitude = 0.0;
        mainActivity.createStoreLongitude = 0.0;
        mainActivity.createStoreAddress = null;
        mainActivity.createStoreOptions = null;
        mainActivity.createStorePhotos = new ArrayList<>();

        if (mainActivity.isProfileSettingsFragmentActive) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_profile, new ProfileSettingsFragment())
                    .addToBackStack("profile")
                    .commit();
        }
    }

    private void setupFragment(final View profileFragment) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);

        ImageButton settingsButton = profileFragment.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.editName = mainActivity.userName;
                mainActivity.editEmail = mainActivity.userEmail;
                mainActivity.editPhone = mainActivity.userPhone;
                mainActivity.editGender = mainActivity.userGender;
                mainActivity.editBirthday = mainActivity.userBirthday;
                mainActivity.editCity = mainActivity.userCity;
                mainActivity.editPassword = mainActivity.userPassword;

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_profile, new ProfileSettingsFragment())
                        .addToBackStack("profile")
                        .commit();
            }
        });

        ImageButton logoutButton = profileFragment.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        TabLayout tabLayout = profileFragment.findViewById(R.id.layout_profile_tabs);
        tabLayout.setOnTabSelectedListener(handleTabItemClick());

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        database.collection("users")
                .whereEqualTo("username", sharedPreferences.getString("username", ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                            mainActivity.userPicture = (String) userDocument.get("picture");
                            mainActivity.userUsername = (String) userDocument.get("username");
                            mainActivity.userPassword = (String) userDocument.get("password");
                            mainActivity.userName = (String) userDocument.get("name");
                            mainActivity.userEmail = (String) userDocument.get("email");
                            mainActivity.userPhone = (String) userDocument.get("phone");
                            mainActivity.userGender = (String) userDocument.get("gender");
                            mainActivity.userBirthday = (String) userDocument.get("birthday");
                            mainActivity.userCity = (String) userDocument.get("city");
                            mainActivity.userRegisterDate = (long) userDocument.get("register_date");
                            mainActivity.userPoints = (long) userDocument.get("points");
                            mainActivity.userRedeemedPrizes = (ArrayList<String>) userDocument.get("redeemed_prizes");
                            mainActivity.userVisits = StoreVisit.toVisitsList((ArrayList<HashMap<String, Object>>) userDocument.get("visits"));
                            mainActivity.userComments = (ArrayList<HashMap<String, String>>) userDocument.get("comments");
                            mainActivity.userDocumentID = userDocument.getReference().getPath().split("/")[1];

                            String picture = mainActivity.userPicture;
                            CircleImageView circleImageView = profileFragment.findViewById(R.id.profile_image);
                            if (picture.equals("N/A")) circleImageView.setImageResource(R.drawable.ic_user_empty);
                            else {
                                byte[] pictureArray = android.util.Base64.decode(picture, android.util.Base64.DEFAULT);
                                Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);
                                circleImageView.setImageBitmap(pictureBitmap);
                            }

                            TextView username = profileFragment.findViewById(R.id.profile_username);
                            username.setText(mainActivity.userUsername);

                            TextView city = profileFragment.findViewById(R.id.profile_header_city);
                            city.setText(mainActivity.userCity);

                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.tab_content, new ProfileInfoFragment())
                                    .commit();

                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        } else {
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            showToast("Não foi possível obter a informação do utilizador. Por favor, tente mais tarde.", getContext());
                        }
                    }
                });
    }

    private TabLayout.OnTabSelectedListener handleTabItemClick() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;

                int position = tab.getPosition();
                if (position == 0) fragment = new ProfileInfoFragment();
                else if (position == 1) fragment = new ProfileLocationsFragment();
                else fragment = new ProfileCommentsFragment();

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.tab_content, fragment)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }
}