package com.example.inclass15_group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyAdapter.InteractWithMainActivity {
    EditText et_taskName;
    Spinner sp_priority;
    Button bt_add;
    RecyclerView recyclerView;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;
    private FirebaseFirestore db;
    public static final String[] priorityList = {"Priority","High", "Medium", "Low"};
    ArrayList<Data> list=new ArrayList<Data>();
    ArrayList<Data> completedList=new ArrayList<Data>();
    ArrayList<Data> pendingList=new ArrayList<Data>();
    ArrayList<Data> mergedList=new ArrayList<Data>();
    ArrayList<Data> highCompleted=new ArrayList<Data>();
    ArrayList<Data> mediumCompleted=new ArrayList<Data>();
    ArrayList<Data> lowCompleted=new ArrayList<Data>();
    ArrayList<Data> highPending=new ArrayList<Data>();
    ArrayList<Data> mediumPending=new ArrayList<Data>();
    ArrayList<Data> lowPending=new ArrayList<Data>();
    Boolean showAll=true;
    Boolean showCompleted=false;
    Boolean showPending=false;




    Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO List");

        et_taskName=findViewById(R.id.et_taskName);
        sp_priority=findViewById(R.id.sp_priority);
        bt_add=findViewById(R.id.bt_add);
        recyclerView=findViewById(R.id.recyclerView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,priorityList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_priority.setAdapter(adapter);
        sp_priority.setOnItemSelectedListener(new spinnerOnItemSelectedListener());

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_taskName.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Please Enter Task Name",Toast.LENGTH_SHORT).show();

                }
               else if(sp_priority.getSelectedItem().toString().equals("Priority")){
                    Toast.makeText(MainActivity.this,"Please Select Priority",Toast.LENGTH_SHORT).show();

                }
                else{

                PrettyTime p = new PrettyTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String newDate=dateFormat.format(date);
                String nDate=newDate.replace("/","-");
                Log.d("Datedaw","date before pretty is"+newDate);
                Log.d("Datedaw","date before pretty is ndate"+nDate);

                data=new Data(et_taskName.getText().toString(),sp_priority.getSelectedItem().toString(),nDate,"","Pending");
                db = FirebaseFirestore.getInstance();
                Log.d("demo","Date is "+p.format(new Date()));
                db.collection("task")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("demo","Inside db");
                                data.DocID = documentReference.getId();

//                                rv_adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("gokul","Fail");

                            }
                        });


                updaterRecycler();

            }}

        });

        updaterRecycler();


    }

    public void updaterRecycler(){
        highCompleted.clear();
                mediumCompleted.clear();
        lowCompleted.clear();
                highPending.clear();
        mediumPending.clear();
                lowPending.clear();
        completedList.clear();
                pendingList.clear();
        mergedList.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("task")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Data data1 = new Data(document.getString("taskName"),document.getString("priority"), getPrettyDate(document.getString("date")),document.getId(),document.getString("taskStatus"));
                                Log.d("goku2", document.getId() + " => " + document.getData());
                                list.add(data1);
//                                if(data1.getTaskStatus().equals("Completed")){
//                                    completedList.add(data1);
//                                }
//                                else{
//                                    pendingList.add(data1);
//                                }
                                if(data1.getTaskStatus().equals("Completed")){
                                    if(data1.getPriority().equals("High")){
                                        highCompleted.add(data1);
                                        Log.d("highCompleted","merged list is"+highCompleted);

                                    }
                                    else if(data1.getPriority().equals("Medium")){
                                        mediumCompleted.add(data1);
                                    }
                                    else{
                                        lowCompleted.add(data1);
                                    }

                                    Log.d("listgoku","Completed list is"+completedList);

                                }
                                else{
                                    if(data1.getPriority().equals("High")){
                                        highPending.add(data1);
                                    }
                                    else if(data1.getPriority().equals("Medium")){
                                        mediumPending.add(data1);
                                    }
                                    else{
                                        lowPending.add(data1);
                                    }

                                    Log.d("listgoku","Pending list is"+pendingList);

                                }

                            }
                            Log.d("goku2", "List is "+list);
//                            if(list.size()>0){
                                completedList.addAll(highCompleted);
                                completedList.addAll(mediumCompleted);
                                completedList.addAll(lowCompleted);
                                pendingList.addAll(highPending);
                                pendingList.addAll(mediumPending);
                                pendingList.addAll(lowPending);
                                mergedList.addAll(pendingList);
                                mergedList.addAll(completedList);
                                Log.d("listgoku","merged list is"+mergedList);
                                Log.d("listgoku","normal list is"+list);
                                recyclerView=findViewById(R.id.recyclerView);

                                recyclerView.setHasFixedSize(true);

                                rv_layoutManager = new LinearLayoutManager(MainActivity.this);
                                recyclerView.setLayoutManager(rv_layoutManager);
                                rv_adapter = new MyAdapter(mergedList, MainActivity.this);
                                recyclerView.setAdapter(rv_adapter);
                                rv_adapter.notifyDataSetChanged();
                                Log.d("newlist","add merge list after retrieve is "+mergedList);


