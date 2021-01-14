package fcul.cm.g20.ecopack.fragments.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.StoreFragment;
import fcul.cm.g20.ecopack.utils.Utils;

import static fcul.cm.g20.ecopack.utils.Utils.showToast;

// TODO: DECIDE WHAT TO DO WITH SAVING THE LAST SHOWN LOCATION

public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private int DEFAULT_MAP_ZOOM = 16;
    private MainActivity mainActivity;
    private FirebaseFirestore database;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private LatLng currentCoordinates;
    private Marker userLastLocationMarker;
    private boolean isMenuOpen = false;
    private HashMap<String, DocumentSnapshot> storesMap;
    private List<DocumentSnapshot> stores;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = FirebaseFirestore.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mapFragment = inflater.inflate(R.layout.fragment_map, container, false);
        setupFragment(mapFragment);
        return mapFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // IMPORTANTE!
        mainActivity.isProfileSettingsFragmentActive = false;
        mainActivity.editPicture = null;
        mainActivity.editName = null;
        mainActivity.editEmail = null;
        mainActivity.editPhone = null;
        mainActivity.editGender = null;
        mainActivity.editBirthday = null;
        mainActivity.editCity = null;
        mainActivity.editPassword = null;
    }

    private void setupFragment(final View mapFragment) {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(supportMapFragment).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                storesMap = new HashMap<>();

                map.getUiSettings().setCompassEnabled(false);

                // NECESSÁRIO PARA ESTABELECER/ATUALIZAR O ESTADO DO UTILIZADOR (SE DER ALGUM TIPO DE ERRO, NÃO HÁ PROBLEMA NENHUM)
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
                database.collection("users")
                        .whereEqualTo("username", sharedPreferences.getString("username", ""))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                    mainActivity.userPicture = (String) userDocument.get("picture");
                                    mainActivity.userUsername = (String) userDocument.get("username");
                                    mainActivity.userPassword = (String) userDocument.get("password");
                                    mainActivity.userName = (String) userDocument.get("name");
                                    mainActivity.userEmail = (String) userDocument.get("email");
                                    mainActivity.userPhone = (String) userDocument.get("phone");
                                    mainActivity.userGender = (String) userDocument.get("gender");
                                    mainActivity.userBirthday = (String) userDocument.get("birthday");
                                    mainActivity.userCity = (String) userDocument.get("city");
                                    mainActivity.userRegisterDate = (long) userDocument.get("register_date");
                                    mainActivity.userPoints = (long) userDocument.get("points");
                                    mainActivity.userRedeemedPrizes = (ArrayList<String>) userDocument.get("redeemed_prizes");
                                    //mainActivity.userVisits = (ArrayList<HashMap<String, String>>) userDocument.get("visits");
                                    //mainActivity.userComments = (ArrayList<HashMap<String, String>>) userDocument.get("comments");
                                    mainActivity.userDocumentID = userDocument.getReference().getPath().split("/")[1];
                                }
                            }
                        });

                database.collection("stores")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    stores = task.getResult().getDocuments();
                                    for (DocumentSnapshot store : stores) {
                                        LatLng storeCoordinates = new LatLng((double) store.get("lat"), (double) store.get("lng"));
                                        Marker currMarker = map.addMarker(new MarkerOptions().position(storeCoordinates));

                                        long[] counters = new long[5];
                                        HashMap<String, Long> countersMap = (HashMap<String, Long>) store.get("counters");
                                        counters[0] = countersMap.get("reusable");
                                        counters[1] = countersMap.get("bio");
                                        counters[2] = countersMap.get("paper");
                                        counters[3] = countersMap.get("plastic");
                                        counters[4] = countersMap.get("home");

                                        long mostFrequentIndex = getMostFrequentPackageType(counters);
                                        if (counters[4] == 1) {
                                            if (mostFrequentIndex == 0) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_reusable_home));
                                            else if (mostFrequentIndex == 1) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_bio_home));
                                            else if (mostFrequentIndex == 2) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_paper_home));
                                            else if (mostFrequentIndex == 3) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_plastic_home));
                                        } else {
                                            if (mostFrequentIndex == 0) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_reusable));
                                            else if (mostFrequentIndex == 1) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_bio));
                                            else if (mostFrequentIndex == 2) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_paper));
                                            else if (mostFrequentIndex == 3) currMarker.setIcon(drawableToBitmap(mainActivity.getApplicationContext(), R.drawable.ic_marker_plastic));
                                        }

                                        storesMap.put(currMarker.getId(), store);
                                    }
                                } else showToast("Não foi possível obter os estabelecimentos existentes. Por favor, tente mais tarde.", getContext());
                            }
                        });

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) buildGoogleApiClient();
                else showToast("Não foi possível obter a sua localização. Por favor, dê as permissões necessárias.", getContext());

                // LOCALIZAÇÃO TEMPORÁRIA, NO CASO DO UTILIZADOR TER O SERVIÇO DE LOCALIZAÇÃO DESLIGADO, A LOCALIZAÇÃO NÃO TER SIDO AINDA CALCULADA OU NÃO HAVER PERMISSÕES
                LatLng defaultCoordinates = new LatLng(38.71667, -9.13333);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultCoordinates, DEFAULT_MAP_ZOOM));

                /*
                if (mainActivity.visibleLocationLatitude == 0.0 && mainActivity.visibleLocationLongitude == 0.0) {
                    LatLng defaultCoordinates = new LatLng(38.71667, -9.13333);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultCoordinates, DEFAULT_MAP_ZOOM));
                } else map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mainActivity.visibleLocationLatitude, mainActivity.visibleLocationLongitude), DEFAULT_MAP_ZOOM));
                */

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        DocumentSnapshot storeDocument = storesMap.get(marker.getId());
                        if (storeDocument != null) createFragment(new StoreFragment(storeDocument));
                        return false;
                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(final LatLng latLng) {
                        new AlertDialog.Builder(getActivity(), R.style.AlertDialogMap)
                                .setTitle("Registar Estabelecimento")
                                .setMessage("Tem a certeza que quer registar um estabelecimento neste local?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                            mainActivity.createStoreAddress = addresses.get(0).getAddressLine(0);
                                            mainActivity.createStoreLatitude = latLng.latitude;
                                            mainActivity.createStoreLongitude = latLng.longitude;
                                            createFragment(new CreateStoreFragment());
                                        } catch (IOException e) {
                                            showToast("Não foi possível registar o estabelecimento. Por favor, verifique a sua conexão à Internet.", getContext());
                                        }
                                    }
                                })
                                .setNegativeButton("Não", null)
                                .show();
                    }
                });

                /*
                // THIS SAVES THE LAST COORDINATES SHOWN
                // IT HAS A BIG PROBLEM: THIS RUNS ON THE MAIN THREAD, WHICH MAKES THE MAP RUN A LOT SLOWER, WHICH TAKES A TOLL ON OTHER FRAGMENTS
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        LatLng visibleRegionCoordinates = map.getProjection().getVisibleRegion().latLngBounds.getCenter();
                        mainActivity.visibleLocationLatitude = visibleRegionCoordinates.latitude;
                        mainActivity.visibleLocationLongitude = visibleRegionCoordinates.longitude;
                    }
                });
                */
            }
        });

        final SearchView searchView = mapFragment.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String addressInput = searchView.getQuery().toString();
                if (!addressInput.equals("")) {
                    try {
                        Geocoder geocoder = new Geocoder(getContext());
                        List<Address> addressList = geocoder.getFromLocationName(addressInput, 1);
                        if (addressList == null || addressList.size() == 0) showToast("Não foi possível obter a localização. Localização não existente.", getContext());
                        else {
                            Address address = addressList.get(0);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_MAP_ZOOM));
                        }
                    } catch (IOException e) {
                        showToast("Não foi possível obter a localização. Por favor, verifique a sua conexão à Internet.", getContext());
                        return false;
                    }
                } else showToast("Por favor, insira uma localização.", getContext());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.findViewById(R.id.marker_information_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment(new MarkersInformationFragment());
            }
        });

        mapFragment.findViewById(R.id.create_information_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragment(new CreateInformationFragment());
            }
        });

        mapFragment.findViewById(R.id.center_map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isLocationEnabled(getContext())) showToast("Não foi possível centrar o mapa na sua localização. Por favor, verifique se tem o serviço de localização ativado.", getContext());
                else if (currentCoordinates != null) map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, DEFAULT_MAP_ZOOM));
            }
        });

        mapFragment.findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuOpen) {
                    isMenuOpen = false;
                    mapFragment.findViewById(R.id.marker_information_button).setVisibility(View.INVISIBLE);
                    mapFragment.findViewById(R.id.create_information_button).setVisibility(View.INVISIBLE);
                    mapFragment.findViewById(R.id.center_map_button).setVisibility(View.INVISIBLE);
                } else {
                    isMenuOpen = true;
                    mapFragment.findViewById(R.id.marker_information_button).setVisibility(View.VISIBLE);
                    mapFragment.findViewById(R.id.create_information_button).setVisibility(View.VISIBLE);
                    mapFragment.findViewById(R.id.center_map_button).setVisibility(View.VISIBLE);
                }
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

    private long getMostFrequentPackageType(long[] counters) {
        long max = counters[0], index = 0;

        for (int i = 0; i < counters.length; i++) {
            if (max < counters[i]) {
                max = counters[i];
                index = i;
            }
        }

        return index;
    }

    private BitmapDescriptor drawableToBitmap(Context context, int markerID) {
        if (mainActivity != null) {
            Drawable marker = ContextCompat.getDrawable(context, markerID);
            marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
            Bitmap markerBitmap = Bitmap.createBitmap(marker.getIntrinsicWidth(), marker.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(markerBitmap);
            marker.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(markerBitmap);
        } else return null;
    }

    private void createFragment(Fragment fragment) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_map, fragment)
                .addToBackStack("map")
                .commit();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        if (userLastLocationMarker == null) map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinates, DEFAULT_MAP_ZOOM));
        else userLastLocationMarker.remove();
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