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

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.LoginActivity;
import fcul.cm.g20.ecopack.R;

import static fcul.cm.g20.ecopack.utils.Utils.showToast;

// TODO (OPTIMIZATION): RETRIEVE INFORMATION FROM DOCUMENT THAT IS LOADED ON LOGIN OR WHEN ENTERING THE MAP, IDK

public class ProfileFragment extends Fragment {
    private FirebaseFirestore database;
    private DocumentSnapshot userDocument;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileFragment = inflater.inflate(R.layout.fragment_profile, container, false);
        setupFragment(profileFragment);
        return profileFragment;
    }

    private void setupFragment(final View profileFragment) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);

        ImageButton settingsButton = profileFragment.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_profile, new ProfileSettingsFragment(userDocument))
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
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                            userDocument = task.getResult().getDocuments().get(0);

                            String picture = (String) userDocument.get("picture");
                            CircleImageView circleImageView = profileFragment.findViewById(R.id.profile_image);
                            if (picture.equals("N/A")) circleImageView.setImageResource(R.drawable.ic_user_empty);
                            else {
                                byte[] pictureArray = android.util.Base64.decode((String) userDocument.get("picture"), android.util.Base64.DEFAULT);
                                Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);
                                circleImageView.setImageBitmap(pictureBitmap);
                            }

                            TextView username = profileFragment.findViewById(R.id.profile_username);
                            username.setText((String) userDocument.get("username"));

                            TextView city = profileFragment.findViewById(R.id.profile_header_city);
                            city.setText((String) userDocument.get("city"));

                            getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.tab_content, new ProfileInfoFragment())
                                    .commit();
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
                else if (position == 1) fragment = new ProfileLocationsFragment(userDocument);
                else fragment = new ProfileOpinionsFragment(userDocument);

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