package ru.netology.autounit.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@SelectClasspathResource("ru/netology/autounit/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ru.netology.autounit.steps")
public class CucumberTestRunner {
}