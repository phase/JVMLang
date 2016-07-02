package xyz.jadonfowler.lang.ast;

public enum LType {

    VOID(Void.class),
    STRING(String.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    BYTE(Byte.class),
    FLOAT(Float.class),
    DOUBLE(Double.class);

    private Class<?> clazz;

    LType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getJavaClass() {
        return clazz;
    }

}
