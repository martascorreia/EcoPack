package fcul.cm.g20.ecopack.fragments.map.store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.objects.Comment;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.CommentAdapter;
import fcul.cm.g20.ecopack.utils.Utils;

public class StoreOpinionsFragment extends Fragment {

    private FirebaseFirestore database;
    HorizontalScrollView scroll;
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
        View view = inflater.inflate(R.layout.fragment_store_opinions, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // GETS USER DOCUMENT
        updateUserDocument();

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

                if (scroll.getVisibility() == View.VISIBLE) {
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
                scroll.setVisibility(View.INVISIBLE);
            }
        });

        // PLASTIC + HOME
        getView().findViewById(R.id.plastic_home_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_plastic_home_round));
                currentIcon = "ic_marker_plastic_home_round";
                scroll.setVisibility(View.INVISIBLE);
            }
        });

        // REUSABLE + HOME
        getView().findViewById(R.id.reusable_home_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_reusable_home_round));
                currentIcon = "ic_marker_reusable_home_round";
                scroll.setVisibility(View.INVISIBLE);
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
                scroll.setVisibility(View.INVISIBLE);
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
                scroll.setVisibility(View.INVISIBLE);
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
                scroll.setVisibility(View.INVISIBLE);
            }
        });

        // REUSABLE
        getView().findViewById(R.id.reusable_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                icon_button = getView().findViewById(R.id.icon_button);
                icon_button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
                icon_button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_marker_reusable_round));
                currentIcon = "ic_marker_reusable_round";
                scroll.setVisibility(View.INVISIBLE);
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
                scroll.setVisibility(View.INVISIBLE);
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
                scroll.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void comments() {
        final EditText text = getView().findViewById(R.id.comment);
        // ADD OLD COMMENTS
        addComments();

        // PUBLISH COMMENT
        getView().findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().length() == 0)
                    Utils.showToast("Por favor, faça um comentário antes de publicar...", getContext());
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A public comentário...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    // GET OLD COMMENTS
                    List<Map<String, Object>> comments = new ArrayList<>();
                    List<Map<String, Object>> oldComments = (List<Map<String, Object>>) storeDocument.get("comments");
                    if (oldComments != null) {
                        for (int i = 0; i < oldComments.size(); i++) {
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
                    comment.put("marker", currentIcon);
                    comments.add(comment);

                    final Map<String, Object> store = new HashMap<>();
                    store.put("comments", comments);
                    store.put("counters", setCounter());

                    if (isNetworkAvailable(getContext())) {
                        database.document(storeDocument.getReference().getPath())
                                .update(store)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Utils.showToast("Comentário publicado com sucesso!", getContext());
                                        text.setText("");

                                        // ADD COMMENT TO LAYOUT
                                        addComment(comment);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Utils.showToast("Não foi possível publicar o seu comentário. Por favor, tente mais tarde.", getContext());
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        Utils.showToast("Não foi possível publicar o seu comentário. Por favor, verifique a sua conexão à Internet.", getContext());
                    }
                }
            }
        });
    }

    private Map<String, Double> setCounter() {
        Double value = 0.15;
        Map<String, Double> counters = (Map<String, Double>) storeDocument.get("counters");
        if(currentIcon.equals("ic_marker_paper_home_round")){
            counters.put("paper", counters.get("paper") + value);
            counters.put("home", counters.get("home") + value);
        } else if(currentIcon.equals("ic_marker_plastic_home_round")){
            counters.put("plastic", counters.get("plastic") + value);
            counters.put("home", counters.get("home") + value);
        } else if(currentIcon.equals("ic_marker_bio_home_round")){
            counters.put("bio", counters.get("bio") + value);
            counters.put("home", counters.get("home") + value);
        } else if(currentIcon.equals("ic_marker_reusable_home_round")){
            counters.put("reusable", counters.get("reusable") + value);
            counters.put("home", counters.get("home") + value);
        } else if(currentIcon.equals("ic_marker_home_round")){
            counters.put("home", counters.get("home") + value);
        } else if(currentIcon.equals("ic_marker_paper_round")){
            counters.put("paper", counters.get("paper") + value);
        } else if(currentIcon.equals("ic_marker_plastic_round")){
            counters.put("plastic", counters.get("plastic") + value);
        } else if(currentIcon.equals("ic_marker_bio_round")){
            counters.put("bio", counters.get("bio") + value);
        } else if(currentIcon.equals("ic_marker_reusable_round")){
            counters.put("reusable", counters.get("reusable") + value);
        }
        return counters;
    }

    // colocar no construtor
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

    private void addComments() {
        recyclerView = getView().findViewById(R.id.comments_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_comments = new LinkedList<>();
        CommentAdapter commentAdapter = new CommentAdapter(getContext(), rv_comments);
        recyclerView.setAdapter(commentAdapter);

        // GET OLD COMMENTS
        List<Map<String, Object>> oldComments = (List<Map<String, Object>>) storeDocument.get("comments");
        if (oldComments != null) {
            for (int i = 0; i < oldComments.size(); i++) {
                Map<String, Object> oldComment = oldComments.get(i);

                Drawable drawable = null;
                if (!oldComment.get("marker").equals("ic_plus")) {
                    int id = getResources().getIdentifier((String) oldComment.get("marker"), "drawable", getActivity().getPackageName());
                    drawable = getResources().getDrawable(id);
                }

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
        String marker = (String) comment.get("marker");

        Drawable drawable = null;
        if (!marker.equals("ic_plus")) {
            int id = getResources().getIdentifier(marker, "drawable", getActivity().getPackageName());
            drawable = getResources().getDrawable(id);
        }

        rv_comments.add(new Comment(com, drawable, avatar, user, date, name));
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