package fcul.cm.g20.ecopack.fragments.map.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.LinkedList;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.QRCodeTypeAdapter;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.QR_Code_Type;

public class StoreQRCodesListFragment extends Fragment {
    DocumentSnapshot storeDocument;
    RecyclerView recyclerView;
    LinkedList<QR_Code_Type> qr_code_types;

    public StoreQRCodesListFragment() {
        // Required empty public constructor
    }

    public StoreQRCodesListFragment(DocumentSnapshot storeDocument) {
        this.storeDocument = storeDocument;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_qr_codes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setMarkers();

        getView().findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack("store", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

    }

    private void setMarkers() {
        recyclerView = getView().findViewById(R.id.qr_code_type_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        long[] counters = new long[5];
        HashMap<String, Long> countersMap = (HashMap<String, Long>) storeDocument.get("counters");
        counters[0] = countersMap.get("reusable");
        counters[1] = countersMap.get("bio");
        counters[2] = countersMap.get("paper");
        counters[3] = countersMap.get("plastic");
        counters[4] = countersMap.get("home");

        qr_code_types = new LinkedList<>();

        if(counters[0] != 0){
            QR_Code_Type qr_code_type = new QR_Code_Type(getString(R.string.reusable), ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_reusable_round));
            qr_code_types.add(qr_code_type);
        }

        if(counters[1] != 0){
            QR_Code_Type qr_code_type = new QR_Code_Type(getString(R.string.bio), ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_bio_round));
            qr_code_types.add(qr_code_type);
        }

        if(counters[2] != 0){
            QR_Code_Type qr_code_type = new QR_Code_Type(getString(R.string.paper), ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_paper_round));
            qr_code_types.add(qr_code_type);
        }

        if(counters[3] != 0){
            QR_Code_Type qr_code_type = new QR_Code_Type(getString(R.string.plastic), ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_plastic_round));
            qr_code_types.add(qr_code_type);
        }

        if(counters[4] != 0){
            QR_Code_Type qr_code_type = new QR_Code_Type(getString(R.string.home), ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_home_round));
            qr_code_types.add(qr_code_type);
        }


        QRCodeTypeAdapter qrCodeTypeAdapter = new QRCodeTypeAdapter(getContext(), qr_code_types);
        recyclerView.setAdapter(qrCodeTypeAdapter);

        qrCodeTypeAdapter.setOnQRCodeTypeClickListener(new QRCodeTypeAdapter.OnTypeClickListener() {
            @Override
            public void onTypeClickListener(int position) {
                QR_Code_Type qr_code_type = qrCodeTypeAdapter.getQR_Code_Type(position);
                if (qr_code_type == null)
                    return;

                StoreQRCodeFragment storeQRCodeFragment = new StoreQRCodeFragment(qr_code_type);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_store_qr_codes_list, storeQRCodeFragment)
                        .addToBackStack("qr_codes")
                        .commit();
            }
        });
    }
}