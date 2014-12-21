package org.saliya.threads.frameworks.hj;

import edu.rice.hj.Module0;
import edu.rice.hj.runtime.config.HjConfiguration;

public class HjTester {
    public static void main(String[] args) {
//        HjSystemProperty.runtime
        Module0.initializeHabanero();
        HjConfiguration.printConfiguredOptions();
        Module0.finalizeHabanero();
    }
}
