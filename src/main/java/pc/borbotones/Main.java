package pc.borbotones;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

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

    private static void fireSequencesTest(List<Transition> transitions, DataController log) {
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

    public static void markInitial(List<Place> placeList, int[] initial_marking) {
        for (int i = 0; i < initial_marking.length; i++) {
            for (int j = 0; j < initial_marking[i]; j++) {
                placeList.get(i).addToken();
            }
        }
    }

    public static void main(String[] args) {
        DataController log = new DataController();
        List<Place> placeList = Arrays.stream(Config.PLACES.values()).map(p -> new Place(p.name())).collect(Collectors.toList());
        List<Transition> transitionList = Arrays.stream(Config.TRANSITIONS.values()).map(t -> new Transition(t.name(), t.ordinal()+1)).collect(Collectors.toList());
        printState(placeList, transitionList);

        connectElements(placeList, transitionList, Config.INCIDENCE_MATRIX);
        markInitial(placeList, Config.INITIAL_MARKING);

        printInOuts(transitionList);
        printState(placeList, transitionList);
        fireSequencesTest(transitionList, log);

    }


}