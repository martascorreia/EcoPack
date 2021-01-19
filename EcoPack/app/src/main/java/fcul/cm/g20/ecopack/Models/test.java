package fcul.cm.g20.ecopack.Models;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fcul.cm.g20.ecopack.Mappers.UserMapper;
import fcul.cm.g20.ecopack.utils.Utils;

public class test {
    private int number;
    private String name;
    private ArrayList<String> values;
    private String path;
    private test test;
    private List<Prize> prizes;

    public String getNumber() {
        return number+"";
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTest(test test) {
        this.test = test;
    }

    public test getTest() {
        return this.test;
    }


    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public static void getTest(int number, Context ctx) {
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("tests")
                    .whereEqualTo("number", number)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                User user = new User(task.getResult().getDocuments().get(0));
                            }
                        }
                    });
        } else {
            Utils.showToast("Falha ao obter utilizador, não é possivel aceder a internet.", ctx);
        }
    }

    public static void updateTest(String fielName, test tests, test t, Context ctx) {
        HashMap<String, Object> ola = new HashMap<String, Object>();
        ola.put(fielName, tests);
        if (Utils.isNetworkAvailable(ctx)) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.document("test/74kMvKwQjHIjijqoiFj6")
                    .update(ola)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        } else {
            Utils.showToast("Falha ao gravar utilizador, não é possivel aceder a internet.", ctx);
        }
    }

    public static void saveTest(test t){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // Get a new write batch
        WriteBatch batch = database.batch();
        DocumentReference ref = database.collection("test").document();
        batch.set(ref, t);
        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // done
            }
        });
    }
}
