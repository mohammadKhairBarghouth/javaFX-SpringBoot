package Backend;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Kunde {
	private String vorname;
	private String nachname;
	private Integer adresseid;
	private Integer mlID;
	private ArrayList<Zaehler> zaehlern = new ArrayList<Zaehler>();
	
	public Kunde(String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
	}
	
	public Kunde(String vorname, String nachname, Integer adresseid, Integer mid ) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.adresseid = adresseid;
		this.mlID = mid;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Integer getAdresseid() {
		return adresseid;
	}

	public void setAdresseid(Integer adresseid) {
		this.adresseid = adresseid;
	}

	public Integer getMlID() {
		return mlID;
	}

	public void setMlID(Integer mlID) {
		this.mlID = mlID;
	}

	public ArrayList<Zaehler> getZaehlern() {
		return zaehlern;
	}

	public void setZaehlern(ArrayList<Zaehler> zaehlern) {
		this.zaehlern = zaehlern;
	}
	
	public void addToDB(Kunde k, Connection con) {
		String sql = "insert into kunde(vorname, nachname , adresseid) values ('mohammad','Khair',2);";
		
	}
	
	public static String Bedingung(Kunde k, Connection con, String and) {
		String vorn = "", nachm = "", adresseid = "", mldi = "", where ="";
		if ( k.getVorname() != null) {
			vorn = " vorname = '"+ k.getVorname()+ "'";
			where= "where";
		}
		if ( k.getNachname() != null) {
			if(k.getVorname() != null) {
				nachm=  and + " nachname = '" + k.getNachname()+ "'";
			}else {
				nachm = " nachname = '" +  k.getNachname() + "'";
			}
			where= "where";
		}
		if ( k.getAdresseid() != null) {
			if(k.getNachname() != null || k.getVorname() != null) {
				adresseid = and + " adresseid = "+ k.getAdresseid();
			}else {
				adresseid = " adresseid = "+ k.getAdresseid() +"";
			}
			where= "where";
		}
		if ( k.getMlID() != null) {
			if(k.getNachname() != null || k.getVorname() != null || k.getAdresseid() != null) {
				mldi = and + " messlokationid = "+ k.getMlID() ;
			}else {
				mldi = " messlokationid = "+ k.getMlID()  +"";
			}
			where= "where";
		}
		
		String sql = " " + where + vorn + nachm + adresseid +mldi;
		return sql;
	}
	
	public static ResultSet setResultSet(Kunde k,Connection con) {
		String query = "select * from kunde " + Kunde.Bedingung(k, con, " and ");
		
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
	
	public static void Drop(Kunde k, Connection con) {
		String query = "delete from kunde " + Kunde.Bedingung(k, con, " and ");
		System.out.println(query);
		try {
		Statement s = con.createStatement();
		s.execute(query);
			System.out.println("success ");
		}catch(Exception e) {
			System.out.println("a problem occurred... ");
			e.printStackTrace();
		}
	}
	
	public static int getId(Kunde k, Connection con) {
		ResultSet r = setResultSet(k, con);
		try {
			if(r.next() && (Integer)r.getInt("kundeid") != null ) {
				return  r.getInt("kundeid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void Update(Kunde a1, Kunde a2, Connection con) {
		ResultSet r = setResultSet(a1, con);
		//List <Integer> ids = new ArrayList();
		String ids = "";
		try {
			int c = 0;
			if(r.next() && (Integer)r.getInt("kundeid") != null ) {
				ids = r.getInt("kundeid") +"";
				c++;
			}
			while(r.next()) {
				if(c != 0 && (Integer)r.getInt("kundeid") != null){
					ids = ids + ", "+ r.getInt("kundeid");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String updates = Bedingung(a2,con, ", ");
		updates = updates.substring(6);
		String sql =  "update kunde set "+updates +" where kundeid in ("+ids+")";
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
		if ( this.getVorname() != null) {
			value = value+" vorname ";
			value1 = value1+ "'"+ this.getVorname()+ "'";
		}
		if ( this.getNachname() != null) {
			if(this.getVorname() != null) {
				value  = value + and + " nachname " ;
				value1 = value1+  and +"'"+this.getNachname()+ "'";
			}else {
				value = " nachname ";
				value1 = value1+"'" + this.getNachname() + "'";
			}
		}
		if ( this.getAdresseid()!= null) {
			if(this.getVorname() != null || this.getVorname() != null) {
				value  = value + and + "adresseid " ;
				value1 = value1+ and + this.getAdresseid();
			}else {
				value = " adresseid ";
				value1 = value1+this.getAdresseid();
			}
		}
		
		if ( this.getMlID() != null) {
			if(this.getVorname() != null || this.getVorname() != null || this.getAdresseid() != null) {
				value  = value + and + "  messlokationid" ;
				value1 = value1+ and +this.getMlID();
			}else {
				value = "  messlokationid ";
				value1 = value1+ this.getMlID();
			}
		}
		value = value+")";
		value1 = value1+")";
		
		String sql = "insert into kunde"+value+" values"+value1;
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
