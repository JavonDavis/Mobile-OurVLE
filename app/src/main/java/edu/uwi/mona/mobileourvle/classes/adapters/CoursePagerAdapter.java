package edu.uwi.mona.mobileourvle.classes.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.uwi.mona.mobileourvle.fragments.CourseContentsFragment;
import edu.uwi.mona.mobileourvle.fragments.CourseListFragment;
import edu.uwi.mona.mobileourvle.fragments.ForumListFragment;
import edu.uwi.mona.mobileourvle.fragments.ParticipantsListFragment;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/3/15.
 */
public class CoursePagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Resources", "Forums", "Participants" };
    private int courseId;


    public CoursePagerAdapter(FragmentManager fm, int courseid) {
        super(fm);
        this.courseId = courseid;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return CourseContentsFragment.newInstance(courseId);
            case 1:
                return ForumListFragment.newInstance(courseId);
            case 2:
                return ParticipantsListFragment.newInstance(courseId);

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
