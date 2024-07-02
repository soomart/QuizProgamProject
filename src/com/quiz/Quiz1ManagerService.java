package com.quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.quiz.utils.DBManager;

public class Quiz1ManagerService {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Scanner scanner = new Scanner(System.in);
	
	/*
	 * 설명: 퀴즈 목록
	 */
	public void selectAllQuiz() {
		// 퀴즈 목록 조회
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		System.out.println("번호			퀴즈 내용	                						퀴즈정답                         					정답설명");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		
		
		// Oracle DB의 quiz테이블에서 위의 3가지 항목(번호, 퀴즈 내용, 퀴즈 정답)가 나오도록 출력
		// 1. 게시글의 데이터를 받기 위해 Oracle DB접속 준비
		Connection conn = DBManager.getDBConnection();
		
		String selectSql= """
				SELECT seq, quiz_content, quiz_result, quiz_plus FROM quiz ORDER BY seq
				""";
		
		int countRows = 0;
		try {
			pstmt = conn.prepareStatement(selectSql);
			rs = pstmt.executeQuery();
		while(rs.next()) {
			countRows ++;
				System.out.println(rs.getString(1) + "\t" + "\t" +
								   rs.getString(2) + "\t" + "\t" + "\t" +
								   rs.getString(3) + "\t" + "\t" + "\t" +
								   (rs.getString(4) == null? "" : rs.getString(4))
									);
			}if(countRows == 0) {		// quiz테이블에 데이터가 없을 경우
				System.out.println("퀴즈 문제가 존재하지 않습니다.");	
			}
		} catch(SQLException se) {
			System.out.println("퀴즈 목록 조회하는 도중 에러가 발생했습니다. -> " + se.getMessage());
			
		} finally {
			// 7. Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, rs);
		}
		
	}
	
	/*
	 * 설명 : 퀴즈 추가
	 */
	public int insertQuiz() {
		
		// 4. 위의 입력된 게시글의 제목과 내용을 받아서 Oracle DB에 저장할 수있도록 DB접속준비
		Connection conn = DBManager.getDBConnection();
		
		// 5. Oracle DB에 데이터를 삽입하기 위해 insert sql문을 작성
		String insertSql = """
				INSERT INTO QUIZ (seq, quiz_content, quiz_result, quiz_plus)
				VALUES (seq_quiz_no.nextval, ?, ?,?)
				""";
		int resultRows = 0;		// insert문을 실행한 뒤에 테이블 영향을 받은 행 개수
		
		try {
			while(true) {
				// 1. 퀴즈 문제 내용 받기
				System.out.println("퀴즈 문제 내용(취소:quit): ");
				String quiz_content = scanner.nextLine();		// 키보드로 퀴즈문제 받기
				if(quiz_content.equals("quit")) {
					System.out.println("작성이 취소되었습니다.");
					return -1;
				}
				
				// 2. 퀴즈 정답 받기
				System.out.println("퀴즈 정답(취소:quit): ");
				String quiz_result = scanner.nextLine();		// 키보드로 퀴즈정답 받기
				if(quiz_result.equals("quit")) {
					System.out.println("작성이 취소되었습니다.");
					return -1;
				}
				
				// 3. 퀴즈 정답 부연설명
				System.out.println("퀴즈 정답 부연설명(취소:quit): ");
				String quiz_plus = scanner.nextLine();		// 키보드로 퀴즈정답 부연설명 받기
				if(quiz_plus.equals("quit")) {
					System.out.println("작성이 취소되었습니다.");
					return -1;
				}
			// 6. Oracle DB에 테이터를 삽입 코드를 실행할 준비
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setString(1, quiz_content);		// 위 insertSql에 첫번째?에 키보드로 입력받은 제목을 세팅
			pstmt.setString(2, quiz_result);	// 위 insertSql에 두번째?에 키보드로 입력받은 제목을 세팅
			pstmt.setString(3, quiz_plus);
			
			// 7. 실제로 sql코드를 실행
			resultRows = pstmt.executeUpdate();			// executeUpdate로 insert, update, delete sql코드로 실행
			
			System.out.println("퀴즈가 성공적으로 등록되었습니다.");
			}
		}catch (SQLException se) {
			System.out.println("게시글 삽입하는 도중 에러가 발생 -> " + se.getMessage());
		} finally {
			// 7. Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, null);
		}
				
		return resultRows;
	}
	
