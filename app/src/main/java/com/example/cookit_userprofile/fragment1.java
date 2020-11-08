package com.example.cookit_userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class fragment1 extends Fragment implements View.OnClickListener {

    ImageView imageView;
    TextView nameEt, userNameEt, bioEt, emailEt, locationEt;
    ImageButton ib_edit, imageButtonMenu;
    DocumentReference reference;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView = getActivity().findViewById(R.id.iv_f1);
        nameEt = getActivity().findViewById(R.id.tv_name_f1);
        userNameEt = getActivity().findViewById(R.id.tv_userName_f1);
        bioEt = getActivity().findViewById(R.id.tv_bio_f1);
        emailEt = getActivity().findViewById(R.id.tv_email_f1);
        locationEt = getActivity().findViewById(R.id.tv_location_f1);

        ib_edit = getActivity().findViewById(R.id.ib_edit_f1);
        imageButtonMenu = getActivity().findViewById(R.id.ib_menu_f1);

        imageButtonMenu.setOnClickListener(this);
        ib_edit.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_edit_f1:
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
                break;

            case R.id.ib_menu_f1:
                BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                bottomSheetMenu.show(getFragmentManager(), "bottomsheet");
                break;

            case R.id.iv_f1:
                Intent intent1 = new Intent(getActivity(), ImageActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String currentid = user.getUid(); //Do what we need to do with the id


            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //String currentid = user.getUid();
            DocumentReference reference;
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            reference = firestore.collection("user").document(currentid);


            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.getResult().exists()) {
                                String nameResult = task.getResult().getString("name");
                                String bioResult = task.getResult().getString("bio");
                                String emailResult = task.getResult().getString("email");
                                String userNameResult = task.getResult().getString("userName");
                                String locationResult = task.getResult().getString("location");
                                String url = task.getResult().getString("url");

                                Picasso.get().load(url).into(imageView);
                                nameEt.setText(nameResult);
                                bioEt.setText(bioResult);
                                emailEt.setText(emailResult);
                                userNameEt.setText(userNameResult);
                                locationEt.setText(locationResult);


                            } else {
                                Intent intent = new Intent(getActivity(), CreateProfile.class);
                                startActivity(intent);
                            }

                        }
                    });
        }
    }
}
