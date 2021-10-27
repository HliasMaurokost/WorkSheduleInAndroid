package com.example.wsg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Η ForgotPassword ανακτά το ξεχασμένο κωδικό ενός χρήστη και στέλνει e-mail
 * στη διεύθυνση που έχει δώσει ο χρήστης για να δώσει νέο κωδικό.
 * Επίσης,εμφανίζει ανάλογα μηνύματα επιτυχίας ή αποτυχίας της διαδικασίας ανάκτησης.
 * Εκτελεί έλεγχο εγκυρότητας για τα στοιχεία που προσφέρει ο χρήστης.
 */

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressbar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText)findViewById(R.id.email);
        resetPasswordButton = (Button)findViewById(R.id.resetPassword);
        progressbar = (ProgressBar)findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()){
            emailEditText.setError("Email Is Required");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please Provide Valid Email");
            emailEditText.requestFocus();
        }

        progressbar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check Your Email To Reset Password",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPassword.this,"TryAgain! ResetPassword Failed",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}