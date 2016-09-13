package com;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        for (int i = 1; i < 6; i++)
            rec(i, 6, 3, i + "");
    }

    private static void rec(int start, int maxN, int level, String out) {
        if (level == 1) {
            System.out.println(out);
            return;
        }

        for (int i = start + 1; i <= maxN; i++) {
            rec(i, maxN, level - 1, out + " " + i);
        }


    }

}

// c2,4 =c1,3+c1,4
