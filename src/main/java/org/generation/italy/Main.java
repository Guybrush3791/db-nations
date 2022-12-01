package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

	private static final String URL = "jdbc:mysql://localhost:3306/nations";
	private static final String USER = "guybrush";
	private static final String PWS = "code";
	
	public static void main(String[] args) {
		
		es3_es4();
	}
	
	private static void es3_es4() {
		
		try (Connection con = DriverManager.getConnection(URL, USER, PWS); Scanner sc = new Scanner(System.in)) {
			
			System.out.print("Nazione: ");
			String keyWord = '%' + sc.nextLine() + '%';
			
			final String sqlRegCon = " SELECT countries.country_id AS 'id', countries.name AS 'country', regions.name AS 'region', continents.name AS 'continent' "
							 + " FROM countries "
							 + "	JOIN regions "
							 + "		ON countries.region_id = regions.region_id "
							 + "	JOIN continents "
							 + "		ON regions.continent_id = continents.continent_id "
							 + " WHERE countries.name LIKE ? "
							 + " ORDER BY countries.name ";
			
			try (PreparedStatement ps = con.prepareStatement(sqlRegCon)) {
			
				ps.setString(1, keyWord);
				
				try (ResultSet rs = ps.executeQuery()) {
				
					while(rs.next()) {
						
						final int id = rs.getInt(1);
						final String country = rs.getString(2);
						final String region = rs.getString(3);
						final String continent = rs.getString(4);
						
						System.out.println("(" + id + ") " + country 
									+ " - " + region + " - " + continent);
					}
				}	
			}
			
			System.out.println("");
			System.out.print("Id nazione: ");
			String idStr = sc.nextLine();
			
			int id = Integer.parseInt(idStr);
			
			final String sqlLang = " SELECT DISTINCT language "
								 + " FROM countries "
								 + "	JOIN country_languages "
								 + "		ON countries.country_id = country_languages.country_id "
								 + "	JOIN languages "
								 + "		ON country_languages.language_id = languages.language_id "
								 + " WHERE countries.country_id = ? ";
			
			try (PreparedStatement ps = con.prepareStatement(sqlLang)) {
				
				ps.setInt(1, id);
				
				try (ResultSet rs = ps.executeQuery()) {
				
					System.out.print("Languages: ");
					while(rs.next()) {
						
						final String lang = rs.getString(1);
						
						System.out.print(lang + (!rs.isLast() ? ", " : ""));
					}
				}	
			}
			
			System.out.println("");
			
			final String sqlStat = " SELECT country_stats.* "
								 + " FROM countries "
								 + "	JOIN country_stats "
								 + "		ON countries.country_id = country_stats.country_id "
								 + " WHERE countries.country_id = ? "
								 + " ORDER BY year DESC "
								 + " LIMIT 1 ";
			
			try (PreparedStatement ps = con.prepareStatement(sqlStat)) {
				
				ps.setInt(1, id);
				
				try (ResultSet rs = ps.executeQuery()) {
				
					System.out.println("Most recent stats: ");
					if(rs.next()) {
						
						final String year = rs.getString(2);
						final String pop = rs.getString(3);
						final String gdp = rs.getString(4);
						
						System.out.println("Year: " + year);
						System.out.println("Population: " + pop);
						System.out.println("GDP: " + gdp);
					}
				}	
			}
			
		} catch (Exception e) {
			
			System.err.println("ERROR: " + e.getMessage());
		}
	}
}
