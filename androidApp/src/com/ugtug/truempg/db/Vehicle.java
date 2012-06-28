package com.ugtug.truempg.db;

import java.io.Serializable;

//This is a basic POJO for vehicles
public class Vehicle implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final int INVALID_ID = -1;
    public long id=-1;
    public String make="";
    public String model="";
    public String vin="";
    public String year="";
    public String name="";

}


