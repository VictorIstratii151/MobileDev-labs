package com.example.vic.pamlab5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Welcome extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MainActivity.fa.finish();

        final Button logIn = (Button) findViewById(R.id.buttonLogIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent logInIntent = new Intent(Welcome.this, LogIn.class);
                startActivity(logInIntent);
            }
        });

        Button signUp = (Button) findViewById(R.id.buttonSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent signUpIntent = new Intent(Welcome.this, SignUp.class);
                startActivity(signUpIntent);
            }
        });
    }
}
