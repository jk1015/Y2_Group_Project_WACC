package wacc.types;

import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;

/**
 * Created by jk1015 on 07/12/16.
 */
public class FailType implements Type {
    @Override
    public boolean checkType(Type type2) {
        return false;
    }
}
