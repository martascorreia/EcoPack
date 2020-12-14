package fcul.cm.g20.ecopack.ui.fragments.map;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

public class CreateStoreFragment extends Fragment {
    private String address;
    private double latitude;
    private double longitude;
    private FirebaseFirestore database;
    private LinkedList<String> photos = new LinkedList<>();

    public CreateStoreFragment() {

    }

    public CreateStoreFragment(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = FirebaseFirestore.getInstance();

        View createStoreFragment = inflater.inflate(R.layout.fragment_create_shop, container, false);

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
                    } else loadImageFromGallery();
                } else loadImageFromGallery();
            }
        });

        createStoreFragment.findViewById(R.id.create_store_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                if (inputs[0].getText().toString().equals("")) Toast.makeText(getContext(), "Por favor, insira o nome da loja.", Toast.LENGTH_SHORT).show();
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("A criar loja...");
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
                    store.put("comments", null);
                    // TODO: DECIDIR TODOS OS TIPOS DE CLASSIFICAÇÃO (PAPEL, VIDRO, PLÁSTICO, PAPEL + CASA, ETC.)
                    if (photos.size() != 0) {
                        Map<String, Object> photosMap = new HashMap<>();
                        int i = 0;
                        for (String photo : photos) photosMap.put("photo" + i++, photo);
                        store.put("photos", photosMap);
                    } else store.put("photos", null);
                    store.put("register_date", System.currentTimeMillis());

                    database.collection("stores")
                            .add(store)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Loja criada com sucesso.", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().popBackStack();       // TODO: DELETE MAP FRAGMENT FROM BACKSTACK WHEN ENTERING CREATE STORE
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                v.setEnabled(true);
            }
        });

        return createStoreFragment;
    }

    private void loadImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());

                int len = 0;
                byte[] buffer = new byte[6291456];
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                while ((len = Objects.requireNonNull(inputStream).read(buffer)) != -1) byteBuffer.write(buffer, 0, len);

                photos.add(android.util.Base64.encodeToString(byteBuffer.toByteArray(), android.util.Base64.DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
