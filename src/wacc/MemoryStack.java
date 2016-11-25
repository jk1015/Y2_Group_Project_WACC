package wacc;

import wacc.types.NullType;
import wacc.types.Type;

import java.util.Deque;
import java.util.LinkedList;

public class MemoryStack {

    private LinkedList<TypedVariable> stack;
    private Deque<Integer> scopeSizes;

    public MemoryStack() {
        this.stack = new LinkedList<>();
        this.scopeSizes = new LinkedList<>();
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

    public void add(String id, Type t) {
        stack.addFirst(new TypedVariable(id, t));
        scopeSizes.push(scopeSizes.pop() + 1);
    }

    public int get(String id) {
        return stack.indexOf(new TypedVariable(id, null)) * 4;
    }

    public Type getType(String id) {
        return stack.get(stack.indexOf(new TypedVariable(id, null))).getType();
    }

    public String getOffsetString(String id) {
        int loc = get(id);
        if (loc == 0) {
            return "[sp]";
        } else {
            return "[sp, #" + loc + "]";
        }
    }

}
