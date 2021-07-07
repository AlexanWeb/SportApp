package com.example.cbr__fitness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.cbr__fitness.Data.ExerciseList;
import com.example.cbr__fitness.Data.PlanList;
import com.example.cbr__fitness.Data.User;
import com.example.cbr__fitness.Data.UserList;
import com.example.cbr__fitness.Logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;

/**
 * @author Jobst-Julius Bartels
 */

// Die Klasse stellt die Aktivität für das Startfenster der Applikation bereit.
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    public static User userLogged;
    public static UserList userList;
    public static PlanList planBaseList;
    public static ExerciseList exerciseBaseList;
    private CBRFitnessUtil cbrFitnessUtil;
    private Button createAccButton;
    private ImageButton imageButtonLogin;
    private EditText usernameInput;
    private EditText userPasswordInput;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch das Klicken des imageButtonLogin wird der Login-Prozess gestartet.
            // ToDo: Übergangslösung, Prozess sollte hier korrekt implementiert werden.
            case R.id.imageButtonLogin:
                userLogged = null;
                userList = cbrFitnessUtil.getUserList(cbrFitnessUtil.loadAll("userBase.txt", MainActivity.this));

                // Eingabefelder werden auf Inhalt überprüft.
                if(!usernameInput.getText().toString().isEmpty() && !userPasswordInput.getText().toString().isEmpty()) {

                    // Loginaccess zum Expertenmodus.
                    if(usernameInput.getText().toString().matches("e") && userPasswordInput.getText().toString().matches("e")) {
                        userLogged = userList.getLoggedUser(usernameInput.getText().toString(), userPasswordInput.getText().toString());
                        usernameInput.getText().clear();
                        userPasswordInput.getText().clear();
                        Toast.makeText(this, "Admin Login successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, ExpertLogin.class);
                        startActivity(i);

                    // Überprüfung, ob die Daten valide sind.
                    } else if(userList.checkUser(usernameInput.getText().toString(), userPasswordInput.getText().toString())) {
                        userLogged = userList.getLoggedUser(usernameInput.getText().toString(), userPasswordInput.getText().toString());
                        userLogged.setPlanList(cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll(userLogged.getPathData(), MainActivity.this)));
                        usernameInput.getText().clear();
                        userPasswordInput.getText().clear();
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, UserInterface.class);
                        startActivity(i);
                    } else {
                        usernameInput.getText().clear();
                        userPasswordInput.getText().clear();
                        Toast.makeText(this, "Wrong Username or Password", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Username or Password is missing!", Toast.LENGTH_SHORT).show();
                }

                break;

            // Durch das Klicken des createAccButton wird die Aktivität für das Erstellen eines Benutzerprofils gestartet.
            case R.id.createAccButton:
                Intent i = new Intent(MainActivity.this, CreateUser.class);
                startActivity(i);
                break;
        }
    }
    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisierung der Util-Klassen und anderen Attributen.
        userList = new UserList();
        planBaseList = new PlanList();
        exerciseBaseList = new ExerciseList();
        cbrFitnessUtil = new CBRFitnessUtil();

        //Layout-Attribute
        usernameInput = findViewById(R.id.editUsernameInput);
        userPasswordInput = findViewById(R.id.edituserPasswordInput);
        imageButtonLogin = findViewById(R.id.imageButtonLogin);
        createAccButton = findViewById(R.id.createAccButton);

        // OnClickListener werden den Buttons hinzugefügt.
        createAccButton.setOnClickListener(this);
        imageButtonLogin.setOnClickListener(this);

        // Datensätze werden geladen.
        loadDatabases();
    }

    // Diese Methode ist für das Laden der Datensätze verantwortlich.
    // ToDo Implementierung einer relationalen Datenbank Anbindung.
    private void loadDatabases() {

        if(cbrFitnessUtil.fileExists("userBase.txt", MainActivity.this)) {
            userList = cbrFitnessUtil.getUserList(cbrFitnessUtil.loadAll("userBase.txt", MainActivity.this));
        } else {
            cbrFitnessUtil.save("userBase.txt", "", MainActivity.this);
        }
        if(cbrFitnessUtil.fileExists("planBase.txt", MainActivity.this)) {
            planBaseList =cbrFitnessUtil.getUserPList(cbrFitnessUtil.loadAll("planBase.txt", MainActivity.this));
        } else {
            cbrFitnessUtil.save("planBase.txt", "", MainActivity.this);
        }
        if(cbrFitnessUtil.fileExists("exerciseBase.txt", MainActivity.this)) {
            exerciseBaseList =cbrFitnessUtil.getExList(cbrFitnessUtil.loadAll("exerciseBase.txt", MainActivity.this));
        } else {
            cbrFitnessUtil.save("exerciseBase.txt", "", MainActivity.this);
        }
    }
}