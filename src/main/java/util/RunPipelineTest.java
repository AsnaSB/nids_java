package util;

import core.app.PipelineRunner;

public class RunPipelineTest {

    public static void main(String[] args) {

        PipelineRunner runner = new PipelineRunner();

        runner.run("data/nsl_kdd_test_clean.csv");

    }
}