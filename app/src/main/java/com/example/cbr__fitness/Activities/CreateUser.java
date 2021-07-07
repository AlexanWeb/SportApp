package com.example.cbr__fitness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.cbr__fitness.Data.User;
import com.example.cbr__fitness.Data.UserList;
import com.example.cbr__fitness.Logic.CBRFitnessUtil;
import com.example.cbr__fitness.R;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktitivtät zur Erstellung eines Benutzers dar.
public class CreateUser extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private User user;
    private UserList userList;
    private CBRFitnessUtil cbrFitnessUtil;
    private EditText editName;
    private EditText editPassword;
    private EditText editAge;
    private RadioGroup rgGender;
    private RadioGroup rgWorkType;
    private RadioGroup rgBodyType;
    private EditText editDuration;
    private RadioGroup rgRes;
    private ImageButton imgButtonLogo;
    private Button createUserButton;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {

            // Durch Klicken des createUserButtons wird der Benutzer abgespeichert und die nächste Aktivität gestartet.
            case R.id.createUserButton:
                if(checkInputs()) {
                    if(userList.userExists(editName.getText().toString())) {
                        Toast.makeText(this, "Username already assigned!", Toast.LENGTH_SHORT).show();
                    } else {
                        createUser();
                        cbrFitnessUtil.save("userBase.txt", cbrFitnessUtil.load("userBase.txt", user.getUserToString(), CreateUser.this), CreateUser.this);
                        finish();
                        Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Empty Input fields!", Toast.LENGTH_SHORT).show();
                }
                break;

            // Durch Klicken des imgButtonLogos gelangt der Benutzer zurück zur vorherigen Aktivität.
            case R.id.imgButtonLogo:
                Intent i = new Intent(CreateUser.this, MainActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        //Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_window);

        // Initialisierung der Util-Klassen und anderen Attributen.
        user = new User();
        userList = new UserList();
        cbrFitnessUtil = new CBRFitnessUtil();
        userList = MainActivity.userList;

        //Layout-Attribute.
        imgButtonLogo = findViewById(R.id.imgButtonLogo);
        editName = findViewById(R.id.editTextUsername);
        editPassword = findViewById(R.id.editPassword);
        editAge = findViewById(R.id.editAge);
        rgGender = findViewById(R.id.rgGender);
        rgWorkType = findViewById(R.id.rgWorkoutType);
        rgBodyType = findViewById(R.id.rgBodyType);
        editDuration = findViewById(R.id.editDuration);
        rgRes = findViewById(R.id.rgRes);
        createUserButton = findViewById(R.id.createUserButton);

        // Hinzufügen der OnClickListener zu den Buttons.
        imgButtonLogo.setOnClickListener(this);
        createUserButton.setOnClickListener(this);
    }

    // Diese Methode erstellt aus den Input-Felder einen neuen Benutzer.
    public void createUser() {

        int rbId;
        RadioButton rb;
        user.setUsername(editName.getText().toString());
        editName.getText().clear();
        user.setUserPassword(editPassword.getText().toString());
        editPassword.getText().clear();
        user.setAge(editAge.getText().toString());
        editAge.getText().clear();
        rbId = rgGender.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setGender(rb.getText().toString());
        rgGender.clearCheck();
        rbId = rgWorkType.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setWorktype(rb.getText().toString());
        rgWorkType.clearCheck();
        rbId = rgBodyType.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setBodyType(rb.getText().toString());
        user.setDuration(editDuration.getText().toString());
        editDuration.getText().clear();
        rbId = rgRes.getCheckedRadioButtonId();
        rb = findViewById(rbId);
        user.setRes(rb.getText().toString());
        rgRes.clearCheck();
        user.setPathData(user.getUsername() +".txt");
        cbrFitnessUtil.save(user.getPathData(),"", CreateUser.this);
    }

    // Diese Methode überprüft, ob die Eingaben der Input-Felder valide sind.
    public boolean checkInputs() {

        boolean check = true;
        if(editName.getText().toString().matches("")){
            check = false;
        } else if(editPassword.getText().toString().matches("")) {
            check = false;
        } else if(editAge.getText().toString().matches("")) {
            check = false;
        } else if(editDuration.getText().toString().matches("")) {
            check = false;
        } else if(rgGender.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(rgRes.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(rgWorkType.getCheckedRadioButtonId() == -1) {
            check = false;
        } else if(rgBodyType.getCheckedRadioButtonId() == -1) {
            check = false;
        }
        return check;
    }
}