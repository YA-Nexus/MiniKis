package minKis;

import java.util.*;

public class Abteilungen {
	private int id;
	private String name;
	private List<Zimmer> _zimmer;
	
	//Standardkonstruktor
	public Abteilungen() {
	}
	
	//Konstruktor mit allen Attributen
	public Abteilungen(int id, String name, int abteilungID) {
		this.id = id;
		this.name = name;
		this._zimmer = new ArrayList<Zimmer>();
	}
	
	public void addZimmer(Zimmer zimmer) {
		_zimmer.add(zimmer);
	}
	
	public int removeZimmer(int id) {
		int result = 0;
		for (Zimmer zimmer : _zimmer) {
			if (zimmer.getId() == id) {
				boolean zDel = _zimmer.remove(zimmer);
				if (!zDel) {
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
				", Zimmer : \n";
		for (Zimmer zimmer : _zimmer) {
			result += zimmer.toString();
			result += "\n";
		}
		return result; 
	} 
}
