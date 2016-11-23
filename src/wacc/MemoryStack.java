package wacc;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class MemoryStack {

    private Deque<String> stack;


    public MemoryStack(int size) {
        this.stack = new LinkedList<String>();
        scope(size);
    }

    public void scope(int size) {
        for(int i = 0; i < size; i++) {
            stack.addFirst(null);
        }
    }

    public void descope(int size) {
        for(int i = 0; i < size; i++) {
            stack.removeFirst();
        }
    }

    public void add(String id) {
        int i = 0;

        for(String s: stack) {
            if (s == null) {
                ((LinkedList) stack).set(i, id);
                return;
            }
            i++;
        }
    }

    public int get(String id) {
        return ((LinkedList)stack).indexOf(id) * 4;
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
