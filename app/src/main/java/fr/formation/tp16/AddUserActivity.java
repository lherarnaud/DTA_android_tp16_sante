package fr.formation.tp16;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import fr.formation.tp16.database.datasource.DataSource;
import fr.formation.tp16.database.modele.User;

public class AddUserActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user);

        final EditText newUserName = (EditText) findViewById(R.id.username);
        Button addButton = (Button) findViewById(R.id.button_addUser);

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                User utilisateur = new User();
                utilisateur.setNom(newUserName.getText().toString());

                // Transformation en JSON :
                String flux = (new Gson()).toJson(utilisateur);

                // On dépose notre utilisateur jsonné dans l'intent
                Intent resultIntent = new Intent();
                resultIntent.putExtra(MainActivity.NEW_USER_EXTRA, flux);
                setResult(MainActivity.NEW_USER_REQUEST_ID, resultIntent);

                // Bye l'activité
                finish();
            }
        });
    }

}

