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

public class BasketDAO {
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public BasketDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List userBasketList(String user) {
		List list = new ArrayList();
		try {
			con = dataFactory.getConnection();
			String query = "select * from basket where user_id=?";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, user);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String user_id = rs.getString("user_id");
				String goods = rs.getString("goods");
				int ea = rs.getInt("ea");
				
				BasketVO vo = new BasketVO();
				vo.setUser_id(user_id);
				vo.setGoods(goods);
				vo.setEa(ea);;
				vo.setPrice(new PartDAO().getPartPrice(goods));
				list.add(vo);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void sumBasketEA(BasketVO vo) {
		try {
			con = dataFactory.getConnection();
			String query = "select * from basket where user_id=? and goods=?";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, vo.getUser_id());
			pstmt.setString(2, vo.getGoods());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
			
			vo.setEa(vo.getEa()+ rs.getInt("ea"));
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addBasket(BasketVO basketVO) {
		try {
			Connection con = dataFactory.getConnection();
			
			String user_id = basketVO.getUser_id();
			String goods = basketVO.getGoods();
			int ea = basketVO.getEa();
			String query = "insert into basket";
			query += " (user_id,goods,ea)";
			query += " values(?,?,?)";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, user_id);
			pstmt.setString(2, goods);
			pstmt.setInt(3, ea);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateBasket(BasketVO basketVO) {
		try {
			Connection con = dataFactory.getConnection();
			Statement stmt = con.createStatement();
			String query = "update basket set ea =? where user_id=? and goods=?";
			System.out.println("prepareStatememt:" + query);
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, basketVO.getEa());
			pstmt.setString(2, basketVO.getUser_id());
			pstmt.setString(3, basketVO.getGoods());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {	
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void delBasket(BasketVO basketVO) {
		try {
			Connection con = dataFactory.getConnection();
			Statement stmt = con.createStatement();
			String query = "delete from basket where user_id=? and goods=?";
			System.out.println("prepareStatememt:" + query+basketVO.getUser_id()+basketVO.getGoods());
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, basketVO.getUser_id());
			pstmt.setString(2, basketVO.getGoods());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isExisted(BasketVO basketVO) {
		boolean result = false;
		String goods = basketVO.getGoods();
		String user_id = basketVO.getUser_id();
		try {
			con = dataFactory.getConnection();
			String query = "select decode(count(*),1,'true','false') as result from basket";
			query += " where user_id=? and goods=?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, user_id);
			pstmt.setString(2, goods);
			ResultSet rs = pstmt.executeQuery();
			rs.next(); //커서를 첫번째 레코드로 위치시킵니다.
			result = Boolean.parseBoolean(rs.getString("result"));
			System.out.println("result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
