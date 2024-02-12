package pc.borbotones;

import java.util.*;

public class DataController {

    private List<List<Integer>> invariantRegisterList;
    private List<Integer> invariantsCounterList;

    public DataController() {
        invariantRegisterList = new ArrayList<>();
        invariantsCounterList = Arrays.asList(0, 0, 0);
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

    private void createNewRegister(Integer id) {
        List<Integer> register = new ArrayList<>();
        register.add(id);
        invariantRegisterList.add(register);
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
                            incrementCounters(reg);
                        });
                });
        } catch (Exception e) {
            throw new RuntimeException();
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
        for(int i = 0; i< Config.T_INVARIANT_LIST.size();i++){
            if (Config.T_INVARIANT_LIST.get(i).stream().allMatch(reg::contains)){
                invariantsCounterList.set(i, invariantsCounterList.get(i) + 1);
            }
        }
        System.out.println("#######################");
        for(int i = 0; i < invariantsCounterList.size(); i++){
            System.out.println("Invariant " + (i+1) + " has been fired " + invariantsCounterList.get(i) + " times");
        }
    }
}



