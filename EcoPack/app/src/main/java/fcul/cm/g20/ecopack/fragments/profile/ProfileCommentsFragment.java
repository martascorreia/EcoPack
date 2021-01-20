package fcul.cm.g20.ecopack.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.StoreFragment;
import fcul.cm.g20.ecopack.fragments.profile.recyclerview.CommentAdapter;

import static fcul.cm.g20.ecopack.utils.Utils.showToast;

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
                database.collection("stores").document(mainActivity.userComments.get(position).get("store").toString().split("/")[1])
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_profile, new StoreFragment(task.getResult()))
                                            .addToBackStack("profile")
                                            .commit();
                                } else showToast("Não foi possível encontrar o estabelecimento selecionado. Por favor, tente mais tarde.", getContext());
                            }
                        });
            }
        });

        return profileCommentsFragmentView;
    }
}