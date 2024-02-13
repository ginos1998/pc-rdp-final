package pc.borbotones;

//import pc.borbotones.exceptions.RdpException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Transition implements Subscriber {
    private String name;
    private int number;
    private boolean enabled;
    private Map<Place, Boolean> input;
    private List<Place> output;
    private long alfaTime;
    private long betaTime;
    private long sensStart;
    private boolean timed;
    private boolean timing;
    private int fires;

    public Transition(String name, int number) {
        this.name = name;
        this.number = number;
        this.enabled = false;
        this.input = new java.util.HashMap<>();
        this.output = new java.util.ArrayList<>();
        this.fires = 0;
    }

    public int getNumber(){
        return this.number;
    }

    public void addInput(Place place) {
        this.input.put(place, false);
        place.atach(this);
    }

    public void addOutput(Place place) {
        this.output.add(place);
    }

    public void becomeTimed(long alfaTime, long betaTime) {
        this.timed = true;
        this.alfaTime = alfaTime;
        this.betaTime = betaTime;
        this.sensStart = 0;
    }

    private void sensibilize(){
        if(!timing) {
            timing = true;
            this.sensStart = System.currentTimeMillis();
        }
    }

    private void unsensibilize(){
            this.sensStart = 0;
            timing = false;
    }

    public long remainingTime(){
        return (alfaTime - ( System.currentTimeMillis() - this.sensStart));
    }

    public boolean isTimed(){
        return this.timed;
    }

    public Config.TRANSITION_STATES getSensStatus() {
        if (input.values().stream().allMatch(Boolean::booleanValue))
            return Config.TRANSITION_STATES.SENSIBILIZED;
        else
            return Config.TRANSITION_STATES.NOT_SENSIBILIZED;
    }

    public Config.TRANSITION_STATES getTimedStatus() {
        if(!this.timing)
            return Config.TRANSITION_STATES.UNDER_WINDOW;

        long senseTime = System.currentTimeMillis() - this.sensStart;
        if (senseTime < alfaTime)
            return Config.TRANSITION_STATES.UNDER_WINDOW;
        else if (senseTime <= betaTime)
            return Config.TRANSITION_STATES.ON_WINDOW;
        else
            System.out.println("Transition: " + this.name + " timed out" + " senseTime: " + senseTime + " alfaTime: " + alfaTime + " betaTime: " + betaTime);
            return Config.TRANSITION_STATES.TIMED_OUT;
    }

    public void update(int num_tokens, Place place) {
        this.input.put(place, num_tokens > 0);
        if (this.input.values().stream().allMatch(Boolean::booleanValue))
            sensibilize();
        else
            unsensibilize();
    }

    public void fire() {
        if (isEnabled()) {
            for (Place place : this.input.keySet()) {
                place.removeToken();
            }
            for (Place place : this.output) {
                place.addToken();
            }
            fires++;
        }
        else {
           // throw new RdpException("Transition not enabled: " + this.name);
        }
    }

    public int getFires() {
        return this.fires;
    }

    public boolean isEnabled() {
            return getSensStatus() == Config.TRANSITION_STATES.SENSIBILIZED;
    }

    public String getName() {
        return this.name;
    }

    public List<Place> getInput() {
        return Arrays.asList(this.input.keySet().toArray(new Place[0]));
    }

    public List<Place> getOutput() {
        return this.output;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
