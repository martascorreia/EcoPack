package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.profile.recyclerview.LocationAdapter;

// TODO: NAVEGAÇÃO / CLICK GOES TO THE STORE IN QUESTION

public class ProfileLocationsFragment extends Fragment {
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
        View profileLocationsFragmentView = inflater.inflate(R.layout.fragment_profile_locations, container, false);

        RecyclerView recyclerView = profileLocationsFragmentView.findViewById(R.id.locations_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LocationAdapter locationAdapter = new LocationAdapter(getContext(), mainActivity.userVisits);
        recyclerView.setAdapter(locationAdapter);

        locationAdapter.setOnLocationClickListener(new LocationAdapter.OnLocationClickListener() {
            @Override
            public void onLocationClick(int position) {
                Toast t = Toast.makeText(getContext(), "Location " + position, Toast.LENGTH_SHORT);
                t.show();
            }
        });

        return profileLocationsFragmentView;
    }
}