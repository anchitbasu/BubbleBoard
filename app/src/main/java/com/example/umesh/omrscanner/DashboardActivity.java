package com.example.umesh.omrscanner;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    FirebaseDatabase database;
    DatabaseReference myRefSurveys,myRefUsers,myRefResponses,myRefOrg;
    RecyclerView ongoingSurveysRecyclerView;
    RecyclerView closedSurveysRecyclerView;
    RecyclerAdapter ongoingSurveysRecyclerAdapter,closedSurveysRecyclerAdapter;
    LinearLayoutManager mOngoingSurveysLinearLayoutManager, mClosedSurveysLinearLayoutManager;
    ArrayList<SurveysResponseInfo> ongoingSurveysArrayList;
    ArrayList<SurveysResponseInfo> closedSurveysArrayList;
    int status;
    ArrayList<String> questions;
    ArrayList<String> questionType;
    FloatingActionButton addFAB;
    SharedPreferences pref;
    String USERID,CITY,SCHOOL,ORGANIZATION;
    String surveyTitle,surveyID,classId,className;
    Long numberOfResponses=0l;
    String a;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen surveyTitle. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        //initialising
        pref=getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE);
        USERID=pref.getString(getString(R.string.sp_user_id),"");
        addFAB=(FloatingActionButton) findViewById(R.id.add_floating_action_button);
        ongoingSurveysArrayList=new ArrayList<>();
        closedSurveysArrayList=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        myRefSurveys=database.getReference("surveys");
        myRefUsers=database.getReference("users");
        myRefResponses=database.getReference("responses");
        ongoingSurveysRecyclerAdapter=new RecyclerAdapter(ongoingSurveysArrayList);
        closedSurveysRecyclerAdapter=new RecyclerAdapter(closedSurveysArrayList);
        ongoingSurveysRecyclerView=(RecyclerView)findViewById(R.id.ongoing_surveys_recycler_view);
        closedSurveysRecyclerView=(RecyclerView)findViewById(R.id.closed_surveys_recycler_view);
        mOngoingSurveysLinearLayoutManager=new LinearLayoutManager(getApplicationContext());
        mClosedSurveysLinearLayoutManager=new LinearLayoutManager(getApplicationContext());
        questions=new ArrayList<String>();
        questionType=new ArrayList<String>();


        ongoingSurveysRecyclerView.setLayoutManager(mOngoingSurveysLinearLayoutManager);
        ongoingSurveysRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ongoingSurveysRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        ongoingSurveysRecyclerView.setAdapter(ongoingSurveysRecyclerAdapter);


        closedSurveysRecyclerView.setLayoutManager(mClosedSurveysLinearLayoutManager);
        closedSurveysRecyclerView.setItemAnimator(new DefaultItemAnimator());
        closedSurveysRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        closedSurveysRecyclerView.setAdapter(closedSurveysRecyclerAdapter);



//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
//        mNavigationDrawerFragment.setUp(
//                R.id.navigation_drawer,
//                (DrawerLayout) findViewById(R.id.drawer_layout));

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SurveyTemplateActivity.class);
                startActivity(intent);
            }
        });




