package com.gsrathoreniks.facefilter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginBtn;
    private ProgressDialog mProgressDialog;
//    private DatabaseReference mDatabaseUsers;
//    private Boolean isOnline = false;
//    private SharedPreferences sharedPreferences;
//    private EditText userNameEditText_RSD;

//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null){
//            finish();
//            Intent intent = new Intent(ActivityLogin.this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            return;
//        }

        findViewById(R.id.signBtn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
            }
        });

        findViewById(R.id.forgot_password_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ForgotPasswordActivity.class);
                intent.putExtra("token", "forgot");
                startActivity(intent);
            }
        });

//        findViewById(R.id.google_btn_activity_login).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ActivityLogin.this, "You are signed in using Google+ Bla Bla Bla", Toast.LENGTH_SHORT).show();
//            }
//        });


//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(USERS);
//        mDatabaseUsers.keepSynced(true);


//        sharedPreferences = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Signing you in...");

        // Initializing Views
        initViews();


    }

    @Override
    protected void onResume() {
        super.onResume();

        emailEditText.setText("");
        passwordEditText.setText("");
    }

    private View.OnClickListener loginBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

                    signInUserWithNameAndPassword(username, password);

                } else if (TextUtils.isEmpty(username)) {

                    emailEditText.setError("Please enter username");
                    emailEditText.requestFocus();

                } else if (TextUtils.isEmpty(password)) {

                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();

                }
            }
        };
    }

    private String getStoredString1(Context context, String name) {
        String PACKAGE_NAME = "dev.moutamid.facefilter.forgot";
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, "Error");
    }

    private void signInUserWithNameAndPassword(final String username, final String password) {

        mProgressDialog.show();

//        if (isOnline) {

        String email = username;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //if Email Address is Invalid..

            mProgressDialog.dismiss();
            emailEditText.setError("Email is not valid. Make sure no spaces and special characters are included");
            emailEditText.requestFocus();
        } else {

            if (!getStoredString1(ActivityLogin.this, "original_password").equals(password)){
                mProgressDialog.dismiss();
                Toast.makeText(this, "Password is not correct!", Toast.LENGTH_SHORT).show();
                return;
            }

//            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {

//                    if (task.isSuccessful()) {

            storeDetails();

            mProgressDialog.dismiss();

            finish();
            Intent intent = new Intent(ActivityLogin.this, ForgotPasswordActivity.class);
//            Intent intent = new Intent(ActivityLogin.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("token", "login");
            startActivity(intent);

            Toast.makeText(ActivityLogin.this, "You are logged in!", Toast.LENGTH_SHORT).show();

            new Utils().storeBoolean(ActivityLogin.this, "isLoggedIn", true);
//                            checkUserExist(username, password);

//                    } else {

//                        mProgressDialog.dismiss();
//                        Toast.makeText(ActivityLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }

//                }
//            });

        }
//        } else {
//
//            mProgressDialog.dismiss();
//            Toast.makeText(this, "You are not online", Toast.LENGTH_SHORT).show();
//        }
    }

    private void storeDetails() {
        new Utils().storeString(ActivityLogin.this, "original_email", emailEditText.getText().toString().trim());
    }

    private void initViews() {

        emailEditText = findViewById(R.id.email_edittext_activity_login);
        passwordEditText = findViewById(R.id.password_edittext_activity_login);
//        showPasswordBtn = findViewById(R.id.showPassword_btn_login);
//        forgotPasswordBtn = findViewById(R.id.forgotPassword_login_textView);
//        goToSignUpActivityBtn = findViewById(R.id.goTo_signUp_activity_textView);
        loginBtn = findViewById(R.id.login_btn_activity_login);
        loginBtn.setOnClickListener(loginBtnListener());
//        progressLayout = findViewById(R.id.progressBar_layout_login);
        // For Dialog
//        mDialog = new Dialog(this);
    }

}