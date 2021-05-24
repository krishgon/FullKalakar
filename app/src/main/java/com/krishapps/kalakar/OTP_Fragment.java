package com.krishapps.kalakar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class OTP_Fragment extends Fragment {
    public OTP_Fragment() {
        super(R.layout.otp_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // assign ui elements to their programatic variables
        TextInputLayout OTP_textInputLayout = view.findViewById(R.id.otp_outlinedTextField);
        EditText OTP_editText = OTP_textInputLayout.getEditText();
        TextView timeRemaining_textView = view.findViewById(R.id.timeRemaining_textView);
        TextView resendOTP_button_textView = view.findViewById(R.id.resendOTP_button_textView);

        //TODO: do something to not repeat the code below for disabling the animation of text field

        // disable the animation of label/hint of material design's text field
        OTP_textInputLayout.setHint(null);
        OTP_editText.setHint("Enter OTP");
        //TODO: set a 1 minute timer and change the text in time remaining text view in accordance to the time left on timer

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isOTP_valid(s)){
                    Fragment fragment = new UserDetailsFragment();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.authentication_fragment_container_view, fragment)
                            .addToBackStack(null) // this line will not exist in the published app
                            .commit();
                }
            }
        };

        OTP_editText.addTextChangedListener(textWatcher);
    }

    private boolean isOTP_valid(Editable text){
        String otpText = text.toString();
        return otpText.equals("1234");
    }
}
