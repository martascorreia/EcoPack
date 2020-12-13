package fcul.cm.g20.ecopack.ui.fragments.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import fcul.cm.g20.ecopack.R;

public class AddMarkerInfoFragment extends Fragment {

    ImageButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_marker_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton();
    }

    private void backButton() {
        backButton = getView().findViewById(R.id.backButtonContribute);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack ("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }
}