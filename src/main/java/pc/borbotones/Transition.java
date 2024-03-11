package pc.borbotones;
import pc.borbotones.logger.Logger;
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

    /**
     * Updates the input places of the transition.
     * If all input places have tokens, the transition is sensibilized.
     * Otherwise, it is unsensibilized.
     * @param numTokens number of tokens in the place
     * @param place place to update
     */
    public void update(int numTokens, Place place) {
        this.input.put(place, numTokens > 0);
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

    /**
     * Sensibilizes the transition by setting the start time to the current time and starting the timing.
     */
    private void sensibilize(){
        if(!timing) {
            timing = true;
            this.sensStart = System.currentTimeMillis();
        }
    }

    /**
     * Unsensibilizes the transition by setting the start time to 0 and stopping the timing.
     */
    private void unsensibilize(){
            this.sensStart = 0;
            timing = false;
    }

    /**
     * @return true if all the Places connected to the transition have tokens
     */
    public boolean isSensed() {
        return (input.values().stream().allMatch(Boolean::booleanValue));
    }

    public boolean isTimed(){
        return this.timed;
    }

    public boolean isTiming(){
        return this.timing;
    }

    public long waitingTime(){
        if(!this.timing)
            return alfaTime;
        if(!isTimed())
            return 0;

        return alfaTime - ( System.currentTimeMillis() - this.sensStart);
    }

    /**
     * Fires the transition by removing tokens from input places and adding tokens to output places.
     * Also, increments the number of times the transition has been fired.
     * If the transition is not enabled, the program ends (shouldn't happen).
     */
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
                if (!isSensed())
                    return false;

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
