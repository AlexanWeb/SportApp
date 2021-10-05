package com.example.cbr__fitness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import com.example.cbr__fitness.data.Exercise;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.databasehelper.FitnessDBSqliteHelper;
import com.example.cbr__fitness.enums.EquipmentEnum;
import com.example.cbr__fitness.enums.GoalEnum;
import com.example.cbr__fitness.enums.MuscleGroupEnum;
import com.example.cbr__fitness.logic.LogUtil;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.example.cbr__fitness.viewModels.PlanViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
    /**
     * Used to prevent the fragment to re-query on each creation, as the data is still valid on
     * back navigation. Will be reset to true on the destruction of this fragment.
     */
    boolean doQuery = true;

    private String mParam1;
    private String mParam2;

    private FitnessDBSqliteHelper helper;

    private PlanViewModel viewModel;

    private User user;

    private List<Pair<ExerciseList, Double>> allPlans;

    private List<Pair<ExerciseList, Double>> allPlansAdaptionClone;

    private RetrievalUtil retrievalUtil;

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
        date += "  GOAL: " + GoalEnum.getEnumByID(bundle.getInt("goal"))
                + " GROUP: " + MuscleGroupEnum.getEnumByID(bundle.getInt("group"))
                + " TIME: " + bundle.getInt("duration");

        TextView headerTextView = view.findViewById(R.id.text_label_cbr_result);
        headerTextView.setText(date);
        if (doQuery) {
            //Start retrieval by querying for similar users first.
            List<Pair<User, Double>> similarUser = getUserFromCBR(user);
            LogUtil.logUserSimilarity(requireContext(),user, similarUser);
            //Get all plans for each user still in the process from the database.
            allPlans = getExerciseListsByUsers(similarUser, bundle, user
                    , CBRConstants.EXERCISE_LIST_SIMILARITY_THRESHOLD);
            allPlansAdaptionClone = new ArrayList<>(allPlans);
            adaptPlans(allPlans, bundle);

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

        retrievalUtil = new RetrievalUtil(this.getActivity().getFilesDir().getAbsolutePath() + "/");
        List<Pair<User, Double>> users = retrievalUtil.retrieveUser(user);
        //to finish the similarity of users, get the limitations for each returned user from the cbr query.
        helper.getLimitationsForListOfUser(users);
        Iterator<Pair<User,Double>> iterator = users.iterator();
        while (iterator.hasNext()) {
            Pair<User,Double> pair = iterator.next();

            double limitationSim = CBRConstants.LimitationSimilarityRelativeToLimitationCount(user.getLimitations()
                    , pair.getFirst().getLimitations());
            System.out.println(">>> LIMIT SIM FUER: " + pair.getFirst().getUserName() + " VALUE: " + limitationSim);
            adjustPersonSimilarity(pair, limitationSim);
            /**
             * We reduce the returned user count by requiring a certain similarity
             * No limitation based on number, its unclear at this point how many plans these
             * users have.
             */

            if (pair.getSecond() < CBRConstants.USER_SIMILARITY_THRESHOLD) {
                iterator.remove();
            }
        }
        //Sort the user by their similarity, top to bottom, to enable cutting off those that do not meet a certain threshold.
        return users.stream()
                .sorted(Comparator.comparingDouble(Pair<User,Double>::getSecond).reversed()).collect(Collectors.toList());
    }

    /**
     * Returns the exercise lists with their similarity values. Those below the needed threshold will
     * be removed from the list.
     * @param userList
     * @param bundle
     * @param user
     * @return
     */
    private List<Pair<ExerciseList, Double>> getExerciseListsByUsers(List<Pair<User, Double>> userList
            , Bundle bundle, User user, double exerciseListThreshold) {
        List<Pair<ExerciseList, Double>> exerciseLists = helper.getPlansForCBRUsers(userList);

        Iterator<Pair<ExerciseList, Double>> it = exerciseLists.iterator();

        while (it.hasNext()){
            Pair<ExerciseList, Double> p = it.next();
            LogUtil.LogPlanSimilarity(requireContext(), "\t>SIMILARITY FOR PLAN: "
                    + p.getFirst().getPlan_name() + " FROM " + p.getFirst().getuID()
                    + " FOR: " + p.getFirst().getMuscle_group().getLabel());
            double planSimilarity = 0.0;
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>USER SIMILARITY: " + p.getSecond());
            //Multiplication as the input is in minutes but the data is held in seconds.
            planSimilarity += CBRConstants.getSimilarityForDuration(p.getFirst().getDuration()
                    ,(bundle.getInt("duration")* 60)) /CBRConstants.getCompleteWeightSumPlan();
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>DURATION SIMILARITY: " + planSimilarity);

            planSimilarity += CBRConstants.getEquipmentSimilarity(p.getFirst()
                    .getNeededEquipment(), user.getEquipments()) / CBRConstants.getCompleteWeightSumPlan();
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>EQUIPMENT SIMILARITY: " + planSimilarity);

            planSimilarity += CBRConstants.getGoalSimilarity(p.getFirst().getGoal(),
                    GoalEnum.getEnumByID(bundle.getInt("goal")))/ CBRConstants.getCompleteWeightSumPlan();
            LogUtil.LogPlanSimilarity(requireContext(), "\t\t>GOAL SIMILARITY: " + planSimilarity);

            p.setSecond(p.getSecond() * CBRConstants.WEIGHT_PERSON
                    + planSimilarity * (1-CBRConstants.WEIGHT_PERSON));

            p.setSecond(p.getSecond()* CBRConstants.getMuscleGroupSimilarityMultiplier
                    (MuscleGroupEnum.getEnumByID(bundle.getInt("group")), p.getFirst().getMuscle_group()));
            LogUtil.LogPlanSimilarity(requireContext(), "\t\tEND SIMILARITY: " + p.getSecond());
            if (p.getSecond() < exerciseListThreshold) {
                it.remove();
            }
        }
        return exerciseLists.stream()
                .sorted(Comparator.comparingDouble(Pair<ExerciseList,Double>::getSecond).reversed())
                .collect(Collectors.toList());
    }

    private void adaptPlans(List<Pair<ExerciseList, Double>> plans, Bundle bundle) {
        for (Pair<ExerciseList, Double> p : plans) {
            LogUtil.LogPlanSimilarity(requireContext(), "\t>Adapting Plan: " + p.getFirst().getPlan_name());
            ListIterator<Exercise> ite = p.getFirst().getExercises().listIterator();
            while (ite.hasNext()) {
                Exercise e = ite.next();
                if (!user.getEquipments().contains(e.getEquipment())
                        && !(user.getEquipments().contains(EquipmentEnum.FitnessStudio)
                        && CBRConstants.equipmentsOfSportStudios.contains(e.getEquipment()))) {
                    LogUtil.LogPlanSimilarity(requireContext(), "\t\t>Adapting Exercise: " + e.getName());
                    List<Pair<Exercise, Double>> exchangeExercises = retrievalUtil
                            .retrieveExercise(helper, e, user.getEquipments(), p.getFirst().getAllExerciseIDs());
                    if (exchangeExercises.size() > 0 && exchangeExercises.get(0) != null) {
                        Exercise exchange = exchangeExercises.get(0).getFirst();
                        LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Similar Exercises Found!"
                                + exchange.getName() + " with SIM:" + exchangeExercises.get(0).getSecond());
                        List<Exercise> similarExercises = getSimilarExercisesForAdaption(exchange, bundle);
                        if (e.isBodyweight() == exchange.isBodyweight() || similarExercises.size() <= 0) { //Assuming that we can copy values
                            LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Exercise Type match, copy values!");
//                            helper.updatePlanExerciseRelation(p.getFirst().getPlan_id()
//                                    , exchange.getExerciseID(), e.getExerciseID());
                            exchange.setWeight(exchange.isBodyweight() ? 0 : e.getWeight()); //to at least remove weight
                            exchange.setSetNumber(e.getSetNumber());
                            exchange.setRepNumber(e.getRepNumber());
                            exchange.setBreakTime(e.getBreakTime());
                        } else {
                            LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>No Match, try estimation! ");
                            adaptExerciseAfter(similarExercises, exchange);
//                            helper.updatePlanExerciseRelation(p.getFirst().getPlan_id()
//                                    , exchange.getExerciseID(), e.getExerciseID());
                        }
                        ite.remove();
                        ite.add(exchange);
                    }
                }
            }
        }
    }

    private List<Exercise> getSimilarExercisesForAdaption(Exercise exchange, Bundle bundle ) {
        List<Exercise> similarExercises = new ArrayList<>();

        for (Pair<ExerciseList, Double> p : allPlansAdaptionClone) {
            if (p.getFirst().getGoal() ==  GoalEnum.getEnumByID(bundle.getInt("goal"))) {
                for (Exercise e : p.getFirst().getExercises()) {
                    if (e.getMuscle() == exchange.getMuscle()
                            && e.isBodyweight() == exchange.isBodyweight()) { //Exercise targeting same muscle with same goal
                        LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Looking for similar to: "
                                + exchange.getName() + "Bodyweight: "+ exchange.isBodyweight()
                                +" Muscle: " + exchange.getMuscle().getLabel() + " FOUND: "
                                + e.getName() + " Bodyweigt : " + e.isBodyweight() + " MUSCLE: "
                                + e.getMuscle().getLabel() + " FROM Plan: " + p.getFirst().getPlan_id());
                        similarExercises.add(e);
                    }
                }
            }
        }

        return similarExercises;
    }

    private void adaptExerciseAfter (List<Exercise> similarExercises, Exercise exchanged) {
        int sumSets = 0;
        int sumReps = 0;
        int sumWeight = 0;
        int sumBreakTime = 0;
        if (similarExercises.size() > 0) {
            for (Exercise e : similarExercises) {
                LogUtil.LogPlanSimilarity(requireContext(), "\t\t\t>Taking Values from: " + e.getName()
                        + "\n\t\t\tSets: " + e.getSetNumber() + " Reps: "
                        + e.getRepNumber() + " Weight: " + e.getWeight()
                        + " Break Time: " + e.getBreakTime());
                sumReps += e.getRepNumber();
                sumSets += e.getSetNumber();
                sumWeight += e.getWeight();
                sumBreakTime += e.getBreakTime();
            }
        }
        exchanged.setWeight(similarExercises.size() == 0 ? sumWeight : sumWeight/similarExercises.size());
        exchanged.setSetNumber(similarExercises.size() == 0 ? sumSets : sumSets/similarExercises.size());
        exchanged.setRepNumber(similarExercises.size() == 0 ? sumReps : sumReps/similarExercises.size());
        exchanged.setBreakTime(similarExercises.size() == 0 ? sumBreakTime : sumBreakTime/similarExercises.size());
    }

    private void adjustPersonSimilarity(Pair<User, Double> user, double limitationSim) {
        double pastSim = user.getSecond();
        System.out.println(">>>> SIM PRE LIMITATION: " + pastSim);
        pastSim *= CBRConstants.getCompleteWeightSumMyCBRPerson(); //denormalize the sum by multiplying with the sum of weights used in the CBR query
        pastSim += CBRConstants.WEIGHT_LIMITATION * limitationSim;
        System.out.println(">>>> SIM AFTER LIMITATION: " + pastSim/CBRConstants.getCompleteWeightSumPerson());
        user.setSecond(pastSim/CBRConstants.getCompleteWeightSumPerson()); //already consider duration weight
    }

    /**
     * Needed to prevent the queries to run while the data is still relevant and accessible.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        doQuery = true;
    }
}