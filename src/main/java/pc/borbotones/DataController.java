package pc.borbotones;

//import pc.borbotones.exceptions.RdpException;
//import pc.borbotones.logger.LoggerFactory;
import pc.borbotones.logger.RdpLogger;

import java.util.*;

public class DataController {

    private List<List<Integer>> invariantRegisterList;
    private List<Integer> invariantsCounterList;

    //private final RdpLogger logger = LoggerFactory.getLogger(DataController.class);

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
            List<String> errors = Arrays.asList("Error adding new transition to register", e.getMessage());
           // throw new RdpException(e, errors);
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
                    //System.out.println("#######################");
                    //for(int j = 0; j < invariantsCounterList.size(); j++){
                    //    System.out.println("Invariant " + (j+1) + " has been fired " + invariantsCounterList.get(j) + " times");
                    //}

                }
            }
            //logger.logInvariants(invariantsCounterList);
        } catch (Exception e) {
            List<String> errors = Arrays.asList("Error incrementing counters", e.getMessage());
            //throw new RdpException(e, errors);
        }
    }
}



