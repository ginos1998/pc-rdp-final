package pc.borbotones;

public class Main {
    public enum Places {
        P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, Cs1, Cs2, Cs3
    }
    public enum Transitions {
        T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12
    }

    public static void createPetriNet(Place[] places, Transition[] transitions) {
        for (Places place : Places.values()) {
            places[place.ordinal()] = new Place(place.name());
        }
        for (Transitions transition : Transitions.values()) {
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
            System.out.println(place.getName() + " has " + place.getNumTokens() + " tokens");
        }
        for (Transition transition : transitions) {
            System.out.println(transition.getName() + " is enabled: " + transition.isEnabled());
            System.out.println("has input: " + transition.getInput().toString());
            System.out.println("has output: " + transition.getOutput().toString());
        }
    }

    public static void main(String[] args) {
        Place[] places = new Place[Places.values().length];
        Transition[] transitions = new Transition[Transitions.values().length];
        createPetriNet(places, transitions);
        connect_elements(places, transitions, Config.INCIDENCE_MATRIX);
        mark_initial(places, Config.INITIAL_MARKING);

        print_state(places, transitions);
        transitions[Transitions.T1.ordinal()].fire();
        System.out.println("\n" + "Fired T1" + "\n");
        print_state(places, transitions);
    }
}