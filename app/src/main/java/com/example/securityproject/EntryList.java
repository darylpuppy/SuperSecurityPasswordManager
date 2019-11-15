package com.example.securityproject;

import java.io.Serializable;
import java.util.ArrayList;

public class EntryList implements Serializable {
    ArrayList<Entry> entryList= new ArrayList<Entry>();

    public void add(Entry entry) {
        this.entryList.add(entry);
    }

    public int length(){
        return this.entryList.size();
    }

    public String toString(int index){
        Entry entry = this.entryList.get(index);
        return "Domain: " + entry.getDomain() + "\t Username: " + entry.getUsername() + "\t Password: " + entry.getPass();
    }

    public Entry get(int x) {
        return this.entryList.get(x);
    }
}

class Entry implements Serializable{
    String domain;
    String username;
    String pass;

    public Entry(String domain, String username, String pass) {
        this.domain = domain;
        this.username = username;
        this.pass = pass;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
