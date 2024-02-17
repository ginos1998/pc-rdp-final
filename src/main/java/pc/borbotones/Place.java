package pc.borbotones;

import java.util.List;

public class Place {
    private String name;
    private int number;
    private int num_tokens;
    List<Subscriber> subscribers;

    public Place(String name, int number) {
        this.name = name;
        this.number = number;
        this.num_tokens = 0;
        this.subscribers = new java.util.ArrayList<>();
    }

    public void addToken() {
        this.num_tokens++;
        notifySubscribers();
    }

    public void removeToken() {
        this.num_tokens--;
        notifySubscribers();
    }

    /**
     * Attaches a subscriber to the place.
     * @param s subscriber (Transition)
     */
    public void atach(Subscriber s) {
        this.subscribers.add(s);
    }


    /**
     * Notifies the subscribers (Transitions) that the place has changed.
     */
    public void notifySubscribers() {
        for (Subscriber subscriber : this.subscribers) {
            subscriber.update(num_tokens, this);
        }
    }

    public int getNumTokens() {
        return this.num_tokens;
    }

    public int getNumber(){
        return this.number;
    }

    public String toString() {
        return this.name;
    }
}
