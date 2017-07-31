package com.example.umesh.omrscanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by basu on 7/23/2017.
 */

public class SurveyTemplateRecyclerAdapter extends RecyclerView.Adapter<SurveyTemplateRecyclerAdapter.MyViewHolder> {
    private List<SurveyInfo> surveyList;
    RecyclerItemClickListener recyclerItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView titleValueTextView,titleTextView,numberOfQuestionsTextView,numberOfQuestionsValueTextView;

        public MyViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);


            titleValueTextView=(TextView)view.findViewById(R.id.survey_title_value_text_view);
            titleTextView=(TextView)view.findViewById(R.id.survey_title_text_view);
            numberOfQuestionsTextView=(TextView)view.findViewById(R.id.number_of_questions_text_view);
            numberOfQuestionsValueTextView=(TextView)view.findViewById(R.id.number_of_questions_value_text_view);

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

    public SurveyTemplateRecyclerAdapter(List<SurveyInfo> surveyList){

        this.surveyList=surveyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_template_recycler_view_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SurveyInfo surveyInfo =surveyList.get(position);
        holder.titleTextView.setText("Title");
        holder.titleValueTextView.setText(surveyInfo.getTitle());
        holder.numberOfQuestionsTextView.setText("No. Of Questions");
        holder.numberOfQuestionsValueTextView.setText(""+ surveyInfo.getNumberOfQuestions());

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
