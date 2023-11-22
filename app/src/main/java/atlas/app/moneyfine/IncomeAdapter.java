package atlas.app.moneyfine;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.MyViewHolder> {

    ArrayList<Datatype> datatypeArrayList;
    Context ctx;
    DatabaseReference databaseReferencedelete;

    String email;
    public IncomeAdapter(Context ctx, ArrayList<Datatype> datatypeArrayList, String email) {
        this.datatypeArrayList = datatypeArrayList;
        this.ctx = ctx;
        this.email = email;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Datatype datatype = datatypeArrayList.get(position);
        databaseReferencedelete = FirebaseDatabase.getInstance().getReference("InputIncome");

        holder.title.setText(datatype.c1);
        holder.incometype.setText(datatype.c2);

        if(datatype.c3.length() == 6){
            holder.earn.setText(datatype.c3.substring(0,1)+"."+datatype.c3.substring(1,2)+"M");
        }else if(datatype.c3.length() == 5){
            holder.earn.setText(datatype.c3.substring(0,2)+"K");
        }else if(datatype.c3.length() == 4){
            holder.earn.setText(datatype.c3.substring(0,1)+"K");
        }else{
            holder.earn.setText(datatype.c3);
        }


        Picasso.with(ctx).load(R.drawable.delete).into(holder.btndelete);

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferencedelete.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            Datatype datatype1 = ds.getValue(Datatype.class);
                            if(datatype1.email.equals(email)){
                                if(datatype1.c1.equals(holder.title.getText().toString()) &&
                                        datatype1.c2.equals(holder.incometype.getText().toString()) &&
                                        datatype1.c3.equals(holder.earn.getText().toString())){
                                    databaseReferencedelete.child(ds.getKey()).removeValue();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return datatypeArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, incometype, earn;
        ImageView btndelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btndelete = itemView.findViewById(R.id.btndelete);
            title = itemView.findViewById(R.id.c1);
            incometype = itemView.findViewById(R.id.c2);
            earn = itemView.findViewById(R.id.c3);
        }
    }
}
