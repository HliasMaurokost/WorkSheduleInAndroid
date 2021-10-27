package com.example.wsg;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wsg.helpers.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Είναι η φόρμα που διαχειρίζεται την εμφάνιση των βασικών
 * κουμπιών λειτουργίας όπως την αποσύνδεση,την ανάκτηση δεδομένων υπαλλήλων από τη βάση,
 * τη γεννήτρια προγράμματος κτλ.
 *
 * Επίσης,ολοκληρώνει την εισαγωγή δεδομένων στη βάση.
 */

public class ProfileActivity extends AppCompatActivity {

    private Button logout;
    private Button btnInsertData;
    private Button btnRetreiveData;
    private Button btnSchedule;
    private Button btnViewSchedule;
    private EditText kodEmployee;
    private EditText fullName;
    private EditText hoursEmployee;
    private EditText weeksOffEmployee;

    private DatabaseReference employeeDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        kodEmployee  = (EditText) findViewById(R.id.kodEmployee);
        fullName  = (EditText) findViewById(R.id.fullName);
        hoursEmployee = (EditText) findViewById(R.id.hoursEmployee);
        weeksOffEmployee = (EditText) findViewById(R.id.weeksOff);
        btnInsertData = (Button) findViewById(R.id.btnInsertData);
        btnRetreiveData = (Button) findViewById(R.id.btnRetreiveData);
        btnSchedule = (Button) findViewById(R.id.btnSchedule);
        btnViewSchedule = (Button) findViewById(R.id.btnViewSchedule);



        employeeDbRef = FirebaseDatabase.getInstance().getReference().child("Employees");

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });
        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertEmployeeData();
            }
        });
        btnRetreiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(ProfileActivity.this,RetrieveDataActivity.class));
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(ProfileActivity.this,Schedule.class));
            }
        });

        btnViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(ProfileActivity.this,ViewSchedule.class));
            }
        });


    }

    private void insertEmployeeData() {
        int kodID = Integer.parseInt(kodEmployee.getText().toString());
        String name = fullName.getText().toString();
        int hours = Integer.parseInt(hoursEmployee.getText().toString());
        int weeksOff = Integer.parseInt(weeksOffEmployee.getText().toString());
        Employee employee = new Employee(kodID,name,hours,weeksOff);

        //Use of .child() to avoid UID from firebase
        employeeDbRef.child(String.valueOf(employee.getKodID())).setValue(employee);
        Toast.makeText(ProfileActivity.this,"Data Inserted",Toast.LENGTH_SHORT).show();
    }
}
