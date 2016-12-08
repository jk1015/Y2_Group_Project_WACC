package wacc.types;

/**
 * Created by jk1015 on 07/12/16.
 */
public class FailType implements Type {
    @Override
    public boolean checkType(Type type2) {
        return false;
    }
}
