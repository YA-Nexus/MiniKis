package minKis;

import java.util.*;

public class Krankenhaus {
	private int id;
	private String name;
	private List<Standort> standorte;
	
	//Standardkonstruktor
	public Krankenhaus() {
	}
	
	//Konstruktor mit allen Attributen
	public Krankenhaus(int id, String name) {
		this.id = id;
		this.name = name;
		this.standorte = new ArrayList<Standort>();
	}
	
	public void addStandort(Standort standort) {
		standorte.add(standort);
	}
	
	public int removeStandort(int id) {
		int result = 0;
		for (Standort standort : standorte) {
			if (standort.getId() == id) {
				boolean bDel = standorte.remove(standort);
				if (!bDel) {
					result = -1;
				} else {
					result = 1;
				}
			}
		}		
		return result;
	}
	
	//Getter und Setter
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	
	@Override
	public String toString() {
		String result = "ID = " + id +
				", Name = " + name +
				", Standorte : \n ";
		for (Standort standort : standorte) {
			result += standort.toString();
			result += "\n";
		}
		return result;
	}	
	
}




