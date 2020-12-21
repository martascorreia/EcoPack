package fcul.cm.g20.ecopack.fragments.map.store;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;

import fcul.cm.g20.ecopack.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreInfoFragment extends Fragment {
    DocumentSnapshot storeDocument;

    public StoreInfoFragment(DocumentSnapshot storeDocument) {
        storeDocument = storeDocument;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_general_view, container, false);
    }
}