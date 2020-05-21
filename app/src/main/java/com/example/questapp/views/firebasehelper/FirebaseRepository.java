package com.example.questapp.views.firebasehelper;

import androidx.annotation.NonNull;

import com.example.questapp.views.model.Quizz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseRepository {
    private FirebaseComplete firebaseComplete;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Query reference = firebaseFirestore.collection("quizz").whereEqualTo("visibility", "public");

    public FirebaseRepository(FirebaseComplete firebaseComplete) {
        this.firebaseComplete = firebaseComplete;
    }

    public void getData() {
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    firebaseComplete.onFirebaseComplete(task.getResult().toObjects(Quizz.class));
                } else {
                    firebaseComplete.onFailed(task.getException());
                }
            }
        });
    }
}
