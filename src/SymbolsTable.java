import types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ad5115 on 08/11/16.
 */
public class SymbolsTable {
    SymbolsTable reference;
    private Map<String, Type> symbolTable;


    public SymbolsTable() {
        this.reference = this;
        this.symbolTable = new HashMap<>();
    }

    public SymbolsTable(SymbolsTable reference) {
        this.reference = reference;
        this.symbolTable = new HashMap<>();
    }

    public void add(String name, Type elem) {
        if (reference == this) {
            symbolTable.put(name, elem);
        } else {
            reference.add(name,elem);
        }
    }

    public Type lookUp(String name){
        if(reference == this) {
            return symbolTable.get(name);
        } else {
            return reference.lookUp(name);
        }
    }
    public boolean isEmpty() {
        return symbolTable.isEmpty();
    }

}
