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
		// Áö¿ªº¯¼ö¼±¾ğ
		boolean run = true;
		int no = 0;
		DBConnection dbCon = new DBConnection();
		// ¹«ÇÑ·çÆ®
		while (run) {
			System.out.println("================================= ¼ºÀûÃ³¸® ÇÁ·Î±×·¥ =================================");
			System.out.println("[1]ÀÔ·Â [2]Ãâ·Â [3]ºĞ¼® [4]°Ë»ö [5]¼öÁ¤ [6]Á¤·Ä(¼ºÀû¼ø) [7]»èÁ¦ [8]Á¾·á");
			System.out.println("=================================================================================");
			System.out.print("¼±ÅÃ >> ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				// »ı¼ºÀÚ¸¦ ¼±ÅÃ ÇØ¾ßµÊ. ÀÌ¸§,³ªÀÌ,±¹¾î,¿µ¾î,¼öÇĞ
				Student student = inputDataStudent();
				// µ¥ÀÌÅÍº£ÀÌ½º ÀÔ·Â
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("»ğÀÔ¼º°ø");
				} else {
					System.out.println("»ğÀÔ½ÇÆĞ");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list2 == null) {
					System.out.println("¼±ÅÃ½ÇÆĞ");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				// ºĞ¼®: ÀÌ¸§, ³ªÀÌ, ÃÑÁ¡, Æò±Õ, µî±Ş
				ArrayList<Student> list3 = dbCon.analizeSelect();
				if (list3 == null) {
					System.out.println("¼±ÅÃ½ÇÆĞ");
				} else {
					printAnalyze(list3);
				}
				break;
			case SEARCH:
				// ÇĞ»ı°Ë»ö ÀÌ¸§¹Ş±â
				String dataName = searchStudent();
				ArrayList<Student> list4 = dbCon.nameSearchSelect(dataName);
				if (list4.size() >= 1) {
					printStudent(list4);
				} else {
					System.out.println("ÇĞ»ıÀÌ¸§ °Ë»ö ¿À·ù");
				}
				break;
			case UPDATE:
				// ÇĞ»ı°Ë»ö Á¡¼ö¸¦ ¼öÁ¤ÇØ¼­ ÀúÀå
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.out.println("¼öÁ¤¿À·ù ¹ß»ı");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}

				if (updateReturnValue == 1) {
					System.out.println("update ¼º°ø");
				} else {
					System.out.println("update ½ÇÆĞ");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.out.println("Á¤·Ä ½ÇÆĞ");
				} else {
					printScoreSort(list5);
				}
//				Collections.sort(list);
				break;
			case DELETE:
				// ÇĞ»ı°Ë»ö
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("»èÁ¦ ¼º°ø");
				} else {
					System.out.println("»èÁ¦ ½ÇÆĞ");
				}
				break;
			case EXIT:
				run = false;
				break;
			}
		} // end of while

		System.out.println("ÇÁ·Î±×·¥ Á¾·áÇÕ´Ï´Ù.");
	}// end of main

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID ÀÔ·Â(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("ID ÀÔ·Â ¿À·ù");
			}
		}
		return id;
	}

	private static void printScoreSort(ArrayList<Student> list) {
		Collections.sort(list, Collections.reverseOrder());
		System.out.println("¼øÀ§" + "\t" + "ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "±¹¾î" + "\t" + "¿µ¾î" + "\t" + "¼öÇĞ"
				+ "\t" + "ÃÑÁ¡" + "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + "µî\t" + list.get(i));
		}
	}

	private static Student updateStudent(Student student) {
		int kor = inputScoreSubject(student.getName(), "±¹¾î", student.getKor());
		student.setKor(kor);
		int eng = inputScoreSubject(student.getName(), "¿µ¾î", student.getEng());
		student.setEng(eng);
		int math = inputScoreSubject(student.getName(), "¼öÇĞ", student.getMath());
		student.setMath(math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		System.out.println("ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "±¹¾î" + "\t" + "¿µ¾î" + "\t" + "¼öÇĞ" + "\t" + "ÃÑÁ¡"
				+ "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		System.out.println(student);
		return student;
	}

	private static int inputScoreSubject(String subject, String name, int score) {
		boolean run = true;
		int data = 0;
		while (run) {
			System.out.print(name + " " + subject + " " + score + "Á¡ >>");
			try {
				data = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(data));
				if (matcher.find() && data < 101 && data >= 0) {
					run = false;
				} else {
					System.out.println("Á¡¼ö¸¦ Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù ÀçÀÔ·Â¹Ù¶ø´Ï´Ù.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Á¡¼ö ÀÔ·Â¿¡ ¿À·ù ¹ß»ı");
				data = 0;
			}
		}
		return data;
	}

	private static String matchingNamePattern() {
		String name = null;
		while (true) {
			try {
				System.out.print("°Ë»öÇÒ ÇĞ»ıÀÌ¸§: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("ÀÌ¸§ÀÔ·Â¿À·ù¹ß»ı ´Ù½ÃÀçÀÔ·ÂÀçÀÔ·Â¹Ù¶ø´Ï´Ù.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("ÀÔ·Â¿¡¼­ ¿À·ù°¡ ¹ß»ıÇß½À´Ï´Ù.");
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
		System.out.println("ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "ÃÑÁ¡" + "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("ID" + "\t" + "ÀÌ ¸§" + "\t" + "³ªÀÌ" + "\t" + "±¹¾î" + "\t" + "¿µ¾î" + "\t" + "¼öÇĞ" + "\t" + "ÃÑÁ¡"
				+ "\t" + "Æò±Õ" + "\t" + "µî±Ş");
		for (Student data : list) {
			System.out.println(data);
		}
	}

	private static Student inputDataStudent() {
		String name = StudentMain.randomName();
		int age = inputAge();
		int kor = inputScore("±¹¾î");
		int eng = inputScore("¿µ¾î");
		int math = inputScore("¼öÇĞ");
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
				System.out.print(subject + "Á¡¼ö ÀÔ·Â : ");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.out.println("Á¡¼ö¸¦ Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù ÀçÀÔ·Â¹Ù¶ø´Ï´Ù");
				}
			} catch (NumberFormatException e) {
				System.out.println("ÀÔ·Â¿À·ù¹ß»ıÇß½À´Ï´Ù.");
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
				System.out.print("³ªÀÌ ÀÔ·Â : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.out.println("³ªÀÌ¸¦ Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù ÀçÀÔ·Â¹Ù¶ø´Ï´Ù");
				}
			} catch (NumberFormatException e) {
				System.out.println("³ªÀÌÀÔ·Â ¿À·ù¹ß»ı");
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
				String[] firstName = new String[] { "±è", "ÀÌ", "¹Ú", "ÃÖ", "Á¤", "°­", "Á¶", "À±", "Àå", "ÀÓ", "ÇÑ", "¿À", "¼­",
						"½Å", "±Ç", "È²", "¾È", "¼Û", "·ù", "Àü", "È«", "°í", "¹®", "¾ç", "¼Õ", "¹è", "Á¶", "¹é", "Çã", "À¯", "³²", "½É",
						"³ë", "Á¤", "ÇÏ", "°û", "¼º", "Â÷", "ÁÖ", "¿ì", "±¸", "½Å", "ÀÓ", "³ª", "Àü", "¹Î", "À¯", "Áø", "Áö", "¾ö", "Ã¤",
						"¿ø", "Ãµ", "¹æ", "°ø", "°­", "Çö", "ÇÔ", "º¯", "¿°", "¾ç", "º¯", "¿©", "Ãß", "³ë", "µµ", "¼Ò", "½Å", "¼®", "¼±",
						"¼³", "¸¶", "±æ", "ÁÖ", "¿¬", "¹æ", "À§", "Ç¥", "¸í", "±â", "¹İ", "¿Õ", "±İ", "¿Á", "À°", "ÀÎ", "¸Í", "Á¦", "¸ğ",
						"Àå", "³²", "Å¹", "±¹", "¿©", "Áø", "¾î", "Àº", "Æí", "±¸", "¿ë" };
				String secondname[] = new String[] { "°¡", "°­", "°Ç", "°æ", "°í", "°ü", "±¤", "±¸", "±Ô", "±Ù", "±â", "±æ", "³ª",
						"³²", "³ë", "´©", "´Ù", "´Ü", "´Ş", "´ã", "´ë", "´ö", "µµ", "µ¿", "µÎ", "¶ó", "·¡", "·Î", "·ç", "¸®", "¸¶", "¸¸",
						"¸í", "¹«", "¹®", "¹Ì", "¹Î", "¹Ù", "¹Ú", "¹é", "¹ü", "º°", "º´", "º¸", "ºû", "»ç", "»ê", "»ó", "»õ", "¼­", "¼®",
						"¼±", "¼³", "¼·", "¼º", "¼¼", "¼Ò", "¼Ö", "¼ö", "¼÷", "¼ø", "¼ş", "½½", "½Â", "½Ã", "½Å", "¾Æ", "¾È", "¾Ö", "¾ö",
						"¿©", "¿¬", "¿µ", "¿¹", "¿À", "¿Á", "¿Ï", "¿ä", "¿ë", "¿ì", "¿ø", "¿ù", "À§", "À¯", "À±", "À²", "À¸", "Àº", "ÀÇ",
						"ÀÌ", "ÀÍ", "ÀÎ", "ÀÏ", "ÀÙ", "ÀÚ", "ÀÜ", "Àå", "Àç", "Àü", "Á¤", "Á¦", "Á¶", "Á¾", "ÁÖ", "ÁØ", "Áß", "Áö", "Áø",
						"Âù", "Ã¢", "Ã¤", "Ãµ", "Ã¶", "ÃÊ", "Ãá", "Ãæ", "Ä¡", "Å½", "ÅÂ", "ÅÃ", "ÆÇ", "ÇÏ", "ÇÑ", "ÇØ", "Çõ", "Çö", "Çü",
						"Çı", "È£", "È«", "È­", "È¯", "È¸", "È¿", "ÈÆ", "ÈÖ", "Èñ", "¿î", "¸ğ", "¹è", "ºÎ", "¸²", "ºÀ", "È¥", "È²", "·®",
						"¸°", "À»", "ºñ", "¼Ø", "°ø", "¸é", "Å¹", "¿Â", "µğ", "Ç×", "ÈÄ", "·Á", "±Õ", "¹¬", "¼Û", "¿í", "ÈŞ", "¾ğ", "·É",
						"¼¶", "µé", "°ß", "Ãß", "°É", "»ï", "¿­", "¿õ", "ºĞ", "º¯", "¾ç", "Ãâ", "Å¸", "Èï", "°â", "°ï", "¹ø", "½Ä", "¶õ",
						"´õ", "¼Õ", "¼ú", "ÈÉ", "¹İ", "ºó", "½Ç", "Á÷", "Èì", "Èç", "¾Ç", "¶÷", "¶ä", "±Ç", "º¹", "½É", "Çå", "¿±", "ÇĞ",
						"°³", "·Õ", "Æò", "´Ã", "´Ì", "¶û", "¾á", "Çâ", "¿ï", "·Ã" };
				first = firstName[(int) (Math.random() * (firstName.length - 1 - 0 + 1) + 0)];
				second = secondname[(int) (Math.random() * (secondname.length - 1 - 0 + 1) + 0)];
				third = secondname[(int) (Math.random() * (secondname.length - 1 - 0 + 1) + 0)];
				name1 = first + second + third;
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{2,4}$");
				Matcher matcher = pattern.matcher(name1);
				if (!matcher.find()) {
					System.out.println("ÀÌ¸§ÀÔ·Â¿À·ù¹ß»ı ÀçÀÔ·Â¹Ù¶ø´Ï´Ù.");
				} else {
					run = false;
				}
			} catch (Exception e) {
				System.out.println("ÀÌ¸§ÀÔ·Â ¿À·ù¹ß»ı");
				name1 = null;
				run = false;
			}
		} // end of while
		return name1;
	}
}
