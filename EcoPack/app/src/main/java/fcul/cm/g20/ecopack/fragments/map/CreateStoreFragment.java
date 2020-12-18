package fcul.cm.g20.ecopack.fragments.map;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import fcul.cm.g20.ecopack.R;

// TODO: TRATAR DA ROTAÇÃO E CRIAR LISTENER PARA RETER AS COORDENADAS E A ADDRESS
// TODO: ARRANJAR FORMA DO UTILIZADOR NÃO PODER FAZER ONBACKPRESSED DEPOIS DE ENVIAR O PEDIDO À FIREBASE
// TODO: DELETE MAP FRAGMENT FROM BACKSTACK AND CREATE NEW MAP FRAGMENT

public class CreateStoreFragment extends Fragment {
    private double latitude;
    private double longitude;
    private String address;
    private FirebaseFirestore database;
    private boolean[] storeOptions;
    private LinkedList<String> photos = new LinkedList<>();

    public CreateStoreFragment() {

    }

    public CreateStoreFragment(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        storeOptions = new boolean[5];
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View createStoreFragment = inflater.inflate(R.layout.fragment_create_store, container, false);
        setupFragment(createStoreFragment);
        return createStoreFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    private void setupFragment(View createStoreFragment) {
        final EditText addressText = createStoreFragment.findViewById(R.id.create_store_address);
        addressText.setText(address);

        final EditText[] inputs = new EditText[]{
                createStoreFragment.findViewById(R.id.create_store_name),
                createStoreFragment.findViewById(R.id.create_store_schedule),
                createStoreFragment.findViewById(R.id.create_store_email),
                createStoreFragment.findViewById(R.id.create_store_phone),
                createStoreFragment.findViewById(R.id.create_store_website)
        };

        final ImageView reusableImageView = createStoreFragment.findViewById(R.id.option_reusable);
        reusableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!storeOptions[0]) {
                    storeOptions[0] = true;
                    ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.glass)));
                } else {
                    storeOptions[0] = false;
                    ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dark_gray)));
                }
            }
        });

        final ImageView bioImageView = createStoreFragment.findViewById(R.id.option_bio);
        bioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!storeOptions[1]) {
                    storeOptions[1] = true;
                    ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.bio)));
                } else {
                    storeOptions[1] = false;
                    ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dark_gray)));
                }
            }
        });

        final ImageView paperImageView = createStoreFragment.findViewById(R.id.option_paper);
        paperImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!storeOptions[2]) {
                    storeOptions[2] = true;
                    ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.paper)));
                } else {
                    storeOptions[2] = false;
                    ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dark_gray)));
                }
            }
        });

        final ImageView plasticImageView = createStoreFragment.findViewById(R.id.option_plastic);
        plasticImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!storeOptions[3]) {
                    storeOptions[3] = true;
                    ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.plastic)));
                } else {
                    storeOptions[3] = false;
                    ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dark_gray)));
                }
            }
        });

        final CheckBox homeCheckbox = createStoreFragment.findViewById(R.id.home_checkbox);
        homeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeOptions[4] = !storeOptions[4];
            }
        });

        final Button uploadButton = createStoreFragment.findViewById(R.id.create_store_upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) loadImageFromGallery();
                    } else loadImageFromGallery();
                } else loadImageFromGallery();
            }
        });

        final Button registerButton = createStoreFragment.findViewById(R.id.create_store_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                if (inputs[0].getText().toString().equals("")) showToast("Por favor, insira o nome.");
                else {
                    int selected = 0;
                    for (int i = 0; i < storeOptions.length - 1; i++) if (storeOptions[i]) selected++;

                    if (selected == 0) {
                        showToast("Por favor, escolha um tipo de embalagem.");
                        v.setEnabled(true);
                        return;
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A registar estabelecimento...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Map<String, Object> store = new HashMap<>();

                    // ADDRESS-RELATED
                    store.put("address", address);
                    store.put("lat", latitude);
                    store.put("lng", longitude);

                    // BASIC INFORMATION
                    store.put("name", inputs[0].getText().toString());
                    if (inputs[1].getText().toString().equals("")) store.put("schedule", "N/A");
                    else store.put("schedule", inputs[1].getText().toString());
                    if (inputs[2].getText().toString().equals("")) store.put("email", "N/A");
                    else store.put("email", inputs[2].getText().toString());
                    if (inputs[3].getText().toString().equals("")) store.put("phone", "N/A");
                    else store.put("phone", inputs[3].getText().toString());
                    if (inputs[4].getText().toString().equals("")) store.put("website", "N/A");
                    else store.put("website", inputs[4].getText().toString());

                    // MARKER COUNTERS
                    Map<String, Integer> counters = new HashMap<>();
                    if (storeOptions[0]) counters.put("reusable", 1);
                    else counters.put("reusable", 0);
                    if (storeOptions[1]) counters.put("bio", 1);
                    else counters.put("bio", 0);
                    if (storeOptions[2]) counters.put("paper", 1);
                    else counters.put("paper", 0);
                    if (storeOptions[3]) counters.put("plastic", 1);
                    else counters.put("plastic", 0);
                    if (storeOptions[4]) counters.put("home", 1);
                    else counters.put("home", 0);
                    store.put("counters", counters);

                    // IMAGES
                    if (photos.size() != 0) {
                        Map<String, Object> photosMap = new HashMap<>();
                        int i = 0;
                        for (String photo : photos) photosMap.put("photo" + i++, photo);
                        store.put("photos", photosMap);
                    } else store.put("photos", null);

                    // COMMENTS
                    store.put("comments", null);

                    // REGISTER DATE
                    store.put("register_date", System.currentTimeMillis());

                    // A ÚNICA FORMA DE CONTORNAR O COMPORTAMENTO DO FIREBASE/FIRESTORE NO QUE TOCA A PEDIDOS ENVIADOS SEM INTERNET É VER PELA CONEXÃO DO DISPOSITIVO
                    if (isNetworkAvailable(getContext())) {
                        database.collection("stores")
                                .add(store)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.dismiss();
                                        showToast("Estabelecimento registado com sucesso!");
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        showToast("Não foi possível registar o estabelecimento. Por favor, tente mais tarde.");
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        showToast("Não foi possível registar o estabelecimento. Por favor, verifique a sua conexão à Internet.");
                    }
                }

                v.setEnabled(true);
            }
        });
    }

    private void loadImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1001);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) return false;
        else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info)
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            try {
                int length = 0;
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                byte[] buffer = new byte[6291456];
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                while ((length = Objects.requireNonNull(inputStream).read(buffer)) != -1) byteBuffer.write(buffer, 0, length);
                photos.add(android.util.Base64.encodeToString(byteBuffer.toByteArray(), android.util.Base64.DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}