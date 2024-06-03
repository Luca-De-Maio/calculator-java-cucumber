Feature: Calculator Operations

  Scenario Outline: Perform operations with different inputs
    Given I have the numbers '<num1>' and '<num2>'
    When I perform '<operation>'
    Then the result should be '<expected>'

    Examples:
      | operation  | num1                   | num2                   | expected                   |
      | add        | 10                     | 0.5                    | 10.5                       |
      | add        | 0.1                    | 0.2                    | 0.3                        |
      | add        | 0000010                | 0000020                | 30.0                       |
      | add        | 1.0E308                | 1.0E308                | Infinity                   |
      | add        | 9999999999999999       | 1                      | 1.0E16                     |
      | add        | 1.0E-308               | 1.0E-308               | 2.0E-308                   |
      | add        | 0.0000000000000000001  | 0.20                   | 0.2                        |
      | subtract   | 10                     | -5.5                   | 15.5                       |
      | subtract   | -1.0E308               | 1.0E308                | -Infinity                  |
      | subtract   | -1.0E-308              | 1.0E-308               | -2.0E-308                  |
      | multiply   | 1.123456789            | 1.987654321            | 2.233043741112635          |
      | multiply   | 1.0E154                | 1.0E154                | 1.0E308                    |
      | multiply   | 0                      | 1.0E308                | 0.0                        |
      | divide     | 1                      | 0.000001               | 1000000.0                  |
      | divide     | 1                      | 1.0E-308               | 1.0E308                    |
      | divide     | 1                      | 0                      |Error: Cannot divide by zero|
