package pc.borbotones;

import java.util.*;
import java.util.stream.Collectors;

public class Logger {
    private int[] invariantsFired;
    private ArrayList<ArrayList<Integer>> invariantsRegister;
    private int[] invariantsCounter;

    public Logger() {
        invariantsRegister = new ArrayList<>();
        invariantsCounter = new int[Config.T_INVARIANTS_INT.length];
    }

    public void registerFire(Transition transition) {
        if(transition.getNumber() == 1 || transition.getNumber() == 9){
            createNewRegister(transition.getNumber());
        }else{
            addTransition(transition);

        }
    }

    public int[] getInvariantsFired() {
        return invariantsFired;
    }

    private void createNewRegister(Integer id) {
        ArrayList<Integer> newRegister = new ArrayList<>();
        newRegister.add(id);
        invariantsRegister.add(newRegister);
    }

    public void addTransition(Transition transition){
        if(transition.getNumber() == 1 || transition.getNumber() == 9){
            createNewRegister(transition.getNumber());
        }
        for (int[] inv: Config.T_INVARIANTS_INT) {
            ArrayList<Integer> invArray = convertIntArrayToArrayList(inv);
            if (invArray.contains(transition.getNumber())) {
                for (ArrayList<Integer> reg : invariantsRegister) {
                    if (!reg.contains(transition.getNumber())) {
                        boolean checkPassed = verifyInvariant(invArray, reg, invArray.indexOf(transition.getNumber()));
                        if(checkPassed){
                            reg.add(transition.getNumber());
                            incrementCounters(reg);
                            break;
                        }

                    }
                }
            }
        }
    }
    private ArrayList<Integer> convertIntArrayToArrayList(int[] intArray){
        return Arrays.stream(intArray)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean verifyInvariant(ArrayList<Integer> invArray, ArrayList<Integer> invReg, int id){
        int[] inv = arrayListToArray(invArray);
        int[] reg = arrayListToArray(invReg);
        int i;
        for(i = 0; i < reg.length; i++){
            if(reg[i] != inv[i]){
                i=-1;
                break;
            }
        }
        return i == id;
    }

    private int[] arrayListToArray(ArrayList<Integer> arrayList) {
        // Convertir ArrayList<Integer> a int[] usando stream
        return arrayList.stream()
                .mapToInt(i -> i) // Convierte Integer a int
                .toArray();
    }

    private void incrementCounters(ArrayList<Integer> reg){
        int[] regArray = arrayListToArray(reg);
        for(int i = 0; i<Config.T_INVARIANTS_INT.length;i++){
            if (Arrays.equals(regArray, Config.T_INVARIANTS_INT[i])){
                invariantsCounter[i]++;
            }
        }
    }
}



