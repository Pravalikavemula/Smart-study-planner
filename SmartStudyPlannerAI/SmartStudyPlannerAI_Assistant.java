import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Subject {
    String name;
    String difficulty;
    double weight;

    public Subject(String name, String difficulty) {
        this.name = name;
        this.difficulty = difficulty;
        this.weight = getWeight(difficulty);
    }

    private double getWeight(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "hard": return 2.0;
            case "medium": return 1.5;
            case "easy": return 1.0;
            default: return 1.0;
        }
    }
}

class QuoteGenerator {
    private static final String[] quotes = {
        "Consistency is more important than intensity.",
        "Don’t watch the clock; do what it does. Keep going.",
        "Push yourself, because no one else will do it for you.",
        "Focus on progress, not perfection.",
        "Discipline today means success tomorrow.",
        "Small steps daily lead to big results.",
        "Dream big, work hard, stay humble."
    };

    public static String getQuote() {
        Random rand = new Random();
        return quotes[rand.nextInt(quotes.length)];
    }
}

public class SmartStudyPlannerAI_Assistant {
    static Scanner sc = new Scanner(System.in);
    static Map<String, List<Subject>> userPlans = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(" Hello! I’m your Smart Study Planner AI Assistant.");

        System.out.print("Please enter your name: ");
        String username = sc.nextLine();
        String planFile = "plan_" + username + ".txt";
        String perfFile = "performance_" + username + ".txt";

        System.out.println("\n Hi " + username + "! How can I help you today?");
        System.out.println("You can say things like:");
        System.out.println("- create plan for 6 hours");
        System.out.println("- show motivational quote");
        System.out.println("- exit\n");

        while (true) {
            System.out.print(username + ": ");
            String input = sc.nextLine().toLowerCase();

            if (input.contains("create plan")) {
                handlePlanCreation(username, input, planFile, perfFile);
            } else if (input.contains("quote")) {
                System.out.println(" " + QuoteGenerator.getQuote());
            } else if (input.contains("exit")) {
                System.out.println(" Goodbye, " + username + "! Keep learning every day!");
                break;
            } else {
                System.out.println(" Sorry, I didn’t get that. Try: 'create plan for X hours' or 'quote'.");
            }
        }
    }

    private static void handlePlanCreation(String username, String input, String planFile, String perfFile) {
        double totalHours = extractHours(input);
        if (totalHours <= 0) {
            System.out.print("Enter total study hours manually: ");
            totalHours = sc.nextDouble(); sc.nextLine();
        }

        System.out.print("How many subjects do you want to plan for? ");
        int n = sc.nextInt(); sc.nextLine();
        List<Subject> subjects = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter subject " + (i + 1) + " name: ");
            String name = sc.nextLine();
            System.out.print("Enter difficulty (Easy / Medium / Hard): ");
            String diff = sc.nextLine();
            subjects.add(new Subject(name, diff));
        }

        userPlans.put(username, subjects);

        generatePlan(username, subjects, totalHours, planFile);
        trackPerformance(username, subjects, perfFile);
    }

    private static double extractHours(String input) {
        String[] words = input.split(" ");
        for (String w : words) {
            try {
                return Double.parseDouble(w);
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    private static void generatePlan(String username, List<Subject> subjects, double totalHours, String planFile) {
        double totalWeight = 0;
        for (Subject s : subjects) totalWeight += s.weight;

        StringBuilder plan = new StringBuilder();
        plan.append("SMART STUDY PLAN FOR ").append(username.toUpperCase()).append("\n\n");
        plan.append("Date: ").append(LocalDate.now()).append("\n");
        plan.append("Total Study Hours: ").append(totalHours).append("\n\n");

        System.out.println("\n===== GENERATED STUDY PLAN =====");
        for (Subject s : subjects) {
            double hours = (s.weight / totalWeight) * totalHours;
            plan.append(String.format("%s (%s): %.2f hours\n", s.name, s.difficulty, hours));
            plan.append(" AI Tip: ").append(QuoteGenerator.getQuote()).append("\n\n");
            System.out.printf("%s (%s): %.2f hours\n", s.name, s.difficulty, hours);
            System.out.println(" AI Tip: " + QuoteGenerator.getQuote() + "\n");
        }

        plan.append("------------------------------------------------------\n");

        try (FileWriter fw = new FileWriter(planFile)) {
            fw.write(plan.toString());
            System.out.println(" Study plan saved to " + planFile);
        } catch (IOException e) {
            System.out.println(" Error saving plan file.");
        }
    }

    private static void trackPerformance(String username, List<Subject> subjects, String perfFile) {
        System.out.println("\n===== PERFORMANCE TRACKING =====");
        try (FileWriter fw = new FileWriter(perfFile)) {
            for (Subject s : subjects) {
                System.out.print("Enter completion percentage for " + s.name + " (0-100): ");
                double percent = sc.nextDouble(); sc.nextLine();
                fw.write(s.name + "," + percent + "\n");
            }
            System.out.println(" Performance saved in " + perfFile);
        } catch (IOException e) {
            System.out.println(" Error saving performance file.");
        }
    }
}

