package core.detection;

import core.contracts.DetectionResult;

public interface Detector {

    DetectionResult detect(double[] features);

}