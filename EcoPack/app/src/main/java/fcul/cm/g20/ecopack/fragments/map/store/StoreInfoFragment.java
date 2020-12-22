package fcul.cm.g20.ecopack.fragments.map.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.profile.ProfileInfoFragment;

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
        View storeFragment = inflater.inflate(R.layout.fragment_company_general_view, container, false);
        return storeFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFragment();
    }

    private void setupFragment() {
        database.collection("stores")
                .whereEqualTo("name", storeDocument.get("name"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            storeDocument = task.getResult().getDocuments().get(0);

                            TextView address = getView().findViewById(R.id.company_address);
                            address.setText((String) storeDocument.get("address"));

                            TextView schedule = getView().findViewById(R.id.company_schedule);
                            schedule.setText((String) storeDocument.get("schedule"));

                            TextView website = getView().findViewById(R.id.company_website);
                            website.setText((String) storeDocument.get("website"));

                            TextView phone = getView().findViewById(R.id.company_phone);
                            phone.setText((String) storeDocument.get("phone"));
                        }
                    }
                });

        int [] images = new int[] {R.drawable.maria_granel, R.drawable.maria_granel_2,  R.drawable.maria_granel_3,  R.drawable.maria_granel_4};
        ViewPager viewPager = getView().findViewById(R.id.view_pager);
        ImageAdapter imageAdapter = new ImageAdapter(getContext(), images);
        viewPager.setAdapter(imageAdapter);
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}