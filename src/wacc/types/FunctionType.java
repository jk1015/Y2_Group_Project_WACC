package wacc.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FunctionType implements Type {

	private final List<Type> argList = new LinkedList<Type>();
	
	public FunctionType(Type ... types) {
		for (Type type : types) {
			argList.add(type);
		}
	}
	
	public Type getReturnType() {
		return argList.get(0);
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

	@Override
	public String toString() {
		Iterator<Type> iter = argList.iterator();
		if (!iter.hasNext()) {
			return "function";
		}
		String msg = iter.next() + " function";
		if (!iter.hasNext()) {
			return msg;
		}
		msg += "(";
		while (iter.hasNext()) {
			msg += iter.next();
			if (iter.hasNext()) {
				msg += ", ";
			}
		}
		msg += ")";
		return msg;
	}
}
