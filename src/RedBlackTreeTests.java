import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class RedBlackTreeTests {
    private static int checks = 0;

    private static void check(boolean condition, String description) {
        checks++;
        if (!condition) {
            throw new AssertionError("НЕ ПРОЙДЕНО: " + description);
        }
    }

    private static void compareWithReference(RedBlackTree tree, TreeSet<Integer> reference) {
        check(tree.isValidRedBlackTree(), "властивості ЧЧ-дерева");
        check(tree.size() == reference.size(), "кількість вузлів");
        check(tree.inOrder().equals(new ArrayList<>(reference)), "симетричний обхід");
    }

    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        TreeSet<Integer> reference = new TreeSet<>();

        compareWithReference(tree, reference);
        check(tree.isEmpty(), "початкове дерево порожнє");
        check(!tree.delete(123), "видалення неіснуючого числа");

        int[] values = {10, 20, 30, 15, 5, 1, 25, 40, -8};
        for (int value : values) {
            check(tree.insert(value), "додавання " + value);
            reference.add(value);
            compareWithReference(tree, reference);
        }
        check(!tree.insert(15), "дублікати не додаються");
        check(tree.contains(25), "пошук наявного числа");
        check(!tree.contains(99), "пошук відсутнього числа");

        for (int value : new int[]{1, 30, 10, 20, -8, 5, 15, 25, 40}) {
            check(tree.delete(value), "видалення " + value);
            reference.remove(value);
            compareWithReference(tree, reference);
        }
        check(tree.isEmpty(), "порожнє після видалення всіх чисел");

        // Вставлення вже відсортованих чисел: дерево не повинно виродитися.
        for (int i = 1; i <= 200; i++) {
            tree.insert(i);
            reference.add(i);
            compareWithReference(tree, reference);
        }
        check(tree.inOrder().size() == 200, "200 чисел у зростаючому порядку");
        tree.clear();
        reference.clear();
        compareWithReference(tree, reference);

        // Випадкові операції порівнюємо з еталонним TreeSet із Java.
        Random random = new Random(2026);
        for (int i = 0; i < 3000; i++) {
            int value = random.nextInt(501) - 250;
            if (random.nextBoolean()) {
                check(tree.insert(value) == reference.add(value), "випадкове додавання");
            } else {
                check(tree.delete(value) == reference.remove(value), "випадкове видалення");
            }
            compareWithReference(tree, reference);
        }
        // Повне видалення у випадковому порядку.
        List<Integer> remaining = new ArrayList<>(reference);
        Collections.shuffle(remaining, random);
        for (int value : remaining) {
            check(tree.delete(value), "очищення через видалення");
            reference.remove(value);
            compareWithReference(tree, reference);
        }

        System.out.println("Усі тести пройдено успішно!");
        System.out.println("Кількість перевірок: " + checks);
        System.out.println("Перевірено додавання, видалення, дублікати, обходи,");
        System.out.println("відсортоване введення та 3000 випадкових операцій.");
    }
}
