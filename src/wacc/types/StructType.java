package wacc.types;

import antlr.WACCParser;
import wacc.exceptions.UndeclaredVariableException;

import java.util.List;

/**
 * Created by jk1015 on 01/12/16.
 */
public class StructType implements Type {

    private String name;
    private final List<Type> contents;
    private final List<String> ids;
    public final static String structListName = "#STRUCTLIST#";


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
            } else if (((StructType) type2).name == structListName || this.name == structListName) {

                List<Type> thatContents = ((StructType) type2).contents;

                if(contents.size() != thatContents.size()) {
                    return false;
                }
                for(int i = 0; i < contents.size(); i++) {
                    if (!thatContents.get(i).checkType(contents.get(i))) {
                        return false;
                    }
                    return true;
                }
            }
        } else if(type2 instanceof NullType) {
            return true;
        }

        return false;
    }

    public Type getType(String identifier) {
        int res = ids.indexOf(identifier);
        if(res == -1) {
            return new FailType();
        } else {
            return contents.get(res);
        }
    }

    public Type getType(int i) {

        if(i < 0 || i >= contents.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            return contents.get(i);
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

    public int getOffset(String id) {
        int offset = 0;
        for(int i = 0; i < ids.indexOf(id); i++) {
            if(contents.get(i) instanceof  StructType) {
                offset += ((StructType) contents.get(i)).getSize();
            } else {
                offset += 4;
            }
        }
        return offset;
    }

}
