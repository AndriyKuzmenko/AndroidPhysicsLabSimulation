package com.example.androidphysicslab;

public class CollisionObject extends Experiment
{
    private double h1,h2,v,u,g;
    private boolean tall;

    public CollisionObject() { }

    /**
     * @param h1 - the rail heigh in meters
     * @param h2 - the table height in meters
     * @param v - the velocity of the first ball before it hit the second one
     * @param u - the velocity of the second ball after it was hit by the first one
     * @param g - the gravity acceleration of the Earth (m/sec^2)
     * @param tall - if true the animnation should be tall, else it should be wide.
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

    /**
     * @param h1 - the new value of the rail height
     * @return updates the rail height
     */

    public void setH1(double h1)
    {
        this.h1=h1;
    }

    /**
     * @param h2 - the new value of the table height
     * @return updates the table height
     */

    public void setH2(double h2)
    {
        this.h2=h2;
    }

    /**
     * @param v - the new value of the velocity of the first ball before it hit the second ball
     * @return updates the velocity of the first ball before it hit the second ball
     */

    public void setV(double v)
    {
        this.v=v;
    }

    /**
     * @param u - the new value of the velocity of the second ball after it was hit by the first one
     * @return updates the velocity of the second ball after it was hit by the first one
     */

    public void setU(double u)
    {
        this.u=u;
    }

    /**
     * @param g - the new value of the gravity acceleration
     * @return updates the gravity acceleration
     */

    public void setG(double g)
    {
        this.g=g;
    }

    /**
     * @param tall - the new value of the boolean variable that decides will the animation be tall or wide
     * @return updates the boolean variable that decides will the animation be tall or wide
     */

    public void setTall(boolean tall)
    {
        this.tall=tall;
    }

    /**
     * @return returns the rail height
     */

    public double getH1()
    {
        return h1;
    }

    /**
     * @return returns the table height
     */

    public double getH2()
    {
        return h2;
    }

    /**
     * @return returns the velocity of the second ball after it was hit by the first one
     */

    public double getV()
    {
        return v;
    }

    /**
     * @return returns the velocity of the second ball after it was hit by the first one
     */

    public double getU()
    {
        return u;
    }

    /**
     * @return returns the gravity acceleration
     */

    public double getG()
    {
        return g;
    }

    /**
     * @return returns the boolean variable that decides will the animation be tall or wide
     */

    public boolean getTall()
    {
        return tall;
    }
}