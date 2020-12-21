package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import fcul.cm.g20.ecopack.R;

public class ProfileOpinionsFragment extends Fragment {
    private DocumentSnapshot userDocument;

    public ProfileOpinionsFragment(DocumentSnapshot userDocument) {
        this.userDocument = userDocument;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }
}