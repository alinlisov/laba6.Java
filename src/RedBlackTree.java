import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class RedBlackTree {
    private enum Color { RED, BLACK }
    private static final class Node {
        int key;
        Color color;
        Node left;
        Node right;
        Node parent;
        Node(int key, Color color) {
            this.key = key;
            this.color = color;
        }
    }
    private final Node nil = new Node(0, Color.BLACK);
    private Node root;
    private int size;
    public RedBlackTree() {
        nil.left = nil;
        nil.right = nil;
        nil.parent = nil;
        root = nil;
    }
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == nil;
    }

    public void clear() {
        root = nil;
        size = 0;
        nil.parent = nil;
    }
    public boolean contains(int key) {
        return findNode(key) != nil;
    }

    private Node findNode(int key) {
        Node current = root;
        while (current != nil) {
            if (key == current.key) {
                return current;
            }
            current = key < current.key ? current.left : current.right;
        }
        return nil;
    }
    /** Додає число і повертає false, якщо таке число вже існує. */
    public boolean insert(int key) {
        Node parent = nil;
        Node current = root;
        while (current != nil) {
            parent = current;
            if (key == current.key) {
                return false;
            }
            current = key < current.key ? current.left : current.right;
        }
        Node node = new Node(key, Color.RED);
        node.left = nil;
        node.right = nil;
        node.parent = parent;
        if (parent == nil) { root = node;
        } else if (key < parent.key) { parent.left = node;
        } else { parent.right = node;
        }
        size++;
        fixAfterInsert(node);
        return true;
    }

    private void rotateLeft(Node node) {
        Node pivot = node.right;
        node.right = pivot.left;
        if (pivot.left != nil) {
            pivot.left.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == nil) {
            root = pivot;
        } else if (node == node.parent.left) {
            node.parent.left = pivot;
        } else {
            node.parent.right = pivot;
        }
        pivot.left = node;
        node.parent = pivot;
    }
    private void rotateRight(Node node) {
        Node pivot = node.left;
        node.left = pivot.right;
        if (pivot.right != nil) {
            pivot.right.parent = node;
        }
        pivot.parent = node.parent;
        if (node.parent == nil) {
            root = pivot;
        } else if (node == node.parent.right) {
            node.parent.right = pivot;
        } else {
            node.parent.left = pivot;
        }
        pivot.right = node;
        node.parent = pivot;
    }
    /** Виправлення кольорів і структури після вставлення вузла. */
    private void fixAfterInsert(Node node) {
        while (node.parent.color == Color.RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }
    /** Видаляє число і повертає false, якщо його немає у дереві. */
    public boolean delete(int key) {
        Node target = findNode(key);
        if (target == nil) {
            return false;
        }
        Node moved = target;
        Color removedColor = moved.color;
        Node replacement;
        if (target.left == nil) {
            replacement = target.right;
            transplant(target, target.right);
        } else if (target.right == nil) {
            replacement = target.left;
            transplant(target, target.left);
        } else {
            moved = minimum(target.right);
            removedColor = moved.color;
            replacement = moved.right;
            if (moved.parent == target) {
                replacement.parent = moved;
            } else {
                transplant(moved, moved.right);
                moved.right = target.right;
                moved.right.parent = moved;
            }
            transplant(target, moved);
            moved.left = target.left;
            moved.left.parent = moved;
            moved.color = target.color;
        }
        size--;
        if (removedColor == Color.BLACK) { fixAfterDelete(replacement);
        }
        return true;
    }

    private Node minimum(Node node) {
        while (node.left != nil) {
            node = node.left;
        }
        return node;
    }
    private void transplant(Node oldNode, Node newNode) {
        if (oldNode.parent == nil) {
            root = newNode;
        } else if (oldNode == oldNode.parent.left) {
            oldNode.parent.left = newNode;
        } else {
            oldNode.parent.right = newNode;
        }
        // NIL.parent використовується під час виправлення після видалення.
        newNode.parent = oldNode.parent;
    }

    /** NIL завжди залишається чорним, навіть якщо логіка виправлення звертається до нього. */
    private void setColor(Node node, Color color) {
        if (node != nil) {
            node.color = color;
        }
    }

    private void fixAfterDelete(Node node) {
        while (node != root && node.color == Color.BLACK) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling.color == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    node.parent.color = Color.RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                if (sibling.left.color == Color.BLACK && sibling.right.color == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = node.parent;
                } else {
                    if (sibling.right.color == Color.BLACK) {
                        setColor(sibling.left, Color.BLACK);
                        setColor(sibling, Color.RED);
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    setColor(sibling, node.parent.color);
                    node.parent.color = Color.BLACK;
                    setColor(sibling.right, Color.BLACK);
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                Node sibling = node.parent.left;
                if (sibling.color == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    node.parent.color = Color.RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (sibling.right.color == Color.BLACK && sibling.left.color == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = node.parent;
                } else {
                    if (sibling.left.color == Color.BLACK) {
                        setColor(sibling.right, Color.BLACK);
                        setColor(sibling, Color.RED);
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    setColor(sibling, node.parent.color);
                    node.parent.color = Color.BLACK;
                    setColor(sibling.left, Color.BLACK);
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        setColor(node, Color.BLACK);
        nil.color = Color.BLACK;
    }

    public List<Integer> inOrder() {
        List<Integer> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(Node node, List<Integer> result) {
        if (node == nil) return;
        inOrder(node.left, result);
        result.add(node.key);
        inOrder(node.right, result);
    }

    public List<Integer> preOrder() {
        List<Integer> result = new ArrayList<>();
        preOrder(root, result);
        return result;
    }

    private void preOrder(Node node, List<Integer> result) {
        if (node == nil) return;
        result.add(node.key);
        preOrder(node.left, result);
        preOrder(node.right, result);
    }

    public List<Integer> postOrder() {
        List<Integer> result = new ArrayList<>();
        postOrder(root, result);
        return result;
    }

    private void postOrder(Node node, List<Integer> result) {
        if (node == nil) return;
        postOrder(node.left, result);
        postOrder(node.right, result);
        result.add(node.key);
    }

    public List<Integer> levelOrder() {
        List<Integer> result = new ArrayList<>();
        if (isEmpty()) return result;
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node node = queue.remove();
            result.add(node.key);
            if (node.left != nil) queue.add(node.left);
            if (node.right != nil) queue.add(node.right);
        }
        return result;
    }

    /** Відображення дерева: праве піддерево зверху, ліве знизу. */
    public void printTree() {
        if (isEmpty()) {
            System.out.println("Дерево порожнє.");
            return;
        }
        System.out.println("Дерево (праві нащадки зверху, ліві знизу):");
        printSideways(root, "", false, true);
        System.out.println("Позначення: (R) — червоний вузол; (B) — чорний вузол.");
        System.out.println("Кількість вузлів: " + size);
    }

    private void printSideways(Node node, String prefix, boolean isLeft, boolean isRoot) {
        if (node == nil) return;
        printSideways(node.right, prefix + (isRoot ? "    " : (isLeft ? "│   " : "    ")), false, false);
        String branch = isRoot ? "" : (isLeft ? "└── " : "┌── ");
        System.out.println(prefix + branch + node.key + " (" + (node.color == Color.RED ? "R" : "B") + ")");
        printSideways(node.left, prefix + (isRoot ? "    " : (isLeft ? "    " : "│   ")), true, false);
    }

    /** Перевіряє всі 5 властивостей ЧЧ-дерева, BST-порядок та кількість вузлів. */
    public boolean isValidRedBlackTree() {
        if (nil.color != Color.BLACK || nil.left != nil || nil.right != nil) return false;
        if (root == nil) return size == 0;
        if (root.color != Color.BLACK || root.parent != nil) return false;
        int[] count = {0};
        int blackHeight = validate(root, Long.MIN_VALUE, Long.MAX_VALUE, count);
        return blackHeight >= 0 && count[0] == size;
    }

    private int validate(Node node, long min, long max, int[] count) {
        if (node == nil) return 1; // NIL-листок чорний
        if (node.key <= min || node.key >= max) return -1;
        if (node.left != nil && node.left.parent != node) return -1;
        if (node.right != nil && node.right.parent != node) return -1;
        if (node.color == Color.RED
                && (node.left.color != Color.BLACK || node.right.color != Color.BLACK)) return -1;
        count[0]++;
        int leftHeight = validate(node.left, min, node.key, count);
        int rightHeight = validate(node.right, node.key, max, count);
        if (leftHeight < 0 || leftHeight != rightHeight) return -1;
        return leftHeight + (node.color == Color.BLACK ? 1 : 0);
    }
}
