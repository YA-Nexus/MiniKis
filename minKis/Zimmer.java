package minKis;

import java.util.*;

public class Zimmer {
	private int id;
	private String name;
	private List<Bett> betten;
	
	//Standardkonstruktor
	public Zimmer() {
	}
	
	//Konstruktor mit allen Attributen
	public Zimmer(int id, String name, int abteilungID) {
		this.id = id;
		this.name = name;
		this.betten = new ArrayList<Bett>();
	}
	
	public void addBett(Bett bett) {
		betten.add(bett);
	}
	
	public int removeBett(int id) {
		int result = 0;
		for (Bett bett : betten) {
			if (bett.getId() == id) {
				boolean fDel = betten.remove(bett);
				if (!fDel) {
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
				", Betten : \n ";
		for (Bett bett : betten) {
			result += bett.toString();
			result += "\n";			
		}
		return result;	
	} 
}


