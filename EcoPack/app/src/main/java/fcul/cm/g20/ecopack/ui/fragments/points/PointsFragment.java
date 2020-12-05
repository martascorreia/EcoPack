package fcul.cm.g20.ecopack.ui.fragments.points;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.ui.fragments.map.MarkersInfoFragment;

public class PointsFragment extends Fragment {

    FloatingActionButton addPointsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_points, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addPointsButton = getView().findViewById(R.id.add_points_button);
        addPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraFragment cameraFragment = new CameraFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_points, cameraFragment)
                        .addToBackStack("points")
                        .commit();
            }
        });

    }
}