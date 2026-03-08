package core.detection;

public class CategoryClassifier {

    public String predict(double[] f) {

        double duration = f[0];
        double srcBytes = f[1];
        double dstBytes = f[2];

        if (srcBytes > 10000 && dstBytes < 100)
            return "dos";

        if (duration > 1000)
            return "probe";

        if (dstBytes == 0 && srcBytes < 200)
            return "r2l";

        if (srcBytes < 50 && duration < 10)
            return "u2r";

        return "normal";
    }
}