	/*
	 * 설명 : 퀴즈 수정
	 */
	public int updateQuiz() {
		
		String update  = "";
		while(true) {
			System.out.println("수정하고자 하는 퀴즈 정답을 입력하세요: ");
			try {
				update = scanner.nextLine();
				break;
			} catch (InputMismatchException | NumberFormatException e) {
				System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("알 수 없는 오류 -> " + e.getMessage());
			}
		}
		
		Connection conn = DBManager.getDBConnection();
		
		String countSql = "SELECT COUNT(*) FROM quiz WHERE quiz_result = ?";
		
		String nowSql = """
				SELECT quiz_content, quiz_result, quiz_plus FROM quiz
				WHERE quiz_result = ?
				""";
		int rowCount = 0;
			try{
				pstmt = conn.prepareStatement(countSql);
				pstmt.setString(1,update);
				rs = pstmt.executeQuery(); 			// select 실행
				
				if (rs.next()) {
					rowCount = rs.getInt(1); 		// count(*)값 가져오기
					
					if(rowCount == 0) {					// 상세조회할 게시글이 없는 번호
						System.out.println("선택하신 퀴즈 정답이 없습니다.");
						return -1;
					}
				}
				pstmt = conn.prepareStatement(nowSql);
				pstmt.setString(1,update);
				rs = pstmt.executeQuery();
				
				// 선택한 게시판 현재 내용 출력화면
				while(rs.next()) {	
					System.out.println("------------------------------------------------------------");
					System.out.println("선택하신 퀴즈 현재 내용입니다.");
					System.out.println("퀴즈 문제 내용: " + rs.getString(1));
					System.out.println("퀴즈 정답: " + rs.getString(2));
					System.out.println("퀴즈 정답 부연설명: " + rs.getString(3));
					System.out.println("------------------------------------------------------------");
				}
			} catch(SQLException se) {
				System.out.println("수정하려고 선택하신 퀴즈를 출력하는 도중 에러가 발생했습니다. -> " + se.getMessage());
				
			}
			
			System.out.println("수정하실 문제 내용을 입력해 주세요.(취소:quit) :");
			String updateQuiz_content = scanner.nextLine();
			if(updateQuiz_content.equals("quit")) {
				System.out.println("작성이 취소되었습니다.");
				return -1;
			}
			
			System.out.println("수정하실 퀴즈 정답을 입력해 주세요.(취소:quit) :");
			String updateQuiz_result = scanner.nextLine();
			if(updateQuiz_result.equals("quit")) {
				System.out.println("작성이 취소되었습니다.");
				return -1;
			}
			
			System.out.println("수정하실 퀴즈 정답 부연설명을 입력해 주세요.(취소:quit) :");
			String updateQuiz_plus = scanner.nextLine();
			if(updateQuiz_plus.equals("quit")) {
				System.out.println("작성이 취소되었습니다.");
				return -1;
			}
			
			// 게시글 특정번호 수정 sql
			String updateSql = """
					Update quiz SET 
					quiz_content = ?, quiz_result = ?, quiz_plus = ?
					WHERE quiz_result = ?
					"""	;
			int resultRows = 0;
			try{
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setString(4,update);
				pstmt.setString(1, updateQuiz_content);
				pstmt.setString(2, updateQuiz_result);
				pstmt.setString(3, updateQuiz_plus);
				
				resultRows= pstmt.executeUpdate();
				
				System.out.println("------------------------------------------------------------");
				System.out.println("수정하신 퀴즈 내용 및 정답 입니다.");
				System.out.println("퀴즈 내용: " + updateQuiz_content);
				System.out.println("퀴즈 정답: " + updateQuiz_result);
				System.out.println("퀴즈 부연설명: " + updateQuiz_plus);
				System.out.println("------------------------------------------------------------");
				
			} catch(SQLException se) {
				System.out.println("퀴즈를 수정하는 도중 에러가 발생했습니다. -> " + se.getMessage());
			} 
			finally {
			// Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, rs);
			}
			return resultRows;
		}
	/*
	 * 설명: 게시글 삭제
	 */
	
	public void deleteQuiz() {
		String result  = "";
		while(true) {
			System.out.println("삭제하고자 하는 퀴즈 정답을 입력하세요: ");
			try {
				result = scanner.nextLine();
				break;
			} catch (InputMismatchException | NumberFormatException e) {
				System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("알 수 없는 오류 -> " + e.getMessage());
			}
		}
		
		System.out.println("정말 " + result + " 퀴즈을 삭제하시려면 'Y'를 입력하세요.");
		String confirmYN = scanner.nextLine();
		if (!confirmYN.equals("Y"))
			return;
		
		// 1. 게시글의 상세 데이터를 받기 위해 oracle db접속 준비
		Connection conn = DBManager.getDBConnection();
		
		String countSql = "SELECT COUNT(*) FROM quiz WHERE quiz_result = ?";	
		
		// 2. 게시글 특정 번호 삭제 sql
		String deleteSql = """
				DELETE FROM quiz WHERE seq = ?
				"""	;
		
		
		try{
			pstmt = conn.prepareStatement(countSql);
			pstmt.setString(1,result);
			rs = pstmt.executeQuery(); 			// select 실행
			
			if (rs.next()) {
				int rowCount = rs.getInt(1); 		// count(*)값 가져오기
				
				if(rowCount == 0) {					// 삭제할 퀴즈가 없는 번호
					System.out.println("삭제할 퀴즈가 없습니다.");
					return;
				}
			}
			
			pstmt = conn.prepareStatement(deleteSql);
			pstmt.setString(1,result);
			pstmt.executeUpdate();			// delete 실행
			System.out.println(result + " 퀴즈 삭제가 완료되었숩니다.");
			
		} catch(SQLException se) {
			System.out.println("퀴즈 삭제하는 도중 에러가 발생했습니다. -> " + se.getMessage());			
		} finally {
			// Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, rs);
		}
		return;
	}
}
