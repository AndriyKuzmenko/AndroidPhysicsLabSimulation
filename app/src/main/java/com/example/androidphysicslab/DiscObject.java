package com.example.androidphysicslab;

import java.util.ArrayList;

public class DiscObject extends Experiment
{
    private double m,mu,g,k,v0,l0,deltax;
    private ArrayList<Double> lList,vList;

    public DiscObject() { }

    public DiscObject(double m,double mu,double g,double k,double v0,double l0,double deltax,ArrayList<Double> lList,ArrayList<Double> vList)
    {
        this.m=m;
        this.mu=mu;
        this.g=g;
        this.k=k;
        this.v0=v0;
        this.l0=l0;
        this.deltax=deltax;
        this.lList=lList;
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

    public void setK(double k)
    {
        this.k=k;
    }

    public void setV0(double v0)
    {
        this.v0=v0;
    }

    public void setL0(double l0)
    {
        this.l0 = l0;
    }

    public void setDeltax(double deltax)
    {
        this.deltax=deltax;
    }

    public void setLList(ArrayList<Double> lList)
    {
        this.lList=lList;
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

    public double getV0()
    {
        return v0;
    }

    public double getL0()
    {
        return l0;
    }

    public double getDeltax()
    {
        return deltax;
    }

    public ArrayList<Double> getLList()
    {
        return lList;
    }

    public ArrayList<Double> getVList()
    {
        return vList;
    }
}