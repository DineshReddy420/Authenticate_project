package com.pujitha.projectfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    NavController navController;
    TextView txt_Register;

    EditText edt_email, edt_pass;
    Button btn_login;

    FirebaseUser currentUser;
    private FirebaseAuth fireAuth;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fireAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_Register = view.findViewById(R.id.txt_register);

        edt_email = view.findViewById(R.id.edt_login_email);
        edt_pass = view.findViewById(R.id.edt_login_password);
        btn_login = view.findViewById(R.id.btn_login);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        txt_Register.setOnClickListener(view1 -> {
            navController.navigate(R.id.registerFragment);
        });

        btn_login.setOnClickListener(view2 ->{

            if (!checkEmptyFields())
            {
                String email = edt_email.getText().toString();
                String pass = edt_pass.getText().toString();
                loginUser(email,pass);
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LoginFragment","onStart Called!");

        currentUser = fireAuth.getCurrentUser();

        if (currentUser != null)
        {
            updateUI(currentUser);
            Toast.makeText(getActivity().getApplicationContext(),"User Already Signing",Toast.LENGTH_LONG).show();
        }
    }

    public void loginUser(String email, String pass)
    {
        fireAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(getActivity(), task -> {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Login Success!", Toast.LENGTH_SHORT).show();
                        currentUser = fireAuth.getCurrentUser();
                        updateUI(currentUser);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),"Authenticate Failed!", Toast.LENGTH_SHORT).show();
                    }

                });
    }


    public void updateUI(FirebaseUser user)
    {
        Bundle b = new Bundle();
        b.putParcelable("user",user);
        navController.navigate(R.id.dashboardFragment,b);
    }

    public boolean checkEmptyFields()
    {
        if(TextUtils.isEmpty(edt_email.getText().toString()))
        {
            edt_email.setError("Email cannot be empty!");
            edt_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(edt_pass.getText().toString()))
        {
            edt_pass.setError("Password cannot be empty!");
            edt_pass.requestFocus();
            return true;
        }
        return false;
    }



}