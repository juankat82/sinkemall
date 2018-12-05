package com.juan.android.database;

/**
 * Created by juan on 03/01/18.
 */

public class Ships {
    public static final class ShipTable
    {
        public static final String NAME="ships";

        public static final class Cols
        {
            public static final String ID="ship_id";
            public static final String CHECK_ONE="check_one";
            public static final String CHECK_TWO="check_two";
            public static final String CHECK_THREE="check_three";
            public static final String CHECK_FOUR="check_four";
            public static final String INDEX_A_STATE="a_state";//X or O
            public static final String INDEX_B_STATE="b_state";
            public static final String INDEX_C_STATE="c_state";
            public static final String INDEX_D_STATE="d_state";
            public static final String STATE="state";//up or down
            public static final String COLOR="color";
        }
    }
}
