package atlas.app.moneyfine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InvestAdapter extends RecyclerView.Adapter<InvestAdapter.MyViewHolder> {
    ArrayList<Datatype> datatypeArrayList;
    Context ctx;
    String email;

    public InvestAdapter(Context ctx, ArrayList<Datatype> datatypeArrayList, String email) {
        this.datatypeArrayList = datatypeArrayList;
        this.ctx = ctx;
        this.email = email;
    }

    @NonNull
    @Override
    public InvestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.items, parent, false);
        return new InvestAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InvestAdapter.MyViewHolder holder, int position) {
        Datatype datatype = datatypeArrayList.get(position);

        if(datatype.c1.length() == 6){
            holder.price.setText(datatype.c1.substring(0,1)+"."+datatype.c1.substring(1,2)+"M");
        }else if(datatype.c1.length() == 5){
            holder.price.setText(datatype.c1.substring(0,2)+"K");
        }else if(datatype.c1.length() == 4){
            holder.price.setText(datatype.c1.substring(0,1)+"K");
        }else{
            holder.price.setText(datatype.c1);
        }

        holder.category.setText(datatype.c2);

        if(datatype.c3.length() == 6){
            holder.estimate.setText(datatype.c3.substring(0,1)+"."+datatype.c3.substring(1,2)+"M");
        }else if(datatype.c3.length() == 5){
            holder.estimate.setText(datatype.c3.substring(0,2)+"K");
        }else if(datatype.c3.length() == 4){
            holder.estimate.setText(datatype.c3.substring(0,1)+"K");
        }else{
            holder.estimate.setText(datatype.c3);
        }


        Picasso.with(ctx).load(R.drawable.delete).into(holder.btndelete);
        DatabaseReference databaseReferencedelete = FirebaseDatabase.getInstance().getReference("InputInvest");

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferencedelete.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            Datatype datatype1 = ds.getValue(Datatype.class);
                            if(datatype1.email.equals(email)){
                                if(datatype1.c1.equals(holder.price.getText().toString()) &&
                                        datatype1.c2.equals(holder.category.getText().toString()) &&
                                        datatype1.c3.equals(holder.estimate.getText().toString())){
                                    databaseReferencedelete.child(ds.getKey()).removeValue();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return datatypeArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView price, category, estimate;
        ImageView btndelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.c1);
            btndelete = itemView.findViewById(R.id.btndelete);
            category = itemView.findViewById(R.id.c2);
            estimate = itemView.findViewById(R.id.c3);
        }
    }
}
