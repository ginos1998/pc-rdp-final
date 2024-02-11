package pc.borbotones;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Policy {
    List<Transition> transitionsList;
    private Logger logger;

    private List<Boolean> getAvailableTransitionsList() {
        return transitionsList.stream()
            .filter(Transition::isEnabled)
            .map(Transition::isEnabled)
            .collect(Collectors.toList());
    }

    private Integer minIndexInList(List<Integer> list){
        if (list == null || list.isEmpty() || list.contains(null)) {
            return -1;
        }

        if (list.size() == 1) {
            return list.get(0);
        }

        return list.indexOf(list.stream().min(Integer::compare).get());
    }

    public Policy(List<Transition> transitionsList, Logger logger) {
        this.transitionsList = transitionsList;
        this.logger = logger;
    }

    public Transition next(){
        List<Boolean> availableTransitionsList = getAvailableTransitionsList();
        List<Integer> invariantsFiredList = logger.getInvariantsFiredList();
        // Ver como equilibrar los invariantes, podes hacer Config.T_INVARIANTS[invariante][transicion]
        // para saber si la transicion está en el invariante
        return  null;
    }
}
