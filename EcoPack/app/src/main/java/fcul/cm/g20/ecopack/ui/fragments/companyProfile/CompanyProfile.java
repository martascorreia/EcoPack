package fcul.cm.g20.ecopack.ui.fragments.companyProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fcul.cm.g20.ecopack.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProfile extends Fragment {

    public CompanyProfile() {
        // Required empty public constructor
    }

    public static CompanyProfile newInstance() {
        CompanyProfile fragment = new CompanyProfile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(getContext());
        viewPager.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_profile, container, false);
    }
}