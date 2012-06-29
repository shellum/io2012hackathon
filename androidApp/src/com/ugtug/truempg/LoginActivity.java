package com.ugtug.truempg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

    private static final String LOGIN_ANONYMOUS = "anonymous";

    private EditText emailView;
    
    public static final String SHARED_PREFS_FILE = "sharedPrefs";
    public static final String SHARED_PREFS_EMAIL = "email";
    
    //Standard onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        //Hook up views
        emailView = (EditText)findViewById(R.id.login_email);
        
        //If the user logged in before... fill in the user id
        SharedPreferences sp = getSharedPreferences(LoginActivity.SHARED_PREFS_FILE, 0);
        String email = sp.getString(LoginActivity.SHARED_PREFS_EMAIL, "");
        
        if(!"".equals(email)) 
        {
            emailView.setText(email);
            login(null);
        }
    }
    
    //Actually log in
    public void login(View view)
    {
        //Save the user id
        //If the user did not enter one, setup the user as being anonymous
        if("".equals(emailView.getText().toString())) emailView.setText(LOGIN_ANONYMOUS);
        
        SharedPreferences.Editor editor = (Editor) getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE).edit();
        editor.putString(SHARED_PREFS_EMAIL, emailView.getText().toString());
        editor.commit();
        
        //Let the user do something
        Intent intent = new Intent(this, AddMpgActivity.class);
        startActivity(intent);
        
        finish();
    }
    
}
