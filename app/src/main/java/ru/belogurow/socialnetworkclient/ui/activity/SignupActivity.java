package ru.belogurow.socialnetworkclient.ui.activity;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class SignupActivity extends AppCompatActivity {
    private UserViewModel mUserViewModel;

    private Button mButtonSignUp;
    private TextInputLayout mTextInputUsername;
    private TextInputLayout mTextInputFullname;
    private TextInputLayout mTextInputPassword;
    private ProgressBar mProgressBarSignUp;

    private Observer<Resource<User>> signUpObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initFields();
        initDataListener();

        mButtonSignUp.setOnClickListener(v -> {
            if (validateFields()) {
                mProgressBarSignUp.setVisibility(View.VISIBLE);

                User newUser = new User();
                newUser.setUsername(mTextInputUsername.getEditText().getText().toString());
                newUser.setName(mTextInputFullname.getEditText().getText().toString());
                newUser.setPassword(mTextInputPassword.getEditText().getText().toString());

                mUserViewModel.registration(newUser).observe((LifecycleOwner) v.getContext(), signUpObserver);
            }
        });
    }

    private void initFields() {
        mButtonSignUp = findViewById(R.id.signup_button);
        mTextInputUsername = findViewById(R.id.username_signup_textinput);
        mTextInputFullname = findViewById(R.id.fullname_signup_textinput);
        mTextInputPassword = findViewById(R.id.password_signup_textinput);
        mProgressBarSignUp = findViewById(R.id.progressBar_signup);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        mProgressBarSignUp.setVisibility(View.GONE);
    }

    private void initDataListener() {
        signUpObserver = userResource -> {
            if (userResource == null) {
                Toast.makeText(this, "Received null data", Toast.LENGTH_LONG).show();
                return;
            }

            switch (userResource.status) {
                case SUCCESS:
//                    Toast.makeText(this, userResource.data.toString(), Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                    break;
                case ERROR:
                    Toast.makeText(this, userResource.message, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Unknown status", Toast.LENGTH_LONG).show();
            }

            mProgressBarSignUp.setVisibility(View.GONE);
        };
    }

    private boolean validateFields() {
        EditText mEditTextUsername = mTextInputUsername.getEditText();
        EditText mEditTextFullname = mTextInputFullname.getEditText();
        EditText mEditTextPassword = mTextInputPassword.getEditText();

        String emptyField = getString(R.string.empty_field);

        if (mEditTextUsername != null) {
            if (mEditTextUsername.getText().length() == 0) {
                mTextInputUsername.setError(emptyField);
                return false;
            } else {
                mTextInputUsername.setErrorEnabled(false);
            }
        }

        if (mEditTextFullname != null) {
            if (mEditTextFullname.getText().length() == 0) {
                mTextInputFullname.setError(emptyField);
                return false;
            } else {
                mTextInputFullname.setErrorEnabled(false);
            }
        }

        if (mEditTextPassword != null) {
            if (mEditTextPassword.getText().length() == 0) {
                mTextInputPassword.setError(emptyField);
                return false;
            } else {
                mTextInputUsername.setErrorEnabled(false);
            }
        }

        return true;
    }
}
