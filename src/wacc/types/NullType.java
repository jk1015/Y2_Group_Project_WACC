package wacc.types;

/**
 * Created by jaspreet on 10/11/16.
 */
public class NullType implements Type {
    @Override
    public boolean checkType(Type type2) {
        return true;
    }
}
