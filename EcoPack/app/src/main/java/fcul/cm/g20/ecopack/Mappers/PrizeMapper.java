package fcul.cm.g20.ecopack.Mappers;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fcul.cm.g20.ecopack.Models.Prize;
import fcul.cm.g20.ecopack.Models.User;

public class PrizeMapper {

    public interface OnCompleteSuccessful{
        void onSuccess(List<Prize> prizes);
    }

    // TODO: CHECK FOR NETWORK
    public static void getPrizes(final PrizeMapper.OnCompleteSuccessful callback){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("prizes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> snapshotList = task.getResult().getDocuments();
                            List<Prize> prizes = new ArrayList<>();

                            for(DocumentSnapshot snapshot : snapshotList){
                                prizes.add(new Prize(snapshot));
                            }
                            callback.onSuccess(prizes);
                        } else callback.onSuccess(null);
                    }
                });
    }

    // TODO: CHECK FOR NETWORK
    public static void savePrizes(List<Prize> prizes){
        List<Map<String, Object>> prizesMap = getHashMapList(prizes);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // Get a new write batch
        WriteBatch batch = database.batch();
        for( Map<String, Object> prize : prizesMap){
            DocumentReference ref = database.collection("prizes").document();
            batch.set(ref, prize);
        }
        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // done
            }
        });
    }

    private static ArrayList<Map<String, Object>> getHashMapList(List<Prize> prizes){
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = new ArrayList<>(prizes.stream().map(Prize::getHashMap).collect(Collectors.toList()));
        }
        return result;
    }
}
