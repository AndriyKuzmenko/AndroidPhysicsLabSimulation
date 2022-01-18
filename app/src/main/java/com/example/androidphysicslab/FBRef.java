package com.example.androidphysicslab;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBRef
{
    public static FirebaseAuth mAuth= FirebaseAuth.getInstance();
    public static FirebaseUser mUser;
    public static FirebaseDatabase database=FirebaseDatabase.getInstance();
    public static DatabaseReference myRef;
}