package atlas.app.moneyfine;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogIncome extends AppCompatDialogFragment {
    EditText c1, c2, c3;
    DatabaseReference dataRendererinputs;
    String email;

    public DialogIncome(String email) {
        this.email = email;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.inputincome, null);
        c1 = v.findViewById(R.id.c1);
        c2 = v.findViewById(R.id.c2);
        c3 = v.findViewById(R.id.c3);

        dataRendererinputs = FirebaseDatabase.getInstance().getReference("InputIncome");

        builder.setView(v)
                .setTitle("Income")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!c1.getText().toString().isEmpty()){
                            if(!c2.getText().toString().isEmpty()){
                                if(!c3.getText().toString().isEmpty()){
                                    Datatype datatype = new Datatype(c1.getText().toString(), c2.getText().toString(), c3.getText().toString(), email);

                                    dataRendererinputs.push().setValue(datatype);
                                }else{
                                    Toast.makeText(getActivity(), "Please, Correct your earning", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getActivity(), "Please, Correct your income type", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "Please, Correct your title", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return builder.create();
    }
}
