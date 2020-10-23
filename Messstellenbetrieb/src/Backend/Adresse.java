package Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Adresse {
	private String strasse;
	private Integer plz;
	private Integer hausNr;
	private String zusatzInfo;
	private Integer id;

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public Integer getPlz() {
		return (Integer)plz;
	}

	public void setPlz(int plz) {
		this.plz = plz;
	}

	public Integer getHausNr() {
		return (Integer)hausNr;
	}

	public void setHausNr(int hausNr) {
		this.hausNr = hausNr;
	}

	public String getZusatzInfo() {
		return zusatzInfo;
	}

	public void setZusatzInfo(String zusatzInfo) {
		this.zusatzInfo = zusatzInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Adresse(Integer plz, Integer hausNr, String strasse, String zusatzInfo) {
		this.plz = plz;
		this.strasse = strasse;
		this.hausNr = hausNr;
		this.zusatzInfo = zusatzInfo;
	}

	public boolean istImDBVorhanden(Connection con) {
		String sql = "select  * from adresse where strasse = ? and hausnr = ? ";
		try {
			PreparedStatement pt = con.prepareStatement(sql);

			pt.setString(1, strasse);
			pt.setInt(2, hausNr);

			ResultSet r = pt.executeQuery();
			return r.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String Bedingung(Adresse a, Connection con, String and) {
		//Statement s = con.createStatement();

		String strasse = "", plz = "", hausNr = "", zusatzInfo ="" , where ="";
		if ( a.getStrasse() != null) {
			strasse = " strasse = '"+ a.getStrasse() + "'";
			where= "where";
		}
		if ( a.getPlz() != null) {
			if( a.getStrasse() != null) {
				plz =  and + " plz = '" + a.getPlz()  +"'";
			}else {
				plz = " plz = '" + a.getPlz() + "'";
			}
			where= "where";
		}
		if ( a.getHausNr() != null) {
			if(a.getPlz()  != null || a.getStrasse() != null) {
				hausNr = and + " hausnr = "+  a.getHausNr() ;
			}else {
				hausNr = " hausnr = "+  a.getHausNr()  +"";
			}
			where= "where";
		}
		
		if ( a.getZusatzInfo()!= null) {
			if( a.getPlz() != null || a.getStrasse() != null || a.getHausNr() != null ) {
				zusatzInfo = and + " zusatzInfo = '"+  a.getZusatzInfo() +"'";
			}else {
				zusatzInfo = " zusatzInfo = '"+  a.getZusatzInfo()  +"'";
			}
			where= "where";
		}
		
		String sql = " " + where +strasse+  plz + hausNr +  zusatzInfo ;
		//System.out.println(sql);
		return sql;
		//ResultSet r = s.executeQuery(sql);
	}
	
	public static void Drop(Adresse a, Connection con) {
		String query = "delete from adresse " + Adresse.Bedingung(a, con, "and ");
		try {
		Statement s = con.createStatement();
		s.execute(query);
			System.out.println("success ");
		}catch(Exception e) {
			System.out.println("a problem occurred ");
		}
	}
	
	public static ResultSet setResultSet(Adresse a,Connection con) {
		String query = "select * from adresse " + Adresse.Bedingung(a, con, " and ");
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
	
	public static void Update(Adresse a1, Adresse a2, Connection con) {
		ResultSet r = setResultSet(a1, con);
		//List <Integer> ids = new ArrayList();
		String ids = "";
		try {
			int c = 0;
			if(r.next() && (Integer)r.getInt("adresseid") != null ) {
				ids = r.getInt("adresseid") +"";
				c++;
			}
			while(r.next()) {
				if(c != 0 && (Integer)r.getInt("adresseid") != null){
					ids = ids + ", "+ r.getInt("adresseid");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String updates = Bedingung(a2,con, ", ");
		updates = updates.substring(6);
		String sql =  "update adresse set "+updates +" where adresseid in ("+ids+")";
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

	public void addtoDB(Connection con) {
		try {
			String sql = "insert into adresse values( ? , ? , ? , ? , ?)";
			Statement s = con.createStatement();
			ResultSet r = s.executeQuery("select max(adresseid) as p from adresse;");
			r.next();
			id = r.getInt("p") + 1;
			PreparedStatement pt = con.prepareStatement(sql);
			pt.setInt(1, id);
			pt.setInt(2, plz);
			if( strasse != null) {
				pt.setString(3, strasse);
			}else {
				pt.setString(3, "");
			}
			
			pt.setInt(4, hausNr);
			if(zusatzInfo!= null) {
				pt.setString(5, zusatzInfo);
			}else {
				pt.setString(5, "");
			}
			pt.execute();
			System.out.println("success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
