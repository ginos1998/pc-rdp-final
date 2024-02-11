package pc.borbotones;

import java.util.*;

public class Logger {
    private List<Integer> invariantsFiredList;
    private List<List<Integer>> invariantRegisterList;
    private List<Integer> invariantsCounterList;

    public Logger() {
        invariantRegisterList = new ArrayList<>();
        invariantsCounterList = new ArrayList<>();
    }

    public void registerFire(Transition transition) {
        if(transition.getNumber() == 1 || transition.getNumber() == 9){
            createNewRegister(transition.getNumber());
        }else{
            addNewTransition(transition);
        }
    }

    public List<Integer> getInvariantsFiredList() {
        return invariantsFiredList;
    }

    private void createNewRegister(Integer id) {
        invariantRegisterList.add(Collections.singletonList(id));
    }

    public void addNewTransition(Transition transition) {
        try {
            if(transition.getNumber() == 1 || transition.getNumber() == 9){
                createNewRegister(transition.getNumber());
            }

            Config.T_INVARIANT_LIST.stream()
                .filter(inv -> inv.contains(transition.getNumber()))
                .forEach(inv -> invariantRegisterList.stream()
                    .filter(reg -> !reg.contains(transition.getNumber()))
                    .filter(reg -> verifyInvariant(inv, reg, inv.indexOf(transition.getNumber())))
                    .peek(reg -> reg.add(transition.getNumber()))
//                    .forEach(reg -> {
//                        reg.add(transition.getNumber());
//                        incrementCounters(reg);
//                    })
                    );
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

//    public void addTransition(Transition transition){
//        if(transition.getNumber() == 1 || transition.getNumber() == 9){
//            createNewRegister(transition.getNumber());
//        }
//        for (int[] inv: Config.T_INVARIANTS_INT) {
//            ArrayList<Integer> invArray = convertIntArrayToArrayList(inv);
//            if (invArray.contains(transition.getNumber())) {
//                for (ArrayList<Integer> reg : invariantsRegister) {
//                    if (!reg.contains(transition.getNumber())) {
//                        boolean checkPassed = verifyInvariant(invArray, reg, invArray.indexOf(transition.getNumber()));
//                        if(checkPassed){
//                            reg.add(transition.getNumber());
//                            incrementCounters(reg);
//                            break;
//                        }
//
//                    }
//                }
//            }
//        }
//    }

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
        Config.T_INVARIANT_LIST.stream()
                .filter(inv -> inv.equals(reg))
                .forEach(inv -> invariantsCounterList.set(Config.T_INVARIANT_LIST.indexOf(inv), invariantsCounterList.get(Config.T_INVARIANT_LIST.indexOf(inv)) + 1));
    }

}



