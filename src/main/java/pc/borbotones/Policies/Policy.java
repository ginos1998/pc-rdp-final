package pc.borbotones.Policies;
import pc.borbotones.Transition;
import java.util.List;

public abstract class Policy {
    public Policy() {}

    public abstract Transition next(List<Transition> availableTransitionsList);
}
