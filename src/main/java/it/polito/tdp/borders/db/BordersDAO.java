package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryIdMap;

public class BordersDAO {

	public List<Country> loadAllCountries(CountryIdMap idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				//System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				Country c =new Country(rs.getString("StateNme"), rs.getString("StateAbb"), rs.getInt("ccode"));
				result.add(idMap.get(c));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(CountryIdMap idMap, int anno) {

		String sql = "SELECT state1no, state2no "
				+ "FROM contiguity AS c " 
				+ "WHERE c.conttype=1 AND c.year<=? ";
		List<Border> result = new ArrayList<Border>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				int c1Code = rs.getInt("state1no");
				int c2Code = rs.getInt("state2no");
				
				Country c1 = idMap.get(c1Code);
				Country c2 = idMap.get(c2Code);
				
				if(c1!=null && c2!=null) {
					result.add(new Border(c1,c2));
				}else {
					System.out.println("Errore skipping"+String.valueOf(c1Code)+" - "+String.valueOf(c2Code));
				}
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		//System.out.println("TODO -- BordersDAO -- getCountryPairs(int anno)");
		
	}
}
