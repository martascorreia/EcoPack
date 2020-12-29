package fcul.cm.g20.ecopack.Mappers;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fcul.cm.g20.ecopack.Models.User;

public class UserMapper {

    public interface OnCompleteSuccessful{
        void onSuccess(User user);
    }

    public static void getUserByUserName(String username, final OnCompleteSuccessful callback){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                            User user =  new User(task.getResult().getDocuments().get(0));
                            callback.onSuccess(user);
                        } else callback.onSuccess(null);
                    }
                });
    }
}
