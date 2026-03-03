package core.detection;

import java.util.List;

public class FlowFeatures {

    public int count;
    public double serror_rate;
    public int dst_host_count;
    public double rerror_rate;
    public boolean incomplete;
    public List<String> missing;   // <-- ADD THIS

    public FlowFeatures(int count,
                        double serror_rate,
                        int dst_host_count,
                        double rerror_rate,
                        boolean incomplete,
                        List<String> missing) {

        this.count = count;
        this.serror_rate = serror_rate;
        this.dst_host_count = dst_host_count;
        this.rerror_rate = rerror_rate;
        this.incomplete = incomplete;
        this.missing = missing;    // <-- ADD THIS
    }
}