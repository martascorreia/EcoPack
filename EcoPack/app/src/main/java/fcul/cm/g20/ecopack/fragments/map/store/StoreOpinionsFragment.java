package fcul.cm.g20.ecopack.fragments.map.store;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
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

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fcul.cm.g20.ecopack.MainActivity;
import fcul.cm.g20.ecopack.R;
import fcul.cm.g20.ecopack.fragments.map.store.objects.Comment;
import fcul.cm.g20.ecopack.fragments.map.store.recyclerview.CommentAdapter;
import fcul.cm.g20.ecopack.utils.Utils;

public class StoreOpinionsFragment extends Fragment {

    private FirebaseFirestore database;
    private MainActivity mainActivity;
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
        mainActivity = (MainActivity) getActivity();
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
        List<Map<String, Object>> oldComments = (List<Map<String, Object>>) storeDocument.get("comments");
        // ADD OLD COMMENTS
        addComments();

        // PUBLISH COMMENT
        getView().findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().length() == 0)
                    Utils.showToast("Por favor, escreva um comentário antes de publicar.", getContext());
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setMessage("A publicar comentário...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    // GET OLD COMMENTS
                    List<Map<String, Object>> comments = new ArrayList<>();
                    if (oldComments != null)
                        for (int i = 0; i < oldComments.size(); i++) comments.add(oldComments.get(i));

                    // NEW COMMENT
                    final Map<String, Object> comment = new HashMap<>();
                    comment.put("user", userDocument.get("username"));
                    comment.put("name", userDocument.get("name"));
                    comment.put("picture", userDocument.get("picture"));
                    comment.put("date", System.currentTimeMillis());
                    comment.put("comment", text.getText().toString());
                    comment.put("marker", currentIcon);
                    comments.add(comment);
                    if (oldComments != null) oldComments.add(comment);

                    final Map<String, Object> store = new HashMap<>();
                    store.put("comments", comments);
                    Map<String, Long> counters = setCounter();
                    store.put("counters", counters);
                    store.put("qrCodes", generateQrCodes(counters));

                    // UPDATE USER COMMENTS
                    ArrayList<HashMap<String, Object>> userComments = (ArrayList<HashMap<String, Object>>) mainActivity.userComments;
                    comment.put("store", (storeDocument.getReference().getPath()));
                    userComments.add((HashMap<String, Object>) comment);

                    final Map<String, Object> user = new HashMap<>();
                    user.put("comments", userComments);

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

                        database.document("users/" + mainActivity.userDocumentID)
                                .update(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
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

    private Map<String, Long> setCounter() {
        Map<String, Long> counters = (Map<String, Long>) storeDocument.get("counters");
        if (currentIcon.equals("ic_marker_paper_home_round")) {
            counters.put("paper", counters.get("paper") + 3l);
            if (counters.get("home") == 0) counters.put("home", 1l);
        } else if (currentIcon.equals("ic_marker_plastic_home_round")) {
            counters.put("plastic", counters.get("plastic") + 3l);
            if (counters.get("home") == 0) counters.put("home", 1l);
        } else if (currentIcon.equals("ic_marker_bio_home_round")) {
            counters.put("bio", counters.get("bio") + 3l);
            if (counters.get("home") == 0) counters.put("home", 1l);
        } else if (currentIcon.equals("ic_marker_reusable_home_round")) {
            counters.put("reusable", counters.get("reusable") + 3l);
            if (counters.get("home") == 0) counters.put("home", 1l);
        } else if (currentIcon.equals("ic_marker_home_round")) {
            counters.put("home", counters.get("home") + 3l);
        } else if (currentIcon.equals("ic_marker_paper_round")) {
            counters.put("paper", counters.get("paper") + 3l);
        } else if (currentIcon.equals("ic_marker_plastic_round")) {
            counters.put("plastic", counters.get("plastic") + 3l);
        } else if (currentIcon.equals("ic_marker_bio_round")) {
            counters.put("bio", counters.get("bio") + 3l);
        } else if (currentIcon.equals("ic_marker_reusable_round")) {
            counters.put("reusable", counters.get("reusable") + 1);
        }
        return counters;
    }

    private enum QRCodesTypes {bio, paper, plastic, reusable, home}

    ;

    @SuppressLint("NewApi")
    public Map<String, String> generateQrCodes(Map<String, Long> counters) {
        Map<String, String> qrCodes = new HashMap<>();
        String storePath = storeDocument.getReference().getPath();
        Arrays.stream(QRCodesTypes.values())
                .forEach(qrType -> {
                    if (counters.containsKey(qrType.toString()) && counters.get(qrType.toString()) > 0) {
                        // means the user choose this as type in there store
                        String type = qrType.toString();
                        String code = type + '\u0000' + storePath; //'\u0000' -> null Char
                        Bitmap bitmap = QRCode.from(code).withColor(0xFFFFFFFF, pickQrCodeColour(qrType)).bitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        qrCodes.put(type, result);
                    }
                });

        return qrCodes;
    }

    private int pickQrCodeColour(QRCodesTypes qrType) {
        int result = 0xFFFFFFFF;
        switch (qrType) {
            case bio:
                result = 0xFF9C693C;
                break;
            case paper:
                result = 0xFF547FCA;
                break;
            case plastic:
                result = 0xFFDAA948;
                break;
            case reusable:
                result = 0xFF66B16F;
                break;
            case home:
                result = 0xFFDA5D44;
                break;
            default:
                result = 0xFFFFFFFF;
                break;
        }
        return result;
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
        CommentAdapter commentAdapter = new CommentAdapter(getContext(), rv_comments);
        recyclerView.setAdapter(commentAdapter);
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
