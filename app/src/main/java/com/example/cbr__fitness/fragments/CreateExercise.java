package com.example.cbr__fitness.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.CreateExerciseAdapter;
import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.interfaces.EnumInterface;
import com.example.cbr__fitness.logic.AccountUtil;
import com.example.cbr__fitness.logic.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * create an instance of this fragment.
 */
public class CreateExercise extends Fragment {

    List<ColorChangeToggleButton> muscleGroupList;

    ConstraintLayout constraintLayout;

    Flow flow;

    public CreateExercise() {
        // Required empty public constructor
        muscleGroupList = new ArrayList<>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_exercise, container, false);
   }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FitnessDBSqliteHelper helper = new FitnessDBSqliteHelper(requireContext());
        User user = helper.getUserById(SharedPreferenceManager.getLoggedUserID(getActivity().getApplicationContext()));
        List<Exercise> allExercises = helper.getAllPossibleExercisesForUser(1, user.getEquipments());

        constraintLayout = view.findViewById(R.id.constraint_create_exercise_constraint);
        flow = view.findViewById(R.id.flow_create_exercise_muscle_group);
        RecyclerView recyclerViewPossible = view.findViewById(R.id.recycler_create_offered_exercises);

        CreateExerciseAdapter adapter = new CreateExerciseAdapter(allExercises);
        recyclerViewPossible.setAdapter(adapter);
        recyclerViewPossible.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        ItemClickSupport.addTo(recyclerViewPossible).setOnItemClickListener((recycler, position, v) -> {

        });

        RecyclerView recyclerViewChosen = view.findViewById(R.id.recycler_create_chosen_exercises);

        muscleGroupList = new ArrayList<>();
        createSwitchButtonsFromEnums(MuscleGroupEnum.values());

    }

    private void createSwitchButtonsFromEnums(EnumInterface[] enums) {
        for (EnumInterface e : enums) {
            ColorChangeToggleButton t = new ColorChangeToggleButton(getActivity(), e);
            t.setId(View.generateViewId());
            t.setOnCheckedChangeListener((buttonView, isChecked) -> {

                ColorChangeToggleListener.onCheckedChanged(buttonView,isChecked);
                ColorChangeToggleListener.onCheckedChangedSingleButtonChecked(buttonView,isChecked, muscleGroupList);
            });

            muscleGroupList.add(t);
            constraintLayout.addView(t);;

            flow.addView(t);
        }
    }
}