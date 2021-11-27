package task;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PartDAO {
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public PartDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List partsList() {
		List list = new ArrayList();
		try {
			con = dataFactory.getConnection();
			String query = "select * from parts ";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String goods = rs.getString("goods");
				String partType = rs.getString("partType");
				int price = rs.getInt("price");
				String manufacturer = rs.getString("manufacturer");
				String country = rs.getString("country");
				System.out.println(goods);
				PartVO vo = new PartVO();
				vo.setGoods(goods);
				vo.setPartType(partType);
				vo.setPrice(price);
				vo.setManufacturer(manufacturer);
				vo.setCountry(country);
				list.add(vo);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}System.out.println(list.size());
		return list;
	}
	
	public PartVO getPartsVO(String goodsName) {
		PartVO vo = new PartVO();
		try {
			con = dataFactory.getConnection();
			String query = "select * from parts where goods=? ";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1,goodsName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String goods = rs.getString("goods");
				String partType = rs.getString("partType");
				int price = rs.getInt("price");
				String manufacturer = rs.getString("manufacturer");
				String country = rs.getString("country");
				System.out.println(goods);
				vo.setGoods(goods);
				vo.setPartType(partType);
				vo.setPrice(price);
				vo.setManufacturer(manufacturer);
				vo.setCountry(country);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	public int getPartPrice(String goods) {
		int price=0;
		try {
			con = dataFactory.getConnection();
			String query = "select price as price from parts ";
			query += " where goods=?";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, goods);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			price = rs.getInt("price");
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}
	
	public void addPart(PartVO partVO) {
		try {
			Connection con = dataFactory.getConnection();
			String goods = partVO.getGoods();
			String partType = partVO.getPartType();
			int price = partVO.getPrice();
			String manufacturer = partVO.getManufacturer();
			String country = partVO.getCountry();
			String query = "insert into part";
			query += " (goods,partType,price,manufacturer,country)";
			query += " values(?,?,?,?,?)";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, goods);
			pstmt.setString(2, partType);
			pstmt.setInt(3, price);
			pstmt.setString(4, manufacturer);
			pstmt.setString(5, country);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delPart(String goods) {
		try {
			Connection con = dataFactory.getConnection();
			Statement stmt = con.createStatement();
			String query = "delete from parts" + " where goods=?";
			System.out.println("prepareStatememt:" + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, goods);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public boolean isExisted(PartVO partVO) { //여기서는 쓸지 안쓸지 모름 boolean result =
	 * false; String goods = partVO.getGoods(); try { con =
	 * dataFactory.getConnection(); String query =
	 * "select decode(count(*),1,'true','false') as result from parts"; query +=
	 * " where goods=?"; System.out.println(query); pstmt =
	 * con.prepareStatement(query); pstmt.setString(1, goods); ResultSet rs =
	 * pstmt.executeQuery(); rs.next(); //커서를 첫번째 레코드로 위치시킵니다. result =
	 * Boolean.parseBoolean(rs.getString("result")); System.out.println("result=" +
	 * result); } catch (Exception e) { e.printStackTrace(); } return result; }
	 */

}
