package wacc;
import wacc.exceptions.RedeclaredVariableException;
import wacc.exceptions.UndeclaredVariableException;
import wacc.types.FunctionType;
import wacc.types.Type;

import java.util.*;

/**
 * Created by ad5115 on 08/11/16.
 */
public class ScopedSymbolTable {
    private Deque<Map<String, Type>> scopes;
    private Map<String, Type> currentScope;

    public ScopedSymbolTable() {
        Map<String, Type> symbolTable = new HashMap<String, Type>();
        this.currentScope = symbolTable;
        this.scopes = new LinkedList<>();
        this.scopes.addFirst(symbolTable);
    }

    public void add(String name, Type elem) {
        if (currentScope.containsKey(name)) {
            throw new RedeclaredVariableException(
            		"Variable " + name + " has already been declared in this scope.");
        }
        currentScope.put(name, elem);
    }

    public void addFunction(String name, FunctionType elem) {
        String funcName = '@' + name;
        add(funcName, elem);
    }

    public Type get(String name){
        Iterator<Map<String, Type>> it = scopes.iterator();
        while (it.hasNext()) {
            Map<String, Type> scope = it.next();
            if(scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new UndeclaredVariableException(
        		"Identifier " + name + " has not been declared yet.");
    }

    public FunctionType getFunction(String name){
        String funcName = '@' + name;
        return (FunctionType) get(funcName);
    }

    public void enterNewScope() {
        Map<String, Type> newScope = new HashMap<String, Type>();
        scopes.addFirst(newScope);
        currentScope = newScope;
    }

    public void exitScope() {
        scopes.removeFirst();
        currentScope = scopes.peekFirst();
    }

}
