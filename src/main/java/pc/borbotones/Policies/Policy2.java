package pc.borbotones.Policies;
import pc.borbotones.Config;
import pc.borbotones.Transition;
import pc.borbotones.DataController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Policy2 extends Policy {

    public Policy2(DataController dataController) {
        super(dataController);
    }

    public Transition next(List<Transition> availableTransitionsList){
        List<Integer> orderedInvariantsCounterList = getOrderedInvariantsCounterList();
        availableTransitionsList.sort(Comparator.comparingInt(Transition::getNumber));
        Collections.reverse(availableTransitionsList);
        for (Integer inv: orderedInvariantsCounterList) {
            for (Transition t : availableTransitionsList) {
                if (Config.T_INVARIANT_LIST.get(inv).contains(t.getNumber())) {
                    return t;
                }
            }
        }
        return null;
    }
}
