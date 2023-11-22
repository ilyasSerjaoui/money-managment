package atlas.app.moneyfine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText fname, lname, email, pass, cpass, db, mb, yb, emailvalidate, passwordvalidate;
    Button  btnre, btnvalidate;
    TextView gotologin, showre;

    ImageView iconlogin, iconre;
    DatabaseReference databaseReferenceaccount;

    LinearLayout re, log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        db = findViewById(R.id.db);
        mb = findViewById(R.id.mb);
        yb = findViewById(R.id.yb);
        btnre = findViewById(R.id.btnre);
        emailvalidate = findViewById(R.id.emailvalidate);
        passwordvalidate = findViewById(R.id.passwordvalidate);
        btnvalidate = findViewById(R.id.btnvalidate);
        gotologin = findViewById(R.id.gotologin);
        showre = findViewById(R.id.showre);
        re = findViewById(R.id.re);
        log = findViewById(R.id.log);
        iconlogin = findViewById(R.id.iconlogin);
        iconre = findViewById(R.id.iconre);

        databaseReferenceaccount = FirebaseDatabase.getInstance().getReference("Account");

        SharedPreferences sharedPreferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        String checkBox = sharedPreferences.getString("remember", "");
        if(!checkBox.isEmpty()){
            Intent ints = new Intent(MainActivity.this, homepage.class);
            ints.putExtra("emailAccount", checkBox);
            startActivity(ints);
        }else{
            Toast.makeText(this, "Please enter your personal account. ", Toast.LENGTH_LONG).show();
        }

        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataValidate();
            }
        });

        showre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                re.setVisibility(View.VISIBLE);
                iconre.setVisibility(View.VISIBLE);
                iconlogin.setVisibility(View.INVISIBLE);
                log.setVisibility(View.INVISIBLE);
            }
        });

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                re.setVisibility(View.INVISIBLE);
                iconre.setVisibility(View.INVISIBLE);
                iconlogin.setVisibility(View.VISIBLE);
                log.setVisibility(View.VISIBLE);
            }
        });

        btnre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationValidate();
            }
        });
    }

    public void DataValidate(){
        databaseReferenceaccount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Account account = ds.getValue(Account.class);
                    if(emailvalidate.getText().toString().equals(account.email)){
                        if(passwordvalidate.getText().toString().equals(account.pass)){
                            Intent ints = new Intent(MainActivity.this, homepage.class);
                            ints.putExtra("emailAccount", emailvalidate.getText().toString());
                            startActivity(ints);


                            SharedPreferences sharedPreferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                            SharedPreferences.Editor  editor = sharedPreferences.edit();
                            editor.putString("remember", emailvalidate.getText().toString());
                            editor.apply();
                        }else{
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void sendDataAccount(){
        Account account = new Account(fname.getText().toString(), lname.getText().toString(), email.getText().toString(),
                pass.getText().toString(), db.getText().toString(), mb.getText().toString(), yb.getText().toString());
        Intent ints = new Intent(MainActivity.this, homepage.class);
        ints.putExtra("emailAccount", email.getText().toString());
        startActivity(ints);
        databaseReferenceaccount.child(email.getText().toString().replace(".", "_")).setValue(account);
    }
    public void registrationValidate(){
        if(!fname.getText().toString().isEmpty()){
            if(!lname.getText().toString().isEmpty()){
                if(!email.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    if(!pass.getText().toString().isEmpty() && pass.getText().toString().length() >= 7){
                        if(!cpass.getText().toString().isEmpty() && cpass.getText().toString().equals(pass.getText().toString())){
                            if(!db.getText().toString().isEmpty() && Integer.parseInt(db.getText().toString()) <= 31 && !db.getText().toString().equals("0")){
                                if(!mb.getText().toString().isEmpty() && Integer.parseInt(mb.getText().toString()) <= 12 && !mb.getText().toString().equals("0")){
                                    if(!yb.getText().toString().isEmpty() && Integer.parseInt(yb.getText().toString()) < 2020){
                                        Toast.makeText(this, "Data are Registered", Toast.LENGTH_SHORT).show();
                                        sendDataAccount();

                                    }else{
                                        Toast.makeText(this, "Please, Correct your birthdate.", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(this, "Please, Correct your birthdate.", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(this, "Please, Correct your birthdate.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this, "Something went wrong on password.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this, "Please, Correct your password, also must be have 7 character or many.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Please, Correct your E-mail.", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "Please, Correct your last name.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Please, Correct your first name.", Toast.LENGTH_LONG).show();
        }
    }
}