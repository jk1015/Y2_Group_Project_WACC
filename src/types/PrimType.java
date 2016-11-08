package types;

public enum PrimType implements Type {
	BOOL, CHAR, INT, STRING;
	
	@Override
	public boolean checkType(Type type2) {
		if (type2 instanceof PrimType) {
			return this == (PrimType)type2;
		}
		return false;
	}
}
