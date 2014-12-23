package com.charlag;

public class Main {

    static final int MAX = 9;
    static final int[] ni = { // aka ni
            3890,
            3448,
            3836,
            3242,
            3773,
            3228,
            3779,
            2327,
            0,
            0,
            0,
            0
    };

    static final double y0 = 0.05;

    static int[][] weight_matrix =
            {
                    {0, MAX, MAX, MAX, MAX, MAX, MAX, MAX, 1, MAX, MAX, MAX}, //1
                    {MAX, 0, MAX, MAX, MAX, MAX, MAX, MAX, MAX, 1, MAX, MAX}, //2
                    {MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 1, MAX, MAX, MAX}, //3
                    {MAX, MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 1, MAX, MAX}, //4
                    {MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 1, MAX}, //5
                    {MAX, MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 1}, //6
                    {MAX, MAX, MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, 1, MAX}, //7
                    {MAX, MAX, MAX, MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, 1}, //8
                    {1, MAX, 1, MAX, MAX, MAX, MAX, MAX, 0, 1, 1, 1},         //9
                    {MAX, 1, MAX, 1, MAX, MAX, MAX, MAX, 1, 0, 1, 1},         //10
                    {MAX, MAX, MAX, MAX, 1, MAX, 1, MAX, 1, 1, 0, 1},         //11
                    {MAX, MAX, MAX, MAX, MAX, 1, MAX, 1, 1, 1, 1, 0},         //12
            };

    // ToDo: Распределение задержки.
    static int[][] time =
            {
                {0, MAX, MAX, MAX, MAX, MAX, MAX, MAX, 20, MAX, MAX, MAX}, //1
                {MAX, 0, MAX, MAX, MAX, MAX, MAX, MAX, MAX, 20, MAX, MAX}, //2
                {MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 20, MAX, MAX, MAX}, //3
                {MAX, MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 1, MAX, MAX}, //4
                {MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 1, MAX}, //5
                {MAX, MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, MAX, MAX, 1}, //6
                {MAX, MAX, MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, 1, MAX}, //7
                {MAX, MAX, MAX, MAX, MAX, MAX, MAX, 0, MAX, MAX, MAX, 1}, //8
                {1, MAX, 1, MAX, MAX, MAX, MAX, MAX, 0, 1, 1, 1},         //9
                {MAX, 1, MAX, 1, MAX, MAX, MAX, MAX, 1, 0, 1, 1},         //10
                {MAX, MAX, MAX, MAX, 1, MAX, 1, MAX, 1, 1, 0, 1},         //11
                {MAX, MAX, MAX, MAX, MAX, 1, MAX, 1, 1, 1, 1, 0},         //12
            };

    static int[][] history_matrix = new int[12][12];

    static double[][] summ_matrix = new double[12][12];

    static int[][] path_length = new int[12][12];

    static final int L0 = 200;

    static final int T = 50;

    public static void main(String[] args) {
        double[] yi = new double[12];
        for (int i = 0; i < 12; i++) {
            yi[i] = ni[i] * y0;
        }

        double sumn = 0;
        for (int i = 0; i < 8; i++) {
            sumn += ni[i];
        }


        double[] kj = new double[12];
        for (int i = 0; i < 12; i++) {
            kj[i] = ni[i] / sumn;
        }

        // using Floid's algorithm to find all shortest paths
        // first fill 2 matrix

        // fill history matrix
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (weight_matrix[i][j] < MAX) {
                    history_matrix[i][j] = i;
                } else {
                    history_matrix[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            history_matrix[i][i] = i;
        }

        for (int k = 0; k < 12; k++) {
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    if (weight_matrix[i][j] > weight_matrix[i][k] + weight_matrix[k][j]) {
                        weight_matrix[i][j] = weight_matrix[i][k] + weight_matrix[k][j];
                        history_matrix[i][j] = history_matrix[k][j];
                    }
                }
            }
        }
        System.out.println("Минимальные пути: ");
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(weight_matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Матрица предков: ");
        System.out.print("   ");
        for (int i = 0; i < 12; i++)
            System.out.print((i + 1) + " ");
        System.out.println();
        for (int i = 0; i < 12; i++) {
            System.out.print((i + 1) + ": ");
            for (int j = 0; j < 12; j++) {
                if (weight_matrix[i][j] < MAX) {
                    System.out.print(history_matrix[i][j] + 1 + " ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }

        System.out.println();
        //make_path(2, 5);
        System.out.println("Нагрузка от узла к узлу Yij: ");
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                summ_matrix[i][j] = kj[j] * yi[i];
                System.out.print(summ_matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                summ_matrix[i][j] = 0;
                //System.out.print(summ_matrix[i][j] + " ");
            }
            //System.out.println();
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double yij = yi[i] * kj[j];
                make_path(i, j, yij);
            }
        }
        System.out.println("Нагрузка на ребре: ");
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(summ_matrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Количество каналов: ");
        int[][] v_arr = new int[12][12];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                v_arr[i][j] = number_of_links(summ_matrix[i][j]);
                if (v_arr[i][j] == 1)
                    v_arr[i][j] = 0;
                System.out.print(v_arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Распределение по времени: ");
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(T / (double)weight_matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Интенсивность траффика: ");

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                v_arr[i][j] *= 85000;
                System.out.print(v_arr[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Интенсивность поступления заявок: ");
        double[][] lambda = new double[12][12];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                lambda[i][j] = v_arr[i][j] / (L0 * 8);
                System.out.print(lambda[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        double[][] mu = new double[12][12];

        System.out.println("Интенсивность обслуживания: ");
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                mu[i][j] = weight_matrix[i][j]/(double)T + lambda[i][j];
                System.out.print(mu[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Пропускная способность: ");
        double[][] b = new double[12][12];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                b[i][j] = mu[i][j] * L0 * 8;
                System.out.print(b[i][j] + " ");
            }
            System.out.println();
        }

    }

    private static void make_path(int from, int to, double x) {
        if (history_matrix[from][to] == from) {
            //System.out.print(history_matrix[from][to] + " ");
            summ_matrix[from][to] += x;
            return;
        }
        make_path(from, history_matrix[from][to], x);
        //System.out.print(history_matrix[from][to] + " ");
        summ_matrix[history_matrix[from][to]][to] += x;
    }

    private static int number_of_links(double y) {
        double pp = 0.02;
        int i = 1;
        double p = 1;
        while (p > pp) {
            if (i == 1) {
                p = y / (1 + y);
            } else {
                p = (y * p) / (i + y * p);
            }
            i++;
        }
        if (i > 1) {
            return i - 1;
        }
        return 0;
    }

}