package pc.borbotones;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Transition implements Subscriber {
    private String name;
    private int number;
    private boolean enabled;
    private String segmentTag;
    private Map<Place, Boolean> input;
    private List<Place> output;

    public Transition(String name) {
        this.name = name;
        this.enabled = false;
        this.input = new java.util.HashMap<>();
        this.output = new java.util.ArrayList<>();
    }
    public void setNumber(int number){
        this.number = number;
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

    public void update(int num_tokens, Place place) {
        this.input.put(place, num_tokens > 0);
        this.enabled = this.input.values().stream().allMatch(b -> b);
    }

    public void fire() {
        if (this.enabled) {
            for (Place place : this.input.keySet()) {
                place.removeToken();
            }
            for (Place place : this.output) {
                place.addToken();
            }
        }
        else {
            throw new IllegalStateException("Transition not enabled");
        }
    }

    public boolean isEnabled() {
        return this.enabled;
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

}
