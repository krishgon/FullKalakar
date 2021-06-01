package com.krishapps.kalakar.AuthenticationFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.krishapps.kalakar.MainActivity;
import com.krishapps.kalakar.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {
    public LoginFragment(){
        super(R.layout.login_fragment);
    }

    String countryCode = "91";
    String userPhoneNumber, verificationId;
    FirebaseAuth fAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;
    long secondsLeft;
    CountDownTimer timer;
    NumberFormat f;
    TextInputLayout OTP_textInputLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if the user is already signed in
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                switchToHomePage();
            }
            Log.d("krishlog", "onCreate: passed the on create of login frag");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programmatic variables
            TextInputLayout phNum_textInputLayout = view.findViewById(R.id.phnum_outlinedTextField);
            OTP_textInputLayout = view.findViewById(R.id.otp_outlinedTextField);
            EditText OTP_editText = OTP_textInputLayout.getEditText();
            EditText phNum_editText = phNum_textInputLayout.getEditText();
            Button verify_button = view.findViewById(R.id.verify_button);
            TextView resend_button = view.findViewById(R.id.resendOTP_button_textView);
            Button otp_Button = view.findViewById(R.id.otp_button);
            TextView timer_textView = view.findViewById(R.id.timeRemaining_textView);
            LinearLayout phNum_layout = view.findViewById(R.id.phNum_linearLayout);
            LinearLayout OTP_layout = view.findViewById(R.id.OTP_linearLayout);
        
        // hide the OTP screen
            OTP_layout.setVisibility(View.GONE);

        // get firebase auth instance
            Log.d("krishlog", "onViewCreated: idhar pohoch gya mai");
            fAuth = FirebaseAuth.getInstance();

        // disable the animation of label/hint of material design's text field
            phNum_textInputLayout.setHint(null);
            phNum_editText.setHint("phone number (India)");
            OTP_textInputLayout.setHint(null);
            OTP_editText.setHint("Enter OTP");
        

        // set the countdown timer
            f = new DecimalFormat("00");

            timer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    secondsLeft = millisUntilFinished/1000;
                    timer_textView.setText("0:"+f.format(secondsLeft));
                }

                @Override
                public void onFinish() {
                    timer_textView.setText("0:00");
                    resend_button.setEnabled(true);
                    resend_button.setTextColor(getResources().getColor(R.color.gray_dark_slate));
                }
            };

        // check if the phone number is valid everytime user types a number
            phNum_editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.d("krishlog", "onKey: lagta hai apne button dabai hai");
                    if (isPhoneNumberValid(phNum_editText.getText())){
                        phNum_textInputLayout.setError(null);
                    }
                    return false;
                }
            });

        // when send otp is clicked, send the user his tasty OTP after passing checks
            otp_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isPhoneNumberValid(phNum_editText.getText())){
                        phNum_textInputLayout.setError("You need atleast 10 digits in your phone number");
                    }
                    else{
                        phNum_editText.setError(null);
                        userPhoneNumber = "+" + countryCode + phNum_editText.getText().toString();
                        verifyPhoneNumber(userPhoneNumber);
                        Toast.makeText(getContext(), userPhoneNumber, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        // define callbacks for otp verification
            callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    authenticateUser(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                    token = forceResendingToken;

                    phNum_layout.setVisibility(View.GONE);
                    OTP_layout.setVisibility(View.VISIBLE);

                    // start the 1 minute timer
                    timer.start();
                }
            };

        // resend otp when resend otp button will be clicked
            resend_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyPhoneNumber(userPhoneNumber);
                    resend_button.setEnabled(false);
                }
            });

        // verify otp when verify button is clicked
            verify_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get the OTP

                    if(OTP_editText.getText().toString().isEmpty()){
                        OTP_textInputLayout.setError("Sorry ham khali hat nhi jane dege");
                        return;
                    }

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, OTP_editText.getText().toString());
                    authenticateUser(credential);
                }
            });
    }

    public void verifyPhoneNumber(String phoneNum){
        // send OTP
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
                .setActivity(getActivity())
                .setPhoneNumber(phoneNum)
                .setTimeout(2L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void authenticateUser(PhoneAuthCredential credential){
        fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                timer.cancel();
                switchToUserDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                OTP_textInputLayout.setError("Please enter the correct OTP");
            }
        });
    }

    private boolean isPhoneNumberValid(Editable text){
        return (text != null) && (text.length() == 10);
    }

    public void switchToUserDetails(){
        Fragment fragment = new UserDetailsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.authentication_fragment_container_view, fragment)
                .commit();
    }

    public void switchToHomePage(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

// TODO: set a system on the otp text box such that when correct OTP is entered, the fragments should autonomously switch to userDetails fragment