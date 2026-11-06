import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static final int MAX_ARRAY_SIZE = 30;
    private static RedBlackTree tree = new RedBlackTree();
    private static int[] lastGeneratedArray;
    public static void main(String[] args) {
        System.out.println("ЛАБОРАТОРНА РОБОТА №6 - ЧЕРВОНО-ЧОРНЕ ДЕРЕВО");
        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Оберіть дію: ");
            switch (choice) {
                case 1 -> addNumber();
                case 2 -> deleteNumber();
                case 3 -> showTraversals();
                case 4 -> tree.printTree();
                case 5 -> createRandomTree();
                case 6 -> createSortedTree();
                case 7 -> enterNumbers();
                case 8 -> showValidation();
                case 9 -> {
                    tree.clear();
                    System.out.println("Дерево очищено.");
                }
                case 0 -> {
                    running = false;
                    System.out.println("Роботу завершено.");
                }
                default -> System.out.println("Немає такого пункту меню. Спробуйте знову.");
            }
        }
    }
    private static void showMenu() {
        System.out.println("\n========== МЕНЮ ==========");
        System.out.println("1 — Додати одне число");
        System.out.println("2 — Видалити число");
        System.out.println("3 — Показати обходи дерева");
        System.out.println("4 — Відобразити дерево");
        System.out.println("5 — Нове дерево: випадковий порядок чисел");
        System.out.println("6 — Нове дерево: числа за зростанням");
        System.out.println("7 — Додати кілька чисел із клавіатури");
        System.out.println("8 — Перевірити властивості дерева");
        System.out.println("9 — Очистити дерево");
        System.out.println("0 — Вийти");
    }
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try { return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Помилка: введіть ціле число, наприклад 12 або -5.");
            }
        }
    }
    private static int readArraySize() {
        while (true) {
            int size = readInt("Кількість елементів (від 1 до " + MAX_ARRAY_SIZE + "): ");
            if (size >= 1 && size <= MAX_ARRAY_SIZE) return size;
            System.out.println("Кількість має бути в межах 1–" + MAX_ARRAY_SIZE + ".");
        }
    }
    private static void addNumber() {
        int value = readInt("Число для додавання: ");
        if (tree.insert(value)) {
            System.out.println("Додано: " + value);
        } else {
            System.out.println("Число " + value + " вже є у дереві. Дублікати не додаємо.");
        }
        tree.printTree();
    }
    private static void deleteNumber() {
        int value = readInt("Число для видалення: ");
        System.out.println(tree.delete(value)
                ? "Число " + value + " видалено. Баланс дерева збережено."
                : "Числа " + value + " у дереві немає.");
        tree.printTree();
    }
    private static void showTraversals() {
        if (tree.isEmpty()) {
            System.out.println("Дерево порожнє. Спочатку додайте числа.");
            return;
        }
        System.out.println("Симетричний (LNR): " + tree.inOrder());
        System.out.println("Прямий (NLR):      " + tree.preOrder());
        System.out.println("Зворотний (LRN):  " + tree.postOrder());
        System.out.println("За рівнями (BFS): " + tree.levelOrder());
    }
    private static int[] generateUniqueNumbers(int count) {
        int[] values = new int[count];
        Set<Integer> unique = new HashSet<>();
        for (int i = 0; i < count; i++) {
            int value;
            do {
                value = RANDOM.nextInt(199) - 99; // числа від -99 до +99
            } while (!unique.add(value));
            values[i] = value;
        }
        return values;
    }
    private static void createRandomTree() {
        int count = readArraySize();
        lastGeneratedArray = generateUniqueNumbers(count);
        System.out.println("Згенерований масив і порядок додавання: "
                + Arrays.toString(lastGeneratedArray));
        tree.clear();
        for (int value : lastGeneratedArray) tree.insert(value);
        tree.printTree();
        showValidation();
    }
    private static void createSortedTree() {
        if (lastGeneratedArray == null) {
            System.out.println("Попереднього масиву немає. Спочатку створимо новий.");
            lastGeneratedArray = generateUniqueNumbers(readArraySize());
        }
        int[] sorted = Arrays.copyOf(lastGeneratedArray, lastGeneratedArray.length);
        Arrays.sort(sorted);
        System.out.println("Початковий масив: " + Arrays.toString(lastGeneratedArray));
        System.out.println("Впорядкований масив і порядок додавання: " + Arrays.toString(sorted));
        tree.clear();
        for (int value : sorted) tree.insert(value);
        tree.printTree();
        showValidation();
    }

    private static void enterNumbers() {
        System.out.println("Введіть цілі числа через пробіл або кому (наприклад: 10 20 -5 15).");
        System.out.print("Числа: ");
        String line = SCANNER.nextLine().trim();
        if (line.isEmpty()) {
            System.out.println("Нічого не введено.");
            return;
        }

        String[] tokens = line.split("[\\s,;]+");
        int added = 0;
        int skipped = 0;
        for (String token : tokens) {
            try {
                int value = Integer.parseInt(token);
                if (tree.insert(value)) added++;
                else skipped++;
            } catch (NumberFormatException ex) {
                System.out.println("Пропущено некоректне значення: " + token);
                skipped++;
            }
        }
        System.out.println("Додано нових чисел: " + added + "; пропущено: " + skipped + ".");
        tree.printTree();
        showValidation();
    }
    private static void showValidation() {
        System.out.println("Перевірка властивостей червоно-чорного дерева: "
                + (tree.isValidRedBlackTree() ? "УСПІШНА" : "ПОМИЛКА"));
    }
}
