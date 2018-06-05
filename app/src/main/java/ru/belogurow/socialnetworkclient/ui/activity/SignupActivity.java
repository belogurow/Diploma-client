package ru.belogurow.socialnetworkclient.ui.activity;

import android.app.DatePickerDialog;
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
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Pattern;

import ru.belogurow.socialnetworkclient.R;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.dto.UserDto;
import ru.belogurow.socialnetworkclient.users.model.User;
import ru.belogurow.socialnetworkclient.users.model.UserProfile;
import ru.belogurow.socialnetworkclient.users.model.UserProfileRole;
import ru.belogurow.socialnetworkclient.users.viewModel.UserProfileViewModel;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;

public class SignupActivity extends AppCompatActivity {
    private UserViewModel mUserViewModel;
    private UserProfileViewModel mUserProfileViewModel;

    private Button mButtonSignUp;
    private TextInputLayout mTextInputUsername;
    private TextInputLayout mTextInputFullname;
    private TextInputLayout mTextInputPassword;

    private TextInputLayout mTextInputProfession;
    private TextInputLayout mTextInputDescription;
    private TextInputLayout mTextInputBirthDate;
    private Button mButtonPickBirthDate;

    private ToggleButton mToggleButtonDoctor;
    private ToggleButton mToggleButtonPatient;


    private ProgressBar mProgressBarSignUp;

    private Observer<Resource<UserDto>> signUpObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initFields();
        initDataListener();

        mButtonSignUp.setOnClickListener(v -> {
            if (validateFields()) {
                User newUser = new User();
                newUser.setUsername(mTextInputUsername.getEditText().getText().toString());
                newUser.setName(mTextInputFullname.getEditText().getText().toString());
                newUser.setPassword(mTextInputPassword.getEditText().getText().toString());

                mUserViewModel.registration(newUser).observe(this, signUpObserver);
            }
        });

