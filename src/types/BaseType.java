package types;

/**
 * Created by ad5115 on 04/11/16.
 */
public class BaseType implements Type {

    @Override
    public boolean checkType(Type t) {
        return this.getClass() == t.getClass();
    }
}
