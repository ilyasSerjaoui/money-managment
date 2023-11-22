package atlas.app.moneyfine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Commitment extends AppCompatActivity {
    ImageView btnhome, btninc, btninv, btncom;
    Button btnadd;
    RecyclerView recyclecommitment;
    DatabaseReference databaseReferencecommitment;
    ArrayList<Datatype> datatypeArrayList;
    int sum, sum1, total, total1;
    String value, value1, lvl;
    TextView ia, accountlevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitment);

        btnadd = findViewById(R.id.btnadd);
        lvl = getIntent().getStringExtra("lvl");
        btnhome = findViewById(R.id.btnhome);
        btninc = findViewById(R.id.btninc);
        btninv = findViewById(R.id.btninv);
        btncom = findViewById(R.id.btncom);
        ia = findViewById(R.id.ia);
        String email = getIntent().getStringExtra("emailAccount");
        accountlevel = findViewById(R.id.accountlevel);

        if(lvl.equals("Bad")){
            accountlevel.setBackground(getResources().getDrawable(R.drawable.badshape));
            accountlevel.setTextColor(getResources().getColor(R.color.red));
        }else if(lvl.equals("Good")){
            accountlevel.setBackground(getResources().getDrawable(R.drawable.goodshape));
            accountlevel.setTextColor(getResources().getColor(R.color.orange));
        }else if(lvl.equals("Excellent")){
            accountlevel.setBackground(getResources().getDrawable(R.drawable.excshape));
            accountlevel.setTextColor(getResources().getColor(R.color.yellow));
        }if(lvl.equals("Advanced")) {
            accountlevel.setBackground(getResources().getDrawable(R.drawable.advshape));
            accountlevel.setTextColor(getResources().getColor(R.color.green));
        }

        accountlevel.setText("Lvl: " + lvl);

        recyclecommitment = findViewById(R.id.recyclecom);

        recyclecommitment.setHasFixedSize(true);
        recyclecommitment.setLayoutManager(new LinearLayoutManager(Commitment.this));

        datatypeArrayList = new ArrayList<>();
        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();

        databaseReferencecommitment = FirebaseDatabase.getInstance().getReference("InputCommitment");

        databaseReferencecommitment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sum = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    Datatype datatype = ds.getValue(Datatype.class);
                    if(datatype.email.equals(email)) {
                        datatypeArrayList.add(datatype);
                        value = ds.child("c2").getValue(String.class);
                        assert value != null;
                        total = Integer.parseInt(value);
                        sum = sum + total;
                        value1 = ds.child("c3").getValue(String.class);
                        assert value1 != null;
                        total1 = Integer.parseInt(value1);
                        sum1 = sum1 + total1;
                        if(String.valueOf(sum).length() == 6){
                            ia.setText(String.valueOf(sum*sum1).substring(0,1)+"."+String.valueOf(sum*sum1).substring(1,2)+"M");
                        }else if(String.valueOf(sum*sum1).length() == 5){
                            ia.setText(String.valueOf(sum*sum1).substring(0,3)+"K");
                        }else if(String.valueOf(sum*sum1).length() == 4){
                            ia.setText(String.valueOf(sum*sum1).substring(0,2)+"K");
                        }else if(String.valueOf(sum*sum1).length() == 3){
                            ia.setText(String.valueOf(sum*sum1).substring(0,1)+"K");
                        }else{
                            ia.setText(String.valueOf(sum*sum1));
                        }
                        pieEntryArrayList.add(new PieEntry(RealPotencial(Integer.parseInt(datatype.c3), sum*sum1) * 1f, datatype.c1));
                    }
                }

                PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Subjects");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);

                pieChart.getDescription().setEnabled(false);
                pieChart.animateY(2000);
                pieChart.invalidate();

                CommitmentAdapter commitmentAdapter = new CommitmentAdapter(Commitment.this, datatypeArrayList, email);
                recyclecommitment.setAdapter(commitmentAdapter);

                //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: Menu Button ::::::::::::::::::::::

                btnhome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Commitment.this, homepage.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btninv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Commitment.this, Investing.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btninc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Commitment.this, Income.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btncom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Commitment.this, Commitment.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCommitment dialogCommitment = new DialogCommitment(email);
                dialogCommitment.show(getSupportFragmentManager(), "Commitment inputs");
            }
        });
    }
    public float RealPotencial(float v, float t){
        return (v*100/t);
    }
}