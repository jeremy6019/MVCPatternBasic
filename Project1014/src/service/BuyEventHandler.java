package service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import dao.BuyDAO;
import dto.Buy;
import view.BuyView;

public class BuyEventHandler implements ActionListener {
	
	BuyView buyView;	
	// 지움버튼을 눌렀는지 확인할 변수 
	boolean flag;
	//로그를 기록할 문자 스트림 
	PrintWriter pw; 
		
	public BuyEventHandler(
			BuyView buyView) {
		super();
		//변수는 아무런 기호가 붙지 않으면 
		//가까운 곳에서 만들어진 것을 사용 
		//this.을 붙이면 메소드 외부에서 찾아서 사용 
		//super.을 붙이면 상위클래스에서 찾아서 사용 
		this.buyView = buyView;
		//오늘 날짜를 문자열로 만들기 
	    Calendar cal = 
	    		new GregorianCalendar();
	    java.sql.Date today = 
	    		new java.sql.Date(
	    				cal.getTimeInMillis());
	    String filename = today.toString();
	    //파일이 존재하면 내용을 추가하기 
	    //위해서 FileOutputStream을 만들고 
	    //PrintWriter를 생성 
	    try {
		    pw = new PrintWriter(
				    new FileOutputStream(
					    	"./"+ filename, true));
	    } catch(Exception e) {}
	    
	}
    


	// 버튼을 누르거나 
	// 텍스트 필드에서 Enter키를 누를때 
	// 처리를 위한 메소드 
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		
		case "처음":
			    if(buyView.idx ==0) {
				    JOptionPane.showMessageDialog(
						null, "첫번째 데이터입니다.");
				    return;
			    }
			    buyView.idx =0;
			    buyView.display();
			    
			break;
			
		case "이전":
			//첫번째 데이터이면 마지막으로 변경 
			   if(buyView.idx ==0) {
				   buyView.idx = 
						   buyView.list.size() -1;				    
			    } else {
			    	buyView.idx = buyView.idx -1;
			    }
			    buyView.display();
			    
			    break;
			
		case "다음":
			//첫번째 데이터이면 마지막으로 변경 
			   if(buyView.idx ==
			          buyView.list.size()-1) {
				   buyView.idx =0; 				    
			    } else {
			    	buyView.idx = buyView.idx + 1;
			    }
			    buyView.display();
			break;
			
		case "끝":
			    if(buyView.idx ==
			        buyView.list.size()-1) {
				        JOptionPane.showMessageDialog(
						    null, "마지막 데이터입니다.");
				        return;
			    }
			    buyView.idx =
			    		buyView.list.size()-1;
			    buyView.display();
		    break;
		  
		case "삭제": 
			int result = 
			    JOptionPane.showConfirmDialog( 
				null, "정말로 삭제?", "삭제",
				JOptionPane.YES_NO_OPTION);
			if( result == JOptionPane.YES_OPTION) {
				//현재 데이터를 가져오기 
				Buy buy = buyView.list.get(buyView.idx);
				//데이터베이스에서 삭제 
				BuyDAO dao = new BuyDAO();
				dao.deleteBuy(buy.getBuycode());
				//메모리에서 삭제
				buyView.list.remove(buyView.idx);
				//idx를 조정하고 다시 출력 
				buyView.idx = 0;
				buyView.display();
			}
			Calendar cal = 
					new GregorianCalendar(); 
			java.util.Date today =
					new java.util.Date(
							cal.getTimeInMillis());
			SimpleDateFormat sdf = 
					new SimpleDateFormat("hh:mm:ss");
			String log = ("삭제,"
					+ sdf.format(today) + "\n");
			
			pw.println(log);
			pw.flush();
			break;
		
		case "수정":
			//입력한 내용 가져오기 
		    	Buy buy = new Buy();
		    //현재 데이터의 buycode를 가져와서 설정 
		    	buy.setBuycode(
		    			buyView.list.get(
		    			buyView.idx).getBuycode());
		    	
		    	String itemname = 
		    			buyView.txtItemName
		    			.getText().trim();
		    	if(itemname.length() < 1) {
		    		JOptionPane.showMessageDialog(
		    				null, "아이템 이름은 필수입력!!");
		    		return;
		    	}
		    	buy.setItemname(
		    			itemname);
		    	
		    	String ctmid = 
		    			buyView.txtCtmId
		    			.getText().trim();
		    	if(ctmid.length() < 1) {
		    		JOptionPane.showMessageDialog(
		    				null, "구매자 ID는 필수입력!!");
		    		return;
		    	}
		    	buy.setCtmid(
		    			ctmid);
		    	
