package ru.belogurow.socialnetworkclient.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.common.web.NetworkStatus;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel mUserViewModel;

    private Button mButtonLogin;
    private Button mButtonSignUp;
    private TextInputLayout mTextInputUsername;
    private TextInputLayout mTextInputPassword;
    private ProgressBar mProgressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFields();


        Observer<Resource<User>> loginObserver = user -> {
            if (user != null) {
                if (user.data != null && user.status == NetworkStatus.SUCCESS) {
                    Toast.makeText(this, user.data.toString(), Toast.LENGTH_LONG).show();
                }

                if (user.message != null && user.status == NetworkStatus.ERROR) {
                    Toast.makeText(this, user.message, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Client error", Toast.LENGTH_LONG).show();
            }


            mProgressBarLogin.setVisibility(View.GONE);
        };


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    mProgressBarLogin.setVisibility(View.VISIBLE);

                    // Parse fields for create new User object
                    User newUser = new User();
                    newUser.setUsername(mTextInputUsername.getEditText().getText().toString());
                    newUser.setPassword(mTextInputPassword.getEditText().getText().toString());

                    mUserViewModel.login(newUser).observe((LifecycleOwner) v.getContext(), loginObserver);
                }
            }
        });

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Не реализовано", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFields() {
        mButtonLogin = findViewById(R.id.login_button);
        mButtonSignUp = findViewById(R.id.signup_button);
        mTextInputUsername = findViewById(R.id.login_textinput);
        mTextInputPassword = findViewById(R.id.password_textinput);
        mProgressBarLogin = findViewById(R.id.progressBar_login);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        mProgressBarLogin.setVisibility(View.GONE);
    }

    private boolean validateFields() {
        EditText mEditTextUsername = mTextInputUsername.getEditText();
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

        if (mEditTextPassword != null) {
            if (mEditTextPassword.getText().length() == 0) {
                mEditTextPassword.setError(emptyField);
                return false;
            } else {
                mTextInputUsername.setErrorEnabled(false);
            }
        }

        return true;
    }
}
