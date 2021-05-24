package com.krishapps.kalakar;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            switchToUserDetails();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programmatic variables
        TextInputLayout phNum_textInputLayout = view.findViewById(R.id.phnum_outlinedTextField);
        TextInputLayout fakeOTP_textInputLayout = view.findViewById(R.id.fake_otp_outlinedTextField);
        EditText fakeOTP_editText = fakeOTP_textInputLayout.getEditText();
        EditText phNum_editText = phNum_textInputLayout.getEditText();
        Button verify_button = view.findViewById(R.id.verify_button);
        Button resend_button = view.findViewById(R.id.resend_button);
        Button otp_Button = view.findViewById(R.id.otp_button);

        // get firebase auth instance
        fAuth = FirebaseAuth.getInstance();

        // disable the animation of label/hint of material design's text field
        phNum_textInputLayout.setHint(null);
        phNum_editText.setHint("phone number (India)");

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

                phNum_textInputLayout.setVisibility(View.GONE);
                otp_Button.setVisibility(View.GONE);

                fakeOTP_textInputLayout.setVisibility(View.VISIBLE);
                verify_button.setVisibility(View.VISIBLE);
                resend_button.setVisibility(View.VISIBLE);
                resend_button.setEnabled(false);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resend_button.setEnabled(true);
            }
        };

        resend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumber(userPhoneNumber);
                resend_button.setEnabled(false);
            }
        });

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the OTP

                if(fakeOTP_editText.getText().toString().isEmpty()){
                    fakeOTP_textInputLayout.setError("Sorry ham khali hat nhi jane dege");
                    return;
                }

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, fakeOTP_editText.getText().toString());
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
                Toast.makeText(getContext(), "suffering form success", Toast.LENGTH_SHORT).show();
                switchToUserDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
//                .addToBackStack(null) // this line will not exist in the published app
                .commit();
    }
}
