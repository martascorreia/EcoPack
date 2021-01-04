package fcul.cm.g20.ecopack.fragments.map.store;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.objects.QR_Code_Type;

public class StoreQRCodeFragment extends Fragment {

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
        Bitmap pictureBitmap = BitmapFactory.decodeByteArray(pictureArray, 0, pictureArray.length);
        qr_code_image_view.setImageBitmap(pictureBitmap);

        // TITLE
        TextView qr_code_title = getView().findViewById(R.id.qr_code_title);
        qr_code_title.setText("QR CODE " + title);

        // BACK BUTTON
        getView().findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack("qr_codes", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }
}