        mToggleButtonDoctor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mToggleButtonPatient.setChecked(false);
            }
        });

        mToggleButtonPatient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mToggleButtonDoctor.setChecked(false);
            }
        });

        DatePickerDialog.OnDateSetListener dateListener = (view, year, month, dayOfMonth) -> {
            mTextInputBirthDate.getEditText().setText(getString(R.string.date_formatter, dayOfMonth, month + 1,  year));
            mTextInputBirthDate.setErrorEnabled(false);
        };

        mButtonPickBirthDate.setOnClickListener(view -> {
            new DatePickerDialog(this, dateListener, 1990, Calendar.MONTH, Calendar.DAY_OF_MONTH).show();
        });
    }

    private void initFields() {
        mButtonSignUp = findViewById(R.id.signup_button);
        mTextInputUsername = findViewById(R.id.username_signup_textinput);
        mTextInputFullname = findViewById(R.id.fullname_signup_textinput);
        mTextInputPassword = findViewById(R.id.password_signup_textinput);
        mProgressBarSignUp = findViewById(R.id.progressBar_signup);

        mTextInputProfession = findViewById(R.id.profession_signup_textinput);
        mTextInputDescription = findViewById(R.id.description_signup_textinput);
        mTextInputBirthDate = findViewById(R.id.birthdate_signup_textinput);
        mTextInputBirthDate.setEnabled(false);
        mButtonPickBirthDate = findViewById(R.id.pick_birthdate_singup_button);

        mToggleButtonDoctor = findViewById(R.id.pick_doctor_singup_togglebutton);
        mToggleButtonPatient = findViewById(R.id.pick_patient_singup_togglebutton);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);

        mProgressBarSignUp.setVisibility(View.GONE);
    }

    private void initDataListener() {
        signUpObserver = userResource -> {
            mProgressBarSignUp.setVisibility(View.VISIBLE);

            if (userResource == null) {
                Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                mProgressBarSignUp.setVisibility(View.GONE);
                return;
            }

            switch (userResource.getStatus()) {
                case SUCCESS:
                    uploadUserProfile(userResource.getData().getId());
                    break;
                case ERROR:
                    Toast.makeText(this, userResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, R.string.unknown_status, Toast.LENGTH_LONG).show();
            }

            mProgressBarSignUp.setVisibility(View.GONE);
        };
    }

    private void uploadUserProfile(UUID userId) {
        UserProfile userProfile = new UserProfile();

        // BirthDate
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
//        try {
//            Date date = formatter.parse(mTextInputBirthDate.getEditText().getText().toString());
//            userProfile.setBirthDate(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        // Profession
        if (mTextInputProfession.getEditText().getText().length() != 0) {
            userProfile.setProfession(mTextInputProfession.getEditText().getText().toString());
        } else {
            userProfile.setProfession(null);
        }

        // Description
        if (mTextInputDescription.getEditText().getText().length() != 0) {
            userProfile.setDescription(mTextInputDescription.getEditText().getText().toString());
        } else {
            userProfile.setDescription(null);
        }

        // Role
        if (mToggleButtonDoctor.isChecked()) {
            userProfile.setRole(UserProfileRole.DOCTOR);
        } else if (mToggleButtonPatient.isChecked()) {
            userProfile.setRole(UserProfileRole.PATIENT);
        }

        mUserProfileViewModel.addNewProfile(userId, userProfile).observe(this, userProfileDtoResource -> {
            if (userProfileDtoResource == null) {
                Toast.makeText(this, R.string.received_null_data, Toast.LENGTH_LONG).show();
                mProgressBarSignUp.setVisibility(View.GONE);
                return;
            }

            switch (userProfileDtoResource.getStatus()) {
                case SUCCESS:
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                    break;
                case ERROR:
                    Toast.makeText(this, userProfileDtoResource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, R.string.unknown_status, Toast.LENGTH_LONG).show();
            }

            mProgressBarSignUp.setVisibility(View.GONE);
        });
    }

    private boolean validateFields() {
        Pattern patternDate = Pattern.compile(
                "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                        + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                        + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                        + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

        String emptyField = getString(R.string.empty_field);

        EditText mEditTextUsername = mTextInputUsername.getEditText();
        if (mEditTextUsername != null) {
            if (mEditTextUsername.getText().length() == 0) {
                mTextInputUsername.setError(emptyField);
                return false;
            } else {
                mTextInputUsername.setErrorEnabled(false);
            }
        }

        EditText mEditTextFullname = mTextInputFullname.getEditText();
        if (mEditTextFullname != null) {
            if (mEditTextFullname.getText().length() == 0) {
                mTextInputFullname.setError(emptyField);
                return false;
            } else {
                mTextInputFullname.setErrorEnabled(false);
            }
        }

        EditText mEditTextPassword = mTextInputPassword.getEditText();
        if (mEditTextPassword != null) {
            if (mEditTextPassword.getText().length() == 0) {
                mTextInputPassword.setError(emptyField);
                return false;
            } else {
                mTextInputPassword.setErrorEnabled(false);
            }
        }

//        EditText mEditTextBirthDate = mTextInputBirthDate.getEditText();
//        if (mEditTextBirthDate != null) {
//            if (mEditTextBirthDate.getText().length() == 0) {
//                mTextInputBirthDate.setError(emptyField);
//                return false;
//            } else {
//                mTextInputBirthDate.setErrorEnabled(false);
////                if (patternDate.matcher(mEditTextBirthDate.getText()).matches()) {
////                    mTextInputBirthDate.setErrorEnabled(false);
////                } else {
////                    Toast.makeText(this, "Date format is YYYY-MM-DD", Toast.LENGTH_SHORT).show();
////                    return false;
////                }
//            }
//        }

        if (!mToggleButtonDoctor.isChecked() && !mToggleButtonPatient.isChecked()) {
            Toast.makeText(this, "Pick dockor or patient", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
