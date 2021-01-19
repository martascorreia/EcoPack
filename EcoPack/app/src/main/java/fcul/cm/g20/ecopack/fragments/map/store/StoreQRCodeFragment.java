package fcul.cm.g20.ecopack.fragments.map.store;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.objects.QR_Code_Type;
import fcul.cm.g20.ecopack.utils.JavaMailAPIPhoto;
import fcul.cm.g20.ecopack.utils.PDFPhoto;
import fcul.cm.g20.ecopack.utils.Utils;

public class StoreQRCodeFragment extends Fragment {

    private FirebaseFirestore database;
    DocumentSnapshot userDocument;
    Bitmap pictureBitmap;
    private static final int CREATE_FILE = 1;
    QR_Code_Type qr_code_type;
    String qr_code;
    DocumentSnapshot storeDocument;

    public StoreQRCodeFragment(){
        // Required empty public constructor
    }

    public StoreQRCodeFragment(QR_Code_Type qr_code_type, DocumentSnapshot storeDocument) {
        this.qr_code_type = qr_code_type;
        this.storeDocument = storeDocument;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        getUserDocument();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_qr_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // QR-CODE
        ImageView qr_code_image_view = getView().findViewById(R.id.qr_code);
        HashMap<String, String> qr_codes = (HashMap<String, String>) storeDocument.get("qrCodes");

        String title = qr_code_type.getTitle();
        if(title.equals(getString(R.string.plastic))){
            qr_code = qr_codes.get("plastic");
        } else if(title.equals(getString(R.string.paper))){
            qr_code = qr_codes.get("paper");
        } else if(title.equals(getString(R.string.bio))){
            qr_code = qr_codes.get("bio");
        } else if(title.equals(getString(R.string.home))){
            qr_code = qr_codes.get("home");
        } else if(title.equals(getString(R.string.reusable))){
            qr_code = qr_codes.get("reusable");
        }

        byte[] pictureArray = android.util.Base64.decode(qr_code, android.util.Base64.DEFAULT);
        pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);
        qr_code_image_view.setImageBitmap(pictureBitmap);

        // TITLE
        TextView qr_code_title = getView().findViewById(R.id.qr_code_title);
        qr_code_title.setText("Código " + title);

        // BACK BUTTON
        getView().findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack("qr_codes", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        //PDF
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        Button downloadButton = getView().findViewById(R.id.qr_code_print_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });

        // E-mail
        Button emailButton = getView().findViewById(R.id.qr_code_email_button);
        final Context ctx = getContext();

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = getResources().getString(R.string.email_subject) + title;
                String text = "Olá " + userDocument.get("username") + "!" + "\n" +
                        getResources().getString(R.string.email_text2) + storeDocument.get("name") + "\n" +
                        "Obrigada,\n EcoPack";

                if(!userDocument.get("email").equals("N/A")){
                    JavaMailAPIPhoto javaMail = new JavaMailAPIPhoto(ctx, (String) userDocument.get("email"), subject, text, pictureBitmap);
                    javaMail.execute();
                    Utils.showToast("Email enviado com sucesso!", getContext());
                }else {
                    Utils.showToast("Não foi possível enviar para o endereço de email fornecido. Por favor, escolha um endereço de email válido.", getContext());
                }
            }
        });
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "qr_code.pdf");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, new Uri.Builder().build());
        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == CREATE_FILE
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                PDFPhoto.create(uri,"Loja " + storeDocument.get("name"), getContext(), pictureBitmap);
            }
        }
    }

    private void getUserDocument() {
        // GET CURRENT USER
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username", "");

        // GET USER DOCUMENT
        database.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userDocument = task.getResult().getDocuments().get(0);
                        }
                    }
                });
    }
}