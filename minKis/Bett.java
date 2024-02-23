package minKis;

public class Bett {
	private int id;
	private String name;
	
	//Standardkonstruktor
	public Bett() {
	}
	
	//Konstruktor mit allen Attributen
	public Bett(int id, String name) {
		this.id = id;
		this.name = name;
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
		return  "ID = " + id +
				", Name = " + name;
	}	
}
