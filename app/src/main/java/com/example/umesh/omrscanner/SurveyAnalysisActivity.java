package com.example.umesh.omrscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SurveyAnalysisActivity extends AppCompatActivity {

    SharedPreferences preferences;
    FirebaseDatabase database;
    DatabaseReference myRefResponses,myRefSurveys;
    String userId,surveyName,surveyId,classId;
    ArrayList<Integer> scaleType;
    ArrayList<String> surveyQuestions;
    Bundle bundle;
    ArrayList<SurveyAnalysisInfo> surveyAnalysisInfoArrayList;
    RecyclerView surveyAnalysisRecyclerView;
    SurveyAnalysisRecyclerAdapter surveyAnalysisRecyclerAdapter;
    LinearLayoutManager surveyAnalysisLinearLayoutManager;
    FloatingActionButton addResponseFab;
    int pos=0;
    int favourableResponses, unfavourableResponses, neutral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_analysis);

        surveyAnalysisInfoArrayList=new ArrayList<>();
        surveyQuestions=new ArrayList<>();
        scaleType=new ArrayList<>();
        preferences=getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences),MODE_PRIVATE);
        database=FirebaseDatabase.getInstance();
        myRefResponses=database.getReference("responses");
        myRefSurveys=database.getReference("surveys");
        userId=preferences.getString(getString(R.string.sp_user_id),"");
        bundle=getIntent().getExtras();
        surveyName=bundle.getString("survey_name");
        classId=bundle.getString("class_id");
        surveyAnalysisLinearLayoutManager=new LinearLayoutManager(getApplicationContext());
        surveyAnalysisRecyclerView=(RecyclerView) findViewById(R.id.survey_analysis_recycler_view);
        surveyAnalysisRecyclerAdapter=new SurveyAnalysisRecyclerAdapter(surveyAnalysisInfoArrayList);
        addResponseFab=(FloatingActionButton)findViewById(R.id.add_response_fab);



        surveyAnalysisRecyclerView.setLayoutManager(surveyAnalysisLinearLayoutManager);
        surveyAnalysisRecyclerView.setItemAnimator(new DefaultItemAnimator());
        surveyAnalysisRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        surveyAnalysisRecyclerView.setAdapter(surveyAnalysisRecyclerAdapter);


        myRefSurveys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp:dataSnapshot.getChildren()){
                    if(dsp.child("name").getValue().toString().equals(surveyName)){
                        surveyId=dsp.getKey();
                        for(DataSnapshot d:dsp.child("questions").getChildren()){
                            scaleType.add(Integer.parseInt(d.child("scale_type").getValue().toString()));
                            surveyQuestions.add(d.child("question_text").getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRefResponses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dsp=dataSnapshot.child(userId).child(surveyId).child(classId).child("responses");

                Toast.makeText(SurveyAnalysisActivity.this, surveyQuestions.size()+"", Toast.LENGTH_SHORT).show();
                while(pos<surveyQuestions.size()){
                    favourableResponses=0;
                    unfavourableResponses=0;
                    neutral=0;
                    for(DataSnapshot d:dsp.getChildren()){
                        if(!d.getKey().equals("resp_no")){
                            if(scaleType.get(pos)==0){
                                if(d.child(surveyQuestions.get(pos)).getValue().equals("1") || d.child(surveyQuestions.get(pos)).getValue().equals("2"))
                                    unfavourableResponses++;
                                else if(d.child(surveyQuestions.get(pos)).getValue().equals("4") || d.child(surveyQuestions.get(pos)).getValue().equals("5"))
                                    favourableResponses++;
                                else
                                    neutral++;
                            }
                            else{
                                if(d.child(surveyQuestions.get(pos)).getValue().equals("1") || d.child(surveyQuestions.get(pos)).getValue().equals("2"))
                                    favourableResponses++;
                                else if(d.child(surveyQuestions.get(pos)).getValue().equals("4") || d.child(surveyQuestions.get(pos)).getValue().equals("5"))
                                    unfavourableResponses++;
                                else
                                    neutral++;
                            }
                        }

                    }
                    SurveyAnalysisInfo surveyAnalysisInfo=new SurveyAnalysisInfo(surveyQuestions.get(pos),favourableResponses,unfavourableResponses,neutral);
                    surveyAnalysisInfoArrayList.add(surveyAnalysisInfo);
                    surveyAnalysisRecyclerAdapter.notifyDataSetChanged();
                    ++pos;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addResponseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SurveyAnalysisActivity.this, MainActivity.class);
                i.putStringArrayListExtra("questions_arraylist",surveyQuestions);
                i.putExtra("survey_id",surveyId);
                i.putExtra("class_id",classId);
                startActivity(i);
            }
        });


    }
}
