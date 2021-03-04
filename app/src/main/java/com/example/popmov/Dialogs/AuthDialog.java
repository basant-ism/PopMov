package com.example.popmov.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.popmov.HomeActivity;
import com.example.popmov.Models.User;
import com.example.popmov.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AuthDialog extends Dialog {
    RelativeLayout relativeLayout;
    TextView tv_r_l;
    Button btnLogin,btnRegister;
    TextView tvNewUser,tvLogin;
    EditText etUserName,etEmail,etPassword;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Integer movieId=null;
    public AuthDialog(@NonNull Context context) {
        super(context);
    }
    public AuthDialog(@NonNull Context context, Integer movieId) {
        super(context);
        this.movieId=movieId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_dialog_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initViews();
        setListeners();




    }

    private void setListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);
                tvNewUser.setVisibility(View.GONE);
                tvLogin.setVisibility(View.VISIBLE);
                etUserName.setVisibility(View.VISIBLE);
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                tvLogin.setVisibility(View.GONE);
                tvNewUser.setVisibility(View.VISIBLE);
                etUserName.setVisibility(View.GONE);

            }
        });
    }

    private void register() {
        if(mAuth.getCurrentUser()!=null)
        {
            Toast.makeText(getContext(),"A User Already SignedIn",Toast.LENGTH_LONG).show();
            return;
        }
        String password=etPassword.getText().toString().trim();
        String email=etEmail.getText().toString().trim();
        String userName=etUserName.getText().toString().trim();
        if(TextUtils.isEmpty(userName))
        {
            Toast.makeText(getContext(),"Enter User Name",Toast.LENGTH_LONG).show();
        }
        else  if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getContext(),"Enter Email Id",Toast.LENGTH_LONG).show();
        }
        else   if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getContext(),"Enter Password",Toast.LENGTH_LONG).show();
        }
        else {
            relativeLayout.setVisibility(View.VISIBLE);
            tv_r_l.setText("Creating Account...");
            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    User user=new User();
                    user.setUname(userName);
                    if(movieId!=null)
                    {
                        List<Integer >list=new ArrayList<>();
                        list.add(movieId);
                        user.setMovies_id(list);//user.setMeals_id(list);
                    }
                    db.collection("users").document(mAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent=new Intent(getContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getContext().startActivity(intent);
                            dismiss();
                            relativeLayout.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            relativeLayout.setVisibility(View.GONE);
                            dismiss();
                        }
                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    relativeLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Something went Wrong...please try again",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void login() {
        if(mAuth.getCurrentUser()!=null)
        {
            Toast.makeText(getContext(),"A User Already SignedIn",Toast.LENGTH_LONG).show();
            return;
        }
        String password=etPassword.getText().toString().trim();
        String email=etEmail.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getContext(),"Enter Email Id",Toast.LENGTH_LONG).show();
        }
        else   if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getContext(),"Enter Password",Toast.LENGTH_LONG).show();
        }
        else {
            relativeLayout.setVisibility(View.VISIBLE);
            tv_r_l.setText("Logining...");
                mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(movieId!=null)
                        {
                            db.collection("users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()) {
                                        User user = documentSnapshot.toObject(User.class);
                                        List<Integer> movies_ids = user.getMovies_id();
                                        if ( movies_ids != null) {
                                            if (! movies_ids.contains(movieId)) {
                                                movies_ids.add(movieId);
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("movie_id",  movies_ids);
                                                db.collection("users").document(mAuth.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent intent=new Intent(getContext(), HomeActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        getContext().startActivity(intent);
                                                        dismiss();
                                                        relativeLayout.setVisibility(View.GONE);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        relativeLayout.setVisibility(View.GONE);
                                                        dismiss();
                                                    }
                                                });
                                            }
                                            relativeLayout.setVisibility(View.GONE);
                                            dismiss();

                                        } else {
                                            relativeLayout.setVisibility(View.GONE);
                                            dismiss(

                                            );
                                        }
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    relativeLayout.setVisibility(View.GONE);
                                    dismiss();
                                }
                            });
                        }
                        else{
                            relativeLayout.setVisibility(View.GONE);
                            dismiss();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        relativeLayout.setVisibility(View.GONE);
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

    private void initViews() {
        btnLogin=findViewById(R.id.btn_login);
        btnRegister=findViewById(R.id.btn_register);
        tvNewUser=findViewById(R.id.tv_new_user);
        etEmail=findViewById(R.id.et_user_email);
        etUserName=findViewById(R.id.et_user_name);
        etPassword=findViewById(R.id.et_password);
        tvLogin=findViewById(R.id.tv_login);

        relativeLayout=findViewById(R.id.relative_layout);
        tv_r_l=findViewById(R.id.tv_l_r);


        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }
}
