package fcul.cm.g20.ecopack.fragments.map.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.profile.ProfileInfoFragment;

// TODO: HANDLE CONNECTION ON SENDING COMMENT

public class StoreFragment extends Fragment {
    DocumentSnapshot storeDocument;
    FirebaseFirestore database;

    public StoreFragment() {}

    public StoreFragment(DocumentSnapshot storeDocument) {
        this.storeDocument = storeDocument;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View storeFragment = inflater.inflate(R.layout.fragment_company, container, false);
        setupFragment(storeFragment);
        return storeFragment;
    }

    private void setupFragment(View storeFragmentView) {
        TabLayout tabLayout = storeFragmentView.findViewById(R.id.layout_company_tabs);
        tabLayout.setOnTabSelectedListener(handleTabItemClick());

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new StoreInfoFragment(storeDocument)).commit();

        // TODO: trocar para id
        database.collection("stores")
                .whereEqualTo("name", storeDocument.get("name"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            storeDocument = task.getResult().getDocuments().get(0);

                            TextView name = getView().findViewById(R.id.company_title);
                            name.setText((String) storeDocument.get("name"));
                        }
                    }
                });

        long[] counters = new long[5];
        HashMap<String, Long> countersMap = (HashMap<String, Long>) storeDocument.get("counters");
        counters[0] = countersMap.get("reusable");
        counters[1] = countersMap.get("bio");
        counters[2] = countersMap.get("paper");
        counters[3] = countersMap.get("plastic");
        counters[4] = countersMap.get("home");

        long mostFrequentIndex = getMostFrequentPackageType(counters);
        FloatingActionButton marker_icon = storeFragmentView.findViewById(R.id.marker_icon);

        if (counters[4] == 1) {
            if (mostFrequentIndex == 0) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_reusable_home_round));
            else if (mostFrequentIndex == 1) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_bio_home_round));
            else if (mostFrequentIndex == 2) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_paper_home_round));
            else if (mostFrequentIndex == 3) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_plastic_home_round));
        } else {
            if (mostFrequentIndex == 0) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_reusable_round));
            else if (mostFrequentIndex == 1) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_bio_round));
            else if (mostFrequentIndex == 2) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_paper_round));
            else if (mostFrequentIndex == 3) marker_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_plastic_round));
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private TabLayout.OnTabSelectedListener handleTabItemClick() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;

                int position = tab.getPosition();
                if (position == 0) fragment = new StoreInfoFragment(storeDocument);
                else fragment = new StoreOpinionsFragment(storeDocument);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, fragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }

    private long getMostFrequentPackageType(long[] counters) {
        long max = counters[0], index = 0;

        for (int i = 0; i < counters.length; i++) {
            if (max < counters[i]) {
                max = counters[i];
                index = i;
            }
        }

        return index;
    }
}