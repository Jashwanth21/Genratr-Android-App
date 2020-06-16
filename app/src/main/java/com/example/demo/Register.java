package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

/**
 * Activity that shows the register screen. This allows the user to create a new account.
 */
public class Register extends AppCompatActivity {

    Database db;
    private String access;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialising the database
        db = new Database(this);

        // initialises the register button
        final Button submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //If the user clicks on the register button, the values entered in all fields are saved in variables for further testing
                String username="";
                EditText editText5 = (EditText)findViewById(R.id.editText5);
                username = editText5.getText().toString();

                String email="";
                EditText editText6 = (EditText)findViewById(R.id.editText6);
                email = editText6.getText().toString();

                String password="";
                EditText editText7 = (EditText)findViewById(R.id.editText7);
                password = editText7.getText().toString();

                String confirpw="";
                EditText editText8 = (EditText)findViewById(R.id.editText8);
                confirpw = editText8.getText().toString();

                // If any of the fields are empty, a prompt is shown to the user notifying them to fill in all fields
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirpw.isEmpty()){
                    new AlertDialog.Builder(Register.this).setTitle("Error!").setMessage("Registration cannot be empty.")
                            .setNegativeButton("OK", null)
                            .show();
                }else{
                    //If there are any spaces in any of the fields, a prompt is shown to the user notifying them that no spaces are allowed in the registration fields
                    int result1 = username.indexOf(" ");
                    int result2 = email.indexOf(" ");
                    int result3 = password.indexOf(" ");
                    int result4 = confirpw.indexOf(" ");
                    if (result1!=-1 || result2!=-1 || result3!=-1 || result4!= -1){
                        new AlertDialog.Builder(Register.this).setTitle("Error!").setMessage("Registration cannot be a space.")
                                .setNegativeButton("OK", null)
                                .show();
                    } else{
                        // if all fields are valid the password and password confirmation are compared to each other
                        if(password.equals(confirpw)){
                            // if the password and confirmation password are the same, the username is looked up in the database
                            access = db.loginConfirm(username);
                            if(access.isEmpty()){
                                // if the username doesn't have an appointed password, the user doesn't exist already
                                // A new user is created with default values
                                boolean results = db.insertUser(username,email,password,50, "#228B22", "Dublin", "./");
                                if (results)
                                {
                                    // if the user is created in the database, the user is logged in and a notification is displayed showing the registration is complete
                                    myNotification("Genratr", "Welcome to Genratr");
                                    int id = db.getUserIDByUserName(username);
                                    db.setStatus(true);
                                    db.setUser(id);
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);

                                }
                            }else{
                                // if the username already has an appointed password, the user already exists
                                // a prompt is shown to the user that the username is taken
                                new AlertDialog.Builder(Register.this).setTitle("Error!").setMessage("username already exists")
                                        .setNegativeButton("OK", null)
                                        .show();
                            }
                        }else{
                            // if the password and password confirmation are different, a prompt is shown to the user notifying them that the passwords are different
                            new AlertDialog.Builder(Register.this).setTitle("Error!").setMessage("password inputs are different")
                                    .setNegativeButton("OK", null)
                                    .show();
                        }
                    }
                }
            }
        });
    }


    /**
     * This method creates and shows a notification to the user
     *
     * @param strTitle Title of the notification
     * @param strContent content of the notification
     */
    public void myNotification(String strTitle, String strContent){
        String channelID = "1";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelID, "myChannel",importance);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,channelID);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,resultIntent,0);

        // set all attributes of the notification
        notification.setContentTitle(strTitle);
        notification.setAutoCancel(true);
        notification.setContentIntent(pendingIntent);
        notification.setContentText(strContent);
        notification.setDefaults(Notification.DEFAULT_ALL);
        notification.setPriority(Notification.PRIORITY_HIGH);
        notification.setSmallIcon(R.drawable.ic_notifications);
        notification.setFullScreenIntent(pendingIntent, true);
        notification.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_notifications));

        // show the notification
        manager.notify(1,notification.build());
    }
}
