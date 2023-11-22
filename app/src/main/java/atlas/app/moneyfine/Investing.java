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

import com.github.mikephil.charting.charts.Chart;
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

public class Investing extends AppCompatActivity {

    ImageView btnhome, btninc, btninv, btncom;
    Button btnadd;
    RecyclerView recycleinvest;
    DatabaseReference databaseReferenceinvesting;
    ArrayList<Datatype> datatypeArrayList;
    int sum, total;
    String value, lvl;
    TextView ia, accountlevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investing);
        btnadd = findViewById(R.id.btnadd);
        lvl = getIntent().getStringExtra("lvl");
        btnhome = findViewById(R.id.btnhome);
        btninc = findViewById(R.id.btninc);
        btninv = findViewById(R.id.btninv);
        btncom = findViewById(R.id.btncom);
        ia = findViewById(R.id.ia);

        recycleinvest = findViewById(R.id.recycleinvest);
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

        recycleinvest.setHasFixedSize(true);
        recycleinvest.setLayoutManager(new LinearLayoutManager(Investing.this));

        datatypeArrayList = new ArrayList<>();
        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> pieEntryArrayList = new ArrayList<>();

        String email = getIntent().getStringExtra("emailAccount");
        databaseReferenceinvesting = FirebaseDatabase.getInstance().getReference("InputInvest");

        databaseReferenceinvesting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sum = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    Datatype datatype = ds.getValue(Datatype.class);
                    if(datatype.email.equals(email)) {
                        datatypeArrayList.add(datatype);
                        value = ds.child("c1").getValue(String.class);
                        assert value != null;
                        total = Integer.parseInt(value);
                        sum = sum + total;
                        if(String.valueOf(sum).length() == 6){
                            ia.setText(String.valueOf(sum).substring(0,1)+"."+String.valueOf(sum).substring(1,2)+"M");
                        }else if(String.valueOf(sum).length() == 5){
                            ia.setText(String.valueOf(sum).substring(0,3)+"K");
                        }else if(String.valueOf(sum).length() == 4){
                            ia.setText(String.valueOf(sum).substring(0,2)+"K");
                        }else if(String.valueOf(sum).length() == 3){
                            ia.setText(String.valueOf(sum).substring(0,1)+"K");
                        }else{
                            ia.setText(String.valueOf(sum));
                        }
                        pieEntryArrayList.add(new PieEntry(RealPotencial(Integer.parseInt(datatype.c1), sum) * 1f, datatype.c1));
                    }
                }

                PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Subjects");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);

                pieChart.getDescription().setEnabled(false);
                pieChart.animateY(2000);
                pieChart.invalidate();

                InvestAdapter investAdapter = new InvestAdapter(Investing.this, datatypeArrayList, email);
                recycleinvest.setAdapter(investAdapter);



                //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: Menu Button ::::::::::::::::::::::

                btnhome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Investing.this, homepage.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btninv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Investing.this, Investing.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btninc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Investing.this, Income.class);
                        ints.putExtra("emailAccount", email);
                        ints.putExtra("lvl", lvl);
                        startActivity(ints);
                    }
                });
                btncom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ints = new Intent(Investing.this, Commitment.class);
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
                DialogInvest dialogInvest = new DialogInvest(email);
                dialogInvest.show(getSupportFragmentManager(), "Investing inputs");
            }
        });
    }
    public float RealPotencial(float v, float t){
        return (v*100/t);
    }
}