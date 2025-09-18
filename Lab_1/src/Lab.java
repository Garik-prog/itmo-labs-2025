public class Lab {

    static float random(int min, int max) {
        return (float) (Math.random() * max + Math.random() * min);
    }

    static void result(double[][] r) {
        for (double[] v : r) {
            for (double v1 : v) {
                System.out.printf("%.3f ", v1);
            }
            System.out.println();
        }
    }

    static double schet(int i, int j, long[]w, float[]x){
        double res;
        if (w[i] == 17) {
            res = Math.cos((Math.pow(((x[j] + 0.25) / 0.25), x[j] * Math.cbrt(x[j]) / 3)));
        } else if (w[i] == 11 || w[i] == 13 || w[i] == 19) {
            res = Math.pow(Math.exp(1), Math.atan(Math.pow(Math.exp(1), -1.0 * Math.abs(x[j]))));
        } else {
            res = Math.asin(Math.pow(Math.pow(Math.exp(1), (Math.pow(Math.cos(Math.asin(0.25 * ((x[j] - 0.5) / 27.0))), 2.0) * (Math.sqrt(Math.sqrt(Math.abs(x[j]))) + 1))), -1.0));
        }
        return res;
    }

    public static void main(String[] args) {
        long[] w = new long[7];
        float[] x = new float[18];
        double[][] k = new double[7][18];

        for (int i = 7, j = 0; i <= 19; i += 2, j++) {
            w[j] = i;
        }

        for (int i = 0; i < 18; i++) {
            x[i] = random(-14, 13);
        }

        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < x.length; j++) {
                k[i][j] = schet(i, j, w, x);
            }
        }
        result(k);
    }
}