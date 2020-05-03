package com.example.inclass15_group7;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public static InteractWithMainActivity interact;
    Context ctx;

    ArrayList<Data> data = new ArrayList<Data>();
    static String EP = "expense";
    private FirebaseFirestore db;

    public MyAdapter(ArrayList<Data> data, Context mainActivity) {
        this.data = data;
        this.ctx =(MainActivity) mainActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, final int position) {
        interact = (InteractWithMainActivity) ctx;
//        holder.tv_expName.setText(data.get(position).getTitle());
//        holder.tv_amt.setText("$"+expense.get(position).getCost().toString());
//        holder.exp = expense.get(position);
        holder.tv_taskName.setText((data.get(position).getTaskName()));
        holder.tv_priority.setText((data.get(position).getPriority()));
        holder.tv_time.setText((data.get(position).getDate()));
        Log.d("gokuada","Code in onbind");
        if(data.get(position).getTaskStatus().equals("Completed")) {
            holder.cb_check.setChecked(true);
        }
//        else{
//            holder.cb_check.setChecked(false);
//
//        }
        holder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                db = FirebaseFirestore.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String newDate=dateFormat.format(date);
                String nDate=newDate.replace("/","-");
                if ( isChecked )
                {
                    // perform logic
                    Log.d("insidecheck","selected");
                    db.collection("task")
                            .document(data.get(position).getDocID())
                            .update("taskStatus","Completed","date",nDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                    interact.updateCheck();



                }
                if(!isChecked){
                    Log.d("insidecheck","unselected");
                    db.collection("task")
                            .document(data.get(position).getDocID())
                            .update("taskStatus","Pending")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                    interact.updateCheck();
                }

            }
        });

        db = FirebaseFirestore.getInstance();

        holder.linearLayout_Recycler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                db.collection("task").document(data.get(position).DocID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("adaptergoku", "DocumentSnapshot successfully deleted!");
                                interact.deleteItem(position);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("adaptergoku", "Error deleting document", e);
                            }
                        });
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_taskName, tv_priority,tv_time;
        LinearLayout linearLayout_Recycler;
        CheckBox cb_check;

        public ViewHolder(@NonNull final  View itemView) {
            super(itemView);
            tv_taskName = itemView.findViewById(R.id.tv_taskName);
            tv_priority = itemView.findViewById(R.id.tv_priority);
            tv_time = itemView.findViewById(R.id.tv_time);
            cb_check = itemView.findViewById(R.id.cb_check);

            linearLayout_Recycler=itemView.findViewById(R.id.linearLayout_Recycler);



        }
    }

    public interface InteractWithMainActivity
    {
        void deleteItem(int position);
        void updateCheck();
    }
}

