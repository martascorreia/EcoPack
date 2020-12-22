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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
}