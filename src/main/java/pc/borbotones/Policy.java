package pc.borbotones;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Policy {
    List<Transition> transitionsList;
    private Logger logger;


    private List<Transition> getAvailableTransitionsList() {
        return transitionsList.stream()
            .filter(Transition::isEnabled)
            .collect(Collectors.toList());
    }

    public Policy(List<Transition> transitionsList, Logger logger) {
        this.transitionsList = transitionsList;
        this.logger = logger;
    }

    private List<Integer> getOrderedInvariantsFiredList() {
        List<Integer> invariantsFiredList = logger.getInvariantsFiredList();
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < invariantsFiredList.size(); i++) {
            map.put(i, invariantsFiredList.get(i));
        }

        return map.entrySet().stream()
             .sorted(Map.Entry.comparingByValue())
             .collect(Collectors.toList())
             .stream()
             .map(Map.Entry::getKey)
             .collect(Collectors.toList());
    }

    public Transition next(){
        List<Transition> availableTransitionsList = getAvailableTransitionsList();
        List<Integer> orderedInvariantsFiredList = getOrderedInvariantsFiredList();

        Transition transition = null;
        for (Transition t: availableTransitionsList) {
            for (Integer inv: orderedInvariantsFiredList) {
                if (Config.T_INVARIANTS[inv][t.getNumber()]) {
                    return t;
                }
            }
        }

        return transition;
    }
}
