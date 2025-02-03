package dsaproject;

import java.util.*;

class Exam {
    String subject;
    int totalMarks;
    int duration;
    String timing;
    String date;
    String classroom;
    Map<String, Integer> studentMarks;

    public Exam(String subject) {
        this.subject = subject;
        this.studentMarks = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Exam{" +
                "subject='" + subject + '\'' +
                ", totalMarks=" + totalMarks +
                ", duration=" + duration +
                ", timing='" + timing + '\'' +
                ", date='" + date + '\'' +
                ", classroom='" + classroom + '\'' +
                '}';
    }
}

public class ExamHallManagementSystem {
    static List<Exam> exams = new ArrayList<>();
    static Map<String, Map<String, List<String>>> classRooms = new HashMap<>();
    static Set<String> sickStudents = new HashSet<>();
    static final String ADMIN_PASSWORD = "admin";
    static final String FACULTY_PASSWORD = "faculty";
    static final String SICK_CLASSROOM = "SickRoom";
    static Map<String, List<String>> classStudents = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Admin");
            System.out.println("2. Faculty");
            System.out.println("3. Student");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter admin password: ");
                    String adminPassword = scanner.next();
                    if (adminPassword.equals(ADMIN_PASSWORD)) {
                        adminMenu(scanner);
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;
                case 2:
                    System.out.print("Enter faculty password: ");
                    String facultyPassword = scanner.next();
                    if (facultyPassword.equals(FACULTY_PASSWORD)) {
                        facultyMenu(scanner);
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;
                case 3:
                    studentMenu(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
        scanner.close();
    }

    public static void adminMenu(Scanner scanner) {
        int adminChoice;
        do {
            System.out.println("Admin Menu:");
            System.out.println("1. Create Exams");
            System.out.println("2. Delete Exam");
            System.out.println("3. Schedule Exam");
            System.out.println("4. Allot Classroom");
            System.out.println("5. Reallot Sick Students");
            System.out.println("6. View Seating Arrangement");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            adminChoice = scanner.nextInt();

            switch (adminChoice) {
                case 1:
                    createExamsByAdmin(scanner);
                    break;
                case 2:
                    deleteExam(scanner);
                    break;
                case 3:
                    scheduleExam(scanner);
                    break;
                case 4:
                    allotClassroom(scanner);
                    break;
                case 5:
                    reallotSickStudents();
                    break;
                case 6:
                    viewSeatingArrangement();
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (adminChoice != 7);
    }

    public static void facultyMenu(Scanner scanner) {
        int facultyChoice;
        do {
            System.out.println("Faculty Menu:");
            System.out.println("1. Allot Duration and Marks");
            System.out.println("2. Back to Main Menu");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            facultyChoice = scanner.nextInt();

            switch (facultyChoice) {
                case 1:
                    allotDurationAndMarks(scanner);
                    break;
                case 2:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (facultyChoice != 2);
    }

    public static void studentMenu(Scanner scanner) {
        System.out.println("Student Menu:");
        System.out.println("Available Classes:");
        for (String className : classStudents.keySet()) {
            System.out.println("- " + className);
        }
        System.out.print("Enter your class: ");
        String studentClass = scanner.next();
        System.out.print("Enter your roll number: ");
        String rollNumber = scanner.next();

        boolean scheduledForExam = false;
        for (Exam exam : exams) {
            if (exam.classroom != null && classRooms.containsKey(exam.classroom) && classRooms.get(exam.classroom).containsKey(studentClass) && classRooms.get(exam.classroom).get(studentClass).contains(rollNumber)) {
                System.out.println("You are scheduled for the " + exam.subject + " exam in classroom " + exam.classroom + " at " + exam.timing + " on " + exam.date);
                scheduledForExam = true;
            }
        }

        if (!scheduledForExam) {
            System.out.println("You are not scheduled for any exams.");
        }

        System.out.print("Are you well? (yes/no): ");
        String wellStatus = scanner.next();

        if ("no".equalsIgnoreCase(wellStatus)) {
            System.out.println("Select the issue you are having:");
            System.out.println("1. Cough");
            System.out.println("2. Cold");
            System.out.println("3. Fever");
            System.out.println("4. Body Pains");
            System.out.println("5. Other");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            int healthIssue = scanner.nextInt();

            String issue;
            switch (healthIssue) {
                case 1:
                    issue = "Cough";
                    break;
                case 2:
                    issue = "Cold";
                    break;
                case 3:
                    issue = "Fever";
                    break;
                case 4:
                    issue = "Body Pains";
                    break;
                case 5:
                    System.out.print("Specify other issue: ");
                    issue = scanner.next();
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to 'Other'.");
                    issue = "Other";
            }
            sickStudents.add(rollNumber + ": " + issue);
            System.out.println("You have been marked as sick with issue: " + issue);
            allotSickClassroom(rollNumber, studentClass);
        } else {
            System.out.println("You have been marked as well.");
        }
    }

    public static void allotSickClassroom(String rollNumber, String studentClass) {
        Map<String, List<String>> sickClassRooms = classRooms.getOrDefault(SICK_CLASSROOM, new HashMap<>());
        List<String> sickStudentsList = sickClassRooms.getOrDefault(studentClass, new ArrayList<>());
        sickStudentsList.add(rollNumber);
        sickClassRooms.put(studentClass, sickStudentsList);
        classRooms.put(SICK_CLASSROOM, sickClassRooms);
        System.out.println("You have been allotted to the sick classroom: " + SICK_CLASSROOM);
    }

    public static void createExamsByAdmin(Scanner scanner) {
        System.out.print("Enter number of classes having exams: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int numClasses = scanner.nextInt();

        for (int i = 0; i < numClasses; i++) {
            System.out.print("Enter class name: ");
            String className = scanner.next();
            classStudents.putIfAbsent(className, new ArrayList<>());

            System.out.print("Enter number of exams for class " + className + ": ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            int numExams = scanner.nextInt();

            for (int j = 0; j < numExams; j++) {
                System.out.println("Exam " + (j + 1) + " for class " + className + ":");
                System.out.print("Enter subject: ");
                String subject = scanner.next();
                Exam exam = new Exam(subject);
                exams.add(exam);
                System.out.println("Exam created successfully:");
                System.out.println(exam);
            }
        }
    }

    public static void scheduleExam(Scanner scanner) {
        System.out.println("Schedule Exam:");
        if (exams.isEmpty()) {
            System.out.println("No exams have been created yet. Please create exams first.");
            return;
        }
        for (int i = 0; i < exams.size(); i++) {
            System.out.println((i + 1) + ". " + exams.get(i));
        }
        System.out.print("Enter exam number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int examNumber = scanner.nextInt();

        if (examNumber < 1 || examNumber > exams.size()) {
            System.out.println("Invalid exam number.");
            return;
        }

        Exam exam = exams.get(examNumber - 1);
        System.out.print("Enter date of the exam (YYYY-MM-DD): ");
        exam.date = scanner.next();
        System.out.print("Enter timing of the exam (HH:MM): ");
        exam.timing = scanner.next();

        System.out.println("Exam scheduled successfully.");
        System.out.println(exam);
    }

    public static void allotDurationAndMarks(Scanner scanner) {
        System.out.println("Allot Duration and Marks:");
        if (exams.isEmpty()) {
            System.out.println("No exams have been created yet. Please create exams first.");
            return;
        }
        for (int i = 0; i < exams.size(); i++) {
            System.out.println((i + 1) + ". " + exams.get(i));
        }
        System.out.print("Enter exam number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int examNumber = scanner.nextInt();

        if (examNumber < 1 || examNumber > exams.size()) {
            System.out.println("Invalid exam number.");
            return;
        }

        Exam exam = exams.get(examNumber - 1);
        System.out.print("Enter duration of the exam in hours: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        exam.duration = scanner.nextInt();
        System.out.print("Enter total marks of the exam: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        exam.totalMarks = scanner.nextInt();

        System.out.println("Duration and marks allotted successfully.");
        System.out.println(exam);
    }

    public static void deleteExam(Scanner scanner) {
        System.out.println("Delete Exam:");
        if (exams.isEmpty()) {
            System.out.println("No exams have been created yet.");
            return;
        }
        for (int i = 0; i < exams.size(); i++) {
            System.out.println((i + 1) + ". " + exams.get(i));
        }
        System.out.print("Enter exam number to delete: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int examNumber = scanner.nextInt();

        if (examNumber < 1 || examNumber > exams.size()) {
            System.out.println("Invalid exam number.");
            return;
        }

        exams.remove(examNumber - 1);
        System.out.println("Exam deleted successfully.");
    }

    public static void allotClassroom(Scanner scanner) {
        System.out.println("Allot Classroom:");
        if (exams.isEmpty()) {
            System.out.println("No exams have been created yet. Please create exams first.");
            return;
        }
        for (int i = 0; i < exams.size(); i++) {
            System.out.println((i + 1) + ". " + exams.get(i));
        }
        System.out.print("Enter exam number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int examNumber = scanner.nextInt();

        if (examNumber < 1 || examNumber > exams.size()) {
            System.out.println("Invalid exam number.");
            return;
        }

        Exam exam = exams.get(examNumber - 1);
        System.out.print("Enter classroom: ");
        exam.classroom = scanner.next();
        System.out.print("Enter class name: ");
        String className = scanner.next();

        Map<String, List<String>> classRoom = classRooms.getOrDefault(exam.classroom, new HashMap<>());
        classRoom.putIfAbsent(className, new ArrayList<>());

        System.out.print("Enter starting roll number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int startRollNumber = scanner.nextInt();

        System.out.print("Enter ending roll number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int endRollNumber = scanner.nextInt();

        for (int i = startRollNumber; i <= endRollNumber; i++) {
            String rollNumber = String.valueOf(i);
            if (!sickStudents.contains(rollNumber)) {
                classRoom.get(className).add(rollNumber);
                classStudents.get(className).add(rollNumber);
            } else {
                System.out.println("Student with roll number " + rollNumber + " is sick and cannot be allotted to this classroom.");
            }
        }
        classRooms.put(exam.classroom, classRoom);

        System.out.println("Classroom allotted successfully.");
        System.out.println(exam);
    }

    public static void reallotSickStudents() {
        System.out.println("Reallot Sick Students:");
        if (sickStudents.isEmpty()) {
            System.out.println("No sick students to reallot.");
            return;
        }

        Map<String, List<String>> sickClassRooms = classRooms.getOrDefault(SICK_CLASSROOM, new HashMap<>());
        for (String sickStudent : sickStudents) {
            String[] parts = sickStudent.split(": ");
            String rollNumber = parts[0];
            for (Map.Entry<String, Map<String, List<String>>> entry : classRooms.entrySet()) {
                Map<String, List<String>> classRoom = entry.getValue();
                for (Map.Entry<String, List<String>> classEntry : classRoom.entrySet()) {
                    if (classEntry.getValue().contains(rollNumber)) {
                        String className = classEntry.getKey();
                        List<String> sickStudentsList = sickClassRooms.getOrDefault(className, new ArrayList<>());
                        sickStudentsList.add(rollNumber);
                        sickClassRooms.put(className, sickStudentsList);
                        classEntry.getValue().remove(rollNumber);
                    }
                }
            }
        }
        classRooms.put(SICK_CLASSROOM, sickClassRooms);
        System.out.println("Sick students reallotted to " + SICK_CLASSROOM + " successfully.");
    }

    public static void viewSeatingArrangement() {
        System.out.println("Seating Arrangement:");
        if (classRooms.isEmpty()) {
            System.out.println("No classrooms have been allotted.");
            return;
        }

        for (Map.Entry<String, Map<String, List<String>>> entry : classRooms.entrySet()) {
            String classroom = entry.getKey();
            Map<String, List<String>> classRoom = entry.getValue();
            System.out.println("Classroom: " + classroom);

            List<Pair> studentsList = new ArrayList<>();
            for (Map.Entry<String, List<String>> classEntry : classRoom.entrySet()) 
            {
                String className = classEntry.getKey();
                List<String> students = classEntry.getValue();
                System.out.println("Class: " + className);
                if (students.isEmpty()) {
                    System.out.println("No students allotted.");
                }
                for (String student : students) {
                    studentsList.add(new Pair(className, className + student));
                }
            }

            if (studentsList.isEmpty()) {
                System.out.println("No students allotted.");
                continue;
            }

//            // Sort studentsList to alternate students from different classes
//            studentsList.sort(Comparator.comparing(Pair::getClassName));
            studentsList.sort((p1, p2) -> {
                // Compare class names alternately
                int result = p1.getClassName().compareTo(p2.getClassName());
                if (result == 0) {
                    // If class names are the same, reverse the order
                    result = -1;
                }
                return result;
            });
  

            // Arrange students in the seating matrix
            int totalSeats = studentsList.size();
            int rows = (int) Math.ceil(Math.sqrt(totalSeats));
            int cols = rows;

            Pair[][] seatingMatrix = new Pair[rows][cols];
            for (int i = 0; i < rows; i++) {
                Arrays.fill(seatingMatrix[i], new Pair(" ", " "));
            }

            int row = 0, col = 0;
            for (int i = 0; i < totalSeats; i++) {
                while (!seatingMatrix[row][col].student.equals(" ")) {
                    col++;
                    if (col == cols) {
                        col = 0;
                        row++;
                    }
                }
                seatingMatrix[row][col] = studentsList.get(i);
                col++;
                if (col == cols) {
                    col = 0;
                    row++;
                }
            }

            // Display the seating arrangement
            for (Pair[] seatingRow : seatingMatrix) {
                for (Pair seat : seatingRow) {
                    System.out.print((seat.student.equals(" ") ? " " : seat.student) + " ");
                }
                System.out.println();
            }
        }
    }

    static class Pair {
        String className;
        String student;

        Pair(String className, String student) {
            this.className = className;
            this.student = student;
        }

        public String getClassName() {
            return className;
        }

        public String getStudent() {
            return student;
        }
    }
} 
