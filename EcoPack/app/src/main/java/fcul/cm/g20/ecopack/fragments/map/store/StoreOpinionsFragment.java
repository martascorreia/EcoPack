package fcul.cm.g20.ecopack.fragments.map.store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.adapter.Comment;
import fcul.cm.g20.ecopack.fragments.map.store.adapter.CommentAdapter;
import fcul.cm.g20.ecopack.fragments.profile.recyclerview.LocationAdapter;

public class StoreOpinionsFragment extends Fragment {

    private FirebaseFirestore database;
    ScrollView scroll;
    DocumentSnapshot storeDocument;
    DocumentSnapshot userDocument;
    LinkedList<Comment> rv_comments;
    RecyclerView recyclerView;
    FloatingActionButton icon_button;
    String currentIcon;

    public StoreOpinionsFragment() {
    }

    public StoreOpinionsFragment(DocumentSnapshot storeDoc) {
       storeDocument = storeDoc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_opinions, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // OLD COMMENTS + NEW COMMENT
        comments();

        // MARKERS "EMOJI"
        markerIcons();
    }

    private void markerIcons() {
        // FOR THE SCROLL TO BE BOTTOM -> TOP
        scroll = getView().findViewById(R.id.scroll_icons);

        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });

        //  ICON BUTTON
        icon_button = getView().findViewById(R.id.icon_button);
        icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dark_mint)));
        currentIcon = "ic_plus";

        icon_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scroll = getView().findViewById(R.id.scroll_icons);

                if(scroll.getVisibility() == View.VISIBLE){
                    scroll.setVisibility(View.INVISIBLE);
                } else {
                    scroll.setVisibility(View.VISIBLE);
                }
            }
        });

        // ICONS
        // PAPER + HOME
        getView().findViewById(R.id.paper_home_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_paper_home_round));
                currentIcon = "ic_marker_paper_home_round";
            }
        });

        // BIO + HOME
        getView().findViewById(R.id.bio_home_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_bio_home_round));
                currentIcon = "ic_marker_bio_home_round";
            }
        });

        // HOME
        getView().findViewById(R.id.home_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_home_round));
                currentIcon = "ic_marker_home_round";
            }
        });

        // BIO
        getView().findViewById(R.id.bio_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_bio_round));
                currentIcon = "ic_marker_bio_round";
            }
        });

        // REUSABLE
        getView().findViewById(R.id.reusable_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_glass_round));
                currentIcon = "ic_marker_glass_round";
            }
        });

        // PAPER
        getView().findViewById(R.id.paper_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_paper_round));
                currentIcon = "ic_marker_paper_round";
            }
        });

        // PLASTIC
        getView().findViewById(R.id.plastic_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_plastic_round));
                currentIcon = "ic_marker_plastic_round";
            }
        });
    }

    private void comments() {
        final EditText text = getView().findViewById(R.id.comment);

        // ADD OLD COMMENTS
        addComments();

        // GET USER DOCUMENT
        updateUserDocument();

        // PUBLISH COMMENT
        getView().findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().length() == 0)
                    showToast("Por favor, faça um comentário antes de publicar...");
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A public comentário...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    // GET OLD COMMENTS
                    List<Map<String, Object>> comments = new ArrayList<>();
                    List<Map<String, Object>> oldComments = (List<Map<String, Object>>) storeDocument.get("comments");
                    if (!oldComments.isEmpty()){
                        for(int i = 0; i < oldComments.size(); i++){
                            comments.add(oldComments.get(i));
                        }
                    }

                    // NEW COMMENT
                    final Map<String, Object> comment = new HashMap<>();
                    comment.put("user", userDocument.get("username"));
                    comment.put("name", userDocument.get("name"));
                    comment.put("picture", userDocument.get("picture"));
                    comment.put("date", System.currentTimeMillis());
                    comment.put("comment", text.getText().toString());
                    comment.put("marker", currentIcon.toString());
                    comments.add(comment);

                    final Map<String, Object> store = new HashMap<>();
                    store.put("address", storeDocument.get("address"));
                    store.put("comments", comments);
                    store.put("counters", storeDocument.get("counters"));
                    store.put("email", storeDocument.get("email"));
                    store.put("lat", storeDocument.get("lat"));
                    store.put("lng", storeDocument.get("lng"));
                    store.put("name", storeDocument.get("name"));
                    store.put("phone", storeDocument.get("phone"));
                    store.put("photos", storeDocument.get("photos"));
                    store.put("register_date", storeDocument.get("register_date"));
                    store.put("schedule", storeDocument.get("schedule"));
                    store.put("website", storeDocument.get("website"));

                    if (isNetworkAvailable(getContext())) {
                        database.document(storeDocument.getReference().getPath())
                                .update(store)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        showToast("Comentário publicado com sucesso!");
                                        text.setText("");

                                        // ADD COMMENT TO LAYOUT
                                        addComment(comment);

                                        //UPDATE STORE DOCUMENT
                                        updateStoreDocument();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        showToast("Não foi possível publicar o seu comentário. Por favor, tente mais tarde.");
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        showToast("Não foi possível publicar o seu comentário. Por favor, verifique a sua conexão à Internet.");
                    }
                }
            }
        });
    }

    private void updateUserDocument() {
        // GET CURRENT USER
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userCredentials", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username", "");

        // GET USER DOCUMENT
        database.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userDocument = task.getResult().getDocuments().get(0);
                        }
                    }
                });
    }

    private void updateStoreDocument() {
        database.collection("stores")
                .whereEqualTo("name", storeDocument.get("name"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            storeDocument = task.getResult().getDocuments().get(0);
                        }
                    }
                });
    }

    private void addComments() {
        recyclerView = getView().findViewById(R.id.comments_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_comments = new LinkedList<>();
        CommentAdapter commentAdapter = new CommentAdapter(getContext(), rv_comments);
        recyclerView.setAdapter(commentAdapter);

        // GET OLD COMMENTS
        List<Map<String, Object>> oldComments = (List<Map<String, Object>>) storeDocument.get("comments");
        if (!oldComments.isEmpty()) {
            for (int i = 0; i < oldComments.size(); i++) {
                Map<String, Object> oldComment = oldComments.get(i);

                int id = getResources().getIdentifier((String) oldComment.get("marker"), "drawable", getActivity().getPackageName());
                Drawable drawable = getResources().getDrawable(id);

                rv_comments.add(new Comment(oldComment.get("comment").toString(), drawable, oldComment.get("picture").toString(),
                        oldComment.get("user").toString(), oldComment.get("date").toString(), oldComment.get("name").toString()));
            }
        }
    }

    private void addComment(Map<String, Object> comment) {
        String user = (String) comment.get("user");
        String com = (String) comment.get("comment");
        String date = String.valueOf(comment.get("date"));
        String avatar = (String) comment.get("picture");
        String name = (String) comment.get("name");

        int id = getResources().getIdentifier((String) comment.get("marker"), "drawable", getActivity().getPackageName());
        Drawable drawable = getResources().getDrawable(id);

        rv_comments.add(new Comment(com, drawable, avatar, user, date, name));
    }


    private int getAvatar(String user) {
        return 0;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) return false;
        else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info)
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) return true;
        }

        return false;
    }

}