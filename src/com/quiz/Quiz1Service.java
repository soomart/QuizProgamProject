package com.quiz;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.quiz.utils.DBManager;

public class Quiz1Service {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Scanner scanner = new Scanner(System.in);
	
	String member_id = "";
	public void startQuizService(String member_id) {
		this.member_id = member_id; // 로그인 성공 시 전달받은 member_id 저장
		System.out.println("[ " + member_id + "님 퀴즈를 시작합니다. ]");
		startQuiz();
	}
	
	/*
	 * 설명: 퀴즈 시작
	 */
	public int startQuiz() {
			
		Connection conn = DBManager.getDBConnection();
		
		String startSql = "SELECT quiz_content, quiz_result, quiz_plus FROM quiz WHERE seq =?";
		
		String countSql = """
				UPDATE member1 SET quiz_count = NVL(quiz_count,0)+1 WHERE member_id = ?
				""";
	
		int resultRows = 0;
		int rightresult = 0;
		try {
			
			Random random = new Random();
	        Set<Integer> usedSeqs = new HashSet<>();
	        int count = 1;
	        int wrong_count = 0;
	        System.out.println(count + "번 문제입니다.(종료: quit)");
			while(count<11) {
			
			int randomQuiz = random.nextInt(100) + 1; // 1부터 100까지의 랜덤 숫자
			
			// 중복확인
			if (usedSeqs.contains(randomQuiz)) {
				continue;
			}
			
			pstmt = conn.prepareStatement(startSql);
			pstmt.setInt(1, randomQuiz);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String quizContent = rs.getString(1);
				String quizResult = rs.getString(2);
				String quizPlus = rs.getString(3);
				System.out.println(quizContent);
				usedSeqs.add(randomQuiz);
				count++;
				System.out.print("정답: ");
				String result = scanner.nextLine();
				if (result.equals(quizResult)) {
					System.out.println("정답입니다~!");
					pstmt = conn.prepareStatement(countSql);
					pstmt.setString(1, member_id);
					pstmt.executeUpdate();
					rightresult++;
					} else if(result.equals("quit")) {
						System.out.println("퀴즈를 종료하셧습니다.");
						return -1;
					} else {
					++wrong_count;
					System.out.println("오답입니다. 현재 " + wrong_count +"회 틀렸습니다. 3회 오답 시 탈락입니다.");
					System.out.println("정답은 '"+ quizResult +"' 이고 이유는 " + quizPlus);
					if(wrong_count == 3) {
						System.out.println("3회 오답으로 퀴즈를 종료하겠습니다. 다시 도전해주세요~");
						break;
					}
				}
			} else {
				continue;
			}
			if(count<11) {
			System.out.println(count + "번 문제입니다.(종료: quit)");
			}
			}
			
		}  catch(SQLException se) {
			System.out.println("퀴즈를 출력하는 도중 에러가 발생했습니다. -> " + se.getMessage());	
		}finally {
			
			// Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, rs);
			}
		System.out.println("퀴즈가 종료되었습니다!");
		System.out.println( member_id + "님 " + rightresult +"개 맞쳤습니다." );
		return resultRows;
	}
	
	
	public void Quizmenual() {
		System.out.println("이 퀴즈 프로그램은 넌센스 퀴즈 프로그램으로 퀴즈를 시작하시면 10개의 문제가 순차적으로 나옵니다." + "\n"
				+ "제시되는 10개에 문제를 푸시면 되는데 3번에 오답을 입력 시 탈락됩니다." + "\n"
				+ "문제를 통과하셨으면 맞추신 갯수에 따라 랭킹에 등록됩니다." +"\n" +"랭킹에 정답 갯수는 누적이 되니 여러번 시도해서 많은 문제를 맞출 수록 높은 순위에 등록됩니다."
				+"\n" + "그리고 정답을 입력하실 때 띄어쓰기 없이 맞추시면 됩니다."	);
	}
	
	public void rankQuiz() {
		System.out.println("\t" + "\t" +Date.valueOf(LocalDate.now()) + "기준 랭킹 순위입니다.");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("순위               아이디                    생성일                      정답 갯수");
		System.out.println("-------------------------------------------------------------------------------");

		Connection conn = DBManager.getDBConnection();
		
		String rankSql = """
				SELECT member_id, create_date, quiz_count FROM member1
				ORDER BY quiz_count DESC
				""";
		int countRows = 0;
		try{
			pstmt = conn.prepareStatement(rankSql);
			rs = pstmt.executeQuery();
		while(rs.next()) {
			++countRows;
			System.out.println(countRows+"등"  + "\t" + "\t" + 
					   rs.getString(1) + "\t" + "\t" + "   " +
					   rs.getDate(2) + "\t" +  "\t" + "\t" + "  " +
					   (rs.getString(3) == null? "" : rs.getString(3))
					   );
		}
			if(countRows == 0) {		// member테이블에 데이터가 없을 경우
				System.out.println("랭킹이 존재하지 않습니다.");
			}
		} catch(SQLException se) {
			System.out.println("랭킹을 조회하는 도중 에러가 발생했습니다. -> " + se.getMessage());
			
		} finally {
			// 7. Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, rs);
		}
	}
}
