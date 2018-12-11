/*
Name: Jason Bugallon
ID: 85806059

This file helps setup the FireBase database connection and
handles reading and writing and anything else
that involves the FireBase database
*/

package com.example.jason.tutorial;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FireBaseHelper {
    private DatabaseReference mDatabase;
    private  ArrayList<String> tasks;
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public FireBaseHelper(DatabaseReference dRef, final ArrayList<String> tasks, final ArrayAdapter<String> adapter)
    {
        this.tasks = tasks;
        this.adapter = adapter;
        mDatabase = dRef;
        mDatabase = dRef.child("Tasks");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String task = dataSnapshot.getValue(String.class);
                tasks.add(task);
                keys.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int index = keys.indexOf(dataSnapshot.getKey());
                tasks.set(index, dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String task = dataSnapshot.getValue(String.class);
                String key = dataSnapshot.getKey();
                tasks.remove(task);
                keys.remove(key);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeData(String task)
    {
        mDatabase.push().setValue(task);
    }

    public void deleteData(String task)
    {
        int index = tasks.indexOf(task);
        String key = keys.get(index);
        mDatabase.child(key).removeValue();
    }
}
