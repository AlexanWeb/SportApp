package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.data.Exercise;

import java.util.List;

public class CreateExerciseAdapter extends RecyclerView.Adapter<CreateExerciseAdapter.CreateExerciseViewHolder> {

    List<Exercise> exercises;

    public CreateExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public CreateExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_create_exercise_recycler_item, parent, false);

        CreateExerciseAdapter.CreateExerciseViewHolder viewHolder = new CreateExerciseAdapter.CreateExerciseViewHolder(exerciseListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreateExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        holder.headerText.setText(exercise.getName());
        holder.primaryMuscle.setText(exercise.getMuscle().getLabel());
        holder.secondaryMuscle.setText(exercise.getSecondaryMuscle().getLabel());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class CreateExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView headerText;

        TextView primaryMuscle;

        TextView secondaryMuscle;

        public CreateExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.text_title_create_recycler_item);
            primaryMuscle = itemView.findViewById(R.id.text_prime_muscle_create_recycler_item);
            secondaryMuscle = itemView.findViewById(R.id.text_secondary_muscle_create_recycler_item);
        }
    }
}
