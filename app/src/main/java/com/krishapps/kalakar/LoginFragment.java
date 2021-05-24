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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
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
//    FirebaseAuth fAuth;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
//    PhoneAuthProvider.ForceResendingToken token;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programmatic variables
        TextInputLayout phNum_textInputLayout = view.findViewById(R.id.phnum_outlinedTextField);
        EditText phNum_editText = phNum_textInputLayout.getEditText();
        Button otp_Button = view.findViewById(R.id.otp_button);


        // disable the animation of label/hint of material design's text field
        phNum_textInputLayout.setHint(null);
        phNum_editText.setHint("phone number (India)");

        otp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPhoneNumberValid(phNum_editText.getText())){
                    phNum_textInputLayout.setError("You need atleast 10 digits in your phone number");
                }else{
                    phNum_editText.setError(null);
                    Fragment fragment = new OTP_Fragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    userPhoneNumber = "+" + countryCode + phNum_editText.getText().toString();
                    Log.d("krishlog", "onClick: log is working");
                    Toast.makeText(getContext(), userPhoneNumber, Toast.LENGTH_SHORT).show();



                    fragmentManager.beginTransaction()
                            .replace(R.id.authentication_fragment_container_view, fragment)
                            .addToBackStack(null) // this line will not exist in the published app
                            .commit();
                }
            }
        });

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
    }

    private boolean isPhoneNumberValid(Editable text){
        return (text != null) && (text.length() == 10);
    }
}
