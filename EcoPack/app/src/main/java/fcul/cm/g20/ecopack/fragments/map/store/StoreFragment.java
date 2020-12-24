package fcul.cm.g20.ecopack.fragments.map.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import fcul.cm.g20.ecopack.R;

// TODO: HANDLE CONNECTION ON SENDING COMMENT

public class StoreFragment extends Fragment {
    DocumentSnapshot storeDocument;

    public StoreFragment(){};

    public StoreFragment(DocumentSnapshot storeDocument) {
        this.storeDocument = storeDocument;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View storeFragment = inflater.inflate(R.layout.fragment_company, container, false);
        setupFragment(storeFragment);
        return storeFragment;
    }

    private void setupFragment(View storeFragmentView) {
        TabLayout tabLayout = storeFragmentView.findViewById(R.id.layout_company_tabs);
        tabLayout.setOnTabSelectedListener(handleTabItemClick());

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new StoreInfoFragment()).commit();
    }

    private TabLayout.OnTabSelectedListener handleTabItemClick() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;

                int position = tab.getPosition();
                if (position == 0) fragment = new StoreInfoFragment();
                else fragment = new StoreOpinionsFragment();

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