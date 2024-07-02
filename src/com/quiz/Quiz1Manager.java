package com.quiz;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Quiz1Manager {
	Scanner scanner = new Scanner(System.in);
	Quiz1ManagerService quiz1ManagerService = new Quiz1ManagerService();
	/*
	 * 설명 : 관리자 모드
	 */
	public void QuizManager() {
		while(true) {
			try {
				System.out.println("퀴즈 관리자 모드입니다."); 
				System.out.print("(1.퀴즈 목록 2.퀴즈 추가 3.퀴즈 수정 4.퀴즈 삭제 5.이전화면)>> ");
				
				int managerNo = scanner.nextInt();
				System.out.println("선택된 번호 -> " + managerNo);
				// 1. 선택번호 입력받기
				if (managerNo < 1 || managerNo > 5) {
					System.out.println("잘못된 번호입니다. 1부터 4사이의 숫자를 입력해 주세요.");
					continue;
				}
				switch(managerNo) {
					case 1:		// 퀴즈 목록
						System.out.println("(퀴즈 목록)");
						quiz1ManagerService.selectAllQuiz();
						break;
					case 2:		// 퀴즈 추가
						System.out.println("(퀴즈 추가)");
						quiz1ManagerService.insertQuiz();
						break;
					case 3:		// 퀴브 수정
						System.out.println("(퀴즈 수정)");
						quiz1ManagerService.updateQuiz();
						break;
					case 4:		// 퀴즈 삭제
						System.out.println("(퀴즈 삭제)");
						quiz1ManagerService.deleteQuiz();
						break;
				}
				if(managerNo == 5) {
					System.out.println("(이전화면)");
					return;
				}
				
			} catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해 주세요.");
                scanner.nextLine(); // 잘못된 입력 처리
            } catch (Exception e) {
                System.out.println("예기치 않은 오류가 발생했습니다. 다시 시도해 주세요.");
                e.printStackTrace();
            }
		}
				
	}
		
}
