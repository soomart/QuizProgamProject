package com.member;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Member1 {
	public static void main(String[] args) {
		System.out.println("퀴즈 프로그램에 오신 것을 환영합니다.");
		System.out.println("로그인이나 회원가입을 진행 후 퀴즈를 시작할 수 있습니다.");
		
		Scanner scanner = new Scanner(System.in);
		Member1Service member1Service = new Member1Service();
		// 0. 퀴즈 프로그램 대기를 위해 무한루트 코드 작성
		while(true) {
			try {
				// 퀴즈프로그램 제목
				System.out.println("        <<로그인 프로그램>>");
				// 퀴즈프로그램 메뉴
				System.out.print("(1.회원가입  2.로그인  3.회원수정  4.회원탈퇴  5.종료)>> ");
				
				// 1. 메뉴 선택번호 입력받기
				int menuNumber = scanner.nextInt();
				System.out.println("선택된 번호 -> " + menuNumber);
				// 1~4 범위 내의 숫자인지 확인
				if (menuNumber < 1 || menuNumber > 5) {
					System.out.println("잘못된 번호입니다. 1부터 5사이의 숫자를 입력해 주세요.");
					continue;
				}
				switch(menuNumber) {
					case 1:		// 회원가입
						System.out.println("(회원가입)");
						member1Service.insertMember1();
						break;
					case 2:		// 로그인
						System.out.println("(로그인)");
						member1Service.LoginMember();
						break;
					case 3: 	// 회원 수정
						System.out.println("회원 수정");
						member1Service.updateMember();
						break;
					case 4:		// 회원 탈퇴
						System.out.println("(회원탈퇴)");
						member1Service.deleteMember();
						break;
				}
				if(menuNumber == 5) {
					System.out.println("(종료)");
					break;
				}
				
			} catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해 주세요.");
                scanner.nextLine(); // 잘못된 입력 처리
            } catch (Exception e) {
                System.out.println("예기치 않은 오류가 발생했습니다. 다시 시도해 주세요.");
                e.printStackTrace();
            }
		}
		//프로그램 종료 표시
		System.out.println("퀴즈 프로그램 종료");	
		
	}
}
