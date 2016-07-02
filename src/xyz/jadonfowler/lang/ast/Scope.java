package xyz.jadonfowler.lang.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scope {

    private Scope superScope;
    private ArrayList<Scope> subScopes;
    private ArrayList<LVariable> variables;

    public Scope() {
        this(null);
    }

    public Scope(Scope superScope) {
        this.superScope = superScope;
        this.subScopes = new ArrayList<Scope>();
        this.variables = new ArrayList<LVariable>();
    }

    public List<Scope> getScopeTree() {
        ArrayList<Scope> scopes = new ArrayList<Scope>();
        Scope scope = this;

        do {
            scopes.add(scope);
            scope = scope.getSuperScope();
        } while (scope != null);

        Collections.reverse(scopes);
        return scopes;
    }

    public Scope getSuperScope() {
        return superScope;
    }

    public List<Scope> getSubScopes() {
        return subScopes;
    }

    public void addSubScope(Scope scope) {
        subScopes.add(scope);
    }

    public List<LVariable> getVariables() {
        return variables;
    }

    public void addVariable(LVariable variable) {
        variables.add(variable);
    }

}
