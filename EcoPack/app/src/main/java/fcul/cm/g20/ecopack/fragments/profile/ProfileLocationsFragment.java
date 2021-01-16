package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.StoreFragment;
import fcul.cm.g20.ecopack.fragments.profile.recyclerview.LocationAdapter;
import fcul.cm.g20.ecopack.utils.Utils;

import static fcul.cm.g20.ecopack.utils.Utils.showToast;

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
                database.collection("stores").document(mainActivity.userVisits.get(position).getStoreId().split("/")[1])
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_profile, new StoreFragment(task.getResult()))
                                            .addToBackStack("profile")
                                            .commit();
                                } else showToast("Não foi possível encontrar o estabelecimento selecionado. Por favor, tente mais tarde.", getContext());
                            }
                        });
            }
        });

        return profileLocationsFragmentView;
    }
}