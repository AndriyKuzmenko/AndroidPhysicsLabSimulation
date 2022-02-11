package com.example.androidphysicslab;

import java.util.ArrayList;

public class DiscObject extends Experiment
{
    private double m,mu,g,k;
    private ArrayList<Double> xList,vList;

    public DiscObject() { }

    public DiscObject(double m,double mu,double g,double k,ArrayList<Double> xList,ArrayList<Double> vList)
    {
        this.m=m;
        this.mu=mu;
        this.g=g;
        this.k=k;
        this.xList=xList;
        this.vList=vList;
    }
}