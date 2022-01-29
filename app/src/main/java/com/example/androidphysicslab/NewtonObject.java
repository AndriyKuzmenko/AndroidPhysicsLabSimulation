package com.example.androidphysicslab;

import java.util.ArrayList;

public class NewtonObject extends Experiment {
    private double m1,m2,mu,g;
    private ArrayList<Double> xList,vList;

    public NewtonObject() { }

    public NewtonObject(double m1,double m2,double g,double mu,ArrayList<Double> xList,ArrayList<Double> vList)
    {
        this.m1=m1;
        this.m2=m2;
        this.g=g;
        this.mu=mu;
        this.xList=xList;
        this.vList=vList;
    }

    public void setM1(double m1)
    {
        this.m1=m1;
    }

    public void setM2(double m2)
    {
        this.m2=m2;
    }

    public void setMu(double mu)
    {
        this.mu=mu;
    }

    public void setG(double g)
    {
        this.g=g;
    }

    public void setXList(ArrayList<Double> xList)
    {
        this.xList=xList;
    }

    public void setVList(ArrayList<Double> vList)
    {
        this.vList=vList;
    }

    public double getM1()
    {
        return m1;
    }

    public double getM2()
    {
        return m2;
    }

    public double getMu()
    {
        return mu;
    }

    public double getG()
    {
        return g;
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