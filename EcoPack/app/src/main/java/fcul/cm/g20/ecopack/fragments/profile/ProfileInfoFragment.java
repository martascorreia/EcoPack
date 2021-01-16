package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

public class ProfileInfoFragment extends Fragment {
    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileInfoFragment = inflater.inflate(R.layout.fragment_profile_info, container, false);
        setupFragment(profileInfoFragment);
        return profileInfoFragment;
    }

    private void setupFragment(View profileInfoFragment) {
        TextView name = profileInfoFragment.findViewById(R.id.profile_info_name_text);
        name.setText(mainActivity.userName);

        TextView email = profileInfoFragment.findViewById(R.id.profile_info_email_text);
        email.setText(mainActivity.userEmail);

        TextView phone = profileInfoFragment.findViewById(R.id.profile_info_phone_text);
        phone.setText(mainActivity.userPhone);

        TextView gender = profileInfoFragment.findViewById(R.id.profile_info_gender_text);
        gender.setText(mainActivity.userGender);

        TextView birthday = profileInfoFragment.findViewById(R.id.profile_info_birthday_text);
        birthday.setText(mainActivity.userBirthday);

        TextView city = profileInfoFragment.findViewById(R.id.profile_info_city_text);
        city.setText(mainActivity.userCity);

        TextView visits = profileInfoFragment.findViewById(R.id.profile_info_visits_text);
        visits.setText(mainActivity.userVisits.size() + "");

        TextView comments = profileInfoFragment.findViewById(R.id.profile_info_comments_text);
        comments.setText(mainActivity.userComments.size() + "");

        TextView registerDate = profileInfoFragment.findViewById(R.id.profile_info_register_text);
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mainActivity.userRegisterDate);
        stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH));
        stringBuilder.append("-");
        stringBuilder.append(calendar.get(Calendar.MONTH));
        stringBuilder.append("-");
        stringBuilder.append(calendar.get(Calendar.YEAR));
        stringBuilder.append(" ");
        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
        stringBuilder.append(":");
        int minutes = calendar.get(Calendar.MINUTE);
        stringBuilder.append((minutes < 10) ? "0" + minutes : minutes + "");
        registerDate.setText(stringBuilder.toString());
    }
}