package com.example.androidphysicslab;

import java.util.ArrayList;

public class SpringObject extends Experiment
{
    private ArrayList<Double> xList;
    private ArrayList<Double> vList;
    private ArrayList<Double> aList;
    private double m,g,k,amplitude,periods;

    public SpringObject() { }

    public SpringObject(ArrayList<Double> xList, ArrayList<Double> vList, ArrayList<Double> aList, double m, double g, double k, double amplitude, double periods)
    {
        this.xList=xList;
        this.vList=vList;
        this.aList=aList;
        this.m=m;
        this.g=g;
        this.k=k;
        this.amplitude=amplitude;
        this.periods=periods;
    }

    public ArrayList<Double> getXList()
    {
        return xList;
    }

    public ArrayList<Double> getVList()
    {
        return vList;
    }

    public ArrayList<Double> getAList()
    {
        return aList;
    }

    public double getM()
    {
        return m;
    }

    public double getG()
    {
        return g;
    }

    public double getK()
    {
        return k;
    }

    public double getAmplitude()
    {
        return amplitude;
    }

    public double getPeriods()
    {
        return periods;
    }

    public void setXList(ArrayList<Double> xList)
    {
        this.xList=xList;
    }

    public void setVList(ArrayList<Double> vList)
    {
        this.vList=vList;
    }

    public void setAList(ArrayList<Double> aList)
    {
        this.aList=aList;
    }

    public void setM(double m)
    {
        this.m=m;
    }

    public void setG(double g)
    {
        this.g=g;
    }

    public void setK(double k)
    {
        this.k=k;
    }

    public void setAmplitude(double amplitude)
    {
        this.amplitude=amplitude;
    }

    public void setPeriods(double periods)
    {
        this.periods=periods;
    }
}