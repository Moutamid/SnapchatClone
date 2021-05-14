package com.gsrathoreniks.facefilter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText emailEditText, answerEditText;
    private Button submitButton;
    private Utils utils = new Utils();
    private LinearLayout emailLayout;
    private RelativeLayout changePasswordLayout, securityQuestionLayout;
    private static final String PACKAGE_NAME = "dev.moutamid.facefilter.forgot";

    private static final String FORGOT_STR = "forgot";
    private static final String LOGIN_STR = "login";
    private static final String SIGNUP_STR = "signup";

    private String currentToken;

    private void getCurrentToken() {
        Intent intent = getIntent();
        currentToken = intent.getStringExtra("token");
    }

    private String getStoredString1(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, "Error");
    }

    private void storeString1(Context context, String name, String object) {
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(name, object).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailLayout = findViewById(R.id.email_layout_forgot);
        emailEditText = findViewById(R.id.email_activity_forgot);
        answerEditText = findViewById(R.id.answer_forgot);
        submitButton = findViewById(R.id.submit_btn_forgot);
        securityQuestionLayout = findViewById(R.id.security_questions_layout);
        changePasswordLayout = findViewById(R.id.change_password_layout);
//original_email
        getCurrentToken();

        hideEmailLayoutIfPossible();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentToken.equals(LOGIN_STR) || currentToken.equals(SIGNUP_STR)) {

                    saveAnswer();

                } //else { checkAnswer(); }

            }
        });

        checkAnswer();
    }

    private void checkAnswer() {

//        if (emailEditText.getText().toString().equals("") || TextUtils.isEmpty(emailEditText.getText().toString())) {
//
//            emailEditText.setError("Please enter your email!");
//            emailEditText.requestFocus();
//            return;
//
//        }
//
//        if (answerEditText.getText().toString().equals("") || TextUtils.isEmpty(answerEditText.getText().toString())) {
//
//            answerEditText.setError("Please enter your answer!");
//            answerEditText.requestFocus();
//            return;
//
//        }
//
//        if (answerEditText.getText().toString().trim().equals(
//                getStoredString1(ForgotPasswordActivity.this, "user_answer")
//        )) {
        // ANSWER IS RIGHT

        showChangePasswordsLayout();
//
//        } else {
//            // ANSWER IS FALSE
//            answerEditText.setError("Your answer is wrong!");
//            answerEditText.requestFocus();
//        }

    }

    private void showChangePasswordsLayout() {
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog.dismiss();
//
//                Toast.makeText(ForgotPasswordActivity.this, "Your answer is right!", Toast.LENGTH_SHORT).show();
//                Toast.makeText(ForgotPasswordActivity.this, "Please enter a new password!", Toast.LENGTH_SHORT).show();
//securityQuestionLayout.setVisibility(View.GONE);
//                changePasswordLayout.setVisibility(View.VISIBLE);
//            }
//        }, 2000);

        findViewById(R.id.change_password_btn_activity_forgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText passwordEditText = findViewById(R.id.password_activity_forgot);
                final EditText confirmPasswordEditText = findViewById(R.id.confirm_password_activity_forgot);


                if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    passwordEditText.setError("Please enter a password!");
                    passwordEditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(confirmPasswordEditText.getText().toString())) {
                    passwordEditText.setError("Please confirm your password!");
                    passwordEditText.requestFocus();
                    return;
                }

                storeString1(ForgotPasswordActivity.this, "original_password", passwordEditText.getText().toString().trim());

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        passwordEditText.setText("");
                        confirmPasswordEditText.setText("");
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Your password is changed!", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }, 3000);

//                Toast.makeText(ForgotPasswordActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
//
//                new Utils().storeBoolean(ForgotPasswordActivity.this, "isLoggedIn", true);
//
//                Intent intent = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                finish();
//                startActivity(intent);

            }
        });
    }

    private void saveAnswer() {

        if (answerEditText.getText().toString().equals("") || TextUtils.isEmpty(answerEditText.getText().toString())) {

            answerEditText.setError("Please enter your answer!");
            answerEditText.requestFocus();
            return;

        }

        storeString1(ForgotPasswordActivity.this, "user_answer", answerEditText.getText().toString().trim());
        Intent intent = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private void hideEmailLayoutIfPossible() {
        if (currentToken.equals(LOGIN_STR) || currentToken.equals(SIGNUP_STR))
            emailLayout.setVisibility(View.GONE);
        else {
            securityQuestionLayout.setVisibility(View.GONE);
            changePasswordLayout.setVisibility(View.VISIBLE);
        }
    }
}