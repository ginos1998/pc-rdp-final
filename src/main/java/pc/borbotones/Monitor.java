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
    private final HashMap<List<Integer>, Integer> pInvariants;
    private final List<Place> placeList;
    private Transition next;
    private final Policy policy;
    private final DataController dataController;

    public Monitor(List<Transition> transitions, Policy policy, DataController dataController, HashMap<List<Integer>, Integer> pInvariants, List<Place> placeList) {
        this.lock = new ReentrantLock(true);
        this.transitionsQueues = new HashMap<>();
        for (Transition transition : transitions) {
            this.transitionsQueues.put(transition, this.lock.newCondition());
        }
        this.pInvariants = pInvariants;
        this.placeList = placeList;
        this.dataController = dataController;
        this.policy = policy;
    }

    public boolean requestFire(Transition transition)  {
        try {
            lock.lock();

            if (dataController.getTotalInvariants() == Config.MAX_INVARIANTS) {
                System.exit(0);
            }

            while(!((next == null || transition.equals(next)) && transition.isEnabled())) {
                long waitingTime = transition.waitingTime();
                if (waitingTime <= 0)
                    transitionsQueues.get(transition).await();
                else
                    transitionsQueues.get(transition).await(transition.waitingTime(), TimeUnit.MILLISECONDS);
            }

            transition.fire();
            if(!(dataController.checkPInvariants(pInvariants, placeList))){
                throw new RdpException("No se verifican los invariantes de plaza\n");
            }

            next = policy.next(readyTransitions());
            transitionsQueues.get(next).signal();
            dataController.registerFire(transition);
            return true;
        }
        catch (Exception e) {
            Logger.getLogger().error("Error firing transition " + transition.getName());
            throw new RdpException("Error firing transition ");
        } finally {
            lock.unlock();
        }
    }

    private List<Transition> readyTransitions() {
        List<Transition> readyTransitions = new ArrayList<>();
        for (Transition transition : transitionsQueues.keySet()){
            if(transition.isSensed()){
                readyTransitions.add(transition);
            }
        }
        return  readyTransitions;
    }
}
