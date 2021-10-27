package com.example.wsg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Η MainActivity ιαχειρίζεται τη βασική και πρώτη φόρμα που εμφανίζεται στο χρήστη
 * κατά είσοδο του στο πρόγραμμα και ελέγχει τη διαδικασία της επιτυχημένης σύνδεσης του χρήστη
 * στη βάση (authentication).
 *
 * Επίσης,ανακατεθύνει στην ανάκτηση κωδικού,στην εγγραφή χρήστη και κάνει
 * έλεγχο εγκυρότητας για την ορθή είσοδο των στοιχείων σύνδεσης.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register,forgotPassword;
    private EditText editTextemail,editTextpassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextemail = (EditText) findViewById(R.id.email);
        editTextpassword = (EditText)findViewById(R.id.password);

        progressBar =(ProgressBar) findViewById (R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextemail.setError("Email Is Required");
            editTextemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please Enter a Valid Email");
            editTextemail.requestFocus();
            return;

        }

        if(password.isEmpty()){
            editTextpassword.setError("Min Password Length is 6 Characters");
            editTextpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                }else{
                    Toast.makeText(MainActivity.this,"Failed To Login! Failed Check Your Credentials",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}