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

import com.example.wsg.helpers.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
/**
 * Η κλάση RegisterUser ολοκληρώνει την εγγραφή του χρήστη και εισάγει τα στοιχεία του
 * στη βάση δεδομένων,με ανάλογα μηνύματα επιτυχίας ή αποτυχίας.
 *
 * Επίσης,κάνει ελέγχους εγκυρότητας των στοιχείων που πληκτρολογεί ο χρήστης
 * όπως για κενό όνομα,e-mail,ηλικία,για κωδικό πρόσβασης μικρότερο των 6 χαρακτήρων,για ορθή
 * διεύθυνση e-mail κτλ.
 */

@SuppressWarnings("java:S110")
public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private TextView banner,register;
    private EditText editTextfullname,editTextage,editTextemail,editTextpassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        editTextfullname = (EditText) findViewById(R.id.fullname);
        editTextage = (EditText) findViewById(R.id.age);
        editTextemail = (EditText) findViewById(R.id.email);
        editTextpassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.register:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {

        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String fullName = editTextfullname.getText().toString().trim();
        String age = editTextage.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextfullname.setError("FullName Is Required");
            editTextfullname.requestFocus();
            return;
        }
        if(age.isEmpty()){
            editTextage.setError("Age Is Required");
            editTextage.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextemail.setError("Email Is Required");
            editTextemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please Provide Valid Email");
            editTextemail.requestFocus();
        }
        if(password.isEmpty()){
            editTextpassword.setError("Password Is Required");
            editTextpassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextpassword.setError("Min Password Length Should 6 Characters");
            editTextpassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(fullName, age, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "Users Has Been Registered Successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);


                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed To Register! Try Again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterUser.this, "Failed To Register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

    }
}