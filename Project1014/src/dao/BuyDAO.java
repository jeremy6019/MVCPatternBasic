package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.Buy;

public class BuyDAO {
//클래스를 처음 사용할 때 한번만 수행하는 코드 
	static {
		
        try {  	
   	      Class.forName(
   				"oracle.jdbc.driver.OracleDriver" );    
         } catch(Exception e) {  	 
   	     System.out.printf("드라이버 클래스 로드 예외%s\n",
   	    		 e.getMessage());
   	    		 e.printStackTrace();
        }
	} 

	//데이터베이스 연동에 필요한 변수 
	//데이터베이스 연결변수 
	Connection con; 
	//SQL실행변수 
	PreparedStatement pstmt;
	//select 구문의 결과 사용변수 
	ResultSet rs; 
	
	//데이터를 삽입하는 메소드 
	public int insertBuy(Buy buy) {
		int result = -1; 
		
		try {
			
			 con = DriverManager.getConnection(
				     	"jdbc:oracle:thin:@192.168.0.13:1521:xe",
					    "user05", "user05" );
			 pstmt = con.prepareStatement(					
					 "insert into buy("
					 + "buycode, itemname, ctmid, count)"
					 + "values(buyseq.nextval,?,?,?)");
			 //sql에 물음표가 있으면 실제 데이터 바인딩 
			 pstmt.setString(1, buy.getItemname());
			 pstmt.setString(2, buy.getCtmid());
			 pstmt.setInt(3,  buy.getCount());
			 //sql실행 
			 result = pstmt.executeUpdate();		       
			 //정리 
			 pstmt.close();
			 con.close();
			 
		}catch(Exception e) {
			System.out.printf("데이터삽입 예외:%s\n",
					e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}

	
	// 데이터를 수정하는 메소드 
	public int updateBuy(Buy buy) {
        int result = -1; 
		
		try {
			
			 con = DriverManager.getConnection(
				     	"jdbc:oracle:thin:@192.168.0.13:1521:xe",
					    "user05", "user05" );
			 pstmt = con.prepareStatement(					
					 "update buy "
					 + "set itemname=?, ctmid=?, count=? "
					 + "where buycode=?" );
			 //sql에 물음표가 있으면 실제 데이터 바인딩 
			 pstmt.setString(1, buy.getItemname());
			 pstmt.setString(2, buy.getCtmid());
			 pstmt.setInt(3,  buy.getCount());
			 pstmt.setInt(4, buy.getBuycode());
			 //sql실행 
			 result = pstmt.executeUpdate();		       
			 //정리 
			 pstmt.close();
			 con.close();
			 
		}catch(Exception e) {
			System.out.printf("데이터 수정 예외:%s\n",
					e.getMessage());
			e.printStackTrace();
		}	
		return result;	
	}

	
	//데이터를 삭제하는 메소드 
	public int deleteBuy(int buycode) {
        int result = -1; 
		
		try {			
			 con = DriverManager.getConnection(
				     	"jdbc:oracle:thin:@192.168.0.13:1521:xe",
					    "user05", "user05" );
			 //데이터를 삭제하는 SQL 객체 생성 
			 pstmt = con.prepareStatement(					
					 "delete from buy where buycode = ? " );
			 pstmt.setInt(1, buycode);
			 //sql실행 
			 result = pstmt.executeUpdate();		       
			 //정리 
			 pstmt.close();
			 con.close();
			 
		}catch(Exception e) {
			System.out.printf("데이터 삭제 예외:%s\n",
					e.getMessage());
			e.printStackTrace();
		}	
		
		return result;	
	}
	
		
	//전체 데이터를 조회하는 메소드 
	public List<Buy> getReadAll(){
		
		List<Buy> list = new ArrayList<Buy>();
		
		try {
			
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.0.13:1521:xe",
					"user05","user05" );
			pstmt = con.prepareStatement(
					"select * from buy");
			//select 구문 실행 
			rs = pstmt.executeQuery();			
			while(rs.next()) {
				Buy buy = new Buy();
				buy.setBuycode(
						rs.getInt("buycode"));
				buy.setItemname(
						rs.getString("itemname"));
				buy.setCtmid(
						rs.getString("ctmid"));
				buy.setCount(
						rs.getInt("count"));
				buy.setBuydate(
						rs.getDate("buydate"));
				
				list.add(buy);
			}
					
		} catch(Exception e) {
			System.out.printf("데이터 읽기 예외%s\n",
					e.getMessage());
			e.printStackTrace();
		}
			
		return list;
	}
	
	
	//기본키를 가지고 조회하는 메소드 
	public Buy readBuy(int buycode) {
		Buy buy =null;
	
		try {
		    con = DriverManager.getConnection(
			     	"jdbc:oracle:thin:@192.168.0.13:1521:xe",
				    "user05", "user05" );
		    //SQL실행객체 생성 
		    pstmt = con.prepareStatement(
		    		"select * from buy where buycode = ? ");
		    //?에 실제 데이터를 바인딩 
		    pstmt.setInt(1, buycode);
		    //SQL실행 
		    rs = pstmt.executeQuery();
		    //데이터 읽기 
		    if( rs.next()) {
		    	buy = new Buy(); 
		    	buy.setBuycode(rs.getInt("buycode"));
		    	buy.setItemname(rs.getString("itemname"));
				buy.setCtmid(rs.getString("ctmid"));
				buy.setCount(rs.getInt("count"));
			    buy.setBuydate(rs.getDate("buydate"));							
		    }
		    rs.close();
		    pstmt.close();
		    con.close();
		    
		} catch(Exception e) {
			System.out.printf("1개 가져오기 예외:%s\n",
					e.getMessage());
			e.printStackTrace();
		}	
		return buy;
	  }	
	
	
	
	
	
	
	
}
