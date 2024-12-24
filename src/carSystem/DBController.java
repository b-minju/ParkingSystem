package carSystem;

import java.sql.*;
 
public class DBController {
	Connection con;
 
	String url="jdbc:oracle:thin:@localhost:1521:xe";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
	String userid="c##javap"; /* 12버전 이상은 c##을 붙인다. */
	String pwd="javap";
	
	public DBController () {
		try { /* 드라이버를 찾는 과정 */
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println ("드라이버 로드 성공");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean startConnection() {
		boolean ret = false;
		try /* 데이터베이스를 연결하는 과정 */
		{
			System.out.println ("데이터베이스 연결 준비 ...");
			con = DriverManager.getConnection(url, userid, pwd);
			System.out.println ("데이터베이스 연결 성공");
			ret = true;
		} catch(SQLException e) {
			e.printStackTrace();
			ret = false;
		}
		
		return ret;
	}
	
	public boolean duplicate(String number) { // 중복확인 -  중복이 있을 경우 true
		String qry = "select carNum from Car where carnum = '" + number + "'" ;
		boolean result = false;
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(qry);
			if (rs.next()){// 중복
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public int getId(String qry) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int result = 0;
	    try {
	        pstmt = con.prepareStatement(qry);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            result = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}
	
	public String getInfo(String qry, String columnName) {
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String r = "error";
	    try {
	        pstmt = con.prepareStatement(qry);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            r = rs.getString(columnName);
	            if (r == null) {
	                r = "";
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return r;
	}
	
	public boolean isEmpty(String kinds) {
	    String qry = "select count(*) from Car";
	    boolean result = true;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        if (kinds.equals("승용차") || kinds.equals("SUV")) {
	            qry += " where kinds = ?";
	        }
	        pstmt = con.prepareStatement(qry);
	        if (kinds.equals("승용차") || kinds.equals("SUV")) {
	            pstmt.setString(1, kinds);
	        }
	        rs = pstmt.executeQuery();
	        if (rs.next() && (((countCar("승용차") >= 15) || (countCar("SUV") >= 5)) || (countCar("승용차") + countCar("SUV") >= 20))) {
	            result = false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}
	
	int count = 0;
	
	public int countCar(String kinds) {
	    String qry = "select count(*) from Car where kinds = '" + kinds + "'";
	    Statement stmt;
	    try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(qry);
	        if (rs.next()) {
	            count = rs.getInt(1); // ResultSet의 첫 번째 열을 가져옵니다.
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return count;
	}

	
	public void exeQry(String action, int count, String kinds, String number) { // insert, delete
		String sql = "";
		if (action.equals("in")) {
			sql = "update car set kinds = '" + kinds + "', carnum = '" + number + "' where carid = " + count;
		}
		else if (action.equals("out")) {
			sql = "update car set kinds = null, carnum = null where carid = " + count;
		}
		
		try {
			Statement stmt = con.createStatement();
			System.out.println("car " + action + " 작업 성공");
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}