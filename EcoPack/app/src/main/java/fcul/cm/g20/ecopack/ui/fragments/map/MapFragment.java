package fcul.cm.g20.ecopack.ui.fragments.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fcul.cm.g20.ecopack.R;

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private LatLng currentCoordinates;
    private Marker userLastLocationMarker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mapFragment = inflater.inflate(R.layout.fragment_map, container, false);

        setupGoogleMap();
        setupSearchView(mapFragment);
        setupMarkerInformationButton(mapFragment);
        setupCreateStoreInformationButton(mapFragment);
        setupCenterMap(mapFragment);

        return mapFragment;
    }

    private void setupGoogleMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(supportMapFragment).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    map.setMyLocationEnabled(true);

                    // TODO: FAZER MELHOR GESTÃO DO SERVIÇO DA LOCALIZAÇÃO ESTAR DESATIVADO
                    LatLng defaultCoordinates = new LatLng(38.71667, -9.13333);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCoordinates, 16.0f));
                } else {
                    showToast("Não foi possível obter a sua localização. Por favor, dê as permissões necessárias.");
                    LatLng defaultCoordinates = new LatLng(38.71667, -9.13333);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCoordinates, 16.0f));
                }

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(final LatLng latLng) {
                        new AlertDialog.Builder(getActivity(), R.style.AlertDialogMap)
                                .setTitle("Registar loja")
                                .setMessage("Tem a certeza que quer registar uma loja neste local?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                            getActivity()
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_map, new CreateStoreFragment(latLng.latitude, latLng.longitude, addresses.get(0).getAddressLine(0)))
                                                    .addToBackStack(null)
                                                    .commit();
                                        } catch (IOException e) {
                                            showToast("Ocorreu um erro. " + e.getMessage());
                                        }
                                    }
                                })
                                .setNegativeButton("Não", null)
                                .show();
                    }
                });
            }
        });
    }

    private void setupSearchView(View mapFragment) {
        final SearchView searchView = mapFragment.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String addressInput = searchView.getQuery().toString();

                if (!addressInput.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(addressInput, 1);
                        if (addressList == null || addressList.size() == 0) showToast("Não foi possível obter localização. Localização não existente.");
                        else {
                            Address address = addressList.get(0);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 20));
                        }
                    } catch (IOException e) {
                        showToast(e.getMessage());
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

    private void setupMarkerInformationButton(View mapFragment) {
        mapFragment.findViewById(R.id.marker_information_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_map, new MarkerInformationFragment())
                        .addToBackStack("map")
                        .commit();
            }
        });
    }

    private void setupCreateStoreInformationButton(View mapFragment) {
        mapFragment.findViewById(R.id.create_store_information_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_map, new CreateStoreInformationFragment())
                        .addToBackStack("map")
                        .commit();
            }
        });
    }

    private void setupCenterMap(View mapFragment) {
        mapFragment.findViewById(R.id.center_map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCoordinates != null) map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, 16.0f));
            }
        });
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (userLastLocationMarker != null) userLastLocationMarker.remove();
        currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, 16.0f));
        userLastLocationMarker = map.addMarker(new MarkerOptions().position(currentCoordinates).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(2000);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}