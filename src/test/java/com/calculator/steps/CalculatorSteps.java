package com.calculator.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import static org.testng.AssertJUnit.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CalculatorSteps {

    private String num1;
    private String num2;
    private String result;

    @Given("I have the numbers {string} and {string}")
    public void i_have_the_numbers_and(String num1, String num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    @When("I perform {string}")
    public void i_perform(String operationType) throws IOException {
        this.result = executeCommand(operationType, num1, num2);
    }

    @Then("the result should be {string}")
    public void the_result_should_be(String expected) {
        assertEquals(expected, extractResult(result));
    }

    private String executeCommand(String operation, String num1, String num2) throws IOException {
        CommandLine cmdLine = new CommandLine("docker");
        cmdLine.addArgument("run");
        cmdLine.addArgument("--rm");
        cmdLine.addArgument("public.ecr.aws/l4q9w4c5/loanpro-calculator-cli");
        cmdLine.addArgument(operation);
        cmdLine.addArgument(num1);
        cmdLine.addArgument(num2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);

        executor.execute(cmdLine);

        return outputStream.toString().trim();
    }

    private String extractResult(String output) {
        if (output.startsWith("Result: ")) {
            return output.substring(8).trim();
        }
        return output;
    }
}
