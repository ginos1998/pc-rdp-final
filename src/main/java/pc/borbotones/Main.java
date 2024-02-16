package pc.borbotones;
import pc.borbotones.Policies.Policy;
import pc.borbotones.Policies.Policy2;
import pc.borbotones.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public static void markInitial(List<Place> placeList, int[] initial_marking) {
        for (int i = 0; i < initial_marking.length; i++) {
            for (int j = 0; j < initial_marking[i]; j++) {
                placeList.get(i).addToken();
            }
        }
    }

    public static void setTimedTransitions(List<Transition> transitionList) {
        for (Transition t : transitionList) {
            if (Config.TIMED_TRANSITIONS.containsKey(t.getName()))
                t.becomeTimed(Config.TIMED_TRANSITIONS.get(t.getName()).get(0), Config.TIMED_TRANSITIONS.get(t.getName()).get(1));
        }
    }

    public static List<Segment> createSegments(List<Transition> transitionList, Monitor monitor) {
        List<Segment> segmentList = new ArrayList<>();
        for (int i = 0; i < Config.SEGMENTS.size(); i++) {
            List<Transition> transitionsInSegment = new ArrayList<>();
            for (Transition t : transitionList) {
                if (Config.SEGMENTS.get(i).contains(t.getNumber())) {
                    transitionsInSegment.add(t);
                }
            }
            Segment segment = new Segment("S" + i, transitionsInSegment, monitor);
            segmentList.add(segment);
        }
        return segmentList;
    }

    public static List<Thread> createThreads(List<Segment> segmentList) {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < segmentList.size(); i++) {
            for (int n = 0; n < Config.SEGMENT_THREADS[i]; n++){
                Thread thread = new Thread(segmentList.get(i), "T"+"S"+i+"-"+n);
                threadList.add(thread);
            }
        }

        return threadList;
    }

    public static void runThreads(List<Thread> threadList) {
        for (Thread t : threadList) {
            t.start();
        }
    }

    public static void setPInvariants(List<List<Integer>> pInvariants, List<Place> places, HashMap<List<Integer>, Integer> map){
        pInvariants.forEach(inv -> {
            List<Integer> pInv = new ArrayList<>();
            places.forEach(p -> {
                if(inv.contains(p.getNumber()) && inv.indexOf(p.getNumber()) != inv.size()-1){
                    pInv.add(p.getNumber());
                }
            });
            map.put(pInv, inv.get(inv.size()-1));
        });
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Logger.getLogger().writeLogsToFile()));

        Logger.getLogger();
        HashMap<List<Integer>, Integer> pInvariants = new HashMap<>();
        List<Place> placeList = Arrays.stream(Config.PLACES.values()).map(p -> new Place(p.name(), p.ordinal()+1)).collect(Collectors.toList());
        List<Transition> transitionList = Arrays.stream(Config.TRANSITIONS.values()).map(t -> new Transition(t.name(), t.ordinal()+1)).collect(Collectors.toList());

        setPInvariants(Config.P_INVARIANTS, placeList, pInvariants);
        DataController dataController = new DataController();
        Policy policy = new Policy2(dataController);
        Monitor monitor = new Monitor(transitionList, policy, dataController, pInvariants, placeList);

        connectElements(placeList, transitionList, Config.INCIDENCE_MATRIX);
        setTimedTransitions(transitionList);
        markInitial(placeList, Config.INITIAL_MARKING);

        List<Segment> segmentList = createSegments(transitionList, monitor);
        List<Thread> trheadList = createThreads(segmentList);

        runThreads(trheadList);
    }

}
