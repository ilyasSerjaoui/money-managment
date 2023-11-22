package atlas.app.moneyfine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class homepage extends AppCompatActivity {
    ImageView btnhome, btninc, btninv, btncom;
    String email;
    double r1, r2;
    double sum, sum1, sum3, sum2, total, total3, total1, total2;
    String value, value1, value2, value3, lvl;
    DatabaseReference databaseReference1, databaseReference2, databaseReference3, databaseReference4, databaseReference5, databaseReference6;
    TextView inc, inv, com, bud, sv, rk, accountlevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        btnhome = findViewById(R.id.btnhome);
        btninc = findViewById(R.id.btninc);
        btninv = findViewById(R.id.btninv);
        btncom = findViewById(R.id.btncom);
        email = getIntent().getStringExtra("emailAccount");
        inv = findViewById(R.id.inv);
        inc = findViewById(R.id.inc);
        com = findViewById(R.id.com);
        bud = findViewById(R.id.bud);
        sv = findViewById(R.id.sv);
        rk = findViewById(R.id.rk);
        accountlevel = findViewById(R.id.accountlevel);

        databaseReference1 = FirebaseDatabase.getInstance().getReference("InputIncome");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("InputInvest");
        databaseReference3 = FirebaseDatabase.getInstance().getReference("InputCommitment");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Datatype datatype1 = ds.getValue(Datatype.class);
                    if(datatype1.email.equals(email)){
                        value = ds.child("c3").getValue(String.class);
                        assert value != null;
                        total = Integer.parseInt(value);
                        sum = sum + total;
                        if(sum<=1000){
                            inc.setText("33%");
                        }else if(sum<=10000){
                            inc.setText("40%");
                        }else if(sum<=100000){
                            inc.setText("60%");
                        }if(sum<=1000000){
                            inc.setText("99%");
                        }
                    }else{
                        inc.setText("0%");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Datatype datatype1 = ds.getValue(Datatype.class);
                    if(datatype1.email.equals(email)){
                        value1 = ds.child("c1").getValue(String.class);
                        assert value1 != null;
                        total1 = Integer.parseInt(value1);
                        sum1 = sum1 + total1;
                        inv.setText(String.valueOf(RealPotencial(sum1, sum)).substring(0,4)+"%");
                    }else{
                        inv.setText("0%");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();

        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Datatype datatype1 = ds.getValue(Datatype.class);
                    if(datatype1.email.equals(email)){
                        value2 = ds.child("c3").getValue(String.class);
                        assert value2 != null;
                        total2 = Integer.parseInt(value2);
                        sum2 = sum2 + total2;
                        value3 = ds.child("c2").getValue(String.class);
                        assert value3 != null;
                        total3 = Integer.parseInt(value3);
                        sum3 = sum3 + total3;
                        com.setText(String.valueOf(RealPotencial(sum2*sum3, sum)).substring(0,4)+"%");
                    }else{
                        com.setText("0%");
                    }
                }
                r1 = sum1+sum2*sum3;
                r2 = (sum-r1)*100/sum;
                sv.setText(String.valueOf(r2).substring(0,3)+"%");
                if(sum <= sum2*sum3){
                    rk.setText("90%");
                }else if(sum == sum2*sum3){
                    rk.setText("60%");
                }else if(sum > sum2*sum3){
                    rk.setText("40%");
                }

                if(sum<=1000){
                    bud.setText("Bad");
                    lvl = "Bad";
                    accountlevel.setBackground(getResources().getDrawable(R.drawable.badshape));
                    accountlevel.setTextColor(getResources().getColor(R.color.red));
                }else if(sum<=10000){
                    bud.setText("Good");
                    lvl = "Good";
                    accountlevel.setBackground(getResources().getDrawable(R.drawable.goodshape));
                    accountlevel.setTextColor(getResources().getColor(R.color.orange));
                }else if(sum<=100000){
                    bud.setText("Exc.");
                    lvl = "Excellent";
                    accountlevel.setBackground(getResources().getDrawable(R.drawable.excshape));
                    accountlevel.setTextColor(getResources().getColor(R.color.yellow));
                }if(sum<=1000000) {
                    bud.setText("Adv.");
                    lvl = "Advanced";
                    accountlevel.setBackground(getResources().getDrawable(R.drawable.advshape));
                    accountlevel.setTextColor(getResources().getColor(R.color.green));
                }
                accountlevel.setText("Lvl: " + lvl);

                //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: PieChart ::::::::::::::::::::::

                pieEntryArrayList.add(new PieEntry((float) RealPotencial(sum1, sum), "Inv."));
                pieEntryArrayList.add(new PieEntry((float) RealPotencial(sum2*sum3, sum), "Comm."));
                pieEntryArrayList.add(new PieEntry((float) r2, "Save."));

                PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Subjects");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);

                pieChart.getDescription().setEnabled(false);
                pieChart.animateY(2000);
                pieChart.invalidate();

                //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: Menu Button ::::::::::::::::::::::

                btnhome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(homepage.this, homepage.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btninv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(homepage.this, Investing.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btninc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(homepage.this, Income.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btncom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(homepage.this, Commitment.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public double RealPotencial(double v, double t){
        return (v*100/t);
    }
}