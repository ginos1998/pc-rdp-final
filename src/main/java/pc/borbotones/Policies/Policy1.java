package pc.borbotones.Policies;

import pc.borbotones.DataController;
import pc.borbotones.Transition;

import java.util.*;

public class Policy1 extends Policy {
    private DataController dataController;

    public Policy1(DataController dataController) {
        super(dataController);
    }

    @Override
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