//                            }

                        }
                        else {
                            Log.d("goku2", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void deleteItem(final int position) {


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        mergedList.remove(position);

                        Log.d("deleteItem","list is "+mergedList);
                        rv_adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"Task Deleted",Toast.LENGTH_SHORT).show();

                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
        public void updateCheck(){
        if(showAll) {
            updaterRecycler();
            Toast.makeText(MainActivity.this, "Task Updated", Toast.LENGTH_SHORT).show();
        }
            if(showCompleted) {
                showCompleted();
                Toast.makeText(MainActivity.this, "Task Updated", Toast.LENGTH_SHORT).show();
            }
            if(showPending) {
                showPending();
                Toast.makeText(MainActivity.this, "Task Updated", Toast.LENGTH_SHORT).show();
            }
        }
    public static String getPrettyDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        try {
            Date converted = format.parse(date);
            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            return prettyTime.format(converted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Unknown Time";
    }

    static  class spinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showAll:
                //your code
                // EX : call intent if you want to swich to other activity
                updaterRecycler();
                showAll=true;
                showPending=false;
                showCompleted=false;
                return true;
            case R.id.showCompleted:
                //your code
                showCompleted();
                showAll=false;
                showCompleted=true;
                showPending=false;
                return true;
            case R.id.showPending:
                showPending();
                showAll=false;
                showCompleted=false;
                showPending=true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void showCompleted(){
        highCompleted.clear();
        mediumCompleted.clear();
        lowCompleted.clear();
        highPending.clear();
        mediumPending.clear();
        lowPending.clear();
        completedList.clear();
        pendingList.clear();
        mergedList.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("task")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Data data1 = new Data(document.getString("taskName"),document.getString("priority"), getPrettyDate(document.getString("date")),document.getId(),document.getString("taskStatus"));
                                Log.d("goku2", document.getId() + " => " + document.getData());
                                list.add(data1);
//                                if(data1.getTaskStatus().equals("Completed")){
//                                    completedList.add(data1);
//                                }
//                                else{
//                                    pendingList.add(data1);
//                                }
                                if(data1.getTaskStatus().equals("Completed")){
                                    if(data1.getPriority().equals("High")){
                                        highCompleted.add(data1);
                                        Log.d("highCompleted","merged list is"+highCompleted);

                                    }
                                    else if(data1.getPriority().equals("Medium")){
                                        mediumCompleted.add(data1);
                                    }
                                    else{
                                        lowCompleted.add(data1);
                                    }

                                    Log.d("listgoku","Completed list is"+completedList);

                                }


                            }
                            Log.d("goku2", "List is "+list);
//                            if(list.size()>0){
                            completedList.addAll(highCompleted);
                            completedList.addAll(mediumCompleted);
                            completedList.addAll(lowCompleted);

                            Log.d("listgoku","merged list is"+mergedList);
                            Log.d("listgoku","normal list is"+list);
                            recyclerView=findViewById(R.id.recyclerView);

                            recyclerView.setHasFixedSize(true);

                            rv_layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(rv_layoutManager);
                            rv_adapter = new MyAdapter(completedList, MainActivity.this);
                            recyclerView.setAdapter(rv_adapter);
                            rv_adapter.notifyDataSetChanged();
                            Log.d("newlist","add merge list after retrieve is "+completedList);


//                            }

                        }
                        else {
                            Log.d("goku2", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void showPending(){
        highCompleted.clear();
        mediumCompleted.clear();
        lowCompleted.clear();
        highPending.clear();
        mediumPending.clear();
        lowPending.clear();
        completedList.clear();
        pendingList.clear();
        mergedList.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("task")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Data data1 = new Data(document.getString("taskName"),document.getString("priority"), getPrettyDate(document.getString("date")),document.getId(),document.getString("taskStatus"));
                                Log.d("goku2", document.getId() + " => " + document.getData());
                                list.add(data1);
//                                if(data1.getTaskStatus().equals("Completed")){
//                                    completedList.add(data1);
//                                }
//                                else{
//                                    pendingList.add(data1);
//                                }
                                if(data1.getTaskStatus().equals("Pending")){
                                    if(data1.getPriority().equals("High")){
                                        highPending.add(data1);
                                    }
                                    else if(data1.getPriority().equals("Medium")){
                                        mediumPending.add(data1);
                                    }
                                    else{
                                        lowPending.add(data1);
                                    }

                                    Log.d("listgoku","Pending list is"+pendingList);

                                }

                            }
                            Log.d("goku2", "List is "+list);
//                            if(list.size()>0){

                            pendingList.addAll(highPending);
                            pendingList.addAll(mediumPending);
                            pendingList.addAll(lowPending);

                            Log.d("listgoku","merged list is"+mergedList);
                            Log.d("listgoku","normal list is"+list);
                            recyclerView=findViewById(R.id.recyclerView);

                            recyclerView.setHasFixedSize(true);

                            rv_layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(rv_layoutManager);
                            rv_adapter = new MyAdapter(pendingList, MainActivity.this);
                            recyclerView.setAdapter(rv_adapter);
                            rv_adapter.notifyDataSetChanged();
                            Log.d("newlist","add merge list after retrieve is "+mergedList);


//                            }

                        }
                        else {
                            Log.d("goku2", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
