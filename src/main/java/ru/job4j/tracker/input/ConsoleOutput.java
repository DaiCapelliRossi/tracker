package ru.job4j.tracker.input;

public class ConsoleOutput implements Output {

    @Override
    public void println(String println) {
        System.out.println(println);
    }
}
