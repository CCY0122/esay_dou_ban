package com.example.materialdesigntest.gsonBean;

import java.security.PublicKey;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CastsOverview {
    public Avatars avatars;

    public class Avatars {
        public String small;
        public String large;
        public String medium;
    }

    public String name;
    public String id;
    public String alt;
}
