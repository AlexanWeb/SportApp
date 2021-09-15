package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.customListenerMethods.ColorChangeToggleListener;
import com.example.cbr__fitness.customViewElements.ColorChangeToggleButton;
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.ExerciseViewModel;
import com.example.cbr__fitness.viewModels.PlanViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExchangeExercise#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExchangeExercise extends Fragment {

    List<ColorChangeToggleButton> equipmentButtons;

    FitnessDBSqliteHelper helper;

    ExerciseViewModel model;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExchangeExercise() {
        equipmentButtons = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExchangeExercise.
     */
    // TODO: Rename and change types and number of parameters
    public static ExchangeExercise newInstance(String param1, String param2) {
        ExchangeExercise fragment = new ExchangeExercise();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exchange_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RetrievalUtil util = new RetrievalUtil(this.getActivity().getFilesDir().getAbsolutePath() + "/");
        helper = new FitnessDBSqliteHelper(requireContext());
        int id = SharedPreferenceManager.getLoggedUserID(requireContext());
        User user = helper.getUserById(id);
        List<EquipmentEnum> equipments = new ArrayList<>();
        model = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        Flow flowLayout = view.findViewById(R.id.flow_choose_equipment_exchange_exercise);
        ConstraintLayout constraintLayout = view.findViewById(R.id.fragment_exchange_exercise);

        for (EquipmentEnum e : user.getEquipments()) {
            ColorChangeToggleButton button = new ColorChangeToggleButton(requireContext(), e);
            button.setOnCheckedChangeListener((buttonView, isChecked) ->
                    ColorChangeToggleListener.onCheckedChanged(buttonView, isChecked));
            button.setChecked(true);
            equipmentButtons.add(button);
            constraintLayout.addView(button);
            flowLayout.addView(button);
        }

        Button confirmButton = view.findViewById(R.id.button_confirm_exchange);
        confirmButton.setOnClickListener(v -> {
            for (ColorChangeToggleButton c : equipmentButtons) {
                if (c.isChecked()) {
                    equipments.add((EquipmentEnum) c.getEnumE());
                }
            }
            util.retrieveExercise(helper, model.getSelected().getValue() , equipments);

            Navigation.findNavController(view).navigate(R.id.action_fragment_exchange_exercise_to_fragment_exchange_exercise_cbr_result);
        });
    }
}