package pc.borbotones.Policies;

import pc.borbotones.Config;
import pc.borbotones.DataController;
import pc.borbotones.Transition;
import java.util.*;


public class Policy3 extends Policy{
    public Policy3(DataController dataController) {
        super(dataController);
    }
    @Override
    public Transition next(List<Transition> availableTransitionsList){
        List<Integer> orderedInvariantsCounterList = getOrderedInvariantsCounterList();
        availableTransitionsList.sort(Comparator.comparingInt(Transition::getNumber));
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
