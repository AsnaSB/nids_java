package core.detection;

import java.util.List;

public class FlowFeatures {

    // ===== Phase 1 rule-based features =====
    public int count;
    public double serror_rate;
    public int dst_host_count;
    public double rerror_rate;

    // Missing field information
    public boolean incomplete;
    public List<String> missing;

    // ===== Phase 2 ML feature vector =====
    public double[] mlVector;

    public FlowFeatures(
            int count,
            double serror_rate,
            int dst_host_count,
            double rerror_rate,
            boolean incomplete,
            List<String> missing,
            double[] mlVector
    ) {

        this.count = count;
        this.serror_rate = serror_rate;
        this.dst_host_count = dst_host_count;
        this.rerror_rate = rerror_rate;

        this.incomplete = incomplete;
        this.missing = missing;

        this.mlVector = mlVector;
    }
}