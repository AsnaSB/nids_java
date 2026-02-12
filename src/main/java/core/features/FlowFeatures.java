package core.features;

import java.util.List;

public class FlowFeatures {

    // ===== Required rule features =====

    public final int count;
    public final double serrorRate;
    public final int dstHostCount;
    public final double rerrorRate;

    // ===== Data Quality Flags =====

    public final boolean incomplete;      // true if any feature missing
    public final List<String> missingFields;

    // ===== Constructor =====

    public FlowFeatures(int count,
                        double serrorRate,
                        int dstHostCount,
                        double rerrorRate,
                        boolean incomplete,
                        List<String> missingFields) {

        this.count = count;
        this.serrorRate = serrorRate;
        this.dstHostCount = dstHostCount;
        this.rerrorRate = rerrorRate;

        this.incomplete = incomplete;
        this.missingFields = missingFields;
    }
}
