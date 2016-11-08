package types;

public class ArrayType implements Type {

	private final Type contentsType;
	
	ArrayType(Type contentsType) {
		this.contentsType = contentsType;
	}
	
	@Override
	public boolean checkType(Type type2) {
		if (type2 instanceof ArrayType) {
			contentsType.checkType(((ArrayType)type2).contentsType);
		}
		return false;
	}

}
