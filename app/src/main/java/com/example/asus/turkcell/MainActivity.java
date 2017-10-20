package com.example.asus.turkcell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;




public class MainActivity extends AppCompatActivity {

    TextView CreateAccount;
    private Button signIn;
    private  TextView inputEmail;
    private  TextView inputPassword;
    private ProgressDialog progress;

    private static final int RC_SIGN_IN = 123;
    FirebaseDatabase dB;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);

        Start();
        onClickButtonListener();

    }

    //create Account
    public void onClickButtonListener(){
        CreateAccount = (TextView)findViewById(R.id.gotosignup);
        CreateAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ıntent = new Intent(MainActivity.this,SignUp.class );
                        startActivity(ıntent);
                    }
                }
        );
    }
//create Account



    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null)
        {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

    }

    public void Start()
    {

        dB =FirebaseDatabase.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        inputEmail = (TextView) findViewById(R.id.email);
        inputPassword = (TextView) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.login);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Giriş Yapılıyor...");
                progress.show();

//                String email = inputEmail.getText().toString();
//                String password = inputPassword.getText().toString();

                String email = "ozenc.celik@ceng.deu.edu.tr";
                final String password = "123456";//Düzenlenmesi gerekiyor. Kullanıcı iki farklı şifreli hesapla uygulamaya giriş yapamaz.

                if(TextUtils.isEmpty(email)){
                    progress.dismiss();
                    inputEmail.setError("E-posta adresinizi giriniz.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    progress.dismiss();
                    inputPassword.setError("Şifrenizi giriniz.");
                    return;
                }

                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        progress.dismiss();
                                        inputPassword.setError("Şifreniz en az 6 karakter içermelidir.");
                                    } else {
                                        progress.dismiss();
                                        Toast.makeText(MainActivity.this, "Giriş yapılamadı. E-postanızı ve şifrenizi kontrol ediniz", Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    Toast.makeText(MainActivity.this, "Giriş yapıldı.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    startActivity(intent);
                                    progress.dismiss();
                                    finish();

                                }
                            }
                        });

            }
        });



    }









}
