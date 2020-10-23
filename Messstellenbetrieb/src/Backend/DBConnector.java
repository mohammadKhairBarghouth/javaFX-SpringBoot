package Backend;

import java.sql.*;




public class DBConnector {
	
	public static void main( String [] args) {
		String h = "d ";
		
			System.out.println(h.matches("\s*"));
		
		
//		try {
//			Connection con;
//			con =  DriverManager.getConnection("jdbc:postgresql://localhost:5432/messstellenbetrieb", "postgres", "123");
//			//----------------------------------------------------------------------------------------
////			Kunde k = new Kunde("Fabian","Bauermann");
////			k.setMlID(4);
////			k.setAdresseid(2);
////			
////			Kunde k1 = new Kunde("hhaa","haaaaa");
////			
////			Kunde.Update(k, k1, con);
//			Kunde.setResultSet(new Kunde(null,null), con);
//			
//			//----------------------------------------------------------------------------------------
//			con.close();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	Connection con;
	
	public void connect() {
		
		
		
	}
	
}
