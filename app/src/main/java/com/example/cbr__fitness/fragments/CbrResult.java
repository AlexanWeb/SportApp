package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cbr__fitness.R;
import com.example.cbr__fitness.adapters.CBRExerciseListAdapter;
import com.example.cbr__fitness.cbr.CBRConstants;
import com.example.cbr__fitness.cbr.RetrievalUtil;
import com.example.cbr__fitness.customListenerMethods.ItemClickSupport;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.LimitationEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.PlanViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.dfki.mycbr.util.Pair;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CbrResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CbrResult extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    boolean doQuery = true;

    private String mParam1;
    private String mParam2;

    private FitnessDBSqliteHelper helper;

    private PlanViewModel viewModel;

    private User user;

    private List<Pair<ExerciseList, Double>> allPlans;

    public CbrResult() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CbrResult.
     */
    // TODO: Rename and change types and number of parameters
    public static CbrResult newInstance(String param1, String param2) {
        CbrResult fragment = new CbrResult();
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
        return inflater.inflate(R.layout.fragment_cbr_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (doQuery) {
            helper = new FitnessDBSqliteHelper(getActivity().getApplicationContext());

            int id = SharedPreferenceManager.getLoggedUserID(getActivity().getApplicationContext());
            user = helper.getUserById(id);
        }

        //Keys: goal, group, duration
        Bundle bundle = getArguments();

        viewModel = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_show_cbr_exercise_list_results);

        String date = user.toString();
        date += "  GOAL_ " + GoalEnum.getEnumByID(bundle.getInt("goal"))
                + " GROUP: " + MuscleGroupEnum.getEnumByID(bundle.getInt("group"))
                + " TIME: " + bundle.getInt("duration");

        TextView headerTextView = view.findViewById(R.id.text_label_cbr_result);
        headerTextView.setText(date);
        if (doQuery) {
            //Start retrieval by querying for similar users first.
            List<Pair<User, Double>> users = getUserFromCBR(user);

            //Get all plans for each user still in the process from the database.
            allPlans = getExerciseListsByUsers(users, bundle, user);
        }

        CBRExerciseListAdapter adapter = new CBRExerciseListAdapter(allPlans);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView1, position, v) -> {
            viewModel.addPlanList(allPlans.get(position).getFirst());
            Bundle bundleList = new Bundle();
            bundleList.putString("title",allPlans.get(position).getFirst().getPlan_name());
            bundleList.putBoolean("result", true);
            Navigation.findNavController(view).navigate(R.id.action_fragment_show_cbr_result_to_fragment_show_exercise_list, bundleList);
        });

        Button button = view.findViewById(R.id.button_confirm_pick_cbr_result);
        button.setOnClickListener(v -> {
            System.out.println(adapter.getActivePair().getFirst().getPlan_name());
            helper.addPlanToUser(adapter.getActivePair().getFirst(), user.getUid());
            Navigation.findNavController(view).navigate(R.id.action_fragment_show_cbr_result_to_fragment_workout_landing);
        });
        doQuery = false;
    }

    private List<Pair<User, Double>> getUserFromCBR(User user) {

        RetrievalUtil cbrRetrieval = new RetrievalUtil(this.getActivity().getFilesDir().getAbsolutePath() + "/");
        List<Pair<User, Double>> users = cbrRetrieval.retrieveUser(user);
        //to finish the similarity of users, get the limitations for each returned user from the cbr query.
        helper.getLimitationsForListOfUser(users);
        for (Pair<User, Double> p : users) {
            double limitationSim = CBRConstants.LimitationSimilarity(user.getLimitations(), p.getFirst().getLimitations());
            adjustPersonSimilarity(p, limitationSim);
        }
        //Sort the user by their similarity, top to bottom, to enable cutting off those that do not meet a certain threshold.
        return users.stream()
                .sorted(Comparator.comparingDouble(Pair<User,Double>::getSecond).reversed()).collect(Collectors.toList());
    }

    private List<Pair<ExerciseList, Double>> getExerciseListsByUsers(List<Pair<User, Double>> userList, Bundle bundle, User user) {
        List<Pair<ExerciseList, Double>> exerciseLists = helper.getPlansForCBRUsers(userList);

        for (Pair<ExerciseList, Double> p : exerciseLists) {
            p.setSecond(p.getSecond()* CBRConstants.getMuscleGroupSimilarityMultiplier
                    (MuscleGroupEnum.getEnumByID(bundle.getInt("group")), p.getFirst().getMuscle_group()));

            System.out.println(p.getFirst().getPlan_id() + " SIM: " + p.getSecond()
                    + " GROUP: " + p.getFirst().getMuscle_group());

            adjustSimilarityForDuration(p, CBRConstants.getSimilarityForDuration(p.getFirst()
                    .getDuration(),bundle.getInt("duration")* 60));

            System.out.println(p.getFirst().getPlan_id() + " SIM: " + p.getSecond()
                    + " GROUP: " + p.getFirst().getMuscle_group());

            adjustSimilarityForEquipment(p, CBRConstants.getEquipmentSimilarity(p.getFirst()
                    .getNeededEquipment(), user.getEquipments()));

            System.out.println(p.getFirst().getPlan_id() + " SIM: " + p.getSecond()
                    + " GROUP: " + p.getFirst().getMuscle_group());
        }
        return exerciseLists;
    }

    private void adjustPersonSimilarity(Pair<User, Double> user, double limitationSim) {
        user.setSecond((user.getSecond() * (1 - CBRConstants.LIMITATION_SIM_WEIGHT))
                + (limitationSim * CBRConstants.LIMITATION_SIM_WEIGHT));
    }

    private void adjustSimilarityForDuration (Pair<ExerciseList, Double> plan, double durationSim) {
        plan.setSecond(plan.getSecond()
                + ((durationSim * CBRConstants.WEIGHT_DURATION)/CBRConstants.getCompleteWeightSum()));
    }

    private void adjustSimilarityForEquipment(Pair<ExerciseList, Double> plan, double equipmentSim){
        plan.setSecond(plan.getSecond() * (1- CBRConstants.EQUIPMENT_SIM_WEIGHT)
            + CBRConstants.EQUIPMENT_SIM_WEIGHT * equipmentSim);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doQuery = true;
    }
}