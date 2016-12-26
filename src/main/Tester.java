package main;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.function.Consumer;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Tester class automates tests in Junit test framework.
 */
public class Tester {

    private Consumer<String> logger;

    public Tester(Consumer<String> logger) {
        this.logger = logger;
    }

    public Tester() {
        this.logger = System.out::println;
    }

    public void runClassTest(Class classTest) {
        Result result = JUnitCore.runClasses(classTest);

        for (Failure failure : result.getFailures()) {
            logger.accept(failure.toString());
        }

        logger.accept("Result of testing " + classTest.getName() +
                (result.wasSuccessful() ? " positive" : " negative"));
    }
}