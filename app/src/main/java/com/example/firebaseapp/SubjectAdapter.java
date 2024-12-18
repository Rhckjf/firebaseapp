package com.example.firebaseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    private final ArrayList<EnrollmentActivity.Subject> subjects;
    private final BiConsumer<EnrollmentActivity.Subject, Boolean> onSubjectSelected;

    public SubjectAdapter(ArrayList<EnrollmentActivity.Subject> subjects, BiConsumer<EnrollmentActivity.Subject, Boolean> onSubjectSelected) {
        this.subjects = subjects;
        this.onSubjectSelected = onSubjectSelected;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        EnrollmentActivity.Subject subject = subjects.get(position);
        holder.subjectName.setText(subject.name);
        holder.subjectCredits.setText(String.valueOf(subject.credits));
        holder.subjectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> onSubjectSelected.accept(subject, isChecked));
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, subjectCredits;
        CheckBox subjectCheckBox;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectCredits = itemView.findViewById(R.id.subjectCredits);
            subjectCheckBox = itemView.findViewById(R.id.subjectCheckBox);
        }
    }
}
