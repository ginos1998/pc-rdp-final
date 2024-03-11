package pc.borbotones.Policies;
import pc.borbotones.DataController;
import pc.borbotones.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Policy {
    protected DataController dataController;

    protected Policy(DataController dataController) {
        this.dataController = dataController;
    }

    public abstract Transition next(List<Transition> availableTransitionsList);

    protected List<Integer> getOrderedInvariantsCounterList() {
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
}
