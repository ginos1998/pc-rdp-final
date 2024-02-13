package pc.borbotones;

//import pc.borbotones.exceptions.RdpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;

public class Monitor {
    private final ReentrantLock lock;
    private final HashMap<Transition,Condition> transitions_queues;
    private final Condition timedCondition;
    private Transition next;
    private final Policy policy;
    private final DataController dataController;
    private boolean enabled;

    public Monitor(List<Transition> transitions, Policy policy, DataController dataController) {
        this.lock = new ReentrantLock(true);
        this.transitions_queues = new HashMap<>();
        this.timedCondition = lock.newCondition();
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
        try {
            lock.lock();
            if(transition.isTimed()){
                while(transition.getTimedStatus() == Config.TRANSITION_STATES.UNDER_WINDOW){
                    long sleepTime = transition.remainingTime();
                    timedCondition.await(sleepTime, TimeUnit.MILLISECONDS);
                }
            }
            if(next == null){
                this.next = policy.next(readyTransitions(transition));
                System.out.println("Policy said: " + next);
                if(next != null)
                    transitions_queues.get(next).signal();
            }
            if(transition.getSensStatus() == Config.TRANSITION_STATES.NOT_SENSIBILIZED || (transition.getSensStatus() == Config.TRANSITION_STATES.SENSIBILIZED && next != null)){
                transitions_queues.get(transition).await();
            }
            if (transition.isTimed() && transition.getTimedStatus() == Config.TRANSITION_STATES.TIMED_OUT) {
                return false;
            }
            transition.fire();
            System.out.println("Thread: " + Thread.currentThread().getName() + " Fire: " + transition.getName());

            next = null;
            dataController.registerFire(transition);
        }
        catch (Exception e) {
            e.printStackTrace();
           System.exit(1);// throw new RdpException("Error firing transition", e);
        } finally {
            lock.unlock();
        }
        return true;
    }

    private List<Transition> readyTransitions(Transition arribed) {
        List<Transition> readyTransitions = new ArrayList<Transition>();
        for (Transition transition : transitions_queues.keySet()){
            if(transition.getSensStatus() == Config.TRANSITION_STATES.SENSIBILIZED && this.lock.hasWaiters(transitions_queues.get(transition))){
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
