package types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FunctionType implements Type {

	private final List<Type> argList = new LinkedList<Type>();
	
	FunctionType(Type ... types) {
		for (Type type : types) {
			argList.add(type);
		}
	}
	
	@Override
	public boolean checkType(Type type2) {
		if (type2 instanceof FunctionType) {
			Iterator<Type> iter1 = argList.iterator();
			Iterator<Type> iter2 = ((FunctionType)type2).argList.iterator();
			while (iter1.hasNext() || iter2.hasNext()) {
				if (!iter1.next().checkType(iter2.next())) {
					return false;
				}
			}
			if (!iter1.hasNext() && !iter2.hasNext()) {
				return true;
			}
		}
		return false;
	}

}
