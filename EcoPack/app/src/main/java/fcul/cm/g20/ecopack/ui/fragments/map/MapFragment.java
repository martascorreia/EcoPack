package fcul.cm.g20.ecopack.ui.fragments.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import fcul.cm.g20.ecopack.R;

public class MapFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{

        private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    FloatingActionButton markersInfo;
    SearchView searchView;
    private static final int Request_User_Location_Code = 99;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // PERMISSIONS
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    buildGoogleApiClient();
                    map.setMyLocationEnabled(true);
                    locationButton();
                    return;
                }
                // DEFAULT LOCATION
                LatLng lisbon = new LatLng(38.71667, -9.13333);
                map.moveCamera(CameraUpdateFactory.newLatLng(lisbon));

            }
        });

        return view;
    }

    public void locationButton(){
        View locationButton = ((View) getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 0, 200);
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonMarkers();
        searchView();
    }

    private void searchView() {
        searchView = getView().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try{
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch(IOException e){
                        return false;
                    }

                    if(addressList.size() > 0) {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(location));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    } else{
                        return false;
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void buttonMarkers() {
        markersInfo = getView().findViewById(R.id.markers_info_button);
        markersInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkersInfoFragment markersFragment = new MarkersInfoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_map, markersFragment)
                        .addToBackStack("map")
                        .commit();
            }
        });
    }

    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        } else{
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
