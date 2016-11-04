/**
 * Created by ad5115 on 04/11/16.
 */
public class PairType implements Type {
    Type first;
    Type second;

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

    @Override
    public boolean checkType(Type a) {
        if (a instanceof PairType) {
            PairType aPair = (PairType) a;
            return aPair.getFirst().getClass() == getFirst().getClass() &&
                    aPair.getSecond().getClass() == getSecond().getClass();
        } else {
            return false;
        }
    }
}
