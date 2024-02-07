package pc.borbotones;

public class Main {

    public static void createPetriNet(Place[] places, Transition[] transitions) {
        for (Config.PLACES place : Config.PLACES.values()) {
            places[place.ordinal()] = new Place(place.name());
        }
        for (Config.TRANSITIONS transition : Config.TRANSITIONS.values()) {
            transitions[transition.ordinal()] = new Transition(transition.name());
        }
    }

    public static void connect_elements(Place[] places, Transition[] transitions, int[][] incidence_matrix) {
        for (int p = 0; p < incidence_matrix.length; p++) {
            for (int t = 0; t < incidence_matrix[p].length; t++) {
                if (incidence_matrix[p][t] == 1) {
                    transitions[t].addOutput(places[p]);
                } else if (incidence_matrix[p][t] == -1) {
                    transitions[t].addInput(places[p]);
                }
            }
        }
    }

    public static void mark_initial(Place[] places, int[] initial_marking) {
        for (int i = 0; i < initial_marking.length; i++) {
            for (int j = 0; j < initial_marking[i]; j++) {
                places[i].addToken();
            }
        }
    }

    public static void print_state(Place[] places, Transition[] transitions) {
        for (Place place : places) {
            System.out.println(place.toString() + " has " + place.getNumTokens() + " tokens");
        }
        for (Transition transition : transitions) {
            System.out.println(transition.getName() + " is enabled: " + transition.isEnabled());
        }
    }

    public static void print_in_outs(Transition[] transitions) {
        for (Transition transition : transitions) {
            System.out.println(transition.getName() + " has inputs: " + transition.getInput());
            System.out.println(transition.getName() + " has outputs: " + transition.getOutput());
            System.out.println();
        }
    }

    private static void fireSequenceTest(Transition[] transitions, Logger log){
        transitions[Config.TRANSITIONS.T1.ordinal()].fire();
        System.out.println("\n" + "Fired T1" + "\n");
        log.addTransition(transitions[Config.TRANSITIONS.T1.ordinal()]);

        transitions[Config.TRANSITIONS.T3.ordinal()].fire();
        System.out.println("\n" + "Fired T3" + "\n");
        log.addTransition(transitions[Config.TRANSITIONS.T3.ordinal()]);

        transitions[Config.TRANSITIONS.T5.ordinal()].fire();
        System.out.println("\n" + "Fired T5" + "\n");
        log.addTransition(transitions[Config.TRANSITIONS.T5.ordinal()]);

        transitions[Config.TRANSITIONS.T7.ordinal()].fire();
        System.out.println("\n" + "Fired T7" + "\n");
        log.addTransition(transitions[Config.TRANSITIONS.T7.ordinal()]);

        transitions[Config.TRANSITIONS.T8.ordinal()].fire();
        System.out.println("\n" + "Fired T8" + "\n");
        log.addTransition(transitions[Config.TRANSITIONS.T8.ordinal()]);
    }
    public static void main(String[] args) {
        Logger log = new Logger();
        Place[] places = new Place[Config.PLACES.values().length];
        Transition[] transitions = new Transition[Config.TRANSITIONS.values().length];
        createPetriNet(places, transitions);
        connect_elements(places, transitions, Config.INCIDENCE_MATRIX);
        mark_initial(places, Config.INITIAL_MARKING);
        // test the Petri net
        for(int i=0; i< transitions.length;i++){
            transitions[i].setNumber(i+1);
        }
        print_in_outs(transitions);
        print_state(places, transitions);
        fireSequenceTest(transitions, log);
        print_state(places, transitions);
    }


}