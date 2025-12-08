package edu.upc.dsa.dao;

public class FactorySession {
    public static Session openSession() {
        return new SessionImpl();
    }
}
