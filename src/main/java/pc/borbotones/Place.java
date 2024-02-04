package pc.borbotones;

import java.awt.*;
import java.util.List;

public class Place {
    private String name;
    private int num_tokens;
    List<Subscriber> subscribers;

    public Place(String name) {
        this.name = name;
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

    public void atach(Subscriber s) {
        this.subscribers.add(s);
    }

    public void detach(Subscriber s) {
        this.subscribers.remove(s);
    }

    public void notifySubscribers() {
        for (Subscriber subscriber : this.subscribers) {
            subscriber.update(num_tokens, this);
        }
    }

    public int getNumTokens() {
        return this.num_tokens;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
