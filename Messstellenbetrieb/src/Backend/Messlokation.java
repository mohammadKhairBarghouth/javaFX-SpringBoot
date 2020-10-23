package Backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Messlokation {
	private Integer adresseid;
	private Integer kundeid;

	public Integer getAdresseid() {
		return adresseid;
	}

	public void setAdresseid(Integer adresseid) {
		this.adresseid = adresseid;
	}

	public Integer getKundeid() {
		return kundeid;
	}

	public void setKundeid(Integer kundeid) {
		this.kundeid = kundeid;
	}

	public static String Bedingung( Messlokation m, Connection con, String and) {
		String vorn = "", nachm = "", adresseid = "", mldi = "", where ="";
		
		if ( m.getAdresseid() != null) {
			
				adresseid = " adresseid = "+ m.getAdresseid() +"";
			}
			where= "where";
		
		if ( m.getKundeid() != null) {
			if(m.getAdresseid() != null) {
				mldi = and + " kundeid = "+ m.getKundeid() ;
			}else {
				mldi = " kundeid = "+ m.getKundeid()  +"";
			}
			where= "where";
		}
		
		String sql = " " + where + vorn + nachm + adresseid +mldi;
		System.out.println(sql);
		return sql;
	}
	
	public static ResultSet setResultSet(Messlokation m,Connection con) {
		String query = "select * from Messlokation " + Messlokation.Bedingung(m, con, " and ");
		System.out.println(query);
		ResultSet r = null;
		
		try {
		Statement s = con.createStatement();
		r = s.executeQuery(query);
		}catch(Exception e) {
			System.out.println("a problem occurred ");
		}
		
		return r;
	}
	
	public static void Drop(Messlokation k, Connection con) {
		String query = "delete from Messlokation " + Messlokation.Bedingung(k, con, "and ");
		try {
		Statement s = con.createStatement();
		s.execute(query);
			System.out.println("success ");
		}catch(Exception e) {
			System.out.println("a problem occurred ");
		}
	}
	
	public static void Update(Messlokation a1, Messlokation a2, Connection con) {
		ResultSet r = setResultSet(a1, con);
		//List <Integer> ids = new ArrayList();
		String ids = "";
		try {
			int c = 0;
			if(r.next() && (Integer)r.getInt("Messlokationid") != null ) {
				ids = r.getInt("Messlokationid") +"";
				c++;
			}
			while(r.next()) {
				if(c != 0 && (Integer)r.getInt("Messlokationid") != null){
					ids = ids + ", "+ r.getInt("Messlokationid");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String updates = Bedingung(a2,con, ", ");
		updates = updates.substring(6);
		String sql =  "update Messlokation set "+updates +" where Messlokationid in ("+ids+")";
		System.out.println(sql);
		Statement s;
		try {
			s = con.createStatement();
			if(!ids.equals(""))
				s.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addToDB(Connection con) {
		String and = " , ";
		String value ="(";
		String value1 ="(";
		
		if ( this.getAdresseid()!= null) {	
				value = value +" adresseid ";
				value1 = value1+this.getAdresseid();
			
		}
		
		if ( this.getKundeid() != null) {
			if(this.getAdresseid() != null) {
				value  = value + and + "  kundeid" ;
				value1 = value1+ and +this.getKundeid();
			}else {
				value = "  kundeid ";
				value1 = value1+ this.getKundeid();
			}
		}
		value = value+")";
		value1 = value1+")";
		
		String sql = "insert into messlokation"+value+" values"+value1;
		try {
			Statement s = con.createStatement();
			s.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sql);
	}	
}


