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


    public DataController() {
        invariantRegisterList = new ArrayList<>();
        invariantsCounterList = Arrays.asList(0, 0, 0);
        invariantsRunningCounterList = Arrays.asList(0, 0, 0);
    }

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

    public List<Integer> getInvariantsRunningCounterList(){return invariantsRunningCounterList;}

    private void createNewRegister(Integer id) {
        List<Integer> register = new ArrayList<>();
        register.add(id);
        invariantRegisterList.add(register);
        //updateRunningCounter(register);
        totalInvariants++;
    }

    public void addNewTransition(Transition transition) {
        try {
            if(transition.getNumber() == 1 || transition.getNumber() == 9){
                createNewRegister(transition.getNumber());
            }

            Config.T_INVARIANT_LIST.stream()
                .filter(inv -> inv.contains(transition.getNumber()))
                .forEach(inv -> {
                    invariantRegisterList.stream()
                        .filter(reg -> !reg.contains(transition.getNumber()))
                        .filter(reg -> verifyInvariant(inv, reg, inv.indexOf(transition.getNumber())))
                        .findFirst()
                        .ifPresent(reg -> {
                            reg.add(transition.getNumber());
                            //updateRunningCounter(reg);
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

    private void incrementCounters(List<Integer> reg){
        try {
            for(int i = 0; i< Config.T_INVARIANT_LIST.size();i++){
                if (Config.T_INVARIANT_LIST.get(i).stream().allMatch(reg::contains)){
                    invariantsCounterList.set(i, invariantsCounterList.get(i) + 1);
                    invariantRegisterList.remove(reg);
                    invariantsRunningCounterList.set(i, invariantsRunningCounterList.get(i) - 1);
                }
            }
            Logger.getLogger().logInvariants(invariantsCounterList);
        } catch (Exception e) {
            List<String> errors = Arrays.asList("Error incrementing counters", e.getMessage());
            throw new RdpException(e, errors);
        }
    }
    private void updateRunningCounter(List<Integer> reg){
        if(reg.size() <= 2){
            List<List<Integer>> filteredInvs = Config.T_INVARIANT_LIST.stream()
                    .filter(inv -> inv.containsAll(reg))
                    .collect(Collectors.toList());

            if(filteredInvs.size()==1){
                invariantsRunningCounterList.set(Config.T_INVARIANT_LIST.indexOf(filteredInvs.get(0)), invariantsRunningCounterList.get(Config.T_INVARIANT_LIST.indexOf(filteredInvs.get(0)))+1);
            }
        }
    }

    private void calculatePercentages(List<Integer> invariants){
        // Calcular la suma total de invariantes en ejecución
        int totalInvariants = invariantsRunningCounterList.stream().mapToInt(Integer::intValue).sum();
        // Calcular el porcentaje de cada invariante usando stream
        List<Double> invariantPercentages = invariantsRunningCounterList.stream()
                .map(count -> ((double) count / totalInvariants) * 100) // Convertir a porcentaje
                .collect(Collectors.toList()); // Recolectar los resultados en una lista

        Logger.getLogger().logPercentages(invariantPercentages);
    }

    public boolean checkPInvariants(HashMap<List<Integer>, Integer> pInvariants){
        AtomicBoolean checkPassed = new AtomicBoolean(true);
        pInvariants.keySet().forEach(inv -> {
            // Suma de elementos utilizando reduce
            int suma = inv.stream()
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



