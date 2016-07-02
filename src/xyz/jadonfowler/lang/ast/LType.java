package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LType {

    private static List<LType> types = new ArrayList<>();

    public static final LType VOID = new LType("void");
    public static final LType STRING = new LType("string");
    public static final LType INTEGER = new LType("int");
    public static final LType LONG = new LType("long");
    public static final LType BYTE = new LType("byte");
    public static final LType FLOAT = new LType("float");
    public static final LType DOUBLE = new LType("double");

    private String ident;

    public LType(String ident) {
        this.ident = ident;
        types.add(this);
    }

    public String toString() {
        return ident;
    }

    public static boolean isType(String ident) {
        for (LType type : types)
            if (type.toString().equals(ident))
                return true;
        return false;
    }

    public static LType getType(String ident) {
        for (LType type : types)
            if (type.toString().equals(ident))
                return type;
        return null;
    }

}
