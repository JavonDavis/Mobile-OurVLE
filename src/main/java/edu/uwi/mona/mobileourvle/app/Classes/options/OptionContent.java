package edu.uwi.mona.mobileourvle.app.Classes.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class OptionContent {

    /**
     * An array of sample home options.
     */
    public static List<Option> ITEMS = new ArrayList<Option>();

    /**
     * A map of sample home options, by ID.
     */
    public static Map<String, Option> ITEM_MAP = new HashMap<String, Option>();

    static {
        // Add all items here
        addItem(new Option("1", "Courses"));
        addItem(new Option("2", "Assignments"));
        addItem(new Option("3", "Quizzes"));
        addItem(new Option("4", "Messages"));
        addItem(new Option("5", "Forums"));
        addItem(new Option("6", "Calendar"));
        addItem(new Option("7", "Settings"));
    }

    private static void addItem(Option item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static int getCount()
    {
        return ITEMS.size();
    }

    public static Option getOption(int position)
    {
        return ITEMS.get(position);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Option {
        public String id;
        public String content;

        public Option(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
