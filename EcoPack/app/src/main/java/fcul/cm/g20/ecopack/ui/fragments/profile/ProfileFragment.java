package fcul.cm.g20.ecopack.ui.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import fcul.cm.g20.ecopack.R;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        TabLayout tabLayout = profileFragmentView.findViewById(R.id.layout_profile_tabs);
        tabLayout.setOnTabSelectedListener(handleTabItemClick());

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new ProfileInfoFragment()).commit();

        return profileFragmentView;
    }

    private TabLayout.OnTabSelectedListener handleTabItemClick() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;

                int position = tab.getPosition();
                if (position == 0) fragment = new ProfileInfoFragment();
                else if (position == 1) fragment = new ProfileLocationsFragment();
                else fragment = new ProfileOpinionsFragment();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, fragment).commit();
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