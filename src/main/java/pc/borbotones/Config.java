package pc.borbotones;

import java.util.Arrays;
import java.util.List;

public abstract class Config {
    public enum TRANSITIONS {
        T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12
    }

    public enum PLACES {
        P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, Cs1, Cs2, Cs3
    }

    public static final List<List<Integer>> T_INVARIANT_LIST = Arrays.asList(
            Arrays.asList(1,2,4,6,8),
            Arrays.asList(1,3,5,7,8),
            Arrays.asList(9,10,11,12)
    );

    public static final boolean[][] T_INVARIANTS = {
        { true, true, false, true, false, true, false, true, false, false, false, false },
        { true, false, true, false, true, false, true, true, false, false, false, false },
        { false, false, false, false, false, false, false, false, true, true, true, true }
    };

    public static final int[] INITIAL_MARKING = {0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 4, 2, 2, 3, 1, 3, 4, 6};

    public static final int[][] INCIDENCE_MATRIX = {
            //T1  T2  T3  T4  T5  T6  T7  T8  T9 T10 T11 T12
            {  1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //P1
            {  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0}, //P2
            {  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0}, //P3
            {  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0}, //P4
            {  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0}, //P5
            {  0,  0,  0,  0,  0,  1,  1, -1,  0,  0,  0,  0}, //P6
            { -1,  0,  0,  0,  0,  0,  0,  1,  0,  0,  0,  0}, //P7
            {  0,  0,  0,  0,  0,  0,  0,  0,  1, -1,  0,  0}, //P8
            {  0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1,  0}, //P9
            {  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1}, //P10
            {  0,  0,  0,  0,  0,  0,  0,  0, -1,  0,  0,  1}, //P11
            { -1,  1,  1,  0,  0,  0,  0,  0,  0,  0, -1,  1}, //P12
            {  0, -1, -1,  1,  1,  0,  0,  0,  0, -1,  1,  0}, //P13
            {  0,  0,  0, -1, -1,  1,  1,  0, -1,  1,  0,  0}, //P14
            {  0,  0,  0,  0,  0, -1, -1,  1,  0,  0,  0,  0}, //P15
            { -1,  1,  1,  0,  0,  0,  0,  0, -1, -1,  1,  0}, //CS1
            {  0, -1, -1,  1,  1,  0,  0,  0, -1,  1,  0,  0}, //CS2
            { -1,  0,  0,  1,  1,  0,  0,  0,  0,  0,  1,  0}, //CS3
    };

}