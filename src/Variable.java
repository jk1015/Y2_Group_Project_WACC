

public  class Variable {
    Type type;
    String identifier;
    public Variable(String identifier, Type type){
        this.identifier = identifier;
        this.type = type;
    }
    boolean checkType(Type a){
        return type.checkType(a);
    };
}