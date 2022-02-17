package com.example.androidphysicslab;

public class CollisionObject extends Experiment
{
    private double h1,h2,v,u,g;

    public CollisionObject() { }

    public CollisionObject(double h1,double h2,double v,double u,double g)
    {
        this.h1=h1;
        this.h2=h2;
        this.v=v;
        this.u=u;
        this.g=g;
    }

    public void setH1(double h1)
    {
        this.h1=h1;
    }

    public void setH2(double h2)
    {
        this.h2=h2;
    }

    public void setV(double v)
    {
        this.v=v;
    }

    public void setU(double u)
    {
        this.u=u;
    }

    public void setG(double g)
    {
        this.g=g;
    }

    public double getH1()
    {
        return h1;
    }

    public double getH2()
    {
        return h2;
    }

    public double getV()
    {
        return v;
    }

    public double getU()
    {
        return u;
    }

    public double getG()
    {
        return g;
    }
}