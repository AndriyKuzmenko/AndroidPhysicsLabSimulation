package com.example.androidphysicslab;

import java.util.ArrayList;

public class GalvanometerObject extends Experiment
{
    private double epsilon,area,hEarthMagneticField;
    private int n;
    private ArrayList<Double> iList,rList,tgList,thetaList;

    public GalvanometerObject() { }

    public GalvanometerObject(double epsilon,double area,int n,double hEarthMagneticField,ArrayList<Double> iList,ArrayList<Double> rList,ArrayList<Double> tgList,ArrayList<Double> thetaList)
    {
        this.epsilon=epsilon;
        this.area=area;
        this.n=n;
        this.iList=iList;
        this.rList=rList;
        this.hEarthMagneticField=hEarthMagneticField;
        this.tgList=tgList;
        this.thetaList=thetaList;
    }

    public void setEpsilon(double epsilon)
    {
        this.epsilon=epsilon;
    }

    public void setArea(double area)
    {
        this.area=area;
    }

    public void setN(int n)
    {
        this.n=n;
    }

    public void sethearthMagneticField(double hEarthMagneticField)
    {
        this.hEarthMagneticField=hEarthMagneticField;
    }

    public void setIList(ArrayList<Double> iList)
    {
        this.iList=iList;
    }

    public void setRList(ArrayList<Double> rList)
    {
        this.rList=rList;
    }

    public void setTgList(ArrayList<Double> tgList)
    {
        this.tgList=tgList;
    }

    public void setThetaList(ArrayList<Double> thetaList)
    {
        this.thetaList=thetaList;
    }

    public double getEpsilon()
    {
        return epsilon;
    }

    public double getArea()
    {
        return area;
    }

    public int getN()
    {
        return n;
    }

    public double getHEarthMagenticField()
    {
        return hEarthMagneticField;
    }

    public ArrayList<Double> getIList()
    {
        return iList;
    }

    public ArrayList<Double> getRList()
    {
        return rList;
    }

    public ArrayList<Double> getTgList()
    {
        return tgList;
    }

    public ArrayList<Double> getThetaList()
    {
        return thetaList;
    }
}