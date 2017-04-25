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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.data.UserProfileContract;
import com.knowme.knowmeapp.models.UserProfile;
import com.knowme.knowmeapp.utils.TextUtils;
import com.knowme.knowmeapp.utils.UIUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mFullNameEditText;
    EditText mUsernameEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mConfirmEditText;
    Button mRegisterButton;
    TextView mLoginTextView;
    ProgressDialog mProgressDialog;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

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

        setContentView(R.layout.activity_register);

        mFullNameEditText = (EditText) findViewById(R.id.et_full_name);
        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mEmailEditText = (EditText) findViewById(R.id.et_email);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mConfirmEditText = (EditText) findViewById(R.id.et_confirm_password);
        mRegisterButton = (Button) findViewById(R.id.bt_register);
        mLoginTextView = (TextView) findViewById(R.id.tv_login);

        mLoginTextView.setOnClickListener(this);

        mRegisterButton.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        int idOfViewThatWasClicked = v.getId();

        if (idOfViewThatWasClicked == R.id.tv_login) {
            finish();
        } else if (idOfViewThatWasClicked == R.id.bt_register) {
            register();
        }
    }

    private void register() {
        UIUtils.hideKeyboard(this);
        String email = mEmailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            mEmailEditText.setError(getString(R.string.authentication_field_empty));
            return;
        }

        if (email.indexOf(" ") > 0) {
            mEmailEditText.setError(getString(R.string.authenctication_error_username_empty_between_email));
            return;
        }
        if (!TextUtils.isValidEmail(email)) {
            mEmailEditText.setError(getString(R.string.authentication_error_invalid_email));
            return;
        }

        String password = mPasswordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            mPasswordEditText.setError(getString(R.string.authentication_field_empty));
            return;
        }

        String cPassword = mConfirmEditText.getText().toString().trim();
        if (cPassword.isEmpty()) {
            mConfirmEditText.setError(getString(R.string.authentication_field_empty));
            return;
        }

        boolean passwordMatch = cPassword.equals(password);
        if (!passwordMatch) {
            mConfirmEditText.setError(getString(R.string.register_password_does_not_match));
            return;
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.register_dialog_message));
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }
                            Snackbar.make(mRegisterButton, getString(R.string.register_something_went_wrong), Snackbar.LENGTH_LONG)
                                    .show();
                        } else {
                            saveUserProfile();
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }
                            startHomeActivity();
                        }
                    }
                }
        );

    }

    private void saveUserProfile() {
        String fullname = mFullNameEditText.getText().toString().trim();
        String username = mFullNameEditText.getText().toString().trim();
        UserProfile profile = new UserProfile(fullname, username);

        String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child(UserProfileContract.TABLE_NAME)
                .child(userId).setValue(profile);

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

    private void startHomeActivity() {
        Context context = this;
        Class componentToStart = HomeActivity.class;
        Intent intent = new Intent(context, componentToStart);
        startActivity(intent);


    }
}
