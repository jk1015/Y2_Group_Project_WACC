package wacc;

import wacc.types.Type;

/**
 * Created by mws15 on 24/11/16.
 */
public class TypedVariable {

    private final String name;
    private final Type type;


    public TypedVariable(String name, Type type) {
        this.name = name;
        this.type = type;
    }


    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TypedVariable) {
            return ((TypedVariable) o).name.equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


}
