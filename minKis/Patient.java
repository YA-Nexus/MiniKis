package minKis;

import java.sql.Date;
import java.util.*;

public class Patient {
    private int id;
    private String name;
    private String vorname;
    private Date geburtsdatum;
    private int insID; // Institut-ID (ehemals Krankenhaus-ID)
    private List<Adresse> adressen; // Adresse-ID

    //Standardkonstruktor
    public Patient() {
    }

    //Konstruktor mit allen Attributen
    public Patient(int id, String name, String vorname, Date geburtsdatum, int insID) {
        this.id = id;
        this.name = name;
        this.vorname = vorname;
        this.geburtsdatum = geburtsdatum;
        this.insID = insID; 
        this.adressen = new ArrayList<Adresse>();
    }

    public void addAdresse(Adresse adresse) {
    	adressen.add(adresse);
    }
    
    public int removeAdresse(int id) {
    	int result = 0;
    	for (Adresse adresse : adressen) {
			if (adresse.getId() == id) {
				boolean aDel = adressen.remove(adresse);
				if (!aDel) {
					result = -1;
				} else {
					result = 1;
				}
			}
		}
    	
    	return result;
    }
    
    public void addAdressen(List<Adresse> adressen) {   	
    	adressen.addAll(adressen);
    }
    
    //Getter und Setter
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public int getInsID() {
        return insID;
    }

    public void setInsID(int insID) {
        this.insID = insID;
    }    

    //Override: Überschreibt die toString Methode für bessere Darstellung
    @Override
    public String toString() {
        return  "ID = " + id +
                ", Name = " + name + 
                ", Vorname = " + vorname + 
                ", Geburtsdatum = " + geburtsdatum +
                ", InsID = " + insID;
    } 
    
}
