package fcul.cm.g20.ecopack.Mappers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fcul.cm.g20.ecopack.Models.Prize;
import fcul.cm.g20.ecopack.Models.Store;
import fcul.cm.g20.ecopack.Models.User;
import fcul.cm.g20.ecopack.utils.Utils;

public class StoreMapper {
    public interface OnCompleteSuccessful {
        void onSuccess(Store store);
    }

    public static boolean saveStore(Store store, Context ctx, final OnSuccessListener<Void> onSuccess, final OnFailureListener onFailure) {
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            // Get a new write batch
            WriteBatch batch = database.batch();
            DocumentReference ref = database.collection("stores").document();
            // save store firebase path
            store.setPath(ref);
            // generate QrCodes
            store.generateQrCodes(ref);
            //Commit
            batch.set(ref, store);
            batch.commit()
                    .addOnSuccessListener(onSuccess)
                    .addOnFailureListener(onFailure);
            return true;
        } else {
            return false;
        }
    }

    public static boolean getStoreByPath(String path, Context ctx, final StoreMapper.OnCompleteSuccessful callback) {
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.document(path)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            if (snapshot != null) {
                                DocumentSnapshot userDocument = snapshot;
                                Store store = new Store(snapshot);
                                callback.onSuccess(store);
                            } else callback.onSuccess(null);
                        }
                    });
            return true;
        } else {
            Utils.showToast("Falha ao obter utilizador, não é possivel aceder a internet.", ctx);
            return false;
        }
    }

    @SuppressLint("NewApi")
    public static void updateCounters(Store store, Context ctx) {
        Map<String, Object> map = new HashMap<>();
        map.put("counters", store.getCounters());
        update(map, store, ctx);
    }

    @SuppressLint("NewApi")
    public static void update(Map<String, Object> fields, Store store, Context ctx) {
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.document(store.getFirebasePath())
                    .update(fields)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        } else {
            Utils.showToast("Falha ao actualizar loja, não é possivel aceder a internet.", ctx);
        }
    }
}