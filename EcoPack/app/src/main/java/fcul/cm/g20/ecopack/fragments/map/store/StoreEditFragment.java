package fcul.cm.g20.ecopack.fragments.map.store;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.PreviewImageAdapter;
import fcul.cm.g20.ecopack.utils.Utils;

import static fcul.cm.g20.ecopack.utils.Utils.isNetworkAvailable;
import static fcul.cm.g20.ecopack.utils.Utils.showToast;

public class StoreEditFragment extends Fragment {
    private MainActivity mainActivity;
    private FirebaseFirestore database;
    DocumentSnapshot storeDocument;
    boolean options[] = new boolean[5];
    boolean options2[] = new boolean[5];
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

    public void setupFragment(View editStoreFragment) {
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

        Map<String, Long> counters = (Map<String, Long>) storeDocument.get("counters");

        // MARKERS
        final ImageView reusableImageView = editStoreFragment.findViewById(R.id.option_reusable);
        final ImageView bioImageView = editStoreFragment.findViewById(R.id.option_bio);
        final ImageView paperImageView = editStoreFragment.findViewById(R.id.option_paper);
        final ImageView plasticImageView = editStoreFragment.findViewById(R.id.option_plastic);

        //REUSABLE
        if (counters.get("reusable") != 0) {
            options[0] = true;
            options2[0] = true;
            ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.reusable)));
        } else {
            options[0] = false;
            options2[0] = false;
            ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dark_gray)));
        }

        // BIO
        if (counters.get("bio") != 0) {
            options[1] = true;
            options2[1] = true;
            ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.bio)));
        } else {
            options[3] = false;
            options2[3] = false;
            ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
        }

        // PAPER
        if (counters.get("paper") != 0) {
            options[2] = true;
            options2[2] = true;
            ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.paper)));
        } else {
            options[2] = false;
            options2[2] = false;
            ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
        }

        // PLASTIC
        if (counters.get("plastic") != 0) {
            options[3] = true;
            options[3] = true;
            ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.plastic)));
        } else {
            options[2] = false;
            options2[2] = false;
            ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
        }

        // CHECKBOX
        CheckBox homeCheckbox = editStoreFragment.findViewById(R.id.home_checkbox);
        if (counters.get("home") != 0) {
            options[4] = true;
            options2[4] = true;
            homeCheckbox.setChecked(true);
        } else {
            options[4] = false;
            options2[4] = false;
            homeCheckbox.setChecked(false);
        }

        //------------------------------------------------------------------------------------------
        // GET NEW INFORMATION
        final EditText[] inputs = new EditText[]{
                editStoreFragment.findViewById(R.id.store_name),
                editStoreFragment.findViewById(R.id.store_schedule),
                editStoreFragment.findViewById(R.id.store_email),
                editStoreFragment.findViewById(R.id.store_phone),
                editStoreFragment.findViewById(R.id.store_website)
        };

        // PHOTOS
        this.photos = (ArrayList<String>) storeDocument.get("photos");
        if (photos != null && !photos.isEmpty()) {
            RecyclerView recyclerView = editStoreFragment.findViewById(R.id.photos_container);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            PreviewImageAdapter previewImageAdapter = new PreviewImageAdapter(getContext(), photos);
            recyclerView.setAdapter(previewImageAdapter);
        }

        final Button uploadButton = editStoreFragment.findViewById(R.id.store_upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                            loadImageFromGallery();
                    } else loadImageFromGallery();
                } else loadImageFromGallery();
            }
        });

        // CHECKBOX
        homeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeCheckbox.isChecked()) {
                    options[4] = true;
                } else {
                    options[4] = false;
                }
            }
        });

        // MARKERS
        reusableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!options[0]) {
                    options[0] = true;
                    ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.reusable)));
                } else {
                    options[0] = false;
                    ImageViewCompat.setImageTintList(reusableImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });

        bioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!options[1]) {
                    options[1] = true;
                    ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.bio)));
                } else {
                    options[1] = false;
                    ImageViewCompat.setImageTintList(bioImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });

        paperImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!options[2]) {
                    options[2] = true;
                    ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.paper)));
                } else {
                    options[2] = false;
                    ImageViewCompat.setImageTintList(paperImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });

        plasticImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!options[3]) {
                    options[3] = true;
                    ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.plastic)));
                } else {
                    options[3] = false;
                    ImageViewCompat.setImageTintList(plasticImageView, ColorStateList.valueOf(ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.dark_gray)));
                }
            }
        });


        // SAVE
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
                if (!inputs[0].getText().toString().equals(storeDocument.get("name")))
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
                if (!photos.isEmpty())
                    store.put("photos", photos);

                // COUNTERS
                long value = 10;
                Map<String, Long> counters2 = new HashMap<>();
                if (!options[0]) counters2.put("reusable", 0l);
                else if (options2[0]) counters2.put("reusable", counters.get("reusable"));
                else counters2.put("reusable", value);

                if (!options[1]) counters2.put("bio", 0l);
                else if (options2[1]) counters2.put("bio", counters.get("bio"));
                else counters2.put("bio", value);

                if (!options[2]) counters2.put("paper", 0l);
                else if (options2[2]) counters2.put("paper", counters.get("paper"));
                else counters2.put("paper", value);

                if (!options[3]) counters2.put("plastic", 0l);
                else if (options2[3]) counters2.put("plastic", counters.get("plastic"));
                else counters2.put("plastic", value);

                if (!options[4]) counters2.put("home", 0l);
                else if (options2[4]) counters2.put("home", counters.get("home"));
                else counters2.put("home", value);

                store.put("counters", counters2);
                store.put("qrCodes", generateQrCodes(counters2));

                if (isNetworkAvailable(getContext())) {
                    database.document(storeDocument.getReference().getPath())
                            .update(store)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Utils.showToast("Alterações feitas com sucesso!", getContext());

                                    getActivity()
                                            .getSupportFragmentManager()
                                            .popBackStack("store", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    private enum QRCodesTypes {bio, paper, plastic, reusable, home}

    ;

    @SuppressLint("NewApi")
    public Map<String, String> generateQrCodes(Map<String, Long> counters) {
        Map<String, String> qrCodes = new HashMap<>();
        String storePath = storeDocument.getReference().getPath();
        Arrays.stream(QRCodesTypes.values())
                .forEach(qrType -> {
                    if (counters.containsKey(qrType.toString()) && counters.get(qrType.toString()) > 0) {
                        // means the user choose this as type in there store
                        String type = qrType.toString();
                        String code = type + '\u0000' + storePath; //'\u0000' -> null Char
                        Bitmap bitmap = QRCode.from(code).withColor(0xFFFFFFFF, pickQrCodeColour(qrType)).bitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        qrCodes.put(type, result);
                    }
                });

        return qrCodes;
    }

    private int pickQrCodeColour(QRCodesTypes qrType) {
        int result = 0xFFFFFFFF;
        switch (qrType) {
            case bio:
                result = 0xFF9C693C;
                break;
            case paper:
                result = 0xFF547FCA;
                break;
            case plastic:
                result = 0xFFDAA948;
                break;
            case reusable:
                result = 0xFF66B16F;
                break;
            case home:
                result = 0xFFDA5D44;
                break;
            default:
                result = 0xFFFFFFFF;
                break;
        }
        return result;
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
                while ((length = Objects.requireNonNull(inputStream).read(buffer)) != -1)
                    byteBuffer.write(buffer, 0, length);
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