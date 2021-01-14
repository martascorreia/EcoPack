package fcul.cm.g20.ecopack.fragments.map;

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
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.Mappers.StoreMapper;
import fcul.cm.g20.ecopack.Models.Store;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.PreviewImageAdapter;

import static fcul.cm.g20.ecopack.utils.Utils.isNetworkAvailable;
import static fcul.cm.g20.ecopack.utils.Utils.showToast;

// TODO: QUANDO CRIAMOS A LOJA, A LOJA CRIADA AINDA NÃO APARECE NO MAPA
// TODO: ADICIONAR ACUMULADOR DE IMAGE SIZE PARA AS IMAGENS QUE SÃO CARREGADAS (DEFINIR UM THRESHOLD)

public class CreateStoreFragment extends Fragment {
    private MainActivity mainActivity;
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View createStoreFragment = inflater.inflate(R.layout.fragment_map_create_store, container, false);
        setupFragment(createStoreFragment);
        return createStoreFragment;
    }

    private void setupFragment(View createStoreFragment) {
        final EditText addressText = createStoreFragment.findViewById(R.id.create_store_address);
        addressText.setText(mainActivity.createStoreAddress);

        final EditText[] inputs = new EditText[]{
                createStoreFragment.findViewById(R.id.create_store_name),
                createStoreFragment.findViewById(R.id.create_store_schedule),
                createStoreFragment.findViewById(R.id.create_store_email),
                createStoreFragment.findViewById(R.id.create_store_phone),
                createStoreFragment.findViewById(R.id.create_store_website)
        };

        final ImageView reusableImageView = createStoreFragment.findViewById(R.id.option_reusable);
        final ImageView bioImageView = createStoreFragment.findViewById(R.id.option_bio);
        final ImageView paperImageView = createStoreFragment.findViewById(R.id.option_paper);
        final ImageView plasticImageView = createStoreFragment.findViewById(R.id.option_plastic);
        final CheckBox homeCheckbox = createStoreFragment.findViewById(R.id.home_checkbox);

        if (mainActivity.createStoreOptions == null) mainActivity.createStoreOptions = new boolean[5];
        else {
            if (mainActivity.createStoreOptions[0]) ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.reusable)));
            else ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
            if (mainActivity.createStoreOptions[1]) ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.bio)));
            else ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
            if (mainActivity.createStoreOptions[2]) ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.paper)));
            else ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
            if (mainActivity.createStoreOptions[3]) ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.plastic)));
            else ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
            if (mainActivity.createStoreOptions[4]) homeCheckbox.setChecked(true);
            else homeCheckbox.setChecked(false);
        }

        if (mainActivity.createStorePhotos != null && mainActivity.createStorePhotos.size() != 0) {
            RecyclerView recyclerView = createStoreFragment.findViewById(R.id.photos_container);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            PreviewImageAdapter previewImageAdapter = new PreviewImageAdapter(getContext(), mainActivity.createStorePhotos);
            recyclerView.setAdapter(previewImageAdapter);
        }

        reusableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainActivity.createStoreOptions[0]) {
                    mainActivity.createStoreOptions[0] = true;
                    ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.reusable)));
                } else {
                    mainActivity.createStoreOptions[0] = false;
                    ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });

        bioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainActivity.createStoreOptions[1]) {
                    mainActivity.createStoreOptions[1] = true;
                    ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.bio)));
                } else {
                    mainActivity.createStoreOptions[1] = false;
                    ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });

        paperImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainActivity.createStoreOptions[2]) {
                    mainActivity.createStoreOptions[2] = true;
                    ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.paper)));
                } else {
                    mainActivity.createStoreOptions[2] = false;
                    ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });

        plasticImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainActivity.createStoreOptions[3]) {
                    mainActivity.createStoreOptions[3] = true;
                    ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.plastic)));
                } else {
                    mainActivity.createStoreOptions[3] = false;
                    ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });

        homeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.createStoreOptions[4] = !mainActivity.createStoreOptions[4];
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

                if (inputs[0].getText().toString().equals("")) showToast("Por favor, insira o nome do estabelecimento.", getContext());
                else {
                    int selected = 0;
                    for (int i = 0; i < mainActivity.createStoreOptions.length - 1; i++)
                        if (mainActivity.createStoreOptions[i]) selected++;

                    if (selected == 0) {
                        showToast("Por favor, escolha um tipo de embalagem.", getContext());
                        v.setEnabled(true);
                        return;
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A registar estabelecimento...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

                    Store store = new Store();

                    // OWNER
                    store.setOwner(mainActivity.userDocumentID);

                    // ADDRESS-RELATED
                    store.setAddress(mainActivity.createStoreAddress);
                    store.setLat(mainActivity.createStoreLatitude);
                    store.setLng(mainActivity.createStoreLongitude);

                    // BASIC INFORMATION
                    store.setName(inputs[0].getText().toString());
                    store.setSchedule(inputs[1].getText().toString());
                    store.setEmail(inputs[2].getText().toString());
                    store.setPhone(inputs[3].getText().toString());
                    store.setWebsite(inputs[4].getText().toString());

                    // MARKER COUNTERS
                    Map<String, Integer> counters = new HashMap<>();
                    if (mainActivity.createStoreOptions[0]) counters.put("reusable",  1);
                    else counters.put("reusable",  0);
                    if (mainActivity.createStoreOptions[1]) counters.put("bio",  1);
                    else counters.put("bio",  0);
                    if (mainActivity.createStoreOptions[2]) counters.put("paper",  1);
                    else counters.put("paper",  0);
                    if (mainActivity.createStoreOptions[3]) counters.put("plastic",  1);
                    else counters.put("plastic",  0);
                    if (mainActivity.createStoreOptions[4]) counters.put("home",  1);
                    else counters.put("home",  0);
                    store.setCounters(counters);

                    // IMAGES
                    ArrayList<String> photos = (mainActivity.createStorePhotos.size() != 0)? mainActivity.createStorePhotos : null;
                    store.setPhotos(photos);

                    // COMMENTS
                    // store initiates comments array in constructor

                    // REGISTER DATE
                    store.setRegister_date(System.currentTimeMillis());

                    boolean transactionInitiated = StoreMapper.saveStore(store, getContext(), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("Estabelecimento registado com sucesso!", getContext());

                            mainActivity.createStoreOptions = null;
                            mainActivity.createStorePhotos = new ArrayList<>();

                            progressDialog.dismiss();
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            getActivity()
                                    .getSupportFragmentManager()
                                    .popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            showToast("Não foi possível registar o estabelecimento. Por favor, tente mais tarde.", getContext());
                        }
                    });

                    if(!transactionInitiated){
                        progressDialog.dismiss();
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        showToast("Não foi possível registar o estabelecimento. Por favor, tente mais tarde.", getContext());
                    }
                }
                v.setEnabled(true);
            }
        });

        createStoreFragment.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.createStoreOptions = null;
                mainActivity.createStorePhotos = new ArrayList<>();

                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
                mainActivity.createStorePhotos.add(android.util.Base64.encodeToString(byteBuffer.toByteArray(), android.util.Base64.DEFAULT));

                RecyclerView recyclerView = getView().findViewById(R.id.photos_container);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                PreviewImageAdapter previewImageAdapter = new PreviewImageAdapter(getContext(), mainActivity.createStorePhotos);
                recyclerView.setAdapter(previewImageAdapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}