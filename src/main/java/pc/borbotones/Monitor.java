package pc.borbotones;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;

public class Monitor {
    private final ReentrantLock lock;
    private final HashMap<String,Condition> transitions_queues;
    private final Policy policy;
    private final Logger logger;
    private boolean enabled;

    private List<int[]> invariantsT;

    public Monitor(Transition[] transitions, Policy policy, Logger logger) {
        this.lock = new ReentrantLock();
        this.transitions_queues = new HashMap<>();
        for (Transition transition : transitions) {
            this.transitions_queues.put(transition.getName(), this.lock.newCondition());
        }
        this.logger = logger;
        this.policy = policy;
        this.enabled = true;
    }

    public boolean requestFire(Transition transition) {
        if(!enabled)
            return false;
        try {
            lock.lock();

            while(!transition.isEnabled())
                transitions_queues.get(transition.getName()).await();

            transition.fire();
            logger.registerFire(transition);


            Transition next = policy.next();
            if(next != null)
                transitions_queues.get(next.getName()).signal();
            else
                throw new IllegalStateException("No transition available to fire, deadlock detected.");

        }
        catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            lock.unlock();
        }
        return true;
    }

    public void disable() {
        enabled = false;
    }
}
