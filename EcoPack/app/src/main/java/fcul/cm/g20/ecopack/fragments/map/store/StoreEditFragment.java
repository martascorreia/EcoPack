package fcul.cm.g20.ecopack.fragments.map.store;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.ImageAdapter;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.PreviewImageAdapter;
import fcul.cm.g20.ecopack.utils.Utils;

import static fcul.cm.g20.ecopack.utils.Utils.isNetworkAvailable;
import static fcul.cm.g20.ecopack.utils.Utils.showToast;

public class StoreEditFragment extends Fragment {
    private MainActivity mainActivity;
    private FirebaseFirestore database;
    DocumentSnapshot storeDocument;
    public ArrayList<String> photos = new ArrayList<>();

    public StoreEditFragment() {
        // Required empty public constructor
    }

    public StoreEditFragment(DocumentSnapshot storeDocument) {
        this.storeDocument = storeDocument;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editStoreFragment = inflater.inflate(R.layout.fragment_store_edit, container, false);
        setupFragment(editStoreFragment);
        return editStoreFragment;
    }

    public void setupFragment(View editStoreFragment){
        // SET UP INFORMATION
        EditText schedule = editStoreFragment.findViewById(R.id.store_name);
        schedule.setText((String) storeDocument.get("name"));

        EditText name = editStoreFragment.findViewById(R.id.store_schedule);
        name.setText((String) storeDocument.get("schedule"));

        EditText email = editStoreFragment.findViewById(R.id.store_email);
        email.setText((String) storeDocument.get("email"));

        EditText website = editStoreFragment.findViewById(R.id.store_website);
        website.setText((String) storeDocument.get("website"));

        EditText phone = editStoreFragment.findViewById(R.id.store_phone);
        phone.setText((String) storeDocument.get("phone"));

        // GET NEW INFORMATION
        final EditText[] inputs = new EditText[]{
                editStoreFragment.findViewById(R.id.store_name),
                editStoreFragment.findViewById(R.id.store_schedule),
                editStoreFragment.findViewById(R.id.store_email),
                editStoreFragment.findViewById(R.id.store_phone),
                editStoreFragment.findViewById(R.id.store_website)
        };

        this.photos = (ArrayList<String>) storeDocument.get("photos");

        if (photos != null && photos.size() != 0) {
            RecyclerView recyclerView = editStoreFragment.findViewById(R.id.photos_container);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            PreviewImageAdapter previewImageAdapter = new PreviewImageAdapter(getContext(), mainActivity.createStorePhotos);
            recyclerView.setAdapter(previewImageAdapter);
        }

        final Button uploadButton = editStoreFragment.findViewById(R.id.store_upload);
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

        final Button saveButton = editStoreFragment.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setMessage("A subtmeter alterações...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                Map<String, Object> store = new HashMap<>();

                // BASIC INFORMATION
                if(!inputs[0].getText().toString().equals(storeDocument.get("name")))
                    store.put("name", inputs[0].getText().toString());

                if (!inputs[1].getText().toString().equals(storeDocument.get("schedule")))
                    store.put("schedule", inputs[1].getText().toString());

                if (!inputs[2].getText().toString().equals(storeDocument.get("email")))
                    store.put("email", inputs[2].getText().toString());

                if (!inputs[3].getText().toString().equals(storeDocument.get("phone")))
                    store.put("phone", inputs[3].getText().toString());

                if (inputs[4].getText().toString().equals(storeDocument.get("website")))
                    store.put("website", inputs[4].getText().toString());

                // IMAGES
                if (mainActivity.createStorePhotos.size() != 0)
                    store.put("photos", photos);

                // REST OF THE INFORMATION
                store.put("address", storeDocument.get("address"));
                store.put("comments", storeDocument.get("comments"));
                store.put("counters", storeDocument.get("counters"));
                store.put("lat", storeDocument.get("lat"));
                store.put("lng", storeDocument.get("lng"));
                store.put("register_date", storeDocument.get("register_date"));

                if (isNetworkAvailable(getContext())) {
                    database.document(storeDocument.getReference().getPath())
                            .update(store)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Utils.showToast("Comentário publicado com sucesso!", getContext());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showToast("Não foi possível submeter as suas alterações. Por favor, tente mais tarde.", getContext());
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    Utils.showToast("Não foi possível submeter as suas alterações. Por favor, verifique a sua conexão à Internet.", getContext());
                }
            }
        });

        editStoreFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack("store", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
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
                Uri selectedImage = data.getData();
                Cursor selectedCursor = mainActivity.getContentResolver().query(selectedImage, null, null, null, null);
                int sizeIndex = selectedCursor.getColumnIndex(OpenableColumns.SIZE);
                selectedCursor.moveToFirst();
                long imageSize = selectedCursor.getLong(sizeIndex);

                if (imageSize > 200000) {
                    showToast("A imagem que está a tentar carregar é demasiado grande.", getContext());
                    return;
                }

                int length = 0;
                InputStream inputStream = getContext().getContentResolver().openInputStream(selectedImage);
                byte[] buffer = new byte[(int) imageSize];
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                while ((length = Objects.requireNonNull(inputStream).read(buffer)) != -1) byteBuffer.write(buffer, 0, length);
                photos.add(android.util.Base64.encodeToString(byteBuffer.toByteArray(), android.util.Base64.DEFAULT));

                RecyclerView recyclerView = getView().findViewById(R.id.photos_container);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                PreviewImageAdapter previewImageAdapter = new PreviewImageAdapter(getContext(), photos);
                recyclerView.setAdapter(previewImageAdapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}