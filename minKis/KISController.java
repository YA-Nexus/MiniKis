package minKis;

import java.sql.*;
import java.util.*;

public class KISController {
	Scanner sc = new Scanner(System.in);
    private Connection conn;

    public KISController(Connection conn) {
        this.conn = conn;
    }
    
    //Auflisten
    //Alle Patienten auflisten
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM YA_Patient ORDER BY ID"; 

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setID(rs.getInt("ID"));
                patient.setName(rs.getString("Name"));
                patient.setVorname(rs.getString("Vorname"));
                patient.setGeburtsdatum(rs.getDate("Geburtsdatum"));
                patient.setInsID(rs.getInt("Kis_ID"));

                patients.add(patient);
            }
        } 
        
        return patients;      
    }
    
    public List<Adresse> getAdressFromPatients(int patientID, List<Adresse> adressList) throws SQLException {
    	List<Adresse> result = new ArrayList<Adresse>();
        String sql = "SELECT Adresse_ID FROM YA_PatientAdress WHERE Patient_ID = ? ORDER BY ID"; 
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, patientID);
        List<Integer> adressIDs = new ArrayList<Integer>();
        
        ResultSet rs = stmt.executeQuery(); 
        while (rs.next()) {
        	adressIDs.add(rs.getInt("Adresse_ID"));
        }        
        for (int adresseID : adressIDs) {
			for (Adresse adresse : adressList) {
				if (adresse.getId() == adresseID) {
					result.add(adresse);
				}
			}
		}
        
    	return result;
    }

    //Alle Adressen auflisten
    public List<Adresse> getAllAdress() throws SQLException {
        List<Adresse> adressen = new ArrayList<>();
        String sql = "SELECT * FROM YA_Adresse ORDER BY ID"; 
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
        	
            while (rs.next()) {
                Adresse adresse = new Adresse();
                adresse.setId(rs.getInt("ID"));
                adresse.setPlz(rs.getString("PLZ"));
                adresse.setStadt(rs.getString("Stadt"));
                adresse.setLand(rs.getString("Land"));
                adresse.setStrassenname(rs.getString("Strassenname"));
                adresse.setStrassennummer(rs.getString("Strassennummer"));

                adressen.add(adresse);
            }
        }
        
		return adressen;
    }
    
    //Hinzufügen 
    public Patient addPatient(Scanner sc) throws SQLException {
        System.out.println("Bitte geben Sie die Daten für den neuen Patienten ein:");
        
        //ErrorHandling
        String[] nameHolder = new String[1];
        if (!errorName(sc, nameHolder)) {
            return null;
        }
        String Name = nameHolder[0];
        
        String[] vornameHolder = new String[1];
        if (!errorVorname(sc, vornameHolder)) {
            return null;
        }
        String Vorname = vornameHolder[0];

        java.sql.Date[] dateHolder = new java.sql.Date[1];
        if (!errorGeburtsdatum(sc, dateHolder)) {
            return null;
        }
        java.sql.Date Geburtsdatum = dateHolder[0];

        Patient newPatient = new Patient();
        newPatient.setName(Name);
        newPatient.setVorname(Vorname);
        newPatient.setGeburtsdatum(Geburtsdatum);
        
        // 1. Auswahl eines Krankenhauses
        System.out.println("Verfügbare Krankenhäuser:");
        listKrankenhaeuser(); 
        System.out.println("Bitte wählen Sie ein Krankenhaus (ID): ");
        int ins_ID = sc.nextInt(); 

        // 2. Standort abfragen
        System.out.println("Verfügbare Standorte für Krankenhaus " + ins_ID + ":");
        listStandorte(ins_ID); 
        System.out.println("Bitte wählen Sie einen Standort (ID): ");
        int standort_ID = sc.nextInt(); 

        // 3. Station abfragen
        System.out.println("Verfügbare Stationen für Standort " + standort_ID + ":");
        listStationen(standort_ID); 
        System.out.println("Bitte wählen Sie eine Station (ID): ");
        int station_ID = sc.nextInt(); 

        // 4. Abteilung abfragen
        System.out.println("Verfügbare Abteilungen für Station " + station_ID + ":");
        listAbteilungen(station_ID); 
        System.out.println("Bitte wählen Sie eine Abteilung (ID): ");
        int abteilung_ID = sc.nextInt(); 

        // 5. Zimmer abfragen
        System.out.println("Verfügbare Zimmer für Abteilung " + abteilung_ID + ":");
        listZimmer(abteilung_ID); 
        System.out.println("Bitte wählen Sie ein Zimmer (ID): ");
        int zimmer_ID = sc.nextInt(); 

        // 6. Bett abfragen
        System.out.println("Verfügbare Betten für Zimmer " + zimmer_ID + ":");
        listBetten(zimmer_ID); 
        System.out.println("Bitte wählen Sie ein Bett (ID): ");
        sc.nextInt(); 

        // 7. Auswahl einer Adresse bzw. Erstellung einer neuen Adresse
        System.out.println("Möchten Sie eine vorhandene Adresse auswählen oder eine neue Adresse erstellen?");
        System.out.println("1: Vorhandene Adresse auswählen");
        System.out.println("2: Neue Adresse erstellen");
        int choice = sc.nextInt();
        int generatedPatientID;
                       
        if (choice == 1) {
            // Auswahl einer vorhandenen Adresse
            System.out.println("Verfügbare Adressen:");
        	List<Adresse> allAdressen = getAllAdress();
            for (Adresse adresse : allAdressen) {
            	System.out.println(adresse);
			}           
            System.out.print("Bitte wählen Sie eine Adresse (ID): ");
            int adresseID = sc.nextInt();
            generatedPatientID = insertNewPatient(conn, newPatient, ins_ID); 
            connPatientAdresse(conn, generatedPatientID, adresseID);
            
        } else if (choice == 2) {
            // Erstellung einer neuen Adresse 
        	System.out.print("PLZ: "); 
            String Plz = sc.next();
            System.out.print("Stadt: ");
            String Stadt = sc.next();
            System.out.print("Land: ");
            String Land = sc.next();
            System.out.print("Strassenname: ");
            String Strassenname = sc.next();
            System.out.print("Strassennummer: ");
            String Strassennummer = sc.next();
           
            Adresse adresse = new Adresse();
            adresse.setPlz(Plz);
            adresse.setStadt(Stadt);
            adresse.setLand(Land);
            adresse.setStrassenname(Strassenname);
            adresse.setStrassennummer(Strassennummer);
            
            int generatedAdressID = insertNewAdresse(conn, adresse);
            generatedPatientID = insertNewPatient(conn, newPatient, ins_ID);
            connPatientAdresse(conn, generatedPatientID, generatedAdressID);
            
        } else {
            System.out.println("Ungültige Auswahl.");
            System.out.println("");           
        }       
        return newPatient; 
    }
  
    //------------------------------------------------------------------------ Einfügen 
    
    //Einfügen des neuen Patienten
    public int insertNewPatient(Connection conn, Patient patient, int ins_ID) throws SQLException {
        String sql = "INSERT INTO YA_Patient (ID, Name, Vorname, Geburtsdatum, Kis_ID) VALUES (seq_patient.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getVorname());
            stmt.setDate(3, patient.getGeburtsdatum());
            stmt.setInt(4, ins_ID);

            int rowsAffected = stmt.executeUpdate();
            int generatedPatientID = 0;
            
            if (rowsAffected > 0) {
                System.out.println("Patient erfolgreich eingefügt!");               
             // Abrufen des letzten generierten Werts aus der Sequence
                try (Statement sequenceStmt = conn.createStatement()) {
                    ResultSet resultSet = sequenceStmt.executeQuery("SELECT seq_patient.CURRVAL FROM dual");
                    if (resultSet.next()) {
                        generatedPatientID = resultSet.getInt(1);
                    }
                }
            } else {
                System.out.println("Fehler beim Einfügen des Patienten.");
            }
            return generatedPatientID;
        }        
    }
    
    //Einfügen der neuen Adresse
    public int insertNewAdresse(Connection conn, Adresse adresse) throws SQLException {
        String sql = "INSERT INTO YA_Adresse (ID, PLZ, Stadt, Land, Strassenname, Strassennummer) VALUES (seq_adresse.NEXTVAL, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, adresse.getPlz());
            stmt.setString(2, adresse.getStadt());
            stmt.setString(3, adresse.getLand());
            stmt.setString(4, adresse.getStrassenname());
            stmt.setString(5, adresse.getStrassennummer());

            int rowsAffected = stmt.executeUpdate();
            int generatedAdressID = 0;
            
            if (rowsAffected > 0) {
                System.out.println("Adresse erfolgreich eingefügt!");
             // Abrufen des letzten generierten Werts aus der Sequence
                try (Statement sequenceStmt = conn.createStatement()) {
                    ResultSet resultSet = sequenceStmt.executeQuery("SELECT seq_adresse.CURRVAL FROM dual");
                    if (resultSet.next()) {
                        generatedAdressID = resultSet.getInt(1);
                    }
                }
            } else {
                System.out.println("Fehler beim Einfügen der Adresse.");
            }
            return generatedAdressID;
        }
    }

    
    //Relation zwischen Patient & Adresse in YA_PatientAdress, so können z.B Patienten mehrere Adressen haben.
    public void connPatientAdresse(Connection conn, int generatedPatientID, int generatedAdressID) throws SQLException { 
        String sql = "INSERT INTO YA_PatientAdress (Patient_ID, Adresse_ID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) { 
            stmt.setInt(1, generatedPatientID); 
            stmt.setInt(2, generatedAdressID); 
            stmt.executeUpdate();
        }
    }

    //------------------------------------------------------------------------ Löschen 
    
    //-----------Patienten löschen
    public void deletePatient(Scanner sc, Connection conn) throws SQLException {
    	List<Patient> allPatienten = getAllPatients();
    	for (Patient patient : allPatienten) {
			System.out.println(patient);
		}
        System.out.print("Bitte geben Sie die ID des Patienten an, den Sie löschen möchten: ");
        int patientIDToDelete = sc.nextInt(); 
        deleteRelationPatient(conn, patientIDToDelete);
        deletePatientById(conn, patientIDToDelete);
        System.out.println("");
        System.out.println("Patient erfolgreich gelöscht!");
        System.out.println("");
    }
    
    //Relation zu YA_PatientAdress
    public void deleteRelationPatient(Connection conn, int patientID) throws SQLException {
    	String deleteRelationSql = "DELETE FROM YA_PatientAdress WHERE Patient_ID = ?";
    	try (PreparedStatement stmt = conn.prepareStatement(deleteRelationSql)) {
            stmt.setInt(1, patientID);
            stmt.executeUpdate();
        }
    }
    
    public void deletePatientById(Connection conn, int patientID) throws SQLException { 
        String deletePatientSql = "DELETE FROM YA_Patient WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deletePatientSql)) {
            stmt.setInt(1, patientID);
            stmt.executeUpdate();
        }
    }

    //-----------Adresse Löschen
    public void deleteAddress(Scanner sc, Connection conn) throws SQLException {
    	List<Adresse> allAdressen = getAllAdress();
        for (Adresse adresse : allAdressen) {
        	System.out.println(adresse);
		}
        System.out.print("Bitte geben Sie die ID der Adresse an, die Sie löschen möchten: ");
        int adressIDToDelete = sc.nextInt(); 
        deleteRelationAdress(conn, adressIDToDelete);
        deleteAdressByID(conn, adressIDToDelete);
        System.out.println("");
        System.out.println("Adresse erfolgreich gelöscht!");
        System.out.println(""); 
    }
    
    //Relation zu YA_PatientAdress
    public void deleteRelationAdress(Connection conn, int adresseID) throws SQLException {
    	String deleteRelationSql = "DELETE FROM YA_PatientAdress WHERE Adresse_ID = ?";
    	try (PreparedStatement stmt = conn.prepareStatement(deleteRelationSql)) {
            stmt.setInt(1, adresseID);
            stmt.executeUpdate();
        }
    }
    
    public void deleteAdressByID(Connection conn, int adressID) throws SQLException {
        String deletePatientSql = "DELETE FROM YA_Adresse WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deletePatientSql)) {
            stmt.setInt(1, adressID);
            stmt.executeUpdate();
        }
    }
    
    //------------------------------------------------------------------------ Error-Handling
    
    //Name
    public boolean errorName(Scanner sc, String[] nameHolder) { 
    	//Testfall 1: Eingabe eines gültigen (Vor-)Namens (z.B. "Max" oder "Maximilian").
    	//Testfall 2: Eingabe eines ungültigen (Vor-)Namens (z.B. mit Zahlen "Max123" oder mit Sonderzeichen "Max!"). 
    	System.out.print("Name: ");
        nameHolder[0] = sc.next();

        if (nameHolder[0].length() <= 2 || nameHolder[0].length() > 20) {
            System.out.println("Fehler: Namen dürfen nicht kürzer als 2 oder länger als 20 Buchstaben sein.");
            return false;
        } else if (!nameHolder[0].matches("[a-zA-Z]+")) {
            System.out.println("Fehler: Der Name darf nur Buchstaben enthalten.");
            return false;
        }
        return true; // Name ist gültig
    }

    //Vorname
    public boolean errorVorname(Scanner sc, String[] vornameHolder) {
        System.out.print("Vorname: ");
        vornameHolder[0] = sc.next();

        if (vornameHolder[0].length() <= 2 || vornameHolder[0].length() > 20) {
            System.out.println("Fehler: Vornamen dürfen nicht kürzer als 2 oder länger als 20 Buchstaben sein.");
            return false;
        } else if (!vornameHolder[0].matches("[a-zA-Z]+")) {
            System.out.println("Fehler: Der Vorname darf nur Buchstaben enthalten.");
            return false;
        }
        return true; // Name ist gültig
    }
    
    //Geburtsdatum
    public boolean errorGeburtsdatum(Scanner sc, java.sql.Date[] dateHolder) {
    	//Testfall 1: Eingabe eines gültigen Datums im Format "YYYY-MM-DD" (z.B. "1990-05-20").
        //Testfall 2: Eingabe eines ungültigen Datums (z.B. "1990-20-05" oder "1990-05-").
    	System.out.print("Geburtsdatum (Format: YYYY-MM-DD): ");
        String geburtsdatumStr = sc.next();
        try {
            dateHolder[0] = java.sql.Date.valueOf(geburtsdatumStr);
            return true; // Datum ist gültig
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler: Ungültiges Datumsformat. Bitte verwenden Sie das Format YYYY-MM-DD.");
            return false;
        }
    }
    
    //------------------------------------------------------------------------ Auflisten
    
    //Krankenhaus
	public void listKrankenhaeuser() throws SQLException {
		String sql = "SELECT ID, Name FROM YA_Krankenhaus ORDER BY ID";
		try (PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery()) {
         	while (rs.next()) {
         		System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
         	}
     	}
 	}
	
    public void listStandorte(int Kis_ID) throws SQLException { //Liste der verfügbaren Standorte
        String sql = "SELECT ID, Name FROM YA_Standort WHERE Kis_ID = ? ORDER BY ID";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, Kis_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
                }
            }
        }
    }
	
    public void listStationen(int standort_ID) throws SQLException { //Liste der verfügbaren Stationen
        String sql = "SELECT ID, Name FROM YA_Station WHERE Standort_ID = ? ORDER BY ID";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, standort_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
                }
            }
        } 
    }
    
    public void listAbteilungen(int station_ID) throws SQLException { //Liste der verfügbaren Abteilungen
        String sql = "SELECT ID, Name FROM YA_Abteilung WHERE Station_ID = ? ORDER BY ID";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, station_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
                }
            }
        }
    }
    
    public void listZimmer(int abteilung_ID) throws SQLException { //Liste der verfügbaren Zimmer
        String sql = "SELECT ID, Name FROM YA_Zimmer WHERE Abteilung_ID = ? ORDER BY ID";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, abteilung_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
                }
            }
        }
    }
    
    public void listBetten(int zimmer_ID) throws SQLException { //Liste der verfügbaren Betten
        String sql = "SELECT ID, Name FROM YA_Bett WHERE Zimmer_ID = ? ORDER BY ID";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, zimmer_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
                }
            }
        }
    }
    
    //------------------------------------------------------------------------ Relationtabelle YA_PatientAdress
    
    
    public void listPatientAdress(Connection conn) throws SQLException { //Liste der Relations
        String sql = "SELECT * FROM YA_PatientAdress ORDER BY ID";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {        	
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID") + ", Patient_ID: " + rs.getString("Patient_ID") 
                    					+ ", Adresse_ID: " + rs.getString("Adresse_ID"));
                }
            } 
        }
    }    
}

