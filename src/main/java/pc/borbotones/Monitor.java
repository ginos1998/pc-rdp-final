package pc.borbotones;

import pc.borbotones.logger.Logger;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;

public class Monitor {
    private final ReentrantLock lock;
    private final HashMap<Transition,Condition> transitions_queues;
    private final HashMap<List<Integer>, Integer> pInvariants;
    private Transition next;
    private final Policy policy;
    private final DataController dataController;
    private boolean enabled;

    public Monitor(List<Transition> transitions, Policy policy, DataController dataController, HashMap<List<Integer>, Integer> pInvariants) {
        this.lock = new ReentrantLock(true);
        this.transitions_queues = new HashMap<>();
        for (Transition transition : transitions) {
            this.transitions_queues.put(transition, this.lock.newCondition());
        }
        this.pInvariants = pInvariants;
        this.dataController = dataController;
        this.policy = policy;
        this.enabled = true;
    }

    public boolean requestFire(Transition transition)  {
        if(!enabled)
            return false;

        try {
            lock.lock();

            if (dataController.getTotalInvariants() == Config.MAX_TRANSITIONS) {
                System.exit(0);
            }

            while(!(transition.equals(next) && transition.isEnabled()) ) {
                if(next == null && transition.isEnabled()) {
                    break;
                }
                if (transition.waitingTime() <= 0)
                    transitions_queues.get(transition).await();

                if(transition.waitingTime() > 0)
                    transitions_queues.get(transition).await(transition.waitingTime(), TimeUnit.MILLISECONDS);
            }

            transition.fire();
            if(!(dataController.checkPInvariants(pInvariants))){
                System.out.println("No se verifican los invariantes de plaza\n");
            }

            next = policy.next(readyTransitions());
            transitions_queues.get(next).signal();
            dataController.registerFire(transition);
            return true;
        }
        catch (Exception e) {
            Logger.getLogger().error("Error firing transition " + transition.getName());
        } finally {
            lock.unlock();
        }
        return true;
    }

    private List<Transition> readyTransitions() {
        List<Transition> readyTransitions = new ArrayList<Transition>();
        for (Transition transition : transitions_queues.keySet()){
            if(transition.getSensStatus() == Config.TRANSITION_STATES.SENSIBILIZED){
                readyTransitions.add(transition);
            }
        }
        return  readyTransitions;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
