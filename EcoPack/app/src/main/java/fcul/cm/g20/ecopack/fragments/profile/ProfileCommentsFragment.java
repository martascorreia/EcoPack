package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.profile.recyclerview.CommentAdapter;

// TODO: NAVEGAÇÃO / CLICK GOES TO THE STORE IN QUESTION

public class ProfileCommentsFragment extends Fragment {
    private MainActivity mainActivity;
    private FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileCommentsFragmentView = inflater.inflate(R.layout.fragment_profile_comments, container, false);

        RecyclerView recyclerView = profileCommentsFragmentView.findViewById(R.id.comments_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        CommentAdapter commentAdapter = new CommentAdapter(getContext(), mainActivity.userComments);
        recyclerView.setAdapter(commentAdapter);

        commentAdapter.setOnCommentClickListener(new CommentAdapter.OnCommentClickListener() {
            @Override
            public void onCommentClick(int position) {
                Toast t = Toast.makeText(getContext(), "Comment " + position, Toast.LENGTH_SHORT);
                t.show();
            }
        });

        return profileCommentsFragmentView;
    }
}