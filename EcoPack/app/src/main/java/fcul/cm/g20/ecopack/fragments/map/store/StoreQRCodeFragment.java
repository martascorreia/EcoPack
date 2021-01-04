package fcul.cm.g20.ecopack.fragments.map.store;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.QR_Code_Type;

public class StoreQRCodeFragment extends Fragment {

    QR_Code_Type qr_code_type;

    public StoreQRCodeFragment(){
        // Required empty public constructor
    }

    public StoreQRCodeFragment(QR_Code_Type qr_code_type) {
        this.qr_code_type = qr_code_type;
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
}