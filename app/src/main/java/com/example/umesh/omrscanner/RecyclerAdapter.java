package com.example.umesh.omrscanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by basu on 7/12/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<SurveysResponseInfo> surveyList;
    SurveyResponseRecyclerItemClickListener recyclerItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView titleValueTextView,titleTextView, classValueTextView,classTextView, responsesValueTextView,responsesTextView;

        public MyViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            titleValueTextView=(TextView)view.findViewById(R.id.survey_title_value_text_view);
            titleTextView=(TextView)view.findViewById(R.id.survey_title_text_view);
            classValueTextView=(TextView)view.findViewById(R.id.class_value_text_view);
            classTextView=(TextView)view.findViewById(R.id.class_text_view);
            responsesValueTextView=(TextView)view.findViewById(R.id.responses_value_text_view);
            responsesTextView=(TextView)view.findViewById(R.id.responses_text_view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onItemClick(v,getPosition());
        }
    }

    public interface SurveyResponseRecyclerItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void SetOnItemClickListener(final SurveyResponseRecyclerItemClickListener mItemClickListener) {
        this.recyclerItemClickListener = mItemClickListener;
    }


    public RecyclerAdapter(ArrayList<SurveysResponseInfo> surveyList){

        this.surveyList=surveyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SurveysResponseInfo surveysResponseInfo =surveyList.get(position);
        holder.titleTextView.setText("Title");
        holder.titleValueTextView.setText(surveysResponseInfo.getTitle());
        holder.classTextView.setText("Class");
        holder.classValueTextView.setText(""+ surveysResponseInfo.getClassName());
        holder.responsesTextView.setText("Number of responses");
        holder.responsesValueTextView.setText(""+ surveysResponseInfo.getNumberOfQuestions());
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
