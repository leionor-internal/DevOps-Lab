package com.example.app;

public class App {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println("=======================================");
        System.out.println(" DevOps CI/CD Lab - Java Maven App");
        System.out.println("=======================================");
        System.out.println("5 + 3 = " + calc.add(5, 3));
        System.out.println("5 - 3 = " + calc.subtract(5, 3));
        System.out.println("5 * 3 = " + calc.multiply(5, 3));
        System.out.println("5 / 3 = " + calc.divide(5, 3));
        System.out.println("App is running inside the container successfully.");

        // Keep the container alive so `docker run` doesn't exit immediately
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
