package fcul.cm.g20.ecopack.Mappers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fcul.cm.g20.ecopack.Models.User;
import fcul.cm.g20.ecopack.utils.Utils;

public class UserMapper {

    public interface OnCompleteSuccessful {
        void onSuccess(User user);
    }

    public static void getUserByUserName(String username, Context ctx, final OnCompleteSuccessful callback) {
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                User user = new User(task.getResult().getDocuments().get(0));
                                callback.onSuccess(user);
                            } else callback.onSuccess(null);
                        }
                    });
        } else {
            Utils.showToast("Falha ao obter utilizador, não é possivel aceder a internet.", ctx);
        }
    }

    public static void updateUserPointsAndVisits(User user, Context ctx) {
        if (Utils.isNetworkAvailable(ctx)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("points", user.getPoints());
            userMap.put("visits", user.getVisits());
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.document(user.getFireBasePath())
                    .update(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        } else {
            Utils.showToast("Falha ao gravar utilizador, não é possivel aceder a internet.", ctx);
        }
    }

    public static void updateUserPointsAndPrizes(User user, Context ctx) {
        if (Utils.isNetworkAvailable(ctx)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("points", user.getPoints());
            userMap.put("redeemed_prizes_ids", user.getRedeemedPrizesIds());
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.document(user.getFireBasePath())
                    .update(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        } else {
            Utils.showToast("Falha ao gravar utilizador, não é possivel aceder a internet.", ctx);
        }
    }

    public static void updateUser(User user, Context ctx) {
        if (Utils.isNetworkAvailable(ctx)) {
            Map<String, Object> userMap = user.getHashMap();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.document(user.getFireBasePath())
                    .update(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        } else {
            Utils.showToast("Falha ao gravar utilizador, não é possivel aceder a internet.", ctx);
        }
    }
}
