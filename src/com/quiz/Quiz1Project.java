package com.quiz;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Quiz1Project {
	
	Quiz1Service quiz1Service = new Quiz1Service();	// 퀴즈 프로그램 접근 및 제어 객체 생성
	Quiz1Manager quiz1Manager = new Quiz1Manager();	// 퀴즈 관리자모드 프로그램 접근 및 제어 객체 생성
	Scanner scanner = new Scanner(System.in);		// 키보드 입력받는 객체
	
	
	
	String member_id = ""; // member_id를 저장하는 인스턴스 변수 추가
	
		public void startQuizProject(String member_id) {
			this.member_id = member_id; // 로그인 성공 시 전달받은 member_id 저장
			System.out.println("[ " + member_id + "님 환영합니다. ]");
			quiz1Menu();
		}
	
	public void quiz1Menu() {
		
		
		// 0. 퀴즈 프로그램 대기를 위해 무한루트 코드 작성
		
		while(true) {
			
			try {
				
				
				// 퀴즈프로그램 제목
				System.out.println("               <<넌센스퀴즈 프로그램>>");
				// 퀴즈프로그램 메뉴
				System.out.print("(1.퀴즈 시작  2.퀴즈 설명  3.퀴즈 랭킹  4.로그아웃(초기화면으로 이동))>> ");
				
				// 1. 메뉴 선택번호 입력받기
				int menuNumber = scanner.nextInt();
				System.out.println("선택된 번호 -> " + menuNumber);
				// 1~4 범위 내의 숫자인지 확인
				if (menuNumber < 0 || menuNumber > 5) {
					System.out.println("잘못된 번호입니다. 1부터 5사이의 숫자를 입력해 주세요.");
					continue;
				}
				switch(menuNumber) {
					case 1:		// 퀴브 시작
						System.out.println("(퀴즈 시작)");
						quiz1Service.startQuizService(member_id);
						break;
					case 2:		// 퀴즈 설명
						System.out.println("(퀴즈 설명)");
						quiz1Service.Quizmenual();
						break;
					case 3:		// 퀴브 랭킹
						System.out.println("(퀴즈 랭킹)");
						quiz1Service.rankQuiz();
						break;
					case 4:
						System.out.println("(로그아웃(초기화면으로 이동))");
						return;
					case 0:
						System.out.println("(관리자 모드)");
						quiz1Manager.QuizManager();
						break;
				}
				
			} catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해 주세요.");
                scanner.nextLine(); // 잘못된 입력 처리
            } catch (Exception e) {
                System.out.println("예기치 않은 오류가 발생했습니다. 다시 시도해 주세요.");
                e.printStackTrace();
                break;
            } 
		}
		//프로그램 종료 표시
		System.out.println("퀴즈 프로그램 종료");
		
	}
}

