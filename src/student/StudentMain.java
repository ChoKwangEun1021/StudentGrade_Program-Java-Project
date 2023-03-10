package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentMain {
	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, PRINT = 2, ANLYZE = 3, SEARCH = 4, UPDATE = 5, SORT = 6, DELETE = 7, EXIT = 8;

	public static void main(String[] args) {
		// 지역변수선언
		boolean run = true;
		int no = 0;
		DBConnection dbCon = new DBConnection();
		// 무한루트
		while (run) {
			System.out.println("================================= 성적처리 프로그램 =================================");
			System.out.println("[1]입력 [2]출력 [3]분석 [4]검색 [5]수정 [6]정렬(성적순) [7]삭제 [8]종료");
			System.out.println("=================================================================================");
			System.out.print("선택 >> ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				// 생성자를 선택 해야됨. 이름,나이,국어,영어,수학
				Student student = inputDataStudent();
				// 데이터베이스 입력
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("삽입성공");
				} else {
					System.out.println("삽입실패");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list2 == null) {
					System.out.println("선택실패");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				// 분석: 이름, 나이, 총점, 평균, 등급
				ArrayList<Student> list3 = dbCon.analizeSelect();
				if (list3 == null) {
					System.out.println("선택실패");
				} else {
					printAnalyze(list3);
				}
				break;
			case SEARCH:
				// 학생검색 이름받기
				String dataName = searchStudent();
				ArrayList<Student> list4 = dbCon.nameSearchSelect(dataName);
				if (list4.size() >= 1) {
					printStudent(list4);
				} else {
					System.out.println("학생이름 검색 오류");
				}
				break;
			case UPDATE:
				// 학생검색 점수를 수정해서 저장
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.out.println("수정오류 발생");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}

				if (updateReturnValue == 1) {
					System.out.println("update 성공");
				} else {
					System.out.println("update 실패");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.out.println("정렬 실패");
				} else {
					printScoreSort(list5);
				}
//				Collections.sort(list);
				break;
			case DELETE:
				// 학생검색
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("삭제 성공");
				} else {
					System.out.println("삭제 실패");
				}
				break;
			case EXIT:
				run = false;
				break;
			}
		} // end of while

		System.out.println("프로그램 종료합니다.");
	}// end of main

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID 입력(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("ID 입력 오류");
			}
		}
		return id;
	}

	private static void printScoreSort(ArrayList<Student> list) {
		Collections.sort(list, Collections.reverseOrder());
		System.out.println("순위" + "\t" + "ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "국어" + "\t" + "영어" + "\t" + "수학"
				+ "\t" + "총점" + "\t" + "평균" + "\t" + "등급");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + "등\t" + list.get(i));
		}
	}

	private static Student updateStudent(Student student) {
		int kor = inputScoreSubject(student.getName(), "국어", student.getKor());
		student.setKor(kor);
		int eng = inputScoreSubject(student.getName(), "영어", student.getEng());
		student.setEng(eng);
		int math = inputScoreSubject(student.getName(), "수학", student.getMath());
		student.setMath(math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		System.out.println("ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "국어" + "\t" + "영어" + "\t" + "수학" + "\t" + "총점"
				+ "\t" + "평균" + "\t" + "등급");
		System.out.println(student);
		return student;
	}

	private static int inputScoreSubject(String subject, String name, int score) {
		boolean run = true;
		int data = 0;
		while (run) {
			System.out.print(name + " " + subject + " " + score + "점 >>");
			try {
				data = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(data));
				if (matcher.find() && data < 101 && data >= 0) {
					run = false;
				} else {
					System.out.println("점수를 잘못입력하였습니다 재입력바랍니다.");
				}
			} catch (NumberFormatException e) {
				System.out.println("점수 입력에 오류 발생");
				data = 0;
			}
		}
		return data;
	}

	private static String matchingNamePattern() {
		String name = null;
		while (true) {
			try {
				System.out.print("검색할 학생이름: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("이름입력오류발생 다시재입력재입력바랍니다.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("입력에서 오류가 발생했습니다.");
				break;
			}

		}
		return name;
	}

	private static String searchStudent() {
		String name = null;
		name = matchingNamePattern();
		return name;
	}

	private static void printAnalyze(ArrayList<Student> list) {
		System.out.println("ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "총점" + "\t" + "평균" + "\t" + "등급");
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("ID" + "\t" + "이 름" + "\t" + "나이" + "\t" + "국어" + "\t" + "영어" + "\t" + "수학" + "\t" + "총점"
				+ "\t" + "평균" + "\t" + "등급");
		for (Student data : list) {
			System.out.println(data);
		}
	}

	private static Student inputDataStudent() {
		String name = StudentMain.randomName();
		int age = inputAge();
		int kor = inputScore("국어");
		int eng = inputScore("영어");
		int math = inputScore("수학");
		Student student = new Student(name, age, kor, eng, math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		return student;
	}

	private static int inputScore(String subject) {
		int score = 0;
		while (true) {
			try {
				System.out.print(subject + "점수 입력 : ");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.out.println("점수를 잘못입력하였습니다 재입력바랍니다");
				}
			} catch (NumberFormatException e) {
				System.out.println("입력오류발생했습니다.");
				score = 0;
				break;
			}
		}
		return score;
	}

	private static int inputAge() {
		int age = 0;
		while (true) {
			try {
				System.out.print("나이 입력 : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.out.println("나이를 잘못입력하였습니다 재입력바랍니다");
				}
			} catch (NumberFormatException e) {
				System.out.println("나이입력 오류발생");
				age = 0;
				break;
			}
		}
		return age;
	}

	public static String randomName() {
		boolean run = true;
		String first = null;
		String second = null;
		String third = null;
		String name1 = null;
		while (run) {
			try {
				String[] firstName = new String[] { "김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서",
						"신", "권", "황", "안", "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심",
						"노", "정", "하", "곽", "성", "차", "주", "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채",
						"원", "천", "방", "공", "강", "현", "함", "변", "염", "양", "변", "여", "추", "노", "도", "소", "신", "석", "선",
						"설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금", "옥", "육", "인", "맹", "제", "모",
						"장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용" };
				String secondname[] = new String[] { "가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나",
						"남", "노", "누", "다", "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만",
						"명", "무", "문", "미", "민", "바", "박", "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석",
						"선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순", "숭", "슬", "승", "시", "신", "아", "안", "애", "엄",
						"여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위", "유", "윤", "율", "으", "은", "의",
						"이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준", "중", "지", "진",
						"찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
						"혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량",
						"린", "을", "비", "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령",
						"섬", "들", "견", "추", "걸", "삼", "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란",
						"더", "손", "술", "훔", "반", "빈", "실", "직", "흠", "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학",
						"개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련" };
				first = firstName[(int) (Math.random() * (firstName.length - 1 - 0 + 1) + 0)];
				second = secondname[(int) (Math.random() * (secondname.length - 1 - 0 + 1) + 0)];
				third = secondname[(int) (Math.random() * (secondname.length - 1 - 0 + 1) + 0)];
				name1 = first + second + third;
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(name1);
				if (!matcher.find()) {
					System.out.println("이름입력오류발생 재입력바랍니다.");
				} else {
					run = false;
				}
			} catch (Exception e) {
				System.out.println("이름입력 오류발생");
				name1 = null;
				run = false;
			}
		} // end of while
		return name1;
	}
}
