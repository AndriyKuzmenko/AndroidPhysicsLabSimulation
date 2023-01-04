package com.example.androidphysicslab;

import java.util.ArrayList;

public class FreeFallObject extends Experiment
{
    private ArrayList<Double> hList;
    private ArrayList<Double> vList;
    private double m,g;

    public FreeFallObject(ArrayList<Double> hList, ArrayList<Double> vList, String name, double g, double m)
    {
        this.hList=hList;
        this.vList=vList;
        this.name=name;
        this.m=m;
        this.g=g;
    }

    public FreeFallObject(){}

    public void setHList(ArrayList<Double> hList)
    {
        this.hList=hList;
    }

    public void setVList(ArrayList<Double> vList)
    {
        this.vList=vList;
    }

    public void setM(double m)
    {
        this.m=m;
    }

    public void setG(double g)
    {
        this.g=g;
    }

    public ArrayList<Double> getHList()
    {
        return hList;
    }

    public ArrayList<Double> getVList()
    {
        return vList;
    }

    public double getM()
    {
        return m;
    }

    public double getG()
    {
        return g;
    }
}
