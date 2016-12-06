package wacc.types;

import antlr.WACCParser;
import wacc.exceptions.UndeclaredVariableException;

import java.util.List;

/**
 * Created by jk1015 on 01/12/16.
 */
public class StructType implements Type {

    private String name;
    private List<Type> contents;
    private List<String> ids;

    public StructType(String name, List<Type> contents, List<String> ids) {
        this.name = name;
        this.contents = contents;
        this.ids = ids;
    }


    @Override
    public boolean checkType(Type type2) {

        if(type2 instanceof StructType) {
            if(this.name == ((StructType) type2).name) {
                return true;
            }
        } else if(type2 instanceof  NullType) {
            return true;
        }

        return false;
    }

    public Type getType(String identifier) {
        int res = ids.indexOf(identifier);
        if(res == -1) {
            return null;
        } else {
            return contents.get(res);
        }
    }

    public int getSize() {
        int size = 0;
        for(Type t: contents) {
            if(t instanceof StructType) {
                size += ((StructType) t).getSize();
            } else {
                size += 4;
            }
        }

        return size;
    }

}
