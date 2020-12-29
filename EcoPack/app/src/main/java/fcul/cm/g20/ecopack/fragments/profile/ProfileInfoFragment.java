package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import fcul.cm.g20.ecopack.R;

// TODO: HANDLE VISITS/COMMENTS HASHMAP

public class ProfileInfoFragment extends Fragment {
    DocumentSnapshot userDocument;

    public ProfileInfoFragment(){};

    public ProfileInfoFragment(DocumentSnapshot userDocument) {
        this.userDocument = userDocument;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileInfoFragment = inflater.inflate(R.layout.fragment_profile_info, container, false);
        setupFragment(profileInfoFragment);
        return profileInfoFragment;
    }

    private void setupFragment(View profileInfoFragment) {
        TextView name = profileInfoFragment.findViewById(R.id.profile_info_name_text);
        name.setText((String) userDocument.get("name"));

        TextView email = profileInfoFragment.findViewById(R.id.profile_info_email_text);
        email.setText((String) userDocument.get("email"));

        TextView phone = profileInfoFragment.findViewById(R.id.profile_info_phone_text);
        phone.setText((String) userDocument.get("phone"));

        TextView gender = profileInfoFragment.findViewById(R.id.profile_info_gender_text);
        gender.setText((String) userDocument.get("gender"));

        TextView birthday = profileInfoFragment.findViewById(R.id.profile_info_birthday_text);
        birthday.setText((String) userDocument.get("birthday"));

        TextView city = profileInfoFragment.findViewById(R.id.profile_info_city_text);
        city.setText((String) userDocument.get("city"));

        TextView visits = profileInfoFragment.findViewById(R.id.profile_info_visits_text);
        visits.setText("0");

        TextView comments = profileInfoFragment.findViewById(R.id.profile_info_comments_text);
        comments.setText("1");

        TextView registerDate = profileInfoFragment.findViewById(R.id.profile_info_register_text);
        registerDate.setText(String.valueOf((long) userDocument.get("register_date")));
    }
}