package fcul.cm.g20.ecopack.ui.fragments.map;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
// TODO: DECIDIR TODOS OS TIPOS DE CLASSIFICAÇÃO (PAPEL, VIDRO, PLÁSTICO, PAPEL + CASA, ETC.) E ADICIONAR NO DOCUMENTO OS CONTADORES
// TODO: ARRANJAR FORMA DO UTILIZADOR NÃO PODER FAZER ONBACKPRESSED DEPOIS DE ENVIAR O PEDIDO À FIREBASE
// TODO: DELETE MAP FRAGMENT FROM BACKSTACK AND CREATE NEW MAP FRAGMENT

public class CreateStoreFragment extends Fragment {
    private double latitude;
    private double longitude;
    private String address;
    private FirebaseFirestore database;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View createStoreFragment = inflater.inflate(R.layout.fragment_create_store, container, false);
        setupFragment(createStoreFragment);
        return createStoreFragment;
    }

    private void setupFragment(View createStoreFragment) {
        final EditText addressText = createStoreFragment.findViewById(R.id.create_store_address);
        addressText.setText(address);

        final EditText[] inputs = new EditText[]{
                createStoreFragment.findViewById(R.id.create_store_name),
                createStoreFragment.findViewById(R.id.create_store_email),
                createStoreFragment.findViewById(R.id.create_store_phone)
        };

        createStoreFragment.findViewById(R.id.create_store_upload).setOnClickListener(new View.OnClickListener() {
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

        createStoreFragment.findViewById(R.id.create_store_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                if (inputs[0].getText().toString().equals("")) showToast("Por favor, insira o nome do estabelecimento.");
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A registar estabelecimento...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Map<String, Object> store = new HashMap<>();
                    store.put("address", address);
                    store.put("lat", latitude);
                    store.put("lng", longitude);
                    store.put("name", inputs[0].getText().toString());
                    if (inputs[1].getText().toString().equals("")) store.put("email", "N/A");
                    else store.put("email", inputs[1].getText().toString());
                    if (inputs[2].getText().toString().equals("")) store.put("phone", "N/A");
                    else store.put("phone", inputs[1].getText().toString());
                    if (photos.size() != 0) {
                        Map<String, Object> photosMap = new HashMap<>();
                        int i = 0;
                        for (String photo : photos) photosMap.put("photo" + i++, photo);
                        store.put("photos", photosMap);
                    } else store.put("photos", null);
                    Map<String, Integer> counters = new HashMap<>();
                    store.put("comments", null);
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