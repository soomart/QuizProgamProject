package com.member;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.quiz.Quiz1Project;
import com.quiz.utils.DBManager;

public class Member1Service {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Scanner scanner = new Scanner(System.in);
	/*
	 * 설명: 회원가입 
	 */
	public int insertMember1() {
		Connection conn = DBManager.getDBConnection();
		
		String checkIdSql = "SELECT COUNT(*) FROM member1 WHERE member_id = ?";
		
		String member1Sql = """
				INSERT INTO member1 
				(seq, member_id, member_pw, create_date, 
				name, gender, birthday, email1, email2, phone, address, quiz_count)
				VALUES (seq_member1_no.nextval, ?, ?, sysdate, ?, ?, ?, ?, ?, ?, ?,0)
				"""; 
		String selectSql ="""
				SELECT * FROM member1 WHERE member_id = ?
				""";
		System.out.println("회원가입을 진행하겠습니다.");
		
		int resultRows = 0;
		try {
			String member_id ="";
			while(true) {
				// 아이디 생성
				System.out.println("아이디를 입력바랍니다.<취소: quit>: ");
				member_id = scanner.nextLine();
			if(member_id.equals("quit")) {
				System.out.println("회원가입이 취소되었습니다.");
				return -1;
			}
			// 아이디 중복 확인 쿼리
            pstmt = conn.prepareStatement(checkIdSql);
            pstmt.setString(1, member_id);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("아이디가 중복됩니다. 다시 입력바랍니다.");
            } else {
                break; // 중복이 아니면 루프 탈출
            }
		}	
			// 비밀번호 생성
			System.out.println("비밀번호를 입력바랍니다.<취소: quit>: ");
			String member_pw = scanner.nextLine();
			if(member_pw.equals("quit")) {
				System.out.println("회원가입이 취소되었습니다.");
				return -1;
			}
			
			// 이름 생성
			System.out.println("이름을 입력바랍니다.<취소: quit>: ");
			String name = scanner.nextLine();
			if(name.equals("quit")) {
				System.out.println("회원가입이 취소되었습니다.");
				return -1;
			}
			
			// 성별 생성
			String gender = "";
			while(true) {	
				System.out.println("성별 입력바랍니다('남자' 또는 '여자).<취소: quit>: ");
				gender = scanner.nextLine();
				if(gender.equals("quit")) {
					System.out.println("회원가입이 취소되었습니다.");
					return -1;
				} else if (gender.equals("남자") || gender.equals("여자")) {
					
					break;
				} else {
					System.out.println("성별을 '남자' 또는 '여자'로 입력바랍니다. ");
					continue;
				}
		
			}
			
			// 생일 생성
			String birthday = "";
			int birthday1 = 0;
			while(true) {
				System.out.println("생년월일 6자리 입력바랍니다.<취소: quit>: ");
				try {
					birthday = scanner.nextLine();
					if(birthday.equals("quit")) { 
						System.out.println("회원가입이 취소되었습니다.");
						return -1;			
					}else {
						birthday1 = Integer.parseInt(birthday);
						if(birthday1 >991231) {
							System.out.println("정확한 생년월일 6자리를 입력바랍니다.");
							continue;
						} else {
							break;
						}
					}
				} catch (InputMismatchException | NumberFormatException e) {
					System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("알 수 없는 오류 -> " + e.getMessage());
				}
			}
			
			// 이메일1 생성
			System.out.println("이메일 앞부분(@전)을 입력바랍니다.<취소: quit>: ");
			String email1 = scanner.nextLine();
			if(email1.equals("quit")) {
				System.out.println("회원가입이 취소되었습니다.");
				return -1;
			}
			// 이메일2 생성
			String email2 = "";
			System.out.println("이메일 뒷부분(@뒤)을 naver.com, daum.net, gmail.com 중에서 입력바랍니다.<취소: quit>: ");
			while(true) {
				email2 = scanner.nextLine();
				if(email2.equals("quit")) {
					System.out.println("회원가입이 취소되었습니다.");
					return -1;
				} else if (email2.equals("naver.com") 
						||email2.equals("daum.net") ||email2.equals("gamil.com")) {
					break;
				} else {
					System.out.println("이메일 뒷부분을 naver.com, daum.net, gmail.com 중에서 다시 입력 바랍니다.<취소: quit>: ");
					continue;
				}
			}
			
			// 생일 생성
			int phone = 0;
			while(true) {
			System.out.println("전화번호를 010제외하고 8자리만 입력해주세요.<취소: quit>: ");
			try {
			String phone1 = scanner.nextLine();
			if(phone1.equals("quit")) {
				System.out.println("회원가입이 취소되었습니다.");
				return -1;
				}else {
					phone = Integer.parseInt(phone1);
					if(phone > 99999999) {
						System.out.println("전화번호를 010제외하고 8자리를 정확하게 입력바랍니다.");
						continue;
					} else {
						break;
					}
				}
			
			}catch (InputMismatchException | NumberFormatException e) {
				System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("알 수 없는 오류 -> " + e.getMessage());
			}
			}
			
			
			// 주소 생성
			System.out.println("주소를 입력바랍니다.<취소: quit>: ");
			String address = scanner.nextLine();
			if(address.equals("quit")) {
				System.out.println("회원가입이 취소되었습니다.");
				return -1;
			}
			
			
			pstmt = conn.prepareStatement(member1Sql);
			pstmt.setString(1, member_id);
			pstmt.setString(2, member_pw);
			pstmt.setString(3, name);
			pstmt.setString(4, gender);
			pstmt.setInt(5, birthday1);
			pstmt.setString(6, email1);
			pstmt.setString(7, email2);
			pstmt.setInt(8, phone);
			pstmt.setString(9, address);
			resultRows= pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				System.out.println("회원가입이 완료되었습니다. 로그인 진행 후 시작해주세요.");
				System.out.println("아이디: " + rs.getString("member_id"));
				System.out.println("비밀번호: " + rs.getString("member_pw"));
				System.out.println("이름: " + rs.getString("name"));
				System.out.println("성별: " + rs.getString("gender"));
				System.out.println("생년월일: " + rs.getString("birthday"));
				System.out.println("이메일: " + rs.getString("email1") + 
									"@" + rs.getString("email2"));
				System.out.println("전화번호: " + rs.getString("phone"));
				System.out.println("주소: " + rs.getString("address"));
				System.out.println("가입일자: " + rs.getDate("create_date"));
			}
			
		}catch(SQLException se) {
			System.out.println("회원가입하는 도중 에러가 발생했습니다. -> " + se.getMessage());	
		}finally {
			// Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, rs);
			}
		return resultRows;
	}
	
	public void LoginMember() {
		System.out.println("<<로그인화면>>");
		
		Connection conn = DBManager.getDBConnection();
		
		String checkIdSql = """
				SELECT member_pw FROM member1 WHERE member_id = ?
				""";
	
		// 아이디 중복 확인 쿼리
		try {
			String member_id ="";
			while(true) {
				// 아이디 생성
				System.out.println("아이디를 입력해주세요.<취소: quit>: ");
				member_id = scanner.nextLine();
				if(member_id.equals("quit")) {
					System.out.println("로그인이 취소되었습니다.");
					return;
				}
				// 아이디 중복 확인 쿼리
	            pstmt = conn.prepareStatement(checkIdSql);
	            pstmt.setString(1, member_id);	        
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	            	//아이디가 존재하면 비밀번호 입력
	            	System.out.println("비밀번호를 입력해주세요.<취소: quit>: ");
	            	String member_pw = scanner.nextLine();
	            	if(member_pw.equals("quit")) {
						System.out.println("로그인이 취소되었습니다.");
						return;
	            	}
	            	//비밀번호 확인
	            	String password = rs.getString("member_pw");
	            	if(password.equals(member_pw)) {
	            		System.out.println("로그인 성공");
	            		Quiz1Project quiz1Project = new Quiz1Project();
	            		quiz1Project.startQuizProject(member_id); // 로그인 성공 시 member_id 전달
	                    return; // 퀴즈 프로그램 실행 후 로그인 메소드 종료
	            	} else {
	            		System.out.println("비밀번호가 틀렸습니다. 다시 시도해주세요.");
	            	}
	            	break;
	            }else {
	            	System.out.println("아이디가 존재하지 않습니다. 다시 시도해주세요.");
	            }
			}
		}catch(SQLException se) {
		System.out.println("로그인 시도 중 에러가 발생했습니다. -> " + se.getMessage());	
		}finally {
		// Oracle DB 접속 관련 객체를 정리
		DBManager.DBClose(conn, pstmt, rs);
		}
	}
	
	public void deleteMember() {
		
		// 1. oracle db접속 준비
		Connection conn = DBManager.getDBConnection();
						
		String countSql = "SELECT COUNT(*) FROM member1 WHERE member_id = ?";	
						
		String checkIdSql = """
				SELECT member_pw FROM member1 WHERE member_id = ?
				""";
		
		// 2. 회원 삭제 sql
		String deleteSql = """
						DELETE FROM member1 WHERE member_id = ?
						"""	;

		String member_id = "";	
		try {
			while(true) {
				// 아이디 생성
				System.out.println("탈퇴할 아이디를 입력해주세요.<취소: quit>: ");
				member_id = scanner.nextLine();
				if(member_id.equals("quit")) {
					System.out.println("회원탈퇴가 취소되었습니다.");
					return;
				}
				// 아이디 중복 확인 쿼리
	            pstmt = conn.prepareStatement(checkIdSql);
	            pstmt.setString(1, member_id);	        
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	            	//아이디가 존재하면 비밀번호 입력
	            	System.out.println("입력하신 아이디의 비밀번호를 입력해주세요.<취소: quit>: ");
	            	String member_pw = scanner.nextLine();
	            	if(member_pw.equals("quit")) {
						System.out.println("회원탈퇴가 취소되었습니다.");
						return;
	            	}
	            	//비밀번호 확인
	            	String password = rs.getString("member_pw");
	            	if(password.equals(member_pw)) {
	            		System.out.println("비밀번호가 일치합니다. 탈퇴를 계속 진행합니다.");
	            		 break;
	                     
	            	} else {
	            		System.out.println("비밀번호가 틀렸습니다. 다시 시도해주세요.");
	            		deleteMember();
	            	}
	            }else {
	            	System.out.println("아이디가 존재하지 않습니다. 다시 시도해주세요.");
	            }
			}
			} catch (InputMismatchException | NumberFormatException e) {
				System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("알 수 없는 오류 -> " + e.getMessage());
		}
		
			System.out.println("정말 " + member_id + "을 삭제하시려면 'Y'를 입력바랍니다/"
								+ "아이디 삭제 시 랭킹에서도 삭제됩니다."
								);
			String confirmYN = scanner.nextLine();
			if (!confirmYN.equals("Y")) {
				return;
			}
			
			try {
				pstmt = conn.prepareStatement(countSql);
				pstmt.setString(1,member_id);
				rs = pstmt.executeQuery(); 			// select 실행	// delete 실행
				if(rs.next()) {
				int rowCount = rs.getInt(1);
				}
				pstmt = conn.prepareStatement(deleteSql);
				pstmt.setString(1,member_id);
				pstmt.executeUpdate();
				System.out.println( member_id + "님 삭제가 완료되었숩니다. 그 동안 이용해주셔서 감사합니다.");
					
		 
		}catch(SQLException se) {
			System.out.println("아이디 삭제하는 도중 에러가 발생했습니다. -> " + se.getMessage());			
		} finally {
			// Oracle DB 접속 관련 객체를 정리
			DBManager.DBClose(conn, pstmt, rs);
		}
	}
	
	public void updateMember() {
		Connection conn = DBManager.getDBConnection();
		
		String checkIdSql = """
				SELECT member_pw FROM member1 WHERE member_id = ?
				""";
		
		String countSql = "SELECT COUNT(*) FROM member1 WHERE member_id = ?";
						
		String selectSql = "SELECT * FROM member1 WHERE member_id =?";
		
		String updateSql="""
				UPDATE member1 SET
				member_id = ?, member_pw = ?, name = ?, gender = ?, birthday = ?, email1 = ?, email2 = ?, phone = ?, address = ?
				WHERE member_id = ?
				""";
		
		String member_id = "";
		String nowId = "";
		String nowPw = "";
		String nowName = "";
		String nowGender = "";
		String nowBirthday = "";
		String nowEmail1 = "";
		String nowEmail2 = "";
		String nowPhone = "";
		String nowAddress = "";
		int nowBirthday1 = 0;
		int nowPhone1 = 0;
		
		try {
			while(true) {
				// 아이디 입력
				System.out.println("회원정보 수정을 위해 아이디를 입력해주세요.<취소: quit>: ");
				member_id = scanner.nextLine();
				if(member_id.equals("quit")) {
					System.out.println("회원수정이 취소되었습니다.");
					return;
				}
				// 아이디 중복 확인 쿼리
	            pstmt = conn.prepareStatement(checkIdSql);
	            pstmt.setString(1, member_id);	        
	            rs = pstmt.executeQuery();
	            

	            if (rs.next()) {
	            	//아이디가 존재하면 비밀번호 입력
	            	System.out.println("입력하신 아이디의 비밀번호를 입력해주세요.<취소: quit>: ");
	            	String member_pw = scanner.nextLine();
	            	if(member_pw.equals("quit")) {
						System.out.println("회원수정이 취소되었습니다.");
						return;
	            	}	    
	            	//비밀번호 확인
	            	String password = rs.getString("member_pw");
	            	if(password.equals(member_pw)) {
	            		System.out.println("비밀번호가 일치합니다. 회원 수정을 계속 진행합니다.");
	            		 break;
	                     
	            	} else {
	            		System.out.println("비밀번호가 틀렸습니다. 다시 시도해주세요.");
	            		deleteMember();
	            	}
	            }else {
	            	System.out.println("아이디가 존재하지 않습니다. 다시 시도해주세요.");
	            }
			}
			} catch (InputMismatchException | NumberFormatException e) {
				System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("알 수 없는 오류 -> " + e.getMessage());
			}
			try{
				pstmt = conn.prepareStatement(selectSql);
				pstmt.setString(1,member_id);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					nowId = rs.getString("member_id");
					nowPw = rs.getString("member_pw");
					nowName = rs.getString("name");
					nowGender = rs.getString("gender");
					nowBirthday = rs.getString("birthday");
					nowEmail1 = rs.getString("email1");
					nowEmail2 = rs.getString("email2");
					nowPhone = rs.getString("phone");
					nowAddress = rs.getString("address");
					
					System.out.println("-------------------------------------------------");
					System.out.println("아이디와 비밀번호가 확인되었습니다. 현재 회원 정보입니다.");
					System.out.println("아이디: " + nowId );
					System.out.println("비밀번호: " + nowPw);
					System.out.println("이름: " + nowName);
					System.out.println("성별: " + nowGender);
					System.out.println("생년월일: " + nowBirthday);
					System.out.println("이메일: " + nowEmail1 + 
										"@" + nowEmail2);
					System.out.println("전화번호: " + nowPhone);
					System.out.println("주소: " + nowAddress);
					System.out.println("가입일자: " + rs.getString("create_date"));
					System.out.println("-------------------------------------------------");
					
					nowBirthday1 = Integer.parseInt(nowBirthday);
					nowPhone1 = Integer.parseInt(nowPhone);
					
				}
			} catch(SQLException se) {
			System.out.println("회원 수정하려고 입력하신 아이디를 처리하는 도중 에러가 발생했습니다. -> " + se.getMessage());
			}
			
			
			String updateMember = "";
			String updateId ="";
			String updatePw = "";
			String updateName = "";
			String updateGender = ""; 
			String updateBirthday = "";
			String updateEmail1= ""; 
			String updateEmail2 = "";
			String updatePhone = "";
			String updateAddress = "";
			int birthday = 0;
			int phone = 0;
			
			while(true) {			
				try {
				System.out.println("수정하실 정보을 입력해 주세요.(취소:quit) :");
				updateMember = scanner.nextLine();
				if(updateMember.equals("quit")) {
					System.out.println("작성이 취소되었습니다.");
					return ;
				} else if(updateMember.equals("아이디") || updateMember.equals("비밀번호") || updateMember.equals("이름") ||
						updateMember.equals("성별") || updateMember.equals("생년월일") || updateMember.equals("이메일") || 
						updateMember.equals("전화번호") || updateMember.equals("주소")
						) {
						break;
				} else {
					System.out.println("아이디, 비밀번호, 이름, 성별, 생년월일, 이메일, 전화번호, 주소 중 입력바랍니다.");
					continue;
				}
				}catch (InputMismatchException e) {
	                System.out.println("잘못된 입력입니다. 숫자를 입력해 주세요.");
	                scanner.nextLine(); // 잘못된 입력 처리
	            } catch (Exception e) {
	                System.out.println("예기치 않은 오류가 발생했습니다. 다시 시도해 주세요.");
	                e.printStackTrace();
	            }}
			
			while(true) {
				try {
				switch(updateMember) {
					case "아이디":
						System.out.println("[아이디 수정]");	
						System.out.println("현재 아이디는" + nowId + " 입니다." + "\n"
											+ "수정하시려는 아이디를 입력해주세요.<취소: quit>: ");		
						while(true) {
						updateId = scanner.nextLine();
						if(updateId.equals("quit")) {
							System.out.println("수정이 취소되었습니다.");
							return;
						}
						// 아이디 중복 확인 쿼리
			            pstmt = conn.prepareStatement(countSql);
			            pstmt.setString(1, updateId);	        
			            rs = pstmt.executeQuery();
						
			            if (rs.next() && rs.getInt(1) > 0) {
			                System.out.println("아이디가 중복됩니다. 다시 입력바랍니다.: ");
			            } else {
			                break; // 중복이 아니면 루프 탈출
			            }
						}
				
						break;
					case "비밀번호":
						System.out.println("[비밀번호 수정]");						
						System.out.println("현재 비밀번호는" + nowPw + " 입니다." + "\n"
								+ "수정하시려는 비밀번호를 입력해주세요.<취소: quit>: ");
						updatePw = scanner.nextLine();
						if(updatePw.equals("quit")) {
							System.out.println("수정이 취소되었습니다.");
							return;
						}
						break;
					case "이름":
						System.out.println("[이름 수정]");					
						System.out.println("현재 이름은 " + nowName + " 입니다." + "\n"
								+ "수정하시려는 이름을 입력해주세요.<취소: quit>: ");
						updateName = scanner.nextLine();
						if(updateName.equals("quit")) {
							System.out.println("수정이 취소되었습니다.");
							return;
						}
						break;
					case "성별":
						System.out.println("[성별 수정]");
						while(true) {
						System.out.println("현재 성별은 " + nowGender + " 입니다." + "\n"
								+ "수정하시려는 성별을 '남자' 또는 '여자' 입력해주세요.<취소: quit>: ");
						updateGender = scanner.nextLine();
						if(updateGender.equals("quit")) {
							System.out.println("수정이 취소되었습니다.");
							return;
						} else if(updateGender.equals("남자") || updateGender.equals("여자")) {
							break;
						} else {
							System.out.println("성별을 '남자' 또는 '여자'로 입력바랍니다.");
							continue;
							}
						}
						break;
					case "생년월일":
						System.out.println("[생년월일 수정]");
						System.out.println("현재 생년월일은 " + nowBirthday + " 입니다." + "\n"
								+ "수정하시려는 생년월일을 주민등록번호 앞에 6자리로 입력해주세요.<취소: quit>: ");
						try {
							updateBirthday = scanner.nextLine();
							if(updateBirthday.equals("quit")) {
								System.out.println("수정이 취소되었습니다.");
								return;
							} else {
								birthday = Integer.parseInt(updateBirthday);
								if(birthday > 991231) {
									System.out.println("주민등록번호 앞에 6자리를 정확하게 입력바랍니다.");
									continue;
								} else {
									break;
								}
							}
						} catch (InputMismatchException | NumberFormatException e) {
							System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
						} catch(Exception e) {
							e.printStackTrace();
							System.out.println("알 수 없는 오류 -> " + e.getMessage());
						}
						break;
					case "이메일":
						System.out.println("[이메일 수정]");					
						System.out.println("현재 이메일은 " + nowEmail1 +"@" + nowEmail2 + " 입니다." + "\n"
								+ "수정하시려는 이메일 앞부분(@ 앞)을 입력해주세요.<취소: quit>: ");
						updateEmail1 = scanner.nextLine();
						if(updateEmail1.equals("quit")) {
							System.out.println("수정이 취소되었습니다.");
							return;
						}
						while(true) {
						System.out.println("수정하시려는 이메일 뒷부분(@ 뒤)을 'naver.com', 'daum.net', 'gmail.com' 중에서 입력해주세요.<취소: quit>: ");
						updateEmail2 = scanner.nextLine();
						if(updateEmail2.equals("quit")) {
							System.out.println("수정이 취소되었습니다.");
							return;
						} else if(updateEmail2.equals("naver.com") 
								||updateEmail2.equals("daum.net") ||updateEmail2.equals("gamil.com")) {
								break;
						} else {
							System.out.println("이메일 뒷부분(@ 뒤)을 'naver.com', 'daum.net', 'gmail.com' 중에서 입력바랍니다.");
							continue;
						}
						}
						break;
					case "전화번호":
						System.out.println("[전화번호 수정]");
						while(true) {
						System.out.println("현재 전화번호는 " + nowPhone + " 입니다." + "\n"
								+ "수정하시려는 전화번호를 010제외하고 8자리만 입력해주세요.<취소: quit>: ");
						try {
							updatePhone = scanner.nextLine();
							if(updatePhone.equals("quit")) {
								System.out.println("수정이 취소되었습니다.");
								return;
							} else {
								phone = Integer.parseInt(updatePhone);
								if(phone > 99999999) {
									System.out.println("전화번호를 010제외하고 8자리를 정확하게 입력바랍니다.");
									continue;
								} else {
									break;
								}
							}
						}catch (InputMismatchException | NumberFormatException e) {
							System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요. ");
						} catch(Exception e) {
							e.printStackTrace();
							System.out.println("알 수 없는 오류 -> " + e.getMessage());
						}
						}
						break;
					case "주소":
						System.out.println("[주소 수정]");			
						System.out.println("현재 주소는 " + nowAddress + " 입니다." + "\n"
								+ "수정하시려는 주소를 입력해주세요.<취소: quit>: ");
						updateAddress = scanner.nextLine();
						if(updateAddress.equals("quit")) {
							System.out.println("수정이 취소되었습니다.");
							return;
						}
						break;
				}
					pstmt = conn.prepareStatement(updateSql);
					pstmt.setString(10, member_id);
					pstmt.setString(1, updateId.isEmpty() ? nowId :updateId);
					pstmt.setString(2, updatePw.isEmpty() ? nowPw :updatePw);
					pstmt.setString(3, updateName.isEmpty() ? nowName :updateName);
					pstmt.setString(4, updateGender.isEmpty() ? nowGender :updateGender);
					pstmt.setInt(5, updateBirthday.isEmpty() ? nowBirthday1 :Integer.parseInt(updateBirthday));
					pstmt.setString(6, updateEmail1.isEmpty() ? nowEmail1 :updateEmail1);
					pstmt.setString(7, updateEmail2.isEmpty() ? nowEmail2 :updateEmail2);
					pstmt.setInt(8, updatePhone.isEmpty() ? nowPhone1 :Integer.parseInt(updatePhone));
					pstmt.setString(9, updateAddress.isEmpty() ? nowAddress :updateAddress);
					
					int updatedRows = pstmt.executeUpdate();
			        if (updatedRows > 0) {
			            System.out.println("회원 정보가 성공적으로 업데이트되었습니다.");
			            pstmt = conn.prepareStatement(selectSql);
						pstmt.setString(1, updateId.isEmpty() ? nowId : updateId);
						rs = pstmt.executeQuery();
						if(rs.next()) {
							System.out.println("회원정보 수정이 완료되었습니다. 로그인 후 퀴즈프로그램 실행바랍니다.");
							System.out.println("아이디: " + rs.getString("member_id"));
							System.out.println("비밀번호: " + rs.getString("member_pw"));
							System.out.println("이름: " + rs.getString("name"));
							System.out.println("성별: " + rs.getString("gender"));
							System.out.println("생년월일: " + rs.getString("birthday"));
							System.out.println("이메일: " + rs.getString("email1") + 
												"@" + rs.getString("email2"));
							System.out.println("전화번호: " + "010" + rs.getString("phone"));
							System.out.println("주소: " + rs.getString("address"));
							System.out.println("가입일자: " + rs.getDate("create_date"));
							System.out.println("수정일자: " + Date.valueOf(LocalDate.now()));
							break;
						}
			        } else {
			            System.out.println("회원 정보 업데이트에 실패했습니다.");
			            break;
			        }
					
				
					
				}catch (InputMismatchException | NumberFormatException e) {
	                System.out.println("잘못된 입력입니다.");
	                scanner.nextLine(); // 잘못된 입력 처리
	            } catch (Exception e) {
	                System.out.println("예기치 않은 오류가 발생했습니다. 다시 시도해 주세요.");
	                e.printStackTrace();
	                break;
	            } finally {
					// Oracle DB 접속 관련 객체를 정리
					DBManager.DBClose(conn, pstmt, rs);
				}
			}
			
		
	}		
			
}

