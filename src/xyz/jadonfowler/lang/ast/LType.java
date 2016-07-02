package xyz.jadonfowler.lang.ast;

public enum LType {

    STRING(String.class),
    INTEGER(Long.class),
    DOUBLE(Double.class);

    private Class<?> clazz;

    LType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getJavaClass() {
        return clazz;
    }

}
