/**
 * Created by ad5115 on 04/11/16.
 */
public class BaseType implements Type {

    @Override
    public boolean checkType(Type a) {
        return this.getClass() == a.getClass();
    }
}
