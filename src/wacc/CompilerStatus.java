package wacc;

public enum CompilerStatus {
    SUCCESS(0),
    SYNTAX_ERROR(100),
    SEMANTIC_ERROR(200);
    
    private final int code;
    
    private CompilerStatus(int code) {
    	this.code = code;
    }
    
    public int code() {
    	return code;
    }
}
