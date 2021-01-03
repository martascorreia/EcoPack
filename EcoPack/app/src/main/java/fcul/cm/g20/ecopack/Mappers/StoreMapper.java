package fcul.cm.g20.ecopack.Mappers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

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

    public static void saveStore(Store store, Context ctx){
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            // Get a new write batch
            WriteBatch batch = database.batch();
            DocumentReference ref = database.collection("stores").document();
            // save store firebase path
            store.setPath(ref);
            batch.set(ref, store);
            // Commit the batch
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    // done
                }
            });
        } else {
            Utils.showToast("Falha ao gravar a loja, não é possivel aceder a internet.", ctx);
        }
    }

    public static void getStoreByPath(String path, Context ctx, final StoreMapper.OnCompleteSuccessful callback) {
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(path)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                Store store = new Store(task.getResult().getDocuments().get(0));
                                callback.onSuccess(store);
                            } else callback.onSuccess(null);
                        }
                    });
        } else {
            Utils.showToast("Falha ao obter utilizador, não é possivel aceder a internet.", ctx);
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
                            //MESSAGE FOR DEBUG!!!!
                            Utils.showToast("Utilizador gravado com sucesso!", ctx);
                        }
                    });
        } else {
            Utils.showToast("Falha ao gravar utilizador, não é possivel aceder a internet.", ctx);
        }
    }
}
