package pc.borbotones;

import pc.borbotones.exceptions.RdpException;
import pc.borbotones.logger.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DataController {

    private List<List<Integer>> invariantRegisterList;
    private List<Integer> invariantsCounterList;
    private List<Integer> invariantsRunningCounterList;
    private int totalInvariants;
    private final HashMap<List<Integer>, Integer> pInvariants;
    private final List<Place> placeList;

    public DataController( HashMap<List<Integer>, Integer> pInvariants, List<Place> placeList) {
        invariantRegisterList = new ArrayList<>();
        invariantsCounterList = Arrays.asList(0, 0, 0);
        invariantsRunningCounterList = Arrays.asList(0, 0, 0);
        this.pInvariants = pInvariants;
        this.placeList = placeList;
    }

    /**
     * If the transition is T1 or T9, it creates a new register.
     * Otherwise, it adds the transition to the register.
     * @param transition transition to register
     */
    public void registerFire(Transition transition) {
        if(transition.getNumber() == 1 || transition.getNumber() == 9){
            createNewRegister(transition.getNumber());
        }else{
            addNewTransition(transition);
        }
    }

    public List<Integer> getInvariantsCounterList() {
        return invariantsCounterList;
    }

    private void createNewRegister(Integer id) {
        List<Integer> register = new ArrayList<>();
        register.add(id);
        invariantRegisterList.add(register);
    }

    /**
     * Adds a new transition to the register and checks if the invariants are being verified.
     * @param transition transition to add
     */
    public void addNewTransition(Transition transition) {
        try {
            Config.T_INVARIANT_LIST.stream()
                .filter(inv -> inv.contains(transition.getNumber()))
                .forEach(inv -> {
                    invariantRegisterList.stream()
                        .filter(reg -> !reg.contains(transition.getNumber()))
                        .filter(reg -> verifyInvariant(inv, reg, inv.indexOf(transition.getNumber())))
                        .findFirst()
                        .ifPresent(reg -> {
                            reg.add(transition.getNumber());
                            incrementCounters(reg);
                        });
                });
        } catch (Exception e) {
            List<String> errors = Arrays.asList("Error adding new transition to register", e.getMessage());
            throw new RdpException(e, errors);
        }
    }

    private boolean verifyInvariant(List<Integer> inv, List<Integer> reg, int id){
        int i;
        for(i = 0; i < reg.size(); i++){
            if(!reg.get(i).equals(inv.get(i))){
                i = -1;
                break;
            }
        }
        return i == id;
    }

    /**
     * When the reg list is complete (matches with T_INVARIANT_LIST), it increments the counter of the invariant.
     * @param reg list of transitions
     */
    private void incrementCounters(List<Integer> reg){
        try {
            for(int i = 0; i< Config.T_INVARIANT_LIST.size();i++){
                if (Config.T_INVARIANT_LIST.get(i).stream().allMatch(reg::contains)){
                    invariantsCounterList.set(i, invariantsCounterList.get(i) + 1);
                    invariantsRunningCounterList.set(i, invariantsRunningCounterList.get(i) - 1);
                    totalInvariants++;
                }
            }
        } catch (Exception e) {
            List<String> errors = Arrays.asList("Error incrementing counters", e.getMessage());
            throw new RdpException(e, errors);
        }
    }

    /**
     * Checks if the invariants are being verified
     * @return true if the invariants are being verified
     */
    public boolean checkPInvariants(){
        AtomicBoolean checkPassed = new AtomicBoolean(true);
        pInvariants.keySet().forEach(inv -> {
            // Suma de elementos utilizando reduce
            List<Integer> tokens = placeList.stream()
                    .filter(t -> inv.contains(t.getNumber()))
                    .map(Place::getNumTokens)
                    .collect(Collectors.toList());

            int suma = tokens.stream()
                    .reduce(0, (subtotal, elemento) -> subtotal + elemento);

            if(pInvariants.get(inv) != suma){
                checkPassed.set(false);
            }
        });
        return checkPassed.get();
    }

    public int getTotalInvariants() {
        return totalInvariants;
    }
}



