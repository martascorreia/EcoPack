package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.LinkedList;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.profile.recyclerview.LocationAdapter;

public class ProfileLocationsFragment extends Fragment {
    DocumentSnapshot userDocument;

    public ProfileLocationsFragment(DocumentSnapshot userDocument) {
        this.userDocument = userDocument;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileLocationsFragmentView = inflater.inflate(R.layout.fragment_profile_locations, container, false);

        RecyclerView recyclerView = profileLocationsFragmentView.findViewById(R.id.locations_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LinkedList<Pair<String, String>> locations = new LinkedList<>();
        locations.add(new Pair<>("A Loja Ali Do Canto", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Strada", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Mais Outra Loja", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Aqui Cheira a Enxofre", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Dados Estáticos São Boring", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("BTS É Giro De Se Ouvir Nos Workouts", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Gastei Demasiado Dinheiro Na Black Friday Da Steam", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Acho Que Já Chega Tantas Locations", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Acho Que Já Chega Tantas Locations 2", "30-11-2020, 09:45h"));
        locations.add(new Pair<>("Acho Que Já Chega Tantas Locations 3", "30-11-2020, 09:45h"));

        LocationAdapter locationAdapter = new LocationAdapter(locations);
        recyclerView.setAdapter(locationAdapter);

        locationAdapter.setOnLocationClickListener(new LocationAdapter.OnLocationClickListener() {
            @Override
            public void onLocationClick(int position) {
                Toast t = Toast.makeText(getContext(), "Location " + position, Toast.LENGTH_SHORT);
                t.show();

                // TODO: ADD NEW MAPFRAGMENT(COORDINATES) IN ORDER TO GO STRAIGHT AWAY TO THE LOCATION WITH THESE COORDINATES
            }
        });

        return profileLocationsFragmentView;
    }
}