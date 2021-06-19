package com.example.smartcart2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button login;
    public EditText name;
    private EditText pass;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        login = (Button)findViewById(R.id.login);
        name = (EditText)findViewById(R.id.name);
        pass = (EditText)findViewById(R.id.pass);
        //password_reset = (TextView)findViewById(R.id.password_reset);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        /*password_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, forgot_password.class));
            }
        });*/

        Button sign_up = (Button)findViewById(R.id.sign_up_btn);
        sign_up.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        startActivity(new Intent(LoginActivity.this, sign_up.class));
                    }
                }
        );


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(name.getText().toString(), pass.getText().toString());
            }
        });


        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            //Intent intent = new Intent(LoginActivity.this, BrCodeActivity.class);
            //Bundle extras = new Bundle();
            //extras.putString("string-key", name.getText().toString());
            //intent.putExtras(extras);
            //startActivity(intent);
            startActivity(new Intent(LoginActivity.this, BrCodeActivity.class));
        }
    }

    public void validate(String username, String password){

        progressDialog.setMessage("Lets do some smart shopping!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, BrCodeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("string-key", name.getText().toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                    //startActivity(new Intent(LoginActivity.this, BrCodeActivity.class));
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Login Failed.Check your email/password",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
