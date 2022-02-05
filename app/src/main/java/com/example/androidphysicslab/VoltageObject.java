package com.example.androidphysicslab;

import java.util.ArrayList;

public class VoltageObject extends Experiment
{
    ArrayList<Double> rList,vList,iList;
    double epsilon,internalR,maxR;

    public VoltageObject() { }

    public VoltageObject(ArrayList<Double> rList,ArrayList<Double> iList,ArrayList<Double> vList,double epsilon,double internalR,double maxR)
    {
        this.rList=rList;
        this.vList=vList;
        this.iList=iList;
        this.epsilon=epsilon;
        this.internalR=internalR;
        this.maxR=maxR;
    }

    public ArrayList<Double> getRList()
    {
        return rList;
    }

    public ArrayList<Double> getIList()
    {
        return iList;
    }

    public ArrayList<Double> getVList()
    {
        return vList;
    }

    public double getEpsilon()
    {
        return epsilon;
    }

    public double getInternalR()
    {
        return internalR;
    }

    public double getMaxR()
    {
        return maxR;
    }

    public void setRList(ArrayList<Double> rList)
    {
        this.rList=rList;
    }

    public void setIList(ArrayList<Double> iList)
    {
        this.iList=iList;
    }

    public void setVList(ArrayList<Double> vList)
    {
        this.vList=vList;
    }

    public void setEpsilon(double epsilon)
    {
        this.epsilon=epsilon;
    }

    public void setInternalR(double internalR)
    {
        this.internalR=internalR;
    }

    public void setMaxR(double maxR)
    {
        this.maxR=maxR;
    }
}
