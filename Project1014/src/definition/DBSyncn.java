package definition;

public class DBSyncn {
/*
 
 ** 리버스 엔지니어링 & 포워드 엔지니어링 
 =>포워드 엔지니어링은 개발 순서대로 개발을 진행  
 분석 -> 설계 -> 구현 
 분석 및 설계를 수행한 정보를 가지고 구현 
 
=> 리버스 엔지니어링은 구현된 정보를 가지고 설계 정보를 추출하는 것 
유지보수를 위해서 이미 구현된 시스템에서 데이터베이스 정보나 클래승 대한 
정보를 추출합니다. 

** 데이터베이스 작업을 할 때 설계한 내용을 가지고 테이블을 만들어 주고 만들어진 
테이블로부터 설계 정보를 추출할 수 있는 프로그램을 E-R Modeling Tool 이라고
합니다.
현업에서 가장 유명한 모델링 도구는 E-R Win입니다.
오픈 소스 형태로 제공되는 eXERD도 있습니다.  

**eXERD 설치 
=> 사이트에서 직접 다운로드 받아서 설치 가능 -

=>eclipse 플러그 인으로 설치 가능 - http://exerd.com/update 
1. [help] - [install new software]를 클릭 
=> 주소를 모를 때는 [Eclipse Marketplace]를 클릭해서 검색해서 설치 

2. work with옆에 URL입력하고 엔터 
http://exerd.com/update 

3.eXERD가 검색되면체크를 하고 Next버튼과 사용권 동의만 체크하면 설치가 됩니다.
=> 플러그인은 설치하고 나면 이클립스를 재시작해야 합니다. 

4. 플러그 인이 제대로 설치 되었는지 확인 
 [Window] - [Perspective] -[Open Perspective] - [Other]에 가보면 
 Perspective가 생기면 제대로 설치된 것입니다. 

** 리버스 엔지니어링 
=> 이미 구현된 테이블을 가지고 E-R다이어그램을 생성 
1.이클립스에서 Perspective를 eXERD추가 

2.eXERD메뉴에서 리버스엔지니어링을 클릭 

** 포워드 엔지니어링 
=>데이터베이스 설계 정보를 가지고 데이터베이스 객체를 생성 
E-R Diagram을 만들어서 이 정보를 가지고 테이블을 생성 

 1.[File] - [New] -[eCERD] - [eXERD File] 을 클릭해서 파일을 생성 
 =>사용할 데이터베이스 종류룰 선택 
 => 파일을 저장할 프로젝트와 파일 이름을 설정 

2. 파일에 테이블을 디자인 

3. [Window] - [Show View] -[eXERD]-[모델]을 실행하고 스키마의 이름을 
자신의 계정으로 변경 
4. 물리적인 테이블을 생성하고자 하면 그때는 [eXERD]메뉴에서 [포워드
엔지니어링]을 선택 

**Class Diagram
=>클래스 들 사이의 관계와 속성들을 다이어그램으로 표현해서 알아보기 쉽게 
해줍니다.
=>이클립스 플러그인: http://www.objectaid.com/update/current   

=>다이어그램 생성 
[File] -[ New] - [Others] - [ObjectAid UML Diagram] - [Class Diagram]

** 로그 기록 
=>Log 기록: 작업내역이나 오류내역 등을 화면이나 파일 또는 데이터베이스에 기록
하는 것 
로그를 기록해 두면 시스템에 장애가 발생했을때 복구가 쉬워지고 다음에 다른 
개발을 할 때 참고가 되기 때문에 학습용으로도 사용 
=>빈번히 발생하는 작업의 로그는 데이터베이스에 기록하기에는 부담이 됩니다. 
데이터베이스에 저장해 두면 검색이 용이하다는 장점이 있는데 기본적인 오버헤드
(순수한 로그 이외의 데이터)가 발생 

=> 삽입, 삭제, 수정 작업이 수행될 때 작업 내용과 시간을 저장 
매일 파일을 새로 생성해서 기록 

1.EventHandler클래스에 파일을 기록할 수 있는 PrintWriter클래스 타입의 
변수를 선언 
=>바이트 단위로 기록하고 읽어오는 경우:BufferedInputStream, PrintStream
=>문자 단위로 읽어오고 기록하는 경우: BufferedReader, PrintWriter 
//로그를 기록할 문자 스트림 
	PrintWriter pw; 
	
2.생성자에서 오늘 날짜에 해당하는 파일에 기록할 수 있도록 pw를 생성 
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
	    
3. 삽입, 삭제, 갱신 작업 후에 기록 
1) 삭제 작업을 수행하는 코드 뒤에 추가 
// 현재 시간을 생성 
Calendar cal = 
					new GregorianCalendar();
//Calendar객체를 이용해서 java.util.Date객체를 생성 
 //날자와 시간 모두 사용할 때는 java.util.Date 
 //날짜만 사용할 때는 java.sql.Date 
			java.util.Date today =
					new java.util.Date(
							cal.getTimeInMillis());
//Date객체를 가지고 날짜나 시간 문자열을 만들기 위한 클래스의 객체 생성 
			SimpleDateFormat sdf = 
					new SimpleDateFormat("hh:mm:ss");
			String log = ("삭제,"
					+ sdf.format(today) + "\n");
			
			pw.println(log);
			pw.flush();

관계형 데이터베이스 
(Relational DataBase Management System) 




** MySQL다운로드 
=>www.mysql.com 에 접속해서 MySQL Community Server를 다운로드(GPL
버전 - 무료로 사용할 수 있는 라이센스) 

**Mac에서 MySQL설치 
=>다운로드 받은파일을 더블 클릭해서 설치
=> 설치가 완료되면 관리자 비밀번호를 대화상자에 출력하기 떄문에 설치가 완료된 
경우 관리자 비밀번호를 메모
=>윈도우즈 용은 비밀번호를 직접 설정합니다. 

r,e4hyIGokze
1.관리자 비밀번호 변경 
1)시스템 환경설정에서 MySQL서비스를 구동 

2) 터미널을 실행시켜서 mysql명령어 디렉토리로 이동 
cd /usr/local/mysql/bin

3) 관리자로 로그인 
./mysql -u root -p

 4)관리자 비밀번호 변경 
 alter user 'root'@'localhost' identified by 'jeremy94';
 
  flush privileges; 
  
2.DBeaver에서 접속 
=> 접속할 데이터베이스 드라이버: oracle을 제외하고는 
www.mvnrepository.com에서 다운로드 가능  - mysql로 검색 

=> 접속할 데이터베이스 URL:mysql은 기본적으로 3306번 포트를 사용하고
mysql이라는 데이터베이스를 소유하고 있습니다. 
 
=> 계정과 비밀번호 

5.데이터베이스 관련 명령 
=>mysql은 접속단위가 database
=>기본적으로 mysql이라는 데이터베이스가 root에게 제공

1)데이터베이스 생성 명령 
create database 데이터베이스 이름;

2)데이터베이스 사용 
use데이터베이스이름;

3)데이터베이스 확인 
show databases;

4)데이터베이스 삭제
drop database 데이터베이스이름;

6. sample.sql을 다운받아서 샘플데이터 생성하기 

7.SQL(Structured Query language)
=>관계형 데이터베이스에 작업을 수행하도록 해주는 구조적 질의 언어 
1)DQL(Data Query Language) 
=>데이터베이스에서 데이터를 조회할 때 사용하는 명령어 
=>select 

2)DML(Data Manipulation Language)
=>테이블에 데이터를 삽입하고 수정하고 삭제하는 명령어 
=>insert, update, delete 

3)TCL(Transaction Control Language) 
=>현재까지 수행한 트랜잭션의 작업을 완료하거나 취소하는 명령어 
=>commit, rollback, savepoint 

4)DDL(data Definition Lnguage) 
=>데이터베이스 개체를 생성하고 구조를 변경하고 삭제하는 명령어 
=>create, alter, drop, truncate 

5)DCL(Data Control Language) 
=>데이터베이스에 권한을 부여하고 취소하는 명령어 
=>grant, revoke 

8.Select
1)select구조 
select         조회할 컬럼이나 연산식 
from           조회할 테이블 이름  
where         조회할 조건 
group by    그룹화 할 컬럼이나 연산식  
having        그룹화 한 이후에 추가할 조건 
order by     정렬할 컬럼이나 연산식 

=> select와 from은 필수 
=> 실행순서는 from -> where -> group by -> having -> select -> order by

2)테이블의 전체 데이터 조회 
select * 
from 테이블이름; 

=> usertbl테이블의 모든 데이터를 조회 

3)특정 컬럼만 조회 
select 컬럼이름 나열 
from 테이블이름; 

=> usertbl테이블에서 userid와 name컬럼만 조회 
select userid,name
from usertbl;

4) 연산식 조회 
select 연산식 
from 테이블이름; 

=>buytbl테이블에서 userid컬럼과 price*amount의 값을 조회 
select userid,price*amount as Total
from buytbl;

5)연산식이나 그룹함수를 사용하게 되면 컬럼이름이 연산식이나 그룹함수이름으로 
출력이 됩니다. 
공백을 한개 주고 별명을 입력해서 별명을 출력하는 것이 보기에 좋습니다. 

6) distinct
=>select절의 맨앞에 사용해서 중복을 제거해주는 명령어 
buytbl테이블에서 userid를 중복없이 출력 

select distinct userid
from buytbl;

7)where절 
=>테이블에서 조건을 적용해서 행단위로 분리하는 절 
=>>,>=,<,<=,=,!=(<>) 연산자를 이용해서 크기비교나 동일성 여부를 비교 가능 
날짜나 문자도 크기비교가 가능 
날짜는 정수와 산술연산이 가능하면 날짜끼리 뺄셈도 가능 
문자를 입력할 때는 작은 따옴표 안에 입력 

=>usertbl테이블에서 userid가 kty인 데이터의 userid와 mdate를 조회 

select userid, mdate 
from usertbl 
where userid = 'kty';
 
 =>mysql은 날짜를 만들 때 일반적인 날짜형식의 문자열 가능 

=>and 와 or를 이용해서 2개의 조건을 묶을 수 있습니다. 

=>between a and b 를 이용해서 a에서b사이의 데이터 조회 가능 

 usertbl테이블에서 birthyear가 1990보다 크거나 갗고 addr이 서울인 데이터의 
 모든 컬럼을 조회 
 select * 
 from usertbl
 where birthyear >= 1990 and addr = '서울'; 
 
 usertbl테이블에서 birthyear가 1990dptj 1993인 데이터의 모든 컬럼을 조회 
 
 select * 
 from usertbl
 where birthyear >= 1990 and birthyear <= 1993;
 
 select * 
 from usertbl
 where birthyear between 1990 and 1993; 
  
  =>in 연산자 
  여러 개 중에서 하나의 값과만 일치하는 데이터 조회 
  usertbl테이블에서 addr 이 서울이거나 대구인 데이터의 모든 컬럼을 조회 
  
  select * 
  from usertbl 
  where addr in('서울','대구');
 

 



  
  
  
  
  
  
 
 */
}
