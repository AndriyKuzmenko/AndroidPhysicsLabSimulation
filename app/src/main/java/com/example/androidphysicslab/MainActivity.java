package com.example.androidphysicslab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    Button logInButton,signInButton,guestButton;
    EditText emailET,passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        logInButton = (Button) findViewById(R.id.logInButton);
        signInButton = (Button) findViewById(R.id.signInButton);
        guestButton=(Button)findViewById(R.id.guestButton);
        if (Languages.signIn == null)
        {
            Languages.toEnglish();
        }
        changeLanguage();
    }

    /**
     * @return checks if the user is registered, if he is and the email is valid, moves to the menu activity. If the email is invalid, logs out.
     */

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FBRef.mUser = FBRef.mAuth.getCurrentUser();
        if (FBRef.mUser != null)
        {
            String email=FBRef.mUser.getEmail().toString().toLowerCase();
            if(email.contains("putin") || notEmail(email))
            {
                FirebaseAuth.getInstance().signOut();
                FBRef.myRef=null;
            }
            else
            {
                nextActivity();
            }
        }
    }

    /**
     * @param view - the button that was pressed
     * @return - creates an account and moves the user to the menu activity
     */

    public void signIn(View view)
    {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if(email.toLowerCase().contains("putin") || notEmail(email))
        {
            return;
        }

        FBRef.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FBRef.mUser = FBRef.mAuth.getCurrentUser();
                            nextActivity();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * @param menu  - the menu
     * @return      - shows the main menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    /**
     * @param item - the item that was selected
     * @return     - Changes the language to the selected language
     */

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.English)
        {
            Languages.toEnglish();
        }
        else if (id == R.id.Hebrew)
        {
            Languages.toHebrew();
        }

        changeLanguage();

        return true;
    }

    /**
     * @return Updates the interface language after it was changed
     */

    public void changeLanguage()
    {
        logInButton.setText(Languages.logIn);
        emailET.setHint(Languages.emailAddress);
        passwordET.setHint(Languages.password);
        signInButton.setText(Languages.signIn);
        guestButton.setText(Languages.enterAsGuest);
    }

    /**
     * @return - finishes the activity
     */

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    /**
     * @return moves the user to the menu activity
     */

    public void nextActivity()
    {
        Intent si = new Intent(this, MenuActivity.class);
        startActivity(si);
    }

    /**
     * @param view - the button that the user pressed
     * @return Logs in the user and moves him to the menu activity
     */

    public void logIn(View view)
    {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if(email.toLowerCase().contains("putin") || notEmail(email))
        {
            return;
        }

        FBRef.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FBRef.mUser = FBRef.mAuth.getCurrentUser();
                            nextActivity();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * @param view - the button that was pressed
     * @return - moves the user to the menu activity as a guest.
     */

    public void guest(View view)
    {
        FBRef.mUser=null;
        nextActivity();
    }

    /**
     * @param str - a string that should contain an email address
     * @return - true is the string doesn't have special characters, otherwise false
     */

    public boolean notEmail(String str)
    {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(str);

        return !mat.matches();
    }
}