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

    public void setM(double m)
    {
        this.m=m;
    }

    public void setMu(double mu)
    {
        this.mu=mu;
    }

    public void setG(double g)
    {
        this.g=g;
    }

    public void getK(double k)
    {
        this.k=k;
    }

    public void setXList(ArrayList<Double> xList)
    {
        this.xList=xList;
    }

    public void setVList(ArrayList<Double> vList)
    {
        this.vList=vList;
    }

    public double getM()
    {
        return m;
    }

    public double getMu()
    {
        return mu;
    }

    public double getG()
    {
        return g;
    }

    public double getK()
    {
        return k;
    }

    public ArrayList<Double> getXList()
    {
        return xList;
    }

    public ArrayList<Double> getVList()
    {
        return vList;
    }
}