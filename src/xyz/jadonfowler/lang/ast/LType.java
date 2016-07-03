package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LType {

    private static List<LType> types = new ArrayList<>();

    public static final LType VOID = new LType("void");
    public static final LType STRING = new LType("string", "\"\"");
    public static final LType INTEGER = new LType("int", "0");
    public static final LType LONG = new LType("long", "0");
    public static final LType BYTE = new LType("byte", "0");
    public static final LType FLOAT = new LType("float", "0.0");
    public static final LType DOUBLE = new LType("double", "0.0");

    private String ident;
    private String defaultValue;

    public LType(String ident) {
        this(ident, "null");
    }

    public LType(String ident, String defaultValue) {
        this.ident = ident;
        this.defaultValue = defaultValue;
        types.add(this);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
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
