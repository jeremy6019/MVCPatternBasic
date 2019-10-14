package definition;

public class SyncOracle {
/*
 
 ** 오라클 연동 프로그램 
 1. 준비 
 => 데이터베이스 연동 프로그램을 만들대는 데이터베이스 드라이버 파일이 있어야 
 합니다.

 2. 클래스
 =>DTO: 데이터베이스 테이블의 하나의 행을 표현하디 위한 클래스 
 => DAO:데이터베이스 작업을 위한 클래스 
 => View:	화면을 위한 클래스 
 =>EventHandler: View에서 발생하는 이벤트를 처리하는 클래스 
 =>Main:실행 클래스 
 
=>EventHandler는 Controller라고 불러도 되는데 사용자의 이벤트에 따라 필요한
작업을 호출해서 수행하는 클래스 

3. 테이블- 구매 테이블  
=> 구매코드: 일련번호, 기본키 
=> 상품명: 문자열(한글 포함 20자 이내), 필수입력 
=> 구매자 아이디: 문자열(영문과 숫자 20자이내), 필수입력 
=> 수량: 정수, 필수입력
=> 구매일자: 날짜로 기본값은 현재 시간 

4. 작업 
=>데이터 추가, 삭제, 수정, 전체 조회, 상품명을 이용한 조회 

실습 
1. 데이터베이스에 접속해서 테이블을 생성 
CREATE TABLE buy(
  buycode number(5) PRIMARY KEY, 
  itemname varchar2(60) NOT NULL,
  ctmid varchar(20) NOT NULL, 
  count number(5) NOT NULL,
  buydate DATE DEFAULT SYSDATE
);

2. 일련번호를 위한 sequence생성 
=> buyseq로 생성하고 1부터 시작 

CREATE SEQUENCE buyseq 
   START WITH 1; 

INSERT INTO buy(
buycode, itemname, ctmid, count)
values(buyseq.nextval, '과자', 'gg', 3);

INSERT INTO buy(
buycode, itemname, ctmid, count)
values(buyseq.nextval, '카봇', 'taein', 5);

INSERT INTO buy(
buycode, itemname, ctmid, count)
values(buyseq.nextval, '액괴', 'ys', 10);

INSERT INTO buy(
buycode, itemname, ctmid, count)
values(buyseq.nextval, '샤넬', 'ey', 7);

COMMIT;

SELECT * FROM buy;
3.Buy테이블의 하나의 행을 표현할DTO클래스 만들기 
1) 프로젝트 생성 

2)ojdbc6.jar파일을 프로젝트에 삽입하고 build path에 추가 

3)DTO로 사용할 Buy클래스를 생성 
=> 테이블의 컬럼에 해당하는 항목을 private변수로 생성 
=> 접근자 메소드 생성 
=> toString 재정의 
private int buycode;
	private String itemname ;
	private String ctmid;  
	private int count;
	private Date buydate;
	
	public int getBuycode() {
		return buycode;
	}
	public void setBuycode(int buycode) {
		this.buycode = buycode;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getCtmid() {
		return ctmid;
	}
	public void setCtmid(String ctmid) {
		this.ctmid = ctmid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getBuydate() {
		return buydate;
	}
	public void setBuydate(Date buydate) {
		this.buydate = buydate;
	}
	
	@Override
	public String toString() {
		return "Buy [buycode=" + buycode + ", itemname=" + itemname + ", ctmid=" + ctmid + ", count=" + count
				+ ", buydate=" + buydate + "]";
	}
	
4.DAO클래스를 생성 
=>5개의 메소드 생성 
1) 드라이버 클래스를 로드: 처음에 1번만 수행 
=> 클래스 내부에 static 영역을 생성해서 작성 	
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
	
2) 데이터베이스 작업에 사용되는 3개의 변수를 멤버변수로 선언
    //데이터베이스 연동에 필요한 변수 
	//데이터베이스 연결변수 
	Connection con; 
	//SQL실행변수 
	PreparedStatement pstmt;
	//select 구문의 결과 사용변수 
	ResultSet rs; 

3) 테이블에 삽입하는 메소드를 생성    

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

4)데이터를 수정하는 메소드 
=>모양은 삽입하는 메소드와 동일 
=>기본키를 가지고 조회해서 나머지 데이터를 수정 

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
					 + "where buycode=?");
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
	
5) 데이터를 삭제하는 메소드 
=>delete from 테이블이름 where 조건 
=> 삭제할 때는 대부분 기본키를 가지고 비교해서 삭제 
=> 메소드의 매개변수가 기본키 
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
	
6) 전체 데이터를 조회하는 메소드 
=>0개 이상의 데이터 조회하는 경우에는 리턴 타입이 List 
=> 매개변수는 where 절에 해당하는 데이터 - where절이 없어서 매개변수는 없음 
//전체 데이터를 조회하는 메소드 
	public List<Buy> getRead(){
		
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

7) 기본키를 가지고 조회하는 메소드 
=> 기본키를 가지고 조회하게 되면 데이터는 0개 또는 1개만 조회
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
	
5. 화면만들기 
public class BuyView extends JFrame {
    //데이터 접속 및 출력을 위한 컴포넌트 
	public JTextField txtItemName, txtCtmId, 
	     txtCount, txtBuyDate; 
	public JLabel lblItemName, lblCtmId, 
	     lblCount, lblBuyDate;
	
	//데이터 조회를 위한 컴포넌트 
	public JButton btnLast, btnPrev, 
	    btnNext, btnFirst;	
	public JLabel lblNum;
	
	//데이터 작업을 수행할 컴포넌트 
	public JButton btnInsert, btnDelete,
	    btnUpdate, btnSearch, btnClear;
	
	
	
	public BuyView() {
		//4줄 2칸으로 만들고 여백을 가로 3 세로 3으로 만듬 
		JPanel p1 = new JPanel(
				new GridLayout(4, 2, 3, 3));
		lblItemName = new JLabel(
				"아이템이름", JLabel.RIGHT);
		txtItemName = new JTextField();
		lblCtmId = new JLabel(
				"구매자 아이디", JLabel.RIGHT);
		txtCtmId = new JTextField();
		lblCount = new JLabel(
				"구매수량", JLabel.RIGHT);
		txtCount = new JTextField(); 
		lblBuyDate = new JLabel(
				"구매일시", JLabel.RIGHT);
		txtBuyDate = new JTextField();
		
		p1.add(lblItemName);
		p1.add(txtItemName);
		p1.add(lblCtmId);
		p1.add(txtCtmId);
		p1.add(lblCount);
		p1.add(txtCount);
		p1.add(lblBuyDate);
		p1.add(txtBuyDate);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(
				new BorderLayout());
		centerPanel.add("Center", p1);
		
		btnNext = new JButton("다음");
		btnPrev = new JButton("이전");
		btnFirst = new JButton("처음");
		btnLast = new JButton("끝");
		
		lblNum = new JLabel(
				"", JLabel.CENTER);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(1,5,7,7));
		p2.add(btnFirst);
		p2.add(btnPrev);
		p2.add(lblNum);
		p2.add(btnNext);
		p2.add(btnLast);
		
		centerPanel.add("South", p2);
		
		add(centerPanel);
		
		btnInsert = new JButton("삽입");
		btnDelete = new JButton("삭제");
		btnUpdate= new JButton("수정");
		btnSearch = new JButton("조회");
		btnClear = new JButton("지움");
		
		JPanel southPanel = 
				new JPanel();
		southPanel.setLayout(
				new GridLayout(1,5,3,3));
		southPanel.add(btnInsert);
		southPanel.add(btnDelete);
		southPanel.add(btnUpdate);
		southPanel.add(btnSearch);
		southPanel.add(btnClear);
		
		add("South", southPanel);
		
		
		setTitle("구매테이블");
		//위치와 크기를 한꺼번에 설정 
		setBounds(200,100,400,300);
		//종료 기능 부여 
		setDefaultCloseOperation(
				EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
}

6. View	가 화면에 출력될 때 전체 데이터를 가져와서 
    첫번째 데이터를 화면에 출력 
1) 데이터 전체를 저장할 List를 인스턴스로 선언  
	public List<Buy> list; 
	
2)현재 보여지는 데이터의 인덱스를 저장할 변수 
	public int idx; 

3)생성자에서 Dao의 전체 데이터를 가져와서 list에 저장하는 코드를 추가 
//전체 데이터 가져와서 list에 저장 
BuyDAO dao = new BuyDAO();
list = dao.getReadAll();

4) list에서 idx번째 데이터를 출력하는 메소드를 생성 
//list에서 idx번쨰 데이터를 가져와서 
	//화면에 출력하는 메소드 
	public void display() {
		//배열이나 List에서 데이터를 꺼낼때 
		//ArrayIndexOutOfBoundsException에 주의 
		//이 작업이 선행되어야 함 
		if(list == null || list.size()==0) {
			JOptionPane.showMessageDialog(
					null, 
					"출력할 데이터가 없습니다.");
			return;
		}
		
		Buy buy = list.get(idx);
		this.txtItemName.setText(
				buy.getItemname());
		this.txtCtmId.setText(
				buy.getCtmid());
		// 숫자나 날짜는 문자열로 변환해서 출력 
		String disp = String.format("%d개", 
				buy.getCount());
		this.txtCount.setText(disp);
		this.txtBuyDate.setText(
				buy.getBuydate().toString());
		this.lblNum.setText(idx+1+"");
	}

5)생성자에서 데이터를 출력하는 display()롤 호출 
display(); 

7.데이터 조회 버튼들의 이벤트 처리 
=>java의 swing과 AWT는 이벤트 처리를 Delegate(위임)패턴으로 처리합니다. 
이벤트가 발생한 뷰가 처리하지 않고 이벤트를 처리할 수 있는 메소드를 소유한 인터
페이스를 구현한 클래스의 객체에게 위임하는 방식입니다.
안드로이드도 뷰들은 이방식으로 이벤트를 처리하는 경우가 많습니다. 
=> View.add이벤트Listener(이벤트Listener인터페이스를 implements한 클래스의 
객체를 대입)
=>버튼 클릭은 ActionListener가 처리 
=>유사한 역활을 하는 이벤트들을 하나의 클래스에서 처리하도록 하는 것을 
이벤트 라우팅이라고 합니다. 
이벤트 처리해주는 클래스를 일반프로그래밍에서는 EventHandler라고 하고 
웹프로그래밍에서는 Controller라고 많이 부릅니다. 

=>이벤트 처리하는 클래스를 EventHandler로 별도로 생성해서 View클래스의 데이터를
조작하려고 합니다. 

동일한 클래스 안에서 데이터를 공유:static변수를 생성  

서로 다른 클래스에서 데이터를 공유: 
=>사용하려고 하는 쪽으로 데이터를 넘겨주는 방법 
=>public클래스를 만들고 그 안에 static변수를 생성 - 좋지 않은 방법으로 취급 

1)ActionListener를 처리할 수 있는 클래스를 생성 
=>ActionListener인터페이스를 구현(implements)하는 클래스로 생성 
=>BuyEventHandler클래스 

2)BuyEventHandler클래스에서 BuyView클래스의 데이터를 조작하기 위한 작업 
=>BuyView클래스를 자료형으로 하는 변수를 인스턴스 변수로 선언 
BuyView buyView;

=>BuyView를 매개변수로 받는 생성자를 만들어서 매개변수의 값을 인스턴스 변수에 
대입- 자동생성 메뉴가 있으므로 자동생성메뉴를 이용하는 것이 편리  
public BuyEventHandler(
			BuyView buyView) {
		super();
		//변수는 아무런 기호가 붙지 않으면 
		//가까운 곳에서 만들어진 것을 사용 
		//this.을 붙이면 메소드 외부에서 찾아서 사용 
		//super.을 붙이면 상위클래스에서 찾아서 사용 
		this.buyView = buyView;
	}	 

3) 버튼 클릭 메소드 구현 
=> Delegate패턴으로 만들어진 이벤트 처리 메소드들은 반드시 매개변수를 가지고
이벤트가 발생한 객체를 찾을 수 있도록 해줍니다. 
=>매개변수가 View형태이면 이 View가 이벤트가 발생한 객체이고 View가 아니면 
매개변수 내의 어떤 속성이나 메소드가 View를 리턴해줍니다.   	  
  
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
		    
		}
		

	}

 4) BuyView클래스의 생성자에서 버튼의 클릭이벤트가 발생하면 EventHandler
 클래스의 객체가 처리하도록 연결 
 
	BuyEventHandler eventHandler = 
				new BuyEventHandler(this);
		btnNext.addActionListener(eventHandler);
		btnPrev.addActionListener(eventHandler);
		btnFirst.addActionListener(eventHandler);
		btnLast.addActionListener(eventHandler);  
  
        btnInsert.addActionListener(eventHandler);
		btnDelete.addActionListener(eventHandler);
		btnUpdate.addActionListener(eventHandler);
		btnSearch.addActionListener(eventHandler);
		btnClear.addActionListener(eventHandler);

5)EventHandler클래스의 버튼을 클릭했을 때 호출되는 메소드에 삭제 버튼을 눌렀을 때의 
처리를 위한 코드를 추가 

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
			break;


6) 수정 버튼을 클릭했을때 이벤트 추가 
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

7)BuyView클래스의 생성자에서 txtCount에 숫자만 입력할 수있도록 키보드 이벤트
처리하는 코드를 추가 

lblCount = new JLabel(
				"구매수량", JLabel.RIGHT);
		txtCount = new JTextField(); 
		
		txtCount.addKeyListener(
				new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void keyPressed(KeyEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void keyReleased(KeyEvent e) {
						//0에서 9사이의 키가 아닌 것을 누르면 
						if(e.getKeyCode() < KeyEvent.VK_0 ||
								e.getKeyCode() > KeyEvent.VK_9) {
							String imsi = "";
							int len = txtCount.getText().length();
							for(int i=0; i<len-1; i++) {
								char ch = 
										txtCount.getText().charAt(i);
								imsi += ch;
							}
							txtCount.setText(imsi);
						}
						
					}
					
				});

8) 지움버튼을 눌렀을때 처리하는 코드를 추가 
=> 지움버튼을 누르면 삽입버튼과 지움버튼만 활성화 나머지 버튼은 비활성화
=>지움버튼을 누르면 텍스트 필드들의 내용을 삭제 
=>지움버튼의 텍스트를 취소로 변경 
=> 취소로 텍스트가 변경된 경우에는 취소버튼을 누르면 다시 전체 데이터를 
가져와서 첫번째 데이터를 출력  
case "지움":
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
			buyView.btnDelete.setEnabled(true);
		    buyView.btnUpdate.setEnabled(true);
		    buyView.btnSearch.setEnabled(true);
		    
		    buyView.btnClear.setText("지움");
		    
		    buyView.display();
			break;
			

** 비 연결 데이터베이스 사용 
=> 한 곳에서만 사용하는 애플리케이션의 경우 데이터베이스에 삽입, 삭제,갱신작업을 할 때 
작업을 하고 매번 데이터를 다시 가져오는 것은 자원의 낭비가 될 수 있습니다. 
이런 경우에는 데이터베이스에 작업을 하고 자신의 메모리에 있는 변수에도 동일한 
작업을 수행하고 다시 출력하는 방법을 사용할 수 있습니다.
이런 형태의 데이터베이스 사용방법을 비 연결형 데이터베이스 사용이라고 합니다. 
메모앱이나 메일 등을 보여주는 앱처럼 자신만이 사용하는 데이터베이스를 가지고 작업을 하는 
앱에 이 방법을 적용하면 트래픽을 많이 줄일 수 있습니다. 

  ** 위와 같은 프로그램을 구현 
  =>클래스를 역할 별로 만드는 연습 
  DTO, DAO(데이터베이스 작업), EventHandler(Controller-연결, Service-알고리즘 
  처리), View(화면), Main
  
  => 이렇게 클래스를 역할 별로 분리해서 프로그래밍하는 방식을 MVC(Model, View, 
  Controller) Pattern 이라고 합니다. 
  
  => MVC패턴을 적용해서 프로그래밍을 하면 만들기는 어렵지만 유지봇수가 수월해 집니다. 
  
  
  
  
  
  
  
 
 */
}
