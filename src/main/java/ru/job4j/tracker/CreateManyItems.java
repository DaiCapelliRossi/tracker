package ru.job4j.tracker;

import ru.job4j.tracker.action.UserAction;
import ru.job4j.tracker.input.*;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

public class CreateManyItems implements UserAction {
    private final Output out;

    public CreateManyItems(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Create many items";
    }


    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Create many items ===");
        int count = input.askInt("Введите кол-во заявок: ");
        for (int i = 0; i < count; i++) {
            tracker.add(new Item("Заявка № " + i));
        }
        out.println("Добавлено заявок: " + count);
        return true;
    }

    public static void main(String[] args) {
        Input validate = new ValidateInput(
                new ConsoleInput()
        );
        CreateManyItems createManyItems = new CreateManyItems(new ConsoleOutput());
        Store tracker = new MemTracker();
        createManyItems.execute(validate, tracker);
    }
}