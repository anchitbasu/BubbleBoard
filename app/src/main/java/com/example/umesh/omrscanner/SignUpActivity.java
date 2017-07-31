package com.example.umesh.omrscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText nameEditText,cityEditText,schoolNameEditText;
    FirebaseDatabase database;
    DatabaseReference myRefUsers;
    DatabaseReference myRefOrgs;
    String name,city,email,schoolName;
    SharedPreferences preferences;
    Spinner classSpinnerView;
    ArrayList<String> spinnerArrayList,selectedClassArrayList, selectedClassIdArrayList;
    ArrayAdapter<String> spinnerArrayAdapter, listViewArrayAdapter;
    String className;
    ListView selectedClassListView;
    int count=0;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please Wait...");
        selectedClassIdArrayList=new ArrayList<String>();
        selectedClassArrayList=new ArrayList<String>();
        selectedClassListView=(ListView)findViewById(R.id.class_names_display_list_view);
        spinnerArrayList=new ArrayList<String>();
        classSpinnerView=(Spinner)findViewById(R.id.class_select_spinner);
        preferences=getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);
        database=FirebaseDatabase.getInstance();
        myRefUsers=database.getReference("users");
        myRefOrgs=database.getReference("organisations");
        signUpButton=(Button)findViewById(R.id.sign_up_button);
        nameEditText=(EditText) findViewById(R.id.full_name_edit_text);
        cityEditText=(EditText)findViewById(R.id.city_edit_text);
        schoolNameEditText=(EditText)findViewById(R.id.school_name_edit_text);
        listViewArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,selectedClassArrayList);
        selectedClassListView.setAdapter(listViewArrayAdapter);
        spinnerArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinnerView.setAdapter(spinnerArrayAdapter);

        classSpinnerView.setVisibility(View.INVISIBLE);
        selectedClassListView.setVisibility(View.INVISIBLE);
        signUpButton.setText("Next");

        email=preferences.getString(getString(R.string.sp_email),"");


        progressDialog.show();
        myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp:dataSnapshot.getChildren()){
                    if(dsp.child("email").getValue().toString().equals(email)){
                        userId=dsp.getKey();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signUpButton.getText().toString().equals("Next")){
                    name=nameEditText.getText().toString();
                    city=cityEditText.getText().toString();
                    schoolName=schoolNameEditText.getText().toString();

                    myRefOrgs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dsp: dataSnapshot.child("tfi").child(city).child(schoolName).getChildren()){
                                className=dsp.child("grade").getValue().toString()+"  "+dsp.child("section").getValue().toString();
                                Toast.makeText(SignUpActivity.this, className, Toast.LENGTH_SHORT).show();
                                spinnerArrayList.add(className);
                                spinnerArrayAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    signUpButton.setText("SignUp");
                    classSpinnerView.setVisibility(View.VISIBLE);
                    selectedClassListView.setVisibility(View.VISIBLE);
                }
                else{
                    myRefOrgs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(String s:selectedClassArrayList){
                                for(DataSnapshot dsp:dataSnapshot.child("tfi").child(city).child(schoolName).getChildren()){
                                   if((dsp.child("grade").getValue().toString()+"  "+dsp.child("section").getValue().toString()).equals(s)){
                                       selectedClassIdArrayList.add(dsp.getKey().toString());
                                       Log.e("test",dsp.getKey().toString());

                                       count=1;





                                   }
                                }
                            }
                            myRefUsers.child(userId).child("city").setValue(city);
                            myRefUsers.child(userId).child("name").setValue(name);
                            myRefUsers.child(userId).child("school").setValue(schoolName);
                            myRefUsers.child(userId).child("organization").setValue("tfi");
                            Toast.makeText(SignUpActivity.this, selectedClassIdArrayList.size()+"", Toast.LENGTH_SHORT).show();
                            myRefUsers.child(userId).child("classrooms").setValue(selectedClassIdArrayList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Intent i=new Intent(SignUpActivity.this,DashboardActivity.class);
                    startActivity(i);

                }
            }
        });


        classSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++count>1) {
                    selectedClassArrayList.add(classSpinnerView.getSelectedItem().toString());
                    listViewArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
}
