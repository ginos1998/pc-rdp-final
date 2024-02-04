package pc.borbotones;
import java.util.List;

public class Segment implements Runnable{
    private String name;
    private List<Transition> transitions;
    private Monitor monitor;

    public Segment(String name,List<Transition> transitions, Monitor monitor) {
        this.name = name;
        this.transitions = transitions;
        this.monitor = monitor;
    }

    public String toString() {
        return this.name;
    }

    public void run() {

    }


}
