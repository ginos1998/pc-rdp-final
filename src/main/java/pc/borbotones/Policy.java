package pc.borbotones;

import java.util.*;
import java.util.stream.Collectors;

public class Policy {
    List<Transition> transitionsList;
    private DataController dataController;

    private List<Place> placeList;


    private List<Transition> getAvailableTransitionsList() {
        return transitionsList.stream()
            .filter(Transition::isEnabled)
            .collect(Collectors.toList());
    }

    public Policy(List<Transition> transitionsList, List<Place> placeList, DataController dataController) {
        this.transitionsList = transitionsList;
        this.dataController = dataController;
        this.placeList = placeList;
    }

    private List<Integer> getOrderedInvariantsCounterList() {
        List<Integer> invariantsFiredList = dataController.getInvariantsCounterList();
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
        List<Integer> orderedInvariantsCounterList = getOrderedInvariantsCounterList();

        Transition transition = null;

        Collections.reverse(availableTransitionsList);

        for (Integer inv: orderedInvariantsCounterList) {
            for (Transition t: availableTransitionsList) {
                if (Config.T_INVARIANT_LIST.get(inv).contains(t.getNumber())){
                    return t;
                }
            }
        }

        return transition;
    }
}
