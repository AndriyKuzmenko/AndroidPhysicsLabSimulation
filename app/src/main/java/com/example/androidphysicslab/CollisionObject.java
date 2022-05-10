package com.example.androidphysicslab;

public class CollisionObject extends Experiment
{
    private double h1,h2,v,u,g;
    private boolean tall;

    public CollisionObject() { }

    /**
     * @param h1 - the rail heigh in meters
     * @parm v - the velocity of the first ball before it hit the second one
     * @param u - the velocity of the second ball after it was hit by the first one
     * @param h2 - the table height in meters
     * @param g - the gravity acceleration of the Earth (m/sec^2)
     * @param tall
     */

    public CollisionObject(double h1,double h2,double v,double u,double g,boolean tall)
    {
        this.h1=h1;
        this.h2=h2;
        this.v=v;
        this.u=u;
        this.g=g;
        this.tall=tall;
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

    public void setTall(boolean tall)
    {
        this.tall=tall;
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

    public boolean getTall()
    {
        return tall;
    }
}