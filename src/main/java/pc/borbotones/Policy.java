package pc.borbotones;

import java.util.Arrays;
import java.util.HashMap;

public class Policy {
    private Transition[] transitions;
    private Logger logger;


    private boolean[] getAvailableTransitions() {
        boolean[] available_transitions = new boolean[transitions.length];
        for (int i = 0; i < transitions.length; i++) {
            available_transitions[i] = transitions[i].isEnabled();
        }
        return available_transitions;
    }

    private int minIndexInArray(int[] array){
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if(array[i] < min){
                min = array[i];
                index = i;
            }
        }
        return index;
    }

    public Policy(Transition[] transitions, Logger logger) {
        this.transitions = transitions;
        this.logger = logger;
    }

    public Transition next(){
        boolean[] available_transitions = getAvailableTransitions();
        int[] invariantsFired = logger.getInvariantsFired();
        // Ver como equilibrar los invariantes, podes hacer Config.T_INVARIANTS[invariante][transicion]
        // para saber si la transicion está en el invariante
        return  null;
    }
}
