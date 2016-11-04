import types.Type;

public  class Variable {
    Type type;
    String identifier;
    public Variable(String identifier, Type type){
        this.identifier = identifier;
        this.type = type;
    }
    boolean checkType(Type t){
        return type.checkType(t);
    };
}