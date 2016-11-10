package wacc.types;

public class ArrayType implements Type {

	private final Type contentsType;
	
	public ArrayType(Type contentsType) {
		this.contentsType = contentsType;
	}

	public Type getContentsType() {
		return contentsType;
	}

	@Override
	public boolean checkType(Type type2) {
		if (type2 instanceof ArrayType) {
			contentsType.checkType(((ArrayType)type2).contentsType);
		}
		return false;
	}

	@Override
	public String toString() {
		return contentsType + "[]";
	}
	
}
