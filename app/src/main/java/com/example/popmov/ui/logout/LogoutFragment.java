package com.example.popmov.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.popmov.HomeActivity;
import com.example.popmov.R;
import com.google.firebase.auth.FirebaseAuth;


public class LogoutFragment extends Fragment {
    FirebaseAuth mAuth;
    CardView cardView;
    Button btnNo,btnYes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_logout, container, false);
        mAuth=FirebaseAuth.getInstance();
        cardView=view.findViewById(R.id.card_layout);
        btnNo=view.findViewById(R.id.btn_no);
        btnYes=view.findViewById(R.id.btn_yes);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                Intent intent=new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                getActivity().finish();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null)
                {
                    mAuth.signOut();
                    Intent intent=new Intent(getActivity(),HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    getActivity().finish();
                    Toast.makeText(getContext(),"SignOut",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(),"No User Exit",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getActivity(),HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
                cardView.setVisibility(View.GONE);
            }
        });

        return  view;
    }
}