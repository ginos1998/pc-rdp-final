package pc.borbotones;

public class Logger {
    private int[] invariantsFired;

    public Logger() {
        invariantsFired = new int[Config.T_INVARIANTS.length];
    }

    public void registerFire(Transition transition) {
        // Hay que implementar como carajo hacer que a partir de este dato
        // se vaya contando la cantidad de veces que se disparó un invariante
    }

    public int[] getInvariantsFired() {
        return invariantsFired;
    }
}
