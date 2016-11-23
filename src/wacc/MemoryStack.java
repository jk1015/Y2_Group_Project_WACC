package wacc;

import java.util.Deque;
import java.util.LinkedList;

public class MemoryStack {

    private LinkedList<String> stack;
    private Deque<Integer> scopeSizes;

    public MemoryStack() {
        this.stack = new LinkedList<String>();
        this.scopeSizes = new LinkedList<Integer>();
    }

    public void newScope() {
        scopeSizes.push(0);
    }

    public int descope() {
        int size = scopeSizes.pop();
        for(int i = 0; i < size; i++) {
            stack.removeFirst();
        }
        return size * 4;
    }

    public void add(String id) {
        stack.addFirst(id);
        scopeSizes.push(scopeSizes.pop() + 1);
    }

    public int get(String id) {
        return stack.indexOf(id) * 4;
    }

    public String getLocationString(String id) {
        int loc = get(id);
        if (loc == 0) {
            return "[sp]";
        } else {
            return "[sp, #" + loc + "]";
        }
    }

}
