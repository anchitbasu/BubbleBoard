package com.example.umesh.omrscanner;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SurveyTemplateActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRefSurveys,myRefUsers,myRefResponses;
    RecyclerView surveyRecyclerView;
    SurveyTemplateRecyclerAdapter recyclerAdapter;
    ArrayList<SurveyInfo> surveyList;
    LinearLayoutManager mSurveyLinearLayoutManager;
    ArrayList<String> questionsArrayList,userClassIdArrayList;
    String surveyName,clickedSurveyName,clickedSurveyId,userId,selectedClassId;
    ListView surveyQuestionsListView;
    Spinner userClassesSelectSpinner;
    int numberOfQuestions;
    ArrayAdapter adapter;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_template);


        userClassIdArrayList=new ArrayList<>();
        preferences=getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences),MODE_PRIVATE);
        database=FirebaseDatabase.getInstance();
        myRefSurveys=database.getReference("surveys");
        myRefUsers=database.getReference("users");
        myRefResponses=database.getReference("responses");
        surveyRecyclerView=(RecyclerView)findViewById(R.id.survey_templates_recycler_view);
        surveyList=new ArrayList<SurveyInfo>();
        recyclerAdapter=new SurveyTemplateRecyclerAdapter(surveyList);
        mSurveyLinearLayoutManager=new LinearLayoutManager(this);
        questionsArrayList=new ArrayList<>();

        userId=preferences.getString(getString(R.string.sp_user_id),"");

        surveyRecyclerView.setLayoutManager(mSurveyLinearLayoutManager);
        surveyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        surveyRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        surveyRecyclerView.setAdapter(recyclerAdapter);


        myRefSurveys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()) {
                    if (d.child("status").getValue().toString().equals("1")) {
                        surveyName = d.child("name").getValue().toString();
                        numberOfQuestions = (int) d.child("questions").getChildrenCount();
//                    for(DataSnapshot dsp:d.child("questions").getChildren()){
//                        questionsArrayList.add(dsp.child("question_text").getValue().toString());
//                    }

                        SurveyInfo surveyInfo = new SurveyInfo(surveyName, questionsArrayList, numberOfQuestions);
                        surveyList.add(surveyInfo);
                        recyclerAdapter.notifyDataSetChanged();
                        questionsArrayList.clear();
                   }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerAdapter.SetOnItemClickListener(new SurveyTemplateRecyclerAdapter.RecyclerItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                TextView e=(TextView) view.findViewById(R.id.survey_title_value_text_view);
                clickedSurveyName=e.getText().toString();
                questionsArrayList.clear();
                Toast.makeText(SurveyTemplateActivity.this, clickedSurveyName+"", Toast.LENGTH_SHORT).show();
                myRefSurveys.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp:dataSnapshot.getChildren()){
                            if(dsp.child("name").getValue().toString().equals(clickedSurveyName)){
                                clickedSurveyId=dsp.getKey();
                                for(DataSnapshot d:dsp.child("questions").getChildren()){
                                    questionsArrayList.add(d.child("question_text").getValue().toString());
                                }
                            }
                        }
                        Toast.makeText(SurveyTemplateActivity.this, questionsArrayList.size()+"", Toast.LENGTH_SHORT).show();
                        //questionsArrayList.clear();

                        createDialog(questionsArrayList,clickedSurveyId);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                //Toast.makeText(SurveyTemplateActivity.this, questionsArrayList.size()+"", Toast.LENGTH_SHORT).show();


//                adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,questionsArrayList);
//                Dialog d=new Dialog(getApplicationContext());
//                d.setContentView(R.layout.survey_info_display_dialog_layout);
//                surveyQuestionsListView=(ListView)d.findViewById(R.id.class_names_display_list_view);
//                d.show();

            }
        });
    }

    public void createDialog(ArrayList<String> questions, String surveyId){
        adapter=new ArrayAdapter(SurveyTemplateActivity.this,R.layout.list_view_item_layout,questionsArrayList);
        final Dialog d=new Dialog(SurveyTemplateActivity.this);
        d.setContentView(R.layout.survey_info_display_dialog_layout);
        surveyQuestionsListView=(ListView)d.findViewById(R.id.survey_questions_display_list_view);
        surveyQuestionsListView.setAdapter(adapter);
        Button addButton=(Button)d.findViewById(R.id.survey_add_button);
        Button cancelButton=(Button)d.findViewById(R.id.cancel_survey_dialog_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClassDialog();
                d.dismiss();
            }
        });
        d.show();
    }

    public void selectClassDialog(){
        myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp:dataSnapshot.child(userId).child("classrooms").getChildren()){
                    userClassIdArrayList.add(dsp.getValue().toString());

                }

                adapter=new ArrayAdapter(SurveyTemplateActivity.this,android.R.layout.simple_spinner_dropdown_item,userClassIdArrayList);
                final Dialog d=new Dialog(SurveyTemplateActivity.this);
                d.setContentView(R.layout.survey_class_select_dialog_layout);
                userClassesSelectSpinner=(Spinner)d.findViewById(R.id.survey_class_select_spinner);
                Button classSelectDoneButton=(Button)d.findViewById(R.id.class_select_dialog_done_button);
                Button classSelectCancelButton=(Button)d.findViewById(R.id.class_select_dialog_cancel_button);
                userClassesSelectSpinner.setAdapter(adapter);
                final TextView displaySelectedSurveyClass=(TextView)d.findViewById(R.id.survey_selected_class_text_view);
                userClassesSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        displaySelectedSurveyClass.setText(userClassIdArrayList.get(position));
                        selectedClassId=userClassIdArrayList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                classSelectCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userClassIdArrayList.clear();
                        d.dismiss();
                    }
                });

                classSelectDoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRefResponses.child(userId).child(clickedSurveyId).child(selectedClassId).child("resp_no").setValue(0);
                        d.dismiss();
                        Intent intent=new Intent(SurveyTemplateActivity.this,DashboardActivity.class);
                        startActivity(intent);
                    }
                });

                d.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
