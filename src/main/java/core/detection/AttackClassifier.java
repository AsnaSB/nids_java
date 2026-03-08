package core.detection;

public class AttackClassifier {

    public String predict(String category, double[] features) {

        switch (category) {

            case "dos":
                return "neptune";

            case "probe":
                return "ipsweep";

            case "r2l":
                return "guess_passwd";

            case "u2r":
                return "buffer_overflow";

            default:
                return "normal";
        }
    }
}