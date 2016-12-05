package wacc.types;

public enum PrimType implements Type {
	BOOL, CHAR, INT, STRING;
	
	@Override
	public boolean checkType(Type type2) {
		if (type2 instanceof PrimType) {
			return this == (PrimType)type2;
		}

		if (type2 instanceof ArrayType) {
			return type2.checkType(this);
		}
		return false;
	}
	
	@Override
	public String toString() {
		switch(this) {
		case BOOL: return "bool";
		case CHAR: return "char";
		case INT: return "int";
		case STRING: return "string";
		default: throw new IllegalArgumentException();
		}
	}
}
