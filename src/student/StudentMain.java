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
		// ������������
		boolean run = true;
		int no = 0;
		DBConnection dbCon = new DBConnection();
		// ���ѷ�Ʈ
		while (run) {
			System.out.println("================================= ����ó�� ���α׷� =================================");
			System.out.println("[1]�Է� [2]��� [3]�м� [4]�˻� [5]���� [6]����(������) [7]���� [8]����");
			System.out.println("=================================================================================");
			System.out.print("���� >> ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				// �����ڸ� ���� �ؾߵ�. �̸�,����,����,����,����
				Student student = inputDataStudent();
				// �����ͺ��̽� �Է�
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("���Լ���");
				} else {
					System.out.println("���Խ���");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list2 == null) {
					System.out.println("���ý���");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				// �м�: �̸�, ����, ����, ���, ���
				ArrayList<Student> list3 = dbCon.analizeSelect();
				if (list3 == null) {
					System.out.println("���ý���");
				} else {
					printAnalyze(list3);
				}
				break;
			case SEARCH:
				// �л��˻� �̸��ޱ�
				String dataName = searchStudent();
				ArrayList<Student> list4 = dbCon.nameSearchSelect(dataName);
				if (list4.size() >= 1) {
					printStudent(list4);
				} else {
					System.out.println("�л��̸� �˻� ����");
				}
				break;
			case UPDATE:
				// �л��˻� ������ �����ؼ� ����
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.out.println("�������� �߻�");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}

				if (updateReturnValue == 1) {
					System.out.println("update ����");
				} else {
					System.out.println("update ����");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.out.println("���� ����");
				} else {
					printScoreSort(list5);
				}
//				Collections.sort(list);
				break;
			case DELETE:
				// �л��˻�
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("���� ����");
				} else {
					System.out.println("���� ����");
				}
				break;
			case EXIT:
				run = false;
				break;
			}
		} // end of while

		System.out.println("���α׷� �����մϴ�.");
	}// end of main

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID �Է�(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("ID �Է� ����");
			}
		}
		return id;
	}

	private static void printScoreSort(ArrayList<Student> list) {
		Collections.sort(list, Collections.reverseOrder());
		System.out.println("����" + "\t" + "ID" + "\t" + "�� ��" + "\t" + "����" + "\t" + "����" + "\t" + "����" + "\t" + "����"
				+ "\t" + "����" + "\t" + "���" + "\t" + "���");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + "��\t" + list.get(i));
		}
	}

	private static Student updateStudent(Student student) {
		int kor = inputScoreSubject(student.getName(), "����", student.getKor());
		student.setKor(kor);
		int eng = inputScoreSubject(student.getName(), "����", student.getEng());
		student.setEng(eng);
		int math = inputScoreSubject(student.getName(), "����", student.getMath());
		student.setMath(math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		System.out.println("ID" + "\t" + "�� ��" + "\t" + "����" + "\t" + "����" + "\t" + "����" + "\t" + "����" + "\t" + "����"
				+ "\t" + "���" + "\t" + "���");
		System.out.println(student);
		return student;
	}

	private static int inputScoreSubject(String subject, String name, int score) {
		boolean run = true;
		int data = 0;
		while (run) {
			System.out.print(name + " " + subject + " " + score + "�� >>");
			try {
				data = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(data));
				if (matcher.find() && data < 101 && data >= 0) {
					run = false;
				} else {
					System.out.println("������ �߸��Է��Ͽ����ϴ� ���Է¹ٶ��ϴ�.");
				}
			} catch (NumberFormatException e) {
				System.out.println("���� �Է¿� ���� �߻�");
				data = 0;
			}
		}
		return data;
	}

	private static String matchingNamePattern() {
		String name = null;
		while (true) {
			try {
				System.out.print("�˻��� �л��̸�: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[��-�R]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("�̸��Է¿����߻� �ٽ����Է����Է¹ٶ��ϴ�.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("�Է¿��� ������ �߻��߽��ϴ�.");
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
		System.out.println("ID" + "\t" + "�� ��" + "\t" + "����" + "\t" + "����" + "\t" + "���" + "\t" + "���");
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("ID" + "\t" + "�� ��" + "\t" + "����" + "\t" + "����" + "\t" + "����" + "\t" + "����" + "\t" + "����"
				+ "\t" + "���" + "\t" + "���");
		for (Student data : list) {
			System.out.println(data);
		}
	}

	private static Student inputDataStudent() {
		String name = StudentMain.randomName();
		int age = inputAge();
		int kor = inputScore("����");
		int eng = inputScore("����");
		int math = inputScore("����");
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
				System.out.print(subject + "���� �Է� : ");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.out.println("������ �߸��Է��Ͽ����ϴ� ���Է¹ٶ��ϴ�");
				}
			} catch (NumberFormatException e) {
				System.out.println("�Է¿����߻��߽��ϴ�.");
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
				System.out.print("���� �Է� : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.out.println("���̸� �߸��Է��Ͽ����ϴ� ���Է¹ٶ��ϴ�");
				}
			} catch (NumberFormatException e) {
				System.out.println("�����Է� �����߻�");
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
				String[] firstName = new String[] { "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "Ȳ", "��", "��", "��", "��", "ȫ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "ä",
						"��", "õ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "ǥ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "Ź", "��", "��", "��", "��", "��", "��", "��", "��" };
				String secondname[] = new String[] { "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "â", "ä", "õ", "ö", "��", "��", "��", "ġ", "Ž", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "ȣ", "ȫ", "ȭ", "ȯ", "ȸ", "ȿ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "ȥ", "Ȳ", "��",
						"��", "��", "��", "��", "��", "��", "Ź", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "Ÿ", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
						"��", "��", "��", "��", "��", "��", "��", "��", "��", "��" };
				first = firstName[(int) (Math.random() * (firstName.length - 1 - 0 + 1) + 0)];
				second = secondname[(int) (Math.random() * (secondname.length - 1 - 0 + 1) + 0)];
				third = secondname[(int) (Math.random() * (secondname.length - 1 - 0 + 1) + 0)];
				name1 = first + second + third;
				Pattern pattern = Pattern.compile("^[��-�R]{2,4}$");
				Matcher matcher = pattern.matcher(name1);
				if (!matcher.find()) {
					System.out.println("�̸��Է¿����߻� ���Է¹ٶ��ϴ�.");
				} else {
					run = false;
				}
			} catch (Exception e) {
				System.out.println("�̸��Է� �����߻�");
				name1 = null;
				run = false;
			}
		} // end of while
		return name1;
	}
}
