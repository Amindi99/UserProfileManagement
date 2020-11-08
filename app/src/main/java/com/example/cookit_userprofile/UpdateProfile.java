package com.example.cookit_userprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {

    EditText etName, etUserName, etBio, etEmail, etLocation;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        documentReference = db.collection("user").document(currentuid);

        etBio = findViewById(R.id.et_bio_up);
        etEmail = findViewById(R.id.et_email_up);
        etUserName = findViewById(R.id.et_username_up);
        etName = findViewById(R.id.et_name_up);
        etLocation = findViewById(R.id.et_location_up);
        button = findViewById(R.id.btn_up);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                updateProfile();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists()){
                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String emailResult = task.getResult().getString("email");
                            String userNameResult = task.getResult().getString("userName");
                            String locationResult = task.getResult().getString("location");
                            String url = task.getResult().getString("url");

                            etName.setText(nameResult);
                            etBio.setText(bioResult);
                            etEmail.setText(emailResult);
                            etUserName.setText(userNameResult);
                            etLocation.setText(locationResult);

                        }else{
                            Toast.makeText(UpdateProfile.this,"No profile exist",Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void updateProfile() {

        final String name = etName.getText().toString();
        final String bio = etBio.getText().toString();
        final String userName = etUserName.getText().toString();
        final String location = etLocation.getText().toString();
        final String email = etEmail.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid= user.getUid();
        final  DocumentReference sDoc = db.collection("user").document(currentuid);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sDoc);

                transaction.update(sDoc,"name",name);
                transaction.update(sDoc,"userName",userName);
                transaction.update(sDoc,"email",email);
                transaction.update(sDoc,"location",location);
                transaction.update(sDoc,"bio",bio);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                });




    }

}