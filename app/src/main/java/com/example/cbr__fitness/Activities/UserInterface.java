package com.example.cbr__fitness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cbr__fitness.R;

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

        //Layout-Attribute
        imgButtonCBR = findViewById(R.id.imgButtonCBR);
        imgButtonPlan = findViewById(R.id.imgButtonPlan);
        imgButtonSetts = findViewById(R.id.imgButtonSetts);
        imgButtonLogo = findViewById(R.id.imgButtonLogo);
        textUser = findViewById(R.id.textUserMsg);

        // OnClickListener werden den Buttons hinzugefügt.
        imgButtonLogo.setOnClickListener(this);
        imgButtonSetts.setOnClickListener(this);
        imgButtonCBR.setOnClickListener(this);
        imgButtonPlan.setOnClickListener(this);

        // Überschrift für die Aktivität wird bestimmt.
        String msgTxt = "Hello " + MainActivity.userLogged.getUsername() + "! Start your workout here!";
        textUser.setText(msgTxt);
    }
}