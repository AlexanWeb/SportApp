package com.example.cbr__fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.enums.MuscleEnum;

import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>{

    private List<ExerciseList> exerciseLists;

    public ExerciseListAdapter (List<ExerciseList> list) {

        exerciseLists = list;
    }

    @NonNull
    @Override
    public ExerciseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exerciseListView= inflater.inflate(R.layout.fragment_exercise_list_recycler_item_layout, parent, false);

        ExerciseListViewHolder viewHolder = new ExerciseListViewHolder(exerciseListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseListViewHolder holder, int position) {
        ExerciseList list = exerciseLists.get(position);

        TextView workout_plan_header = holder.workout_plan_header;
        workout_plan_header.setText(list.getPlan_name());
        TextView workout_plan_duration = holder.workout_plan_duration;
        workout_plan_duration.setText(Integer.toString(list.getDuration()));
        TextView workout_plan_prime_muscle = holder.workout_plan_prime_muscle;
        workout_plan_prime_muscle.setText(list.getMuscle_group().getLabel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("HIT: " + v.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseLists.size();
    }

    public class ExerciseListViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        public TextView workout_plan_header;

        public TextView workout_plan_duration;

        public TextView workout_plan_prime_muscle;

        public ExerciseListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            workout_plan_header = (TextView) itemView.findViewById(R.id.text_plan_name_exercise_list_recycler_item);
            workout_plan_duration = (TextView) itemView.findViewById(R.id.text_duration_exercise_list_recycler_item);
            workout_plan_prime_muscle = (TextView) itemView.findViewById(R.id.text_goal_exercise_list_recycler_item);


        }
    }
}
