package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class LType {

    private static List<LType> types = new ArrayList<>();

    public static final LType VOID = new LType("void", "V");
    public static final LType STRING = new LType("string", "Ljava/lang/String;", "\"\"");
    public static final LType INTEGER = new LType("int", "I", "0");
    public static final LType LONG = new LType("long", "J", "0");
    public static final LType BYTE = new LType("byte", "B", "0");
    public static final LType FLOAT = new LType("float", "F", "0.0");
    public static final LType DOUBLE = new LType("double", "D", "0.0");

    private String ident;
    private String jvmDescription;
    private String defaultValue;

    public LType(String ident, String description) {
        this(ident, description, "null");
    }

    public LType(String ident, String description, String defaultValue) {
        this.ident = ident;
        this.jvmDescription = description;
        this.defaultValue = defaultValue;
        types.add(this);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getJVMDescription() {
        return jvmDescription;
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
