package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cbr__fitness.R;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität des User-Interfaces dar.
public class UserInterface extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private TextView textUser;
    private ImageButton imgButtonCBR;
    private ImageButton imgButtonPlan;
    private ImageButton imgButtonSetts;
    private ImageButton imgButtonLogo;

    private NavController navController;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des imgButtonPlan gelangt der Benutzer zur Aktivität der Planconfiguration.
            case R.id.imgButtonPlan:
                Intent i = new Intent(UserInterface.this, ConfigurePlan.class);
                startActivity(i);
                break;

            // Durch Klicken des imgButtonCBR gelangt der Benutzer zur Aktivität des CBR-Planners.
            case R.id.imgButtonCBR:
                if(MainActivity.userLogged.getPlanList().size() == 4) {
                    Toast.makeText(this, "Pls remove Plan, Maximum reached!", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(MainActivity.userLogged.getPlanList().size());
                    i = new Intent(UserInterface.this, CBRPlanner.class);
                    startActivity(i);
                }
                break;

            // Durch Klicken des imgButtonSetts gelangt der Benutzer zur Aktivität der Benutzereinstellung.
            case R.id.imgButtonSetts:
                i = new Intent(UserInterface.this, UserSettings.class);
                startActivity(i);
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                i = new Intent(UserInterface.this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        // Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interface);

        NavHostFragment fragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navFragmentContainer);
        if (fragment != null) {
            System.out.println("NavController found");
            navController = fragment.getNavController();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if (SharedPreferenceManager.getIsUserAdmin(this)) {
            System.out.println("SETTTING MENUS");
            Menu menu = bottomNavigationView.getMenu();
            menu.add(Menu.NONE, R.id.admin_nav, Menu.NONE, "Admin");
        }


        System.out.println("NAV CONTROLLER: " + navController.toString());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        System.out.println(navController.getGraph().toString());
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Überschrift für die Aktivität wird bestimmt.
        //String msgTxt = "Hello " + MainActivity.userLogged.getUsername() + "! Start your workout here!";
        //textUser.setText(msgTxt);
    }
    //NESSESARY TO ENABLE A BACK BUTTON
    @Override
    public boolean onSupportNavigateUp() {
        boolean test = navController.navigateUp();
        System.out.println("NAVIGATION UP " + test);
        return test;
    }
}