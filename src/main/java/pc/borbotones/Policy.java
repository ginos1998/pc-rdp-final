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

    public Transition next(List<Transition> availableTransitionsList) {

        // Encuentra la cantidad mínima de disparos y el mínimo tiempo de espera inicial
        Transition selectedTransition = availableTransitionsList.get(0);

        for (int i = 1; i < availableTransitionsList.size(); i++) {
            Transition currentTransition = availableTransitionsList.get(i);

            // Comparar por cantidad de disparos
            if (currentTransition.getFires() < selectedTransition.getFires()) {
                selectedTransition = currentTransition;
            }
            // Si tienen la misma cantidad de disparos, comparar por tiempo de espera
            else if (currentTransition.getFires() == selectedTransition.getFires()
                    && currentTransition.waitingTime() < selectedTransition.waitingTime()) {
                selectedTransition = currentTransition;
            }
        }

        return selectedTransition;

    }
}
