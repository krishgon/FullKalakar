package com.krishapps.kalakarbuisness.AuthenticationFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
import com.krishapps.kalakarbuisness.MainActivity;
import com.krishapps.kalakarbuisness.R;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {
    public LoginFragment(){
        super(R.layout.login_fragment);
    }

    String countryCode = "91";
    String artistPhoneNumber, verificationId;
    FirebaseAuth fAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;
    EditText OTP_editText;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//         check if the user is already signed in
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            switchToHomePage();
        }
        Log.d("krishlog", "onCreate: passed the on create of login frag");
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // assign ui elements to their programmatic variables
            EditText phNum_editText = view.findViewById(R.id.phNum_editText);
            OTP_editText = view.findViewById(R.id.otp_editText);
            Button sendOtp_button = view.findViewById(R.id.sendOtp_button);
            Button verifyOtp_button = view.findViewById(R.id.verifyOtp_button);
            TextView resendOTP_textView = view.findViewById(R.id.resendOTP_textView);

        // hide the OTP screen
            OTP_editText.setVisibility(View.GONE);
            verifyOtp_button.setVisibility(View.GONE);
            resendOTP_textView.setVisibility(View.GONE);

        // get firebase auth instance
            fAuth = FirebaseAuth.getInstance();

        // when send otp is clicked, send the user his tasty OTP after passing checks
            sendOtp_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(phNum_editText.getText().length() < 10){
                        phNum_editText.setError("You need atleast 10 digits in your phone number");
                    }
                    else{
                        phNum_editText.setError(null);
                        artistPhoneNumber = "+" + countryCode + phNum_editText.getText().toString();
                        verifyPhoneNumber(artistPhoneNumber);
                        Toast.makeText(getContext(), artistPhoneNumber, Toast.LENGTH_SHORT).show();
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

                    // show the enter otp screen
                        phNum_editText.setVisibility(View.GONE);
                        sendOtp_button.setVisibility(View.GONE);

                        OTP_editText.setVisibility(View.VISIBLE);
                        verifyOtp_button.setVisibility(View.VISIBLE);
                        resendOTP_textView.setVisibility(View.VISIBLE);
                        resendOTP_textView.setEnabled(false);
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                    resendOTP_textView.setEnabled(true);
                }
            };

        // resend otp when resend otp button will be clicked
            resendOTP_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyPhoneNumber(artistPhoneNumber);
                    resendOTP_textView.setEnabled(false);
                }
            });

        // verify otp when verify button is clicked
            verifyOtp_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check if otp typed
                        if(OTP_editText.getText().toString().isEmpty()){
                            OTP_editText.setError("Sorry ham khali hat nhi jane dege");
                            return;
                        }

                    // authenticate the artist
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
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setCallbacks(callbacks)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void authenticateUser(PhoneAuthCredential credential){
        fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                switchToArtistDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                OTP_editText.setError("Please enter the correct OTP");
            }
        });
    }

    public void switchToArtistDetails(){
        // make the fragment container view full screen
            LinearLayout appLogo_layout = getActivity().findViewById(R.id.appLogo_layout);
            appLogo_layout.setVisibility(View.GONE);

        // switch fragment
            Fragment fragment = new RoughDetailsFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.authentication_fragment_container_view, fragment)
                    .commit();
    }

    public void switchToHomePage(){
        Log.d("krishlog", "switchToHomePage: going to main activity");
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
