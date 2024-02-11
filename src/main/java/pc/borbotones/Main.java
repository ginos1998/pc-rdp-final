package pc.borbotones;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {

    public static void createPetriNet(Place[] places, Transition[] transitions) {
        for (Config.PLACES place : Config.PLACES.values()) {
            places[place.ordinal()] = new Place(place.name());
        }
        for (Config.TRANSITIONS transition : Config.TRANSITIONS.values()) {
            transitions[transition.ordinal()] = new Transition(transition.name());
        }
    }

    public static void connectElements(List<Place> placeList, List<Transition> transitionList, int[][] incidenceMatrix) {
        for (int p = 0; p < incidenceMatrix.length; p++) {
            for (int t = 0; t < incidenceMatrix[p].length; t++) {
                if (incidenceMatrix[p][t] == 1) {
                    transitionList.get(t).addOutput(placeList.get(p));
                } else if (incidenceMatrix[p][t] == -1) {
                    transitionList.get(t).addInput(placeList.get(p));
                }
            }
        }
    }

    public static void printState(List<Place> placeList, List<Transition> transitionList) {
        placeList.forEach(p -> System.out.println(p.toString() + " has " + p.getNumTokens() + " tokens"));
        transitionList.forEach(t -> System.out.println(t.getName() + " is enabled: " + t.isEnabled()));
    }

    public static void printInOuts(List<Transition> transitions) {
        transitions.forEach(t -> {
            System.out.println(t.getName() + " has inputs: " + t.getInput());
            System.out.println(t.getName() + " has outputs: " + t.getOutput());
            System.out.println();
        });
    }

    private static void fireSequencesTest(List<Transition> transitions, Logger log) {
        transitions.get(Config.TRANSITIONS.T1.ordinal()).fire();
        System.out.println("\n" + "Fired T1" + "\n");
        log.addNewTransition(transitions.get(Config.TRANSITIONS.T1.ordinal()));

        transitions.get(Config.TRANSITIONS.T3.ordinal()).fire();
        System.out.println("\n" + "Fired T3" + "\n");
        log.addNewTransition(transitions.get(Config.TRANSITIONS.T3.ordinal()));

        transitions.get(Config.TRANSITIONS.T5.ordinal()).fire();
        System.out.println("\n" + "Fired T5" + "\n");
        log.addNewTransition(transitions.get(Config.TRANSITIONS.T5.ordinal()));

        transitions.get(Config.TRANSITIONS.T7.ordinal()).fire();
        System.out.println("\n" + "Fired T7" + "\n");
        log.addNewTransition(transitions.get(Config.TRANSITIONS.T7.ordinal()));

        transitions.get(Config.TRANSITIONS.T8.ordinal()).fire();
        System.out.println("\n" + "Fired T8" + "\n");
        log.addNewTransition(transitions.get(Config.TRANSITIONS.T8.ordinal()));
    }

    public static void main(String[] args) {
        Logger log = new Logger();
        List<Place> placeList = Arrays.stream(Config.PLACES.values()).map(p -> new Place(p.name())).collect(Collectors.toList());
        List<Transition> transitionList = Arrays.stream(Config.TRANSITIONS.values()).map(t -> new Transition(t.name())).collect(Collectors.toList());
        Place[] places = new Place[Config.PLACES.values().length];
        Transition[] transitions = new Transition[Config.TRANSITIONS.values().length];
        createPetriNet(places, transitions);
        connectElements(placeList, transitionList, Config.INCIDENCE_MATRIX);
        // test the Petri net
        AtomicInteger i = new AtomicInteger();
        transitionList.forEach(t -> t.setNumber(i.getAndIncrement()));

        transitionList.forEach(t -> t.setNumber(transitionList.indexOf(t)+1));

        printInOuts(transitionList);
        printState(placeList, transitionList);
        fireSequencesTest(transitionList, log);
        printState(placeList, transitionList);

    }


}