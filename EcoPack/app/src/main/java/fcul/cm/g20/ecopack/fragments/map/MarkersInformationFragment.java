package fcul.cm.g20.ecopack.fragments.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import fcul.cm.g20.ecopack.R;

public class MarkersInformationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View markersInformationFragment = inflater.inflate(R.layout.fragment_map_markers_info, container, false);
        setupFragment(markersInformationFragment);
        return markersInformationFragment;
    }

    private void setupFragment(View markersInformationFragment) {
        markersInformationFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }
}