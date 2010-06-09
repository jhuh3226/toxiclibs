package toxi.sim.erosion;

/**
 * For each neighbour it�s computed the difference between the processed cell
 * and the neighbour:
 * 
 * d[i] = h - h[i];
 * 
 * the maximum positive difference is stored in d_max, and the sum of all the
 * positive differences that are bigger than T (this numer is n), the talus
 * angle, is stored in d_tot.
 * 
 * Now it�s possible to update all the n cells (where d[i] is bigger than T)
 * using this formula:
 * 
 * h[i] = h[i] + c * (d_max - T) * (d[i] / d_tot);
 * 
 * and the main cell with this other formula:
 * 
 * h = h - (d_max - (n * d_max * T / d_tot));
 * 
 * The Talus angle T is a threshold that determines which slopes are affected by
 * the erosion, instead the c constant determines how much material is eroded.
 */
public class TalusAngleErosion extends ErosionFunction {

    private float theta;
    private float amount;

    public TalusAngleErosion(float theta, float amount) {
        this.theta = theta;
        this.amount = amount;
    }

    @Override
    public void erodeAt(int x, int y) {
        int idx = y * width + x;
        float maxD = 0;
        float sumD = 0;
        int n = 0;
        for (int i = 0; i < 9; i++) {
            h[i] = elevation[idx + off[i]];
            d[i] = elevation[idx] - h[i];
            if (d[i] > theta) {
                sumD += d[i];
                n++;
                if (d[i] > maxD) {
                    maxD = d[i];
                }
            }
        }
        if (sumD > 0) {
            elevation[idx] -= (maxD - (n * maxD * theta / sumD));
            for (int i = 0; i < 9; i++) {
                if (d[i] > theta) {
                    elevation[idx + off[i]] =
                            h[i] + amount * (maxD - theta) * (d[i] / sumD);
                }
            }
        }
    }

}
