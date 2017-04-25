package com.knowme.knowmeapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.utils.TextUtils;
import com.knowme.knowmeapp.utils.UIUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button mLoginButton;
    TextView mSignUp;
    EditText mUsernameEditText;
    EditText mPasswordEditText;
    ProgressDialog mProgressDialog;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        //FireBase
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    startHomeActivity();

                } else {
                    //user is signed out

                }

            }
        };
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.bt_login);
        mSignUp = (TextView) findViewById(R.id.tv_sign_up);
        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);

        mLoginButton.setOnClickListener(this);
        mSignUp.setOnClickListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        int idOfItemClicked = v.getId();
        if (idOfItemClicked == R.id.tv_sign_up) {
            signup();
        } else if (idOfItemClicked == R.id.bt_login) {
            login();
        }

    }

    private void login() {
        UIUtils.hideKeyboard(this);
        String email = mUsernameEditText.getText().toString().trim();
        if(email.isEmpty()){
            mUsernameEditText.setError(getString(R.string.authentication_field_empty));
            return;
        }

        if(email.indexOf(" ") >0){
            mUsernameEditText.setError(getString(R.string.authenctication_error_username_empty_between_email));
            return;
        }
        if(!TextUtils.isValidEmail(email)){
            mUsernameEditText.setError(getString(R.string.authentication_error_invalid_email));
            return;
        }

        String password = mPasswordEditText.getText().toString().trim();
        if(password.isEmpty()){
            mPasswordEditText.setError(getString(R.string.authentication_field_empty));
            return;
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.login_dialog_message));
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(mProgressDialog != null){
                    mProgressDialog.dismiss();
                }
                if(!task.isSuccessful()){
                    Snackbar.make(mLoginButton, getString(R.string.login_invalid_username_password), Snackbar.LENGTH_LONG)
                    .show();
                }else {
                   startHomeActivity();
                }

            }
        });


    }

    private void startHomeActivity() {
        Context context = this;
        Class component = HomeActivity.class;
        Intent HomeIntent = new Intent(context, component);
        startActivity(HomeIntent);
    }

    private void signup() {
        Context context = this;
        Class component = RegisterActivity.class;
        Intent signUpIntent = new Intent(context, component);
        startActivity(signUpIntent);
    }
}
