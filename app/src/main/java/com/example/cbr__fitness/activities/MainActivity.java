 package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.cbr__fitness.data.ExerciseList;
import com.example.cbr__fitness.data.PlanList;
import com.example.cbr__fitness.data.User;
import com.example.cbr__fitness.data.UserList;
import com.example.cbr__fitness.logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public void loginClick (View v) {
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
    }

    // Diese Methode ist für das Laden der Datensätze verantwortlich.
    // ToDo Implementierung einer relationalen Datenbank Anbindung.
    private void loadDatabases() {
        checkCBRProject(true);
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

    /**
     * Methode to initially copy the CBR project from the assets where it is ready only to the
     * internal storage, where the app is using it.
     * @param overwrite overwrite the existin g project.
     */
    private void checkCBRProject (boolean overwrite) {
        boolean found = false;
        File assetCBR = null;
        //Check if there already is a CBR Project inside the local storage.
        for(File f : getFilesDir().listFiles()) {
            if (f.getName().equals("CBR_Fitness.prj")) {
                assetCBR = f;
                found = true;
            }
        }

        if (!found || overwrite) {
            AssetManager manager = this.getAssets();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = manager.open("CBR_Fitness.prj");
                String newFile = getFilesDir().getAbsolutePath() + "/CBR_Fitness.prj";
                out = new FileOutputStream(newFile);
                byte[] bytes = new byte[1024];
                int read = 0;
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0 , read);
                }
                in.close();
                out.flush();
                out.close();
            } catch (IOException exc){
                System.out.println("###########");
            }
        }
    }
}