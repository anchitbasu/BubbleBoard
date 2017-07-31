package com.example.umesh.omrscanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by basu on 7/30/2017.
 */

public class SurveyAnalysisRecyclerAdapter extends RecyclerView.Adapter<SurveyAnalysisRecyclerAdapter.MyViewHolder> {
    private List<SurveyAnalysisInfo> surveyList;
    RecyclerItemClickListener recyclerItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView questionTextTextView,numberOfFavourableResponsesTextView,numberOfUnfavourableResponsesTextView;

        public MyViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);


            questionTextTextView=(TextView)view.findViewById(R.id.survey_analysis_question_text_text_view);
            numberOfFavourableResponsesTextView=(TextView)view.findViewById(R.id.survey_analysis_favourable_response_text_view);
            numberOfUnfavourableResponsesTextView=(TextView)view.findViewById(R.id.survey_analysis_unfavourable_response_text_view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onItemClick(v,getPosition());
        }
    }

    public interface RecyclerItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void SetOnItemClickListener(final RecyclerItemClickListener mItemClickListener) {
        this.recyclerItemClickListener = mItemClickListener;
    }

    public SurveyAnalysisRecyclerAdapter(List<SurveyAnalysisInfo> surveyList){

        this.surveyList=surveyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_analysis_recycler_view_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SurveyAnalysisInfo surveyAnalysisInfo =surveyList.get(position);
        holder.questionTextTextView.setText(surveyAnalysisInfo.getQuestionText());
        holder.numberOfFavourableResponsesTextView.setText(""+surveyAnalysisInfo.getNumberFavourable());
        holder.numberOfUnfavourableResponsesTextView.setText(""+surveyAnalysisInfo.getNumberUnfavourable());

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
