package pc.borbotones;

//import pc.borbotones.exceptions.RdpException;

import pc.borbotones.logger.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Transition implements Subscriber {
    private final String name;
    private final int number;
    private final Map<Place, Boolean> input;
    private final List<Place> output;
    private long alfaTime;
    private long betaTime;
    private long sensStart;
    private boolean timed;
    private boolean timing;
    private int fires;

    public Transition(String name, int number) {
        this.name = name;
        this.number = number;
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

    public void update(int num_tokens, Place place) {
        this.input.put(place, num_tokens > 0);
        if (this.input.values().stream().allMatch(Boolean::booleanValue))
            sensibilize();
        else
            unsensibilize();
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

    public boolean isSensed() {
        return (input.values().stream().allMatch(Boolean::booleanValue));
    }

    public boolean isTimed(){
        return this.timed;
    }

    public long waitingTime(){
        if(!this.timing)
            return alfaTime;
        if(!isTimed())
            return 0;

        return alfaTime - ( System.currentTimeMillis() - this.sensStart);
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
            Logger.getLogger().log("Transition " + this.name + " fired");
        }
        else {
            System.exit(1);
        }
    }

    public int getFires() {
        return this.fires;
    }

    public boolean isEnabled() {
            if(isTimed()){
                long senseTime = System.currentTimeMillis() - this.sensStart;
                if (senseTime < alfaTime)
                    return false;
                else if (senseTime <= betaTime)
                    return true;
                else
                    System.out.println("Transition: " + this.name + " timed out" + " senseTime: " + senseTime + " alfaTime: " + alfaTime + " betaTime: " + betaTime);
                    return false;
            }
            else
                return isSensed();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
