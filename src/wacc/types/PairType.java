package wacc.types;

public class PairType implements Type {

	private final Type type1;
	private final Type type2;
	
	PairType(Type type1, Type type2) {
		this.type1 = type1;
		this.type2 = type2;
	}
	
	@Override
	public boolean checkType(Type pair2) {
		if (pair2 instanceof PairType) {
			return type1.checkType(((PairType)pair2).type1)
					&& type2.checkType(((PairType)pair2).type2);
		}
		return false;
	}
	
}
