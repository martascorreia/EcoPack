package fcul.cm.g20.ecopack.fragments.map.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.ImageAdapter;

public class StoreInfoFragment extends Fragment {
    DocumentSnapshot storeDocument;
    FirebaseFirestore database;

    public StoreInfoFragment() {}

    public StoreInfoFragment(DocumentSnapshot storeDoc) {
        storeDocument = storeDoc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View storeFragment = inflater.inflate(R.layout.fragment_store_info, container, false);
        return storeFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFragment();
    }

    private void setupFragment() {

        TextView address = getView().findViewById(R.id.store_address);
        address.setText((String) storeDocument.get("address"));

        TextView schedule = getView().findViewById(R.id.store_schedule);
        schedule.setText((String) storeDocument.get("schedule"));

        TextView email = getView().findViewById(R.id.store_email);
        email.setText((String) storeDocument.get("email"));

        TextView website = getView().findViewById(R.id.store_website);
        website.setText((String) storeDocument.get("website"));

        TextView phone = getView().findViewById(R.id.store_phone);
        phone.setText((String) storeDocument.get("phone"));

        Map<String, Object> photos = (Map<String, Object>) storeDocument.get("photos");

        // either puts the photos or a text N/A
        ViewPager viewPager = getView().findViewById(R.id.view_pager);
        TextView photosText = getView().findViewById(R.id.store_photos);
        if(photos != null){
            viewPager.setVisibility(View.VISIBLE);
            photosText.setVisibility(View.INVISIBLE);
            ImageAdapter imageAdapter = new ImageAdapter(getContext(), photos);
            viewPager.setAdapter(imageAdapter);
        } else {
            viewPager.setVisibility(View.INVISIBLE);
            viewPager.getLayoutParams().height = 0;
            photosText.setVisibility(View.VISIBLE);
            photosText.setText("N/A");
        }
    }
}