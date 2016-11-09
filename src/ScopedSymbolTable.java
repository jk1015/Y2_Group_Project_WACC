import types.Type;

import java.util.*;

/**
 * Created by ad5115 on 08/11/16.
 */
public class ScopedSymbolTable {
    private Deque<Map<String, Type>> scopes;
    private Map<String, Type> currentScope;

    public ScopedSymbolTable() {
        Map symbolTable = new HashMap<>();
        this.currentScope = symbolTable;
        this.scopes = new LinkedList<>();
        this.scopes.addFirst(symbolTable);
    }

    public void add(String name, Type elem) {
        if (currentScope.containsKey(name)) {
            // ERROR
        }
        currentScope.put(name, elem);
    }

    public Type get(String name){
        Iterator<Map<String, Type>> it = scopes.iterator();
        while (it.hasNext()) {
            Map<String, Type> scope = it.next();
            if(scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null;
    }

    public void enterNewScope() {
        Map newScope = new HashMap<>();
        scopes.addFirst(newScope);
        currentScope = newScope;
    }

    public void exitScope() {
        scopes.removeFirst();
        currentScope = scopes.peekFirst();
    }

}
