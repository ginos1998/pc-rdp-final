package pc.borbotones;

//import pc.borbotones.exceptions.RdpException;

import pc.borbotones.exceptions.RdpException;
import pc.borbotones.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;

public class Monitor {
    private final ReentrantLock lock;
    private final HashMap<Transition,Condition> transitions_queues;
    private Transition next;
    private final Policy policy;
    private final DataController dataController;
    private boolean enabled;

    public Monitor(List<Transition> transitions, Policy policy, DataController dataController) {
        this.lock = new ReentrantLock(true);
        this.transitions_queues = new HashMap<>();
        for (Transition transition : transitions) {
            this.transitions_queues.put(transition, this.lock.newCondition());
        }
        this.dataController = dataController;
        this.policy = policy;
        this.enabled = true;
    }

    public boolean requestFire(Transition transition)  {
        if(!enabled)
            return false;

        if (dataController.getTotalInvariants() == 1000) {
            System.exit(0);
        }

        try {
            lock.lock();

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
