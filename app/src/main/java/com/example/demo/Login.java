package com.example.demo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Activity that shows the login screen. This allows the user to login to their own account or register a new account.
 */
public class Login extends AppCompatActivity {

    Database db;

    private String content;
    private String access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialising the database
        db = new Database(this);

        // if the user is already logged it, continue to the main activity
        if (db.getStatus() == 1){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        // initialising the login button and adding the functionalities
        final Button login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // when the user clicks on the login button, the text entered in the username and password components is saved in variables so it can be tested
                String username = "";
                EditText editText1 = (EditText) findViewById(R.id.editText);
                username = editText1.getText().toString();
                String password = "";
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                password = editText2.getText().toString();

                // if the username or password are empty a prompt will be shown notifying the user
                if (username.equals("") || password.equals("")) {
                    new AlertDialog.Builder(Login.this).setTitle("Error").setMessage("username or password cannot be empty")
                            .setNegativeButton("OK", null)
                            .show();
                } else {
                    // if both the username and password are filled in the user's password is loaded from the database based on the username
                    access = db.loginConfirm(username);
                    // the password read from the database is compared to the entered password
                    if (access.equals(password)) {
                        // if the passwords are the same, the user's ID is read using their unique username
                        int userID = db.getUserIDByUserName(username);
                        // the user is set as the current logged in user
                        db.setUser(userID);
                        // the status is set to logged in
                        db.setStatus(true);
                        // the main activity of the application is started
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // If the entered password is not equal to the password saved in the database, this means that the entered password is wrong
                        // A prompt is shown, notifying the user they entered a wrong password
                        new AlertDialog.Builder(Login.this).setTitle("Error!").setMessage("Wrong username or password.")
                                .setNegativeButton("OK", null)
                                .show();
                    }
                }
            }
        });

        //register button
        final Button register = (Button) findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

    }
}
