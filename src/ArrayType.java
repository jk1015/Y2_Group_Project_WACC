/**
 * Created by ad5115 on 04/11/16.
 */
public class ArrayType implements Type {
    Type contained;

    public ArrayType(Type contained) {
        this.contained = contained;
    }

    public Type getContained() {
        return contained;
    }

    @Override
    public boolean checkType(Type a) {
        if (a instanceof ArrayType) {
            ArrayType aArray = (ArrayType) a;
            return aArray.getContained().getClass() == getContained().getClass();
        } else {
            return false;
        }
    }
}
