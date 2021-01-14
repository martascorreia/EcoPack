package fcul.cm.g20.ecopack.fragments.map.store;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;

// TODO: HANDLE CONNECTION ON SENDING COMMENT

public class StoreFragment extends Fragment {
    DocumentSnapshot storeDocument;
    DocumentSnapshot userDocument;
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
        View storeFragment = inflater.inflate(R.layout.fragment_store, container, false);
        setupFragment(storeFragment);
        return storeFragment;
    }

    private void setupFragment(View storeFragmentView) {
        TabLayout tabLayout = storeFragmentView.findViewById(R.id.layout_store_tabs);
        tabLayout.setOnTabSelectedListener(handleTabItemClick());

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tab_content, new StoreInfoFragment(storeDocument)).commit();

        // TITLE
        TextView name = storeFragmentView.findViewById(R.id.store_title);
        name.setText((String) storeDocument.get("name"));

        // PHOTO
        ArrayList<String> photos = (ArrayList<String>) storeDocument.get("photos");
        ImageView photo = storeFragmentView.findViewById(R.id.store_photo);
        if(!photos.isEmpty()){
            byte[] firstPhoto = android.util.Base64.decode((String) photos.get(0), android.util.Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(firstPhoto, 0, firstPhoto.length);
            photo.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth() , bmp.getHeight(), false));
        } else {
            photo.setBackgroundColor(getContext().getResources().getColor(R.color.mint));
        }

        // MARKER
        Long[] counters = new Long[5];
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

        MainActivity mainActivity = (MainActivity) getActivity();

        ImageButton editButton = storeFragmentView.findViewById(R.id.edit_button);
        ImageButton qrCodeButton = storeFragmentView.findViewById(R.id.qr_codes_button);

        if(mainActivity.userDocumentID.equals(storeDocument.get("owner"))){
            editButton.setVisibility(View.VISIBLE);
            editButton.setClickable(true);
            qrCodeButton.setVisibility((View.VISIBLE));
            qrCodeButton.setClickable(true);

            // EDIT BUTTON
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StoreEditFragment fragment = new StoreEditFragment(storeDocument);

                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_store, fragment)
                            .addToBackStack("store")
                            .commit();
                }
            });

            // EDIT BUTTON
            qrCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StoreQRCodesListFragment fragment = new StoreQRCodesListFragment(storeDocument);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_store, fragment)
                            .addToBackStack("store")
                            .commit();
                }
            });

        } else {
            editButton.setVisibility(View.INVISIBLE);
            editButton.setClickable(false);
            qrCodeButton.setVisibility((View.INVISIBLE));
            qrCodeButton.setClickable(false);
        }


    }
    

    private TabLayout.OnTabSelectedListener handleTabItemClick() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;

                //updateStoreDocument();

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

    private long getMostFrequentPackageType(Long[] counters) {
        long max = counters[0];
        long index = 0;

        for (int i = 0; i < counters.length; i++) {
            if (max < counters[i]) {
                max = counters[i];
                index = i;
            }
        }

        return index;
    }
}