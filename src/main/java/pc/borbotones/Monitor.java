package pc.borbotones;

import pc.borbotones.Policies.Policy;
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
    private final HashMap<Transition,Condition> transitionsQueues;
    private final Condition timedWaitingQueue;
    private Transition next;
    private final Policy policy;
    private final DataController dataController;
    private Boolean terminate;

    public Monitor(List<Transition> transitions, Policy policy, DataController dataController) {
        this.lock = new ReentrantLock(true);
        this.transitionsQueues = new HashMap<>();
        for (Transition transition : transitions) {
            this.transitionsQueues.put(transition, this.lock.newCondition());
        }
        this.timedWaitingQueue = this.lock.newCondition();
        this.terminate = false;
        next = policy.next(readyTransitions());
        this.dataController = dataController;
        this.policy = policy;
    }

    /**
     * Requests to fire a transition and waits until it can.
     * When the dataController reaches the maximum number of invariants, the program ends.
     * After transition is fired, verified the invariants of place
     * Then, the next transition is selected using the policy and the next transition is signaled.
     * Finally, free the lock.
     * @param transition
     * @return true if the transition was fired
     */
    public boolean requestFire(Transition transition)  {
        try {
            lock.lock();

            if (dataController.getTotalInvariants() == Config.MAX_INVARIANTS) {
                terminate = true;
            }

            while ( !transition.equals(next) || lock.hasWaiters(timedWaitingQueue))  {
                transitionsQueues.get(transition).await();
            }

            if (transition.waitingTime() > 0){
                timedWaitingQueue.await(transition.waitingTime(), TimeUnit.MILLISECONDS);
            }

            transition.fire();

            if(!(dataController.checkPInvariants()))
                throw new RdpException("No se verifican los invariantes de plaza\n");

            next = policy.next(readyTransitions());
            dataController.registerFire(transition);
            if (next == null) {
                List<Integer> invariantCounterList = dataController.getInvariantsCounterList();
                Logger.getLogger().logInvariants(invariantCounterList);
                System.exit(terminate ? 0 : 1);
            }

            transitionsQueues.get(next).signal();
            return true;
        }
        catch (Exception e) {
            Logger.getLogger().error("Error firing transition " + transition.getName());
            throw new RdpException("Error firing transition ");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the list of sensed transitions.
     * @return list of transitions
     */
    private List<Transition> readyTransitions() {
        List<Transition> readyTransitions = new ArrayList<>();
        for (Transition transition : transitionsQueues.keySet()){
            if(transition.isSensed()){
                if(terminate && (transition.getNumber() == 1 || transition.getNumber() == 9))
                    continue;
                readyTransitions.add(transition);
            }
        }
        return  readyTransitions;
    }
}
