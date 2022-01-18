package com.example.androidphysicslab;

import java.util.ArrayList;

public class FreeFallObject extends Experiment
{
    private ArrayList<Double> hList;
    private ArrayList<Double> vList;
    private String name;

    public FreeFallObject(ArrayList<Double> hList, ArrayList<Double> vList, String name)
    {
        this.hList=hList;
        this.vList=vList;
        this.name=name;
    }

    public void setHList(ArrayList<Double> hList)
    {
        this.hList=hList;
    }

    public void setVList(ArrayList<Double> vList)
    {
        this.vList=vList;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public ArrayList<Double> getHList()
    {
        return hList;
    }

    public ArrayList<Double> getVList()
    {
        return vList;
    }

    public String getName()
    {
        return name;
    }
}
