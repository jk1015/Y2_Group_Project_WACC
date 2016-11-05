package types;

/**
 * Created by ad5115 on 04/11/16.
 */
public class PairType implements Type {
    private Type first;
    private Type second;

    public PairType(Type first, Type second) {
        this.first = first;
        this.second = second;
    }

    public Type getFirst() {
        return first;
    }

    public Type getSecond() {
        return second;
    }

    public Class<? extends Type> getFirstType() {
        return first.getClass();
    }

    public Class<? extends Type> getSecondType() {
        return second.getClass();
    }

    @Override
    public boolean checkType(Type t) {
        if (t instanceof PairType) {
            PairType tPair = (PairType) t;
            return tPair.getFirst().checkType(first) &&
                    tPair.getSecond().checkType(second);
        } else {
            return false;
        }
    }
}
