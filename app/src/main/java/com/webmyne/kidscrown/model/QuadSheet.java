package com.webmyne.kidscrown.model;

import java.util.HashMap;

/**
 * Created by dhruvil on 14-08-2015.
 */
public class QuadSheet {

    public  String ul1[] = {"D-UL-7","D-UL-6","D-UL-5","D-UL-4","D-UL-3","D-UL-2"};
    public  String ul2[] = {"E-UL-7","E-UL-6","E-UL-5","E-UL-4","E-UL-3","E-UL-2"};

    public  String ur1[] = {"D-UR-2","D-UR-3","D-UR-4","D-UR-5","D-UR-6","D-UR-7"};
    public  String ur2[] = {"E-UR-2","E-UR-3","E-UR-4","E-UR-5","E-UR-6","E-UR-7"};

    public  String ll1[] = {"D-LL-7","D-LL-6","D-LL-5","D-LL-4","D-LL-3","D-LL-2"};
    public  String ll2[] = {"E-LL-7","E-LL-6","E-LL-5","E-LL-4","E-LL-3","E-LL-2"};

    public  String lr1[] = {"D-LR-2","D-LR-3","D-LR-4","D-LR-5","D-LR-6","D-LR-7"};
    public  String lr2[] = {"E-LR-2","E-LR-3","E-LR-4","E-LR-5","E-LR-6","E-LR-7"};


    public QuadSheet() {
    }

    public String[] getUl1() {
        return ul1;
    }

    public String[] getUl2() {
        return ul2;
    }

    public String[] getUr1() {
        return ur1;
    }

    public String[] getUr2() {
        return ur2;
    }

    public String[] getLl1() {
        return ll1;
    }

    public String[] getLl2() {
        return ll2;
    }

    public String[] getLr1() {
        return lr1;
    }

    public String[] getLr2() {
        return lr2;
    }
}
