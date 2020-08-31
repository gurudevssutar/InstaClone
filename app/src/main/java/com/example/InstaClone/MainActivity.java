package com.example.InstaClone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.InstaClone.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{


    Boolean isSignUpModeActive =true;
    TextView loginTextView;
    EditText editTextUsername;
    EditText editTextPassword;

    RelativeLayout relativeLayout;
    ImageView imageView;


    ////
    public void showUserlist()
    {
        Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginTextView = (TextView) findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        imageView = (ImageView) findViewById(R.id.imageView);
        relativeLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null)
        {
            showUserlist();
        }

        /*
        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                .clientKey("a4nfoulfm9qD")
                .server("http://18.223.132.148/parse/")
                .build());

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        */

//        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }




        @Override
        public void onClick(View view) {
            if(view.getId()== R.id.loginTextView)
            {
                Button button=(Button) findViewById(R.id.button);
                if(isSignUpModeActive)
                {
                    isSignUpModeActive=false;
                    loginTextView.setText("or, Sign Up");
                    button.setText("Log in");
                }
                else
                {
                    isSignUpModeActive=true;
                    loginTextView.setText("or, Log in");
                    button.setText("Sign up");
                }
            }
            else if(view.getId()==R.id.relativeLayout || view.getId()==R.id.imageView)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }



        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

            if(i== android.view.KeyEvent.KEYCODE_ENTER && keyEvent.getAction()== android.view.KeyEvent.ACTION_DOWN)
            {
                onSignUp(view);
            }


            return false;
        }





        public void onSignUp(View view) {

            editTextUsername = (EditText) findViewById(R.id.editTextUserName);
            editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            editTextPassword.setOnKeyListener(this);
            if(isSignUpModeActive) {//Sign Up


                if (editTextUsername.getText().toString().matches("") || editTextPassword.getText().toString().matches("")) {
                    Toast.makeText(this, "A username and a password is required !", Toast.LENGTH_LONG).show();
                } else {
                    ParseUser user = new ParseUser();
//                    ParseObject user = ParseObject.create("User");
                    user.setUsername(String.valueOf(editTextUsername.getText()));
                    user.setPassword(String.valueOf(editTextPassword.getText()));

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                showUserlist();
                                Toast.makeText(getApplicationContext(), "Signed up successful", Toast.LENGTH_LONG).show();
                            } else {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
            else//Login
            {
                String username=String.valueOf(editTextUsername.getText());
                String pasword=String.valueOf(editTextPassword.getText());

                ParseUser.logInInBackground(username, pasword, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if(e==null)
                        {
                            if(user!=null)
                            {
                                showUserlist();
                                Toast.makeText(getApplicationContext(),"Logged in as "+user.getUsername(),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }

    }
