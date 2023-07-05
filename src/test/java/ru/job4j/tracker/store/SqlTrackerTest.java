package ru.job4j.tracker.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.Item;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

public class SqlTrackerTest {

    private static Connection connection;

    @BeforeAll
    public static void initConnection() {
        try (InputStream in = new FileInputStream("db/liquibase_test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @AfterEach
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(item.getId())).isEqualTo(item);
    }
    @Test
    public void whenReplaceItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        Item item1 = new Item("item1");
        tracker.add(item);
        tracker.replace(item.getId(), item1);
        assertThat(tracker.findById(item1.getId())).isEqualTo(item1);
    }
    @Test
    public void whenDeleteItemAndFindByGeneratedIdThenMustBeNull() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        tracker.delete(item.getId());
        assertNull((tracker.findById(item.getId())));
    }

    @Test
    public void whenFindAll() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        Item item1 = new Item("item1");
        Item item2 = new Item("item2");
        tracker.add(item);
        tracker.add(item1);
        tracker.add(item2);
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item1);
        List<Item> trackerItems = tracker.findAll();
        assertThat(items.size()).isEqualTo(trackerItems.size());
        for (int i = 0; i < items.size(); i++) {
            assertThat(items.get(i)).isEqualTo(trackerItems.get(i));
        }
    }
    @Test
    public void whenFindByName() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        Item item1 = new Item("item");
        Item item2 = new Item("item2");
        tracker.add(item);
        tracker.add(item1);
        tracker.add(item2);
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item1);
        List<Item> trackerItems = tracker.findByName("item");
        assertThat(items.size()).isEqualTo(trackerItems.size());
        for (int i = 0; i < items.size(); i++) {
            assertThat(items.get(i)).isEqualTo(trackerItems.get(i));
        }
    }
    @Test
    public void whenFindById() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        Item trackerItem = tracker.findById(item.getId());
        assertThat(tracker.findById(item.getId())).isEqualTo(trackerItem);
    }




}