package main;

import graph.test.ListGraphTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by Paweł Kopeć on 26.12.16.
 *
 * Tester class automates tests in Junit test framework.
 */
public class Tester {

    public static void runAllTests() {
        runClassTest(ListGraphTest.class);
    }

    public static void runClassTest(Class classTest) {
        Result result = JUnitCore.runClasses(classTest);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}