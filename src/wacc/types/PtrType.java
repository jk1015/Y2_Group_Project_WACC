package wacc.types;

public class PtrType implements Type {

    private final Type refType;

    public PtrType(Type refType) {
        this.refType = refType;
    }

    public Type deref() {
        return refType;
    }

    @Override
    public boolean checkType(Type type2) {
        if (type2 instanceof  PtrType) {
            return refType.checkType(((PtrType) type2).refType);
        }
        return type2 instanceof NullType;
    }

    @Override
    public String toString() {
        return refType + "*";
    }
}
