package com.juan.android.database;

/**
 * Created by juan on 27/01/18.
 */

public class MachineShipsTable {

    public static final String MACHINE_SHIPS_TABLE_NAME="machine_table";

    public static final class Cols
    {
        public static final String COMP_SHIP_ID="comp_ship_id";
        public static final String INDEX_A_NUMBER="index_a_number";
        public static final String INDEX_B_NUMBER="index_b_number";
        public static final String INDEX_C_NUMBER="index_c_number";
        public static final String INDEX_D_NUMBER="index_d_number";
        public static final String INDEX_A_STATE="a_state";//X or O
        public static final String INDEX_B_STATE="b_state";
        public static final String INDEX_C_STATE="c_state";
        public static final String INDEX_D_STATE="d_state";
        public static final String COLOR="color";//nothing will mean no color to be applied
        public static final String STATE="state";//up or down
    }
}
