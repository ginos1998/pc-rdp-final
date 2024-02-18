package pc.borbotones;
import java.util.List;

public class Segment implements Runnable{
    private final String name;
    private final List<Transition> transitions;
    private final Monitor monitor;

    public Segment(String name,List<Transition> transitions, Monitor monitor) {
        this.name = name;
        this.transitions = transitions;
        this.monitor = monitor;
    }

    public String toString() {
        return this.name;
    }

    /**
     * Runs the segment by firing transitions in a loop.
     * If a transition cannot fire, it waits until it can.
     */
    public void run() {
        while(true) {
            for (Transition transition : transitions) {
                boolean fired = false;
                while(!fired)
                    fired = monitor.requestFire(transition);
            }
        }
    }
}
