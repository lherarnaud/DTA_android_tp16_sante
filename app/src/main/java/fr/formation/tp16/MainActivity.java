package fr.formation.tp16;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import fr.formation.tp16.database.datasource.DataSource;
import fr.formation.tp16.database.modele.User;

/**
 * Created by admin on 16/06/2017.
 */

public class MainActivity extends AppCompatActivity {

    public static final int NEW_USER_REQUEST_ID = 2;
    public static final String NEW_USER_EXTRA = "newUser";

    //TODO Cet attribut devait être privé et non statique : a envoyer au bundle pour ne pas reinstancierÒ
    public static DataSource<User> dataSource;
    private int versionDB = 3; // Permet de detruire la base de données SQLite si on incrémente la version

    // Pour quitter l'application :
    private Toast toast;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddUserActivity.class);
                startActivityForResult(i, NEW_USER_REQUEST_ID);
            }
        });

        if(findViewById(R.id.fragment_container) != null) {
            if(savedInstanceState != null) {
                return;
            }

            ListeUtilisateurFragment firstFragment = new ListeUtilisateurFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, firstFragment)
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (dataSource == null) {
                dataSource = new DataSource<>(this, User.class, versionDB);
                dataSource.open();
            }
        } catch (Exception e) {
            // Traiter le cas !
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            dataSource.close();
        } catch (Exception e) {
            // Traiter le cas !
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NEW_USER_REQUEST_ID) {
            String flux = data.getStringExtra(NEW_USER_EXTRA); // Tester si pas null ;-)
            User newUser = new Gson().fromJson(flux, User.class);

            try {
                insertRecord(newUser);
            } catch (Exception e) {
                // Que faire :-(
                e.printStackTrace();
            }
        }
    }

    private long insertRecord(User user) throws Exception {

        // Insert the line in the database
        long rowId = dataSource.insert(user);

        // Test to see if the insertion was ok
        if (rowId == -1) {
            Toast.makeText(this, "Error when creating an User",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User created and stored in database : "+ user.getNom() + "(" + user.getId() + ")",
                    Toast.LENGTH_LONG).show();
        }
        return rowId;
    }

    /*
    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Encore !!", Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, "Bye bye !", Toast.LENGTH_SHORT);
            toast.show();
            super.onBackPressed();
            this.finish();
            System.exit(0);
        }
    }
    */
}
