/**
 *
 */
package edu.uwi.mona.mobileourvle.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.CourseContentAdapter;
import edu.uwi.mona.mobileourvle.classes.RecyclerItemClickListener;
import edu.uwi.mona.mobileourvle.classes.models.CourseModule;
import edu.uwi.mona.mobileourvle.classes.models.CourseSection;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.tasks.CourseContentsTask;
import edu.uwi.mona.mobileourvle.classes.tasks.ForumTask;

/**
 * @author Aston Hamilton
 */
public class CourseContentsFragment extends Fragment {

    private Listener mListener;
    private Activity mActivity;
    private int _id;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<CourseModule> moduleList = new ArrayList<>();
    private String token;


    public static CourseContentsFragment newInstance(int courseId) {
        final CourseContentsFragment f = new CourseContentsFragment();
        f.set_id(courseId);
        return f;
    }


    /*
    public void addSearchOption()
    {
        menu= ((Toolbar) getActivity().findViewById(R.id.course_toolbar)).getMenu();
        //add search button to menu
        MenuItem item = menu.add("Search");
        searchID = item.getItemId();

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SearchView searchView = new SearchView(getActivity());

        searchView.setOnQueryTextListener(new SearchListener());
        item.setActionView(searchView);
    }

    public void removeSearchOption()
    {
        menu= ((Toolbar) getActivity().findViewById(R.id.course_toolbar)).getMenu();
        //add search button to menu
        menu.removeItem(searchID);

    }*/



    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        try {
            mListener = (Listener) activity;
        } catch (final ClassCastException e) {
            mListener = new Listener() {

                @Override
                public void onCourseModuleSelected(final CourseModule module) {
                    Toast.makeText(mActivity,
                                   "Selected " + module.getName(), Toast.LENGTH_LONG)
                         .show();
                }
            };
        }

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

       // mEmptyListString = getString(R.string.no_course_contents);

    }

    @Override
    public void onStart() {
        super.onStart();

        //addSearchOption();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course_contents,
                container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.contentList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CourseContentAdapter(getActivity(),moduleList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mListener.onCourseModuleSelected(moduleList.get(position));
            }
        }));

        token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();
        new CourseSyncThread(token,get_id()).execute();
        return view;
    }
//
//    @Override
//    public void onListItemClick(final ListView l, final View v,
//                                final int position, final long id) {
//        mListener.onCourseModuleSelected(
//                (CourseModule) mCourseModuleListAdapter.getItem(position));
//    }

    @Override
    public void onStop() {
        //removeSearchOption();
        super.onStop();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    /* ======================== Private CLasses ====================== */
   /* private class SearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextChange(String query)
        {
            //list to hold filtered courses
            List<CourseSection> filteredContents = new ArrayList<CourseSection>();

            if(courseContents!=null) {
                for (final CourseSection courseSection : courseContents) {
                    List<CourseModule> filteredModules = new ArrayList<CourseModule>();
                    List<CourseModule> moduleList = new ArrayList<CourseModule>(courseSection.getModuleList());

                    for (CourseModule courseModule : moduleList)
                        if (courseModule.getLabel().toLowerCase().contains(query.toLowerCase()))
                            filteredModules.add(courseModule);

                    filteredContents.add(new CourseSection(courseSection.getName(), filteredModules));
                }

                moduleList.clear();

                for (CourseSection section : filteredContents)
                    if (section.getModuleList().size() > 0)
                        moduleList.addAll(section.getModuleList());

                mCourseModuleListAdapter.notifyDataSetChanged();
            }
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String s) {

            return false;
        }


    }
    */


    private class CourseSyncThread extends AsyncTask<String, Integer, Boolean> {
        CourseContentsTask contentsTask;
        int courseid;
        Boolean syncStatus;

        public CourseSyncThread(String token, int courseid) {
            contentsTask = new CourseContentsTask(token);
            this.courseid = courseid;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("Background execute", "course contents");
            syncStatus = contentsTask.syncCourseContents(courseid);
            ArrayList<CourseSection> sections = contentsTask.getCourseContents(courseid);

            // Save all sections into a listObject array for easy access inside
            //mapSectionsToListObjects(sections);

            new ForumTask(token).syncForums(courseid);

            if (sections == null)
                return false;

            // To avoid duplicates in listing
            moduleList.clear();

            //CourseSection section;
            ArrayList<CourseModule> modules;
            for (CourseSection section:sections) {
                modules = section.getModules();
                if (modules.size() > 0) {

                    // Add modules
                    for (CourseModule module:modules) {
                        moduleList.add(module);
                    }
                }
            }

            if (syncStatus)
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mAdapter.notifyDataSetChanged();
//            if (listObjects.size() != 0)
//                contentEmptyLayout.setVisibility(LinearLayout.GONE);
//            swipeLayout.setRefreshing(false);
        }

    }



    /* ========================== Interfaces ======================= */

    public interface Listener {
        void onCourseModuleSelected(CourseModule module);
    }
}
