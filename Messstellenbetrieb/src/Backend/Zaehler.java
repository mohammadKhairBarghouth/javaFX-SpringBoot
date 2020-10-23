package Backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Zaehler {
	private Integer adresseid;
	private Integer messlokationid;
	private Integer kundenid;
	private int id;

	public Zaehler(Integer ida) {
		this.adresseid = ida;
	}

	public void setMLID(int id) {
		this.messlokationid = id;
	}

	public void setKundeID(int id) {
		this.kundenid = id;
	}
	
	public static void Update(Zaehler z1, Zaehler z2, Connection con) {
		ResultSet r = setResultSet(z1, con);
		//List <Integer> ids = new ArrayList();
		String ids = "";
		try {
			int c = 0;
			if(r.next() && (Integer)r.getInt("zaehlerid") != null ) {
				ids = r.getInt("zaehlerid") +"";
				c++;
			}
			while(r.next()) {
				if(c != 0 && (Integer)r.getInt("zaehlerid") != null){
					ids = ids + ", "+ r.getInt("zaehlerid");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String updates = Bedingung(z2,con, ", ");
		updates = updates.substring(6);
		String sql =  "update zaehler set "+updates +" where zaehlerid in ("+ids+")";
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

	public static String Bedingung(Zaehler z, Connection con, String and) {
		//Statement s = con.createStatement();

		String adresse = "", mID = "", kundeId = "", where ="";
		if ( z.getAdresseID() != null) {
			adresse = " adresseid = "+ z.getAdresseID() + "";
			where= "where";
		}
		if ( z.getKundeID() != null) {
			if((Integer) z.getAdresseID() != null) {
				kundeId =  and + " kundenid = " + z.getKundeID();
			}else {
				kundeId = " kundenid = " + z.getKundeID() + "";
			}
			where= "where";
		}
		if ( z.getMLID() != null) {
			if((Integer) z.getKundeID() != null || (Integer) z.getAdresseID() != null) {
				mID = and + " messlokationid = "+ z.getMLID();
			}else {
				mID = " messlokationid = "+ z.getMLID() +"";
			}
			where= "where";
		}
		
		String sql = " " + where + adresse + kundeId + mID;
		return sql;
		//ResultSet r = s.executeQuery(sql);
	}
	
	
	public static ResultSet setResultSet(Zaehler z, Connection con) {
		String query = "select * from zaehler " + Zaehler.Bedingung(z, con, " and ");
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
	
	public static void view(ResultSet r, boolean zaelerid , boolean aresseid, boolean kundenid, boolean mlID) {
		String spaces = "                   ";
		if(zaelerid ) {
			System.out.print("zaehlerid"+ spaces.substring("zaehlerid".length())+"|");
		}
		if(aresseid ) {
			System.out.print("adresseid"+ spaces.substring("adresseid".length())+"|");
		}
		if(kundenid ) {
			System.out.print("kundenid"+ spaces.substring("kundenid".length())+"|");
		}
		if(mlID) {
			System.out.print( "messlokationid"+ spaces.substring("messlokationid".length())+"|");
		}
		System.out.println();
		try {
			
			while(r.next()) {
				if(zaelerid && r.getString("zaehlerid") != null) {
					System.out.print(r.getString("zaehlerid") + spaces.substring(r.getString("zaehlerid").length())+"|");
				}else {
					System.out.print(spaces+"|");
				}
				if(aresseid && r.getString("adresseid") != null) {
					System.out.print(r.getString("adresseid") + spaces.substring(r.getString("adresseid").length())+"|");
				}else {
					System.out.print(spaces+"|");
				}
				if(kundenid && r.getString("kundenid") != null) {
					System.out.print(r.getString("kundenid") + spaces.substring(r.getString("kundenid").length())+"|");
				}else {
					System.out.print(spaces+"|");
				}
				if(mlID && r.getString("messlokationid") != null) {
					System.out.print(r.getString("messlokationid") + spaces.substring(r.getString("messlokationid").length())+"|");
				}else {
					System.out.print(spaces+"|");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Drop(Zaehler z, Connection con) {
		String query = "delete from zaehler " + Zaehler.Bedingung(z, con, "and ");
		try {
		Statement s = con.createStatement();
		s.execute(query);
			System.out.println("success ");
		}catch(Exception e) {
			System.out.println("a problem occurred ");
		}
	}
	
	
	

	public void addToDB(Connection con) {

		try {
			Statement s = con.createStatement();
			String comma = "", fq = "", sq = "";

			String sql = "insert into zaehler(zaehlerid, adresseid,";
			int fvalue;
			if (this.messlokationid != null) {
				sql = sql + "messlokationid";
				comma = ",";
				fq = "," + this.messlokationid + "";
			}
			if (this.kundenid != null) {
				sql = sql + comma + "kundenid";
				sq = "," + this.kundenid + "";
			}
			sql = sql + ") values( ? ,  ? " + fq + sq + ")";
			System.out.println(sql);
			ResultSet r = s.executeQuery("select max(zaehlerid) as p from zaehler;");

			if (r.next() == true) {
				id = r.getInt("p") + 1;
			} else {
				id = 0;
			}
			PreparedStatement pt = con.prepareStatement(sql);
			pt.setInt(1, id);
			pt.setInt(2, this.adresseid);

			pt.execute();
			System.out.println("success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getID() {
		return 1;
	}

	public Integer getMLID() {
		return (Integer) this.messlokationid;
	}

	public Integer getAdresseID() {
		return (Integer) this.adresseid;
	}

	public Integer getKundeID() {
		return (Integer) this.kundenid;
	}
}
