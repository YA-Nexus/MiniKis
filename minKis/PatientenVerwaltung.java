package minKis;

import java.sql.*;
import java.util.*;

public class PatientenVerwaltung {
    private Connection conn;
    
    public static void main(String[] args) { //Verbindungsdaten Datenbank
        final String url = "jdbc:oracle:thin:@//localhost:1521/MF19";
        final String user = "mf_manager";
        final String password = "mf_manager";

        try {
            PatientenVerwaltung patientenVerwaltung = new PatientenVerwaltung(url, user, password);
            patientenVerwaltung.startPatientenverwaltung();
        } catch (SQLException e) {
        	System.out.println("Verbindung zur Datenbank konnte nicht hergestellt werden: " + e.getMessage());
            return;
        }
               
    }
    
    ///Konstruktor, der die Verbindung zur Datenbank initialisiert
    public PatientenVerwaltung(String url, String user, String password) throws SQLException {
        this.conn = DriverManager.getConnection(url, user, password);
        new KISController(conn);
        System.out.println("Verbunden!");
        System.out.println("Willkommen zur Patientenverwaltung!");
    }

    public void startPatientenverwaltung() {
        Scanner sc = new Scanner(System.in);
        KISController patientController;
        int test = 1;
        int test2 = 5;
        
        if (test2 < test) {
			System.out.println("");
		} else {
			System.out.println(test2);
		}
        
        try {
        	patientController = new KISController(conn);
            while (true) {
            	System.out.println("");
                System.out.println("Was möchten Sie tun?");
                System.out.println("1. Patienten anzeigen");
                System.out.println("2. Patienten hinzufügen");
                System.out.println("3. Patienten löschen");
                System.out.println("4. Adressen anzeigen");
                System.out.println("5. Adressen löschen");
                System.out.println("6. Relationstabelle");
                System.out.println("7. Krankenhaus Informationen");
                

                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                    	List<Patient> patients = patientController.getAllPatients();
                    	List<Adresse> adressen = patientController.getAllAdress();
                    	for (Patient patient : patients) {
                    		List<Adresse> patientAdressen = patientController.getAdressFromPatients(patient.getID(), adressen);
                    		patient.addAdressen(patientAdressen);
                    		System.out.println(patient);                   		
                		}
                        break;
                    case 2:
                    	patientController.addPatient(sc);                    	
                        break;
                    case 3:
                    	patientController.deletePatient(sc, conn);                       
                        break;
                    case 4:
                    	List<Adresse> allAdressen = patientController.getAllAdress();
                        for (Adresse adresse : allAdressen) {
                        	System.out.println(adresse);
            			}
                    	break;
                    case 5:
                    	patientController.deleteAddress(sc, conn);
                    	break;
                    case 6:
                    	patientController.listPatientAdress(conn);
                    	break;
                    case 7:
                        Krankenhaus institutInfos = new Krankenhaus();
                        System.out.println(institutInfos.toString());
                        break;
                    default:
                        System.out.println("Ungültige Eingabe. Bitte erneut versuchen.");
                        break;
                }
            }
        } catch (SQLException e) {
        	System.out.println("Ein Datenbankfehler ist aufgetreten: " + e.getMessage());
		} finally {
            sc.close();           
        }
    }
}