		    	String count = 
		    			buyView.txtCount
		    			.getText().trim();		    	
		    	int len = count.length();
		    	String imsi = "";
		    	//문자열에서 각각의 글자를 가져와서 
		    	//숫자이면 imsi에 더해주고 숫자가 아니면 
		    	// 중단 
		    	for(int i=0; i<len; i+=1) {
		    		char ch = count.charAt(i);
		    		if(ch >= '0' && ch <= '9') {
		    			imsi = imsi + ch;
		    		} else {
		    			break;
		    		}
		    	}
		    	if(imsi.length()==0) {
		    		imsi = "0";
		    	}
		    	buy.setCount(
		    			Integer.parseInt(imsi));
		    	
			   int r = JOptionPane.showConfirmDialog(
					   null, "정말로 수정?", "수정",
					   JOptionPane.YES_NO_OPTION);
			   if( r == JOptionPane.YES_OPTION) {
				   //데이터 수정 
//				   buyView.list.remove(buyView.idx);
//				   buyView.list.add(buyView.idx, buy);
				   BuyDAO dao = new BuyDAO();
				   dao.updateBuy(buy);
				   //데이터 다시 불러오기 
				   buyView.list = dao.getReadAll();
				   buyView.display();
			   }
		    	
			break;
			
		case "지움":
			// 지움 버튼을 눌렀다는 표시를 하기 위해서 
			// flag의 값을 변경 
			flag = true;
			
			buyView.btnDelete
			    .setEnabled(false);
			buyView.btnUpdate
		        .setEnabled(false);
			buyView.btnSearch
		        .setEnabled(false);
			buyView.txtItemName.setText("");
			buyView.txtCtmId.setText("");
			buyView.txtCount.setText("");
			buyView.txtBuyDate.setText(""); 
			//자신의 텍스트를 변경 - 토글버튼으로 사용 
			buyView.btnClear.setText("취소");
			
			break;
			
		case "취소":
			flag = false;
			buyView.btnDelete.setEnabled(true);
		    buyView.btnUpdate.setEnabled(true);
		    buyView.btnSearch.setEnabled(true);
		    
		    buyView.btnClear.setText("지움");
		    
		    buyView.display();
			break;
			
		case "삽입":
			//지움 버튼을 누르지 않았다면 삽입 취소 
			if(flag = false) {
				JOptionPane.showMessageDialog(
						null, "지움 버튼을 누르고 입력하세요!!");
				return;
			}
			// itemname의 null의 입력 체크 
			// itemname에서 중간 공백 삭제? 
			itemname = 
					buyView.txtItemName.
					getText().trim();
			if( itemname.length() < 1 ) {
				JOptionPane.showMessageDialog(
						null, "아이템 이름은 필수!!");
				return;
			}
			
			ctmid = 
					buyView.txtCtmId.
					getText().trim();
			if( ctmid.length() < 1 ) {
				JOptionPane.showMessageDialog(
						null, "구매자 아이디는 필수!!");
				return;
			}
			
			String buyCount =
					buyView.txtCount.
					getText().trim();
			//삽입할 데이터를 생성 
			Buy buy1 = new Buy();
			//값을 채워 넣기 
			buy1.setItemname(itemname);
			buy1.setCtmid(ctmid);
			buy1.setCount(
					Integer.parseInt(buyCount));
			//삽입하는 메소드 호출 
			BuyDAO dao = new BuyDAO();
			int r1 = dao.insertBuy(buy1) ;
			//삽입 성공한 경우 
			if( r1 > 0) {
				Thread th = new Thread() {
				   	public void run() {
						//전체 데이터를 가져오기 
						buyView.list =
								dao.getReadAll();
						//마지막 데이터를 출력 
						buyView.idx = buyView.list.size()-1;
						buyView.display();
					}
				};
				th.start();
				//성공 메시지를 출력 
				JOptionPane.showMessageDialog(
						null, "삽입 성공!!");		
				//버튼들을 활성화 
				buyView.btnClear.setText("지움");
				buyView.btnDelete.setEnabled(true);
				buyView.btnUpdate.setEnabled(true);
				buyView.btnSearch.setEnabled(true);
			}else {
				//실패 메세지를 출력 
				JOptionPane.showMessageDialog(
						null, "삽입 실패!!");		
			}
			
			break;
		
		case "조회":
			
			String code = 
			JOptionPane.showInputDialog(
					"코드를 입력하세요!!");
			//취소 버튼을 누른 것이 아니라면 
		    if( code != null) {
		        dao = new BuyDAO();
		        buy = 
		        		  dao.readBuy(Integer.parseInt(code));
		        if(buy == null) {
		        	JOptionPane.showMessageDialog(
							null, "없는 코드 입니다!!");		
		        }
		        buyView.list.clear();
		        buyView.list.add(buy);
		        buyView.idx = 0; 
		        buyView.display();
		    }
			
			break;
			
	    }
		

	}

}
