package types;

/**
 * Created by ad5115 on 04/11/16.
 */
public class ArrayType implements Type {
    private Type contained;

    public ArrayType(Type contained) {
        this.contained = contained;
    }

    public Type getContained() {
        return contained;
    }

    public Class<? extends Type> getContainedType() {
        return contained.getClass();
    }

    @Override
    public boolean checkType(Type t) {
        if (t instanceof ArrayType) {
            ArrayType arrayT = (ArrayType) t;
            return arrayT.getContained().checkType(contained);
        } else {
            return false;
        }
    }
}
