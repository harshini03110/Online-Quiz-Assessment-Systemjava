import java.io.*;
import java.util.*;
class onlineQA {
    static Scanner sc = new Scanner(System.in);
    static String fname = "quizfile.txt";
    // simple question class
    static class QuestionData {
        String questionText;
        String options[];
        int correct;
        String level;
        QuestionData(String q, String op[], int c, String l) {
            questionText = q;
            options = op;
            correct = c;
            level = l;
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to my Quiz System");
        while (true) {
            System.out.println("\n1 -> Admin");
            System.out.println("2 -> Attempt Quiz");
            System.out.println("3 -> Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt();
            sc.nextLine();
            if (ch == 1) {
                addQ();
            } else if (ch == 2) {
                playQuiz();
            } else {
                System.out.println("Closing program...");
                break;
            }
        }
    }
    // ADMIN FUNCTION
    static void addQ() {
        try {
            File f = new File(fname);
            if (!f.exists()) {
                f.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            System.out.print("Enter number of Qs: ");
            int num = sc.nextInt();
            sc.nextLine();
            for (int i = 0; i < num; i++) {
                System.out.println("\nType question:");
                String q = sc.nextLine();
                String op[] = new String[4];
                for (int j = 0; j < 4; j++) {
                    System.out.print("Option " + (j + 1) + ": ");
                    op[j] = sc.nextLine();
                }
                System.out.print("Correct answer index: ");
                int ans = sc.nextInt();
                sc.nextLine();
                System.out.print("Level: ");
                String lvl = sc.nextLine();
                // different storage pattern
                String line = q + "@@" + op[0] + "@@" + op[1] + "@@" +
                              op[2] + "@@" + op[3] + "@@" + ans + "@@" + lvl;
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            System.out.println("Done adding questions");
        } catch (Exception e) {
            System.out.println("Issue while saving data");
        }
    }
    // LOAD QUESTIONS
    static ArrayList<QuestionData> getQ() {
        ArrayList<QuestionData> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            String line;
            while ((line = br.readLine()) != null) {
                String arr[] = line.split("@@");
                String op[] = new String[4];
                op[0] = arr[1];
                op[1] = arr[2];
                op[2] = arr[3];
                op[3] = arr[4];
                QuestionData q = new QuestionData(arr[0], op,
                        Integer.parseInt(arr[5]), arr[6]);
                list.add(q);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("No file data found");
        }
        return list;
    }
    // QUIZ FUNCTION
    static void playQuiz() {
        ArrayList<QuestionData> data = getQ();
        if (data.size() == 0) {
            System.out.println("No questions available");
            return;
        }
        Collections.shuffle(data);
        int marks = 0;
        int right = 0;
        System.out.println("\nQuiz started... All the best");
        long t1 = System.currentTimeMillis();
        for (QuestionData q : data) {
            System.out.println("\n" + q.questionText);
            for (int i = 0; i < 4; i++) {
                System.out.println((i + 1) + ") " + q.options[i]);
            }
            System.out.print("Enter answer: ");
            int user = sc.nextInt();
            if (user == q.correct) {
                right++;
                if (q.level.equalsIgnoreCase("hard")) {
                    marks += 5;
                } else if (q.level.equalsIgnoreCase("medium")) {
                    marks += 2;
                } else {
                    marks += 1;
                }
            } else {
                System.out.println("Wrong! correct was option " + q.correct);
            }
        }
        long t2 = System.currentTimeMillis();
        int sec = (int) ((t2 - t1) / 1000);
        result(marks, data.size(), right, sec);
    }
    // RESULT DISPLAY
    static void result(int marks, int total, int right, int time) {
        double per = (right * 100.0) / total;
        System.out.println("\nRESULT");
        System.out.println("Marks: " + marks);
        System.out.println("Correct answers: " + right + "/" + total);
        System.out.println("Percentage: " + per + "%");
        System.out.println("Time: " + time + " sec");
        if (per > 85) {
            System.out.println("Excellent work!");
        } else if (per > 60) {
            System.out.println("Good, but can improve");
        } else {
            System.out.println("Try again, need practice");
        }

        if (time < total * 8) {
            System.out.println("You answered quickly");
        } else {
            System.out.println("Time management needed");
        }
        System.out.println("End");
    }
}