//        myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot dsp:dataSnapshot.getChildren()){
//                    Log.e("test","inside if");
//                    if(USERID.equals(dsp.getKey())){
//                        CITY=dsp.child("city").getValue().toString();
//                        SCHOOL=dsp.child("school").getValue().toString();
//                        ORGANIZATION=dsp.child("organization").getValue().toString();
//                        Toast.makeText(DashboardActivity.this, CITY+"", Toast.LENGTH_SHORT).show();
//                        SharedPreferences.Editor editor=pref.edit();
//                        editor.putString(getString(R.string.sp_city),CITY);
//                        editor.putString(getString(R.string.sp_school),SCHOOL);
//                        editor.putString(getString(R.string.sp_org),ORGANIZATION);
//                        editor.apply();
//                        Toast.makeText(DashboardActivity.this, CITY+"      Inside" , Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                }
//
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        CITY=pref.getString(getString(R.string.sp_city),"");
        SCHOOL=pref.getString(getString(R.string.sp_school),"");
        ORGANIZATION=pref.getString(getString(R.string.sp_org),"");



        //fetching data from firebase
        final ProgressDialog dialog = new ProgressDialog(DashboardActivity.this);
        dialog.setMessage("Please Wait...");
        dialog.show();
        myRefResponses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp: dataSnapshot.getChildren()){

                    if(dsp.getKey().equals(USERID)){
                        for(DataSnapshot d:dsp.getChildren()){
                            a=d.getKey();
                            final DataSnapshot y=d;

                            myRefSurveys.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnap) {
                                    for(DataSnapshot ds:dataSnap.getChildren()){

                                        //dialog.show();
                                        if(ds.getKey().equals(y.getKey())){

                                            //if(dsp.child("status").getValue().toString().equals("1")) {
                                            surveyTitle = ds.child("name").getValue().toString();
                                            status=Integer.parseInt(ds.child("status").getValue().toString());
                                            populateRecyclerView(surveyTitle,status,y);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
//                            Toast.makeText(DashboardActivity.this, surveyTitle+"   "+status, Toast.LENGTH_SHORT).show();
//                            for(DataSnapshot ds:d.getChildren()){
//                                classId=ds.getKey();
//
//                                numberOfResponses=ds.getChildrenCount();
//                                SurveysResponseInfo surveysResponseInfo=new SurveysResponseInfo(surveyTitle,classId,status,numberOfResponses);
//                                if(status==1){
//                                    ongoingSurveysArrayList.add(surveysResponseInfo);
//                                    ongoingSurveysRecyclerAdapter.notifyDataSetChanged();
//                                }
//                                else{
//                                    closedSurveysArrayList.add(surveysResponseInfo);
//                                    closedSurveysRecyclerAdapter.notifyDataSetChanged();
//                                }
//                            }
                        }
                    }

                }
                dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ongoingSurveysRecyclerAdapter.SetOnItemClickListener(new RecyclerAdapter.SurveyResponseRecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i=new Intent(DashboardActivity.this,SurveyAnalysisActivity.class);
                i.putExtra("survey_name",ongoingSurveysArrayList.get(position).getTitle());
                i.putExtra("class_id",ongoingSurveysArrayList.get(position).getClassName());
                startActivity(i);
            }
        });

        closedSurveysRecyclerAdapter.SetOnItemClickListener(new RecyclerAdapter.SurveyResponseRecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i=new Intent(DashboardActivity.this,SurveyAnalysisActivity.class);
                i.putExtra("survey_name",closedSurveysArrayList.get(position).getTitle());
                i.putExtra("class_id",closedSurveysArrayList.get(position).getClassName());
                startActivity(i);
            }
        });


//        myRefSurveys.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot dsp:dataSnapshot.getChildren()){
//                    //status=dsp.child("status").getValue();
//                    Toast.makeText(DashboardActivity.this, dsp.child("status").getValue()+"", Toast.LENGTH_SHORT).show();
//                    //if(dsp.child("status").getValue().toString().equals("1")) {
//                        surveyTitle = dsp.child("name").getValue().toString();
//
//                        for (DataSnapshot d : dsp.child("questions").getChildren()) {
//                            questions.add(d.child("question_text").getValue().toString());
//                            questionType.add(d.child("type").getValue().toString());
//                        }
//                        SurveysResponseInfo surveysResponseInfo = new SurveysResponseInfo(surveyTitle, status, questions, questionType);
//                        ongoingSurveysArrayList.add(surveysResponseInfo);
//                        ongoingSurveysRecyclerAdapter.notifyDataSetChanged();
//                        questions.clear();
//                        questionType.clear();
////                    }
////                    else{
////                        surveyTitle = dsp.child("name").getValue().toString();
////
////                        for (DataSnapshot d : dsp.child("questions").getChildren()) {
////                            questions.add(d.child("question_text").getValue().toString());
////                            questionType.add(d.child("type").getValue().toString());
////                        }
////                        SurveysResponseInfo surveysResponseInfo = new SurveysResponseInfo(surveyTitle, status, questions, questionType);
////                        closedSurveysArrayList.add(surveysResponseInfo);
////                        closedSurveysRecyclerAdapter.notifyDataSetChanged();
////                        questions.clear();
////                        questionType.clear();
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void populateRecyclerView(String surveyTitle,int status,DataSnapshot d){

        for(DataSnapshot ds:d.getChildren()){
            classId=ds.getKey();

            numberOfResponses=ds.getChildrenCount();
            SurveysResponseInfo surveysResponseInfo=new SurveysResponseInfo(surveyTitle,classId,status,numberOfResponses);
            if(status==1){
                ongoingSurveysArrayList.add(surveysResponseInfo);
                ongoingSurveysRecyclerAdapter.notifyDataSetChanged();
            }
            else{
                closedSurveysArrayList.add(surveysResponseInfo);
                closedSurveysRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((DashboardActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }


    }

}
