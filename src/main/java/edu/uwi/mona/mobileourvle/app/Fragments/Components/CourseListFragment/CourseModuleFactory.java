/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Components.CourseListFragment;

import java.util.HashMap;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;

/**
 * @author Aston Hamilton
 */
public class CourseModuleFactory {
    public static final HashMap<String, Integer> MODULE_ICONS = new HashMap<String, Integer>() {
        /**
         *
         */
        private static final long serialVersionUID = 3138053826470134989L;

        {
            put("forum", R.drawable.chat_icon);
            put("chat", R.drawable.chat_icon);
            put("wiki", R.drawable.wiki_icon);
            put("glossary", R.drawable.wiki_icon);
            put("data", R.drawable.download_icon);
            put("resource", R.drawable.download_icon);
            put("survey", R.drawable.content_icon);
            put("choice", R.drawable.content_icon);
            put("quiz", R.drawable.content_icon);
            put("label", null);
        }
    };

    public static Integer getSystemIconResource(final CourseModule module) {
        if (CourseModuleFactory.MODULE_ICONS.containsKey(module.getName()))
            return CourseModuleFactory.MODULE_ICONS.get(module.getName());
        return R.drawable.content_icon;
    }
}
