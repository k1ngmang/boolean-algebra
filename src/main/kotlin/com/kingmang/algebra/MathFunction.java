package com.kingmang.algebra;

import com.kingmang.algebra.KarnaughMap.TruthTable;
import com.kingmang.algebra.functions.*;
import com.kingmang.algebra.parser.Lexer;
import com.kingmang.algebra.parser.Parser;

import java.util.*;

public abstract class MathFunction {
    public List<MathFunction> list = new ArrayList<>();
    TruthTable tt = null;

    public abstract String toString();


    public String top() {
        if (isVar()) {
            return toString();
        }
        return "(" + toString() + ")";
    }

    public MathFunction simplify() {
        return this;
    }

    public MathFunction and(MathFunction f) {
        return new And(this, f).simplify();
    }

    public MathFunction or(MathFunction f) {
        return new Or(this, f).simplify();
    }

    public MathFunction xor(MathFunction f) {
        return new Xor(this, f).simplify();
    }

    public MathFunction not() {
        return new Not(this);
    }

    public boolean isAnd() {
        return this instanceof And;
    }

    public boolean isOr() {
        return this instanceof Or;
    }

    public boolean isXor() {
        return this instanceof Xor;
    }

    public boolean isNand() {
        return this instanceof Nand;
    }

    public boolean isNor() {
        return this instanceof Nor;
    }

    public boolean isXnor() {
        return this instanceof Xnor;
    }

    public boolean isCons() {
        return this instanceof Cons;
    }

    public boolean isVar() {
        return this instanceof Variable;
    }

    public boolean isNot() {
        return this instanceof Not;
    }

    public Not asNot() {
        return (Not) this;
    }

    public Cons asCons() {
        return (Cons) this;
    }

    public boolean isLow() {
        return isCons() && !asCons().value;
    }

    public boolean isHigh() {
        return isCons() && asCons().value;
    }

    public abstract Cons get(Variable[] v, Cons[] c);

    public Cons get(List<Variable> v, List<Cons> c) {
        return get(v.toArray(new Variable[0]), c.toArray(new Cons[0]));
    }

    public Cons get(Variable v, Cons c) {
        return get(new Variable[]{v}, new Cons[]{c});
    }

    public MathFunction get(String str) {
        String[] groups = str.split(",");
        Variable[] vars = new Variable[groups.length];
        Cons[] cons = new Cons[groups.length];
        String[] assign;
        for (int i = 0; i < groups.length; i++) {
            assign = groups[i].split("=");
            vars[i] = new Variable(assign[0]);
            cons[i] = new Cons(Integer.parseInt(assign[1]));
        }
        return get(vars, cons);
    }

    public Variable asVar() {
        return (Variable) this;
    }

    public TruthTable truthTable() {
        if (tt == null) {
            tt = new TruthTable(this);
            tt.calc();
        }
        return tt;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MathFunction)) return false;
        MathFunction f = (MathFunction) other;
        if (!list.isEmpty()) {
            return isEq(list, f.list);
        }
        return false;
    }

    public static boolean isEq(List<MathFunction> l1, List<MathFunction> l2) {
        int l;
        if ((l = l1.size()) != l2.size()) {
            return false;
        }
        boolean[] b = new boolean[l];
        boolean u;
        for (int i = 0; i < l; i++) {
            MathFunction p = l1.get(i);
            u = false;
            for (int j = 0; j < l; j++) {
                if (!b[j] && p.equals(l2.get(j))) {
                    b[j] = true;
                    u = true;
                    break;
                }
            }
            if (!u) {
                return false;
            }
        }
        return true;
    }

    public static MathFunction parse(String expr) {
        return new Parser(new Lexer(expr)).expr();
    }

    public static MathFunction parseSOP(String str, String... vars) {
        return parseTT(str, true, vars);
    }

    public static MathFunction parsePOS(String str, String... vars) {
        return parseTT(str, false, vars);
    }

    public static MathFunction parseTT(String str, boolean sop, String... vars) {
        List<MathFunction> l1 = new ArrayList<>();
        for (int i = 0, row = 0; i < str.length(); i++, row++) {
            int x = str.charAt(i) - '0';
            if (sop && x != 1 || !sop && x != 0) continue;

            List<MathFunction> l2 = new ArrayList<>();
            for (int j = 0; j < vars.length; j++) {
                Variable v = new Variable(vars[j]);
                int varVal = (row >> (vars.length - j - 1)) & 1;
                if (sop && varVal == 1 || !sop && varVal == 0) {
                    l2.add(v);
                } else {
                    l2.add(new Not(v));
                }
            }
            if (sop) {
                l1.add(And.make(l2));
            } else {
                l1.add(Or.make(l2));
            }
        }
        if (sop) {
            return Or.make(l1);
        } else {
            return And.make(l1);
        }
    }

    public final List<Variable> list() {
        Set<Variable> res = new HashSet<>();
        vars(res);
        return new ArrayList<>(res);
    }

    public final void vars(Set<Variable> set) {
        if (isCons()) return;
        if (isVar()) {
            set.add(asVar());
            return;
        }
        if (isNot()){
            asNot().f.vars(set);
            return;
        }
        if (list.isEmpty()) throw new RuntimeException("vars on empty func");
        for (MathFunction f : list) {
            f.vars(set);
        }
    }

    public MathFunction sort() {
        if (!list.isEmpty()) {
            sort(list);
        }
        return this;
    }

    static void sort(List<MathFunction> list) {
        list.sort((o1, o2) -> {
            if (o1 instanceof Variable) {
                Variable v1 = (Variable) o1;
                if (o2 instanceof Variable) {
                    Variable v2 = (Variable) o2;
                    return v1.compareTo(v2);
                } else {
                    return -1;
                }
            }
            if (o2 instanceof Variable) {
                return -1;
            }
            return 0;
        });
    }

    public static <T> List<T> asList(T[] array) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    public List<MathFunction> wout(int i) {
        List<MathFunction> copy = new ArrayList<>(list);
        copy.remove(i);
        return copy;
    }

    public MathFunction alternate() {
        return this;
    }

    public final MathFunction sop() {
        TruthTable tt = truthTable();
        String[] vars = new String[tt.vars.size()];
        int i = 0;
        for (Variable v : tt.vars) {
            vars[i++] = v.toString();
        }
        return MathFunction.parseSOP(tt.getOutStr(), vars);
    }

    public final MathFunction pos() {
        TruthTable tt = truthTable();
        String[] vars = new String[tt.vars.size()];
        int i = 0;
        for (Variable v : tt.vars) {
            vars[i++] = v.toString();
        }
        return MathFunction.parsePOS(tt.getOutStr(), vars);
    }

}
