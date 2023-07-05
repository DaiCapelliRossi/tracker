package ru.job4j.tracker;

import ru.job4j.tracker.action.UserAction;
import ru.job4j.tracker.input.*;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import java.util.List;

public class DeleteAllItems implements UserAction {
    private final Output out;

    public DeleteAllItems(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Delete all items";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Delete all items ===");
        List<Item> allItems = tracker.findAll();
        List<Integer> collect = allItems.stream()
                .map(Item::getId).toList();
        for (Integer integer : collect) {
            tracker.delete(integer);
        }
        out.println("=== Все заявки удалены ===");
        return true;
    }

    public static void main(String[] args) {
        Input validate = new ValidateInput(
                new ConsoleInput()
        );
        DeleteAllItems deleteAllItems = new DeleteAllItems(new ConsoleOutput());
        Store tracker = new MemTracker();
        deleteAllItems.execute(validate, tracker);
    }
}