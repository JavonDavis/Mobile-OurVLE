/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course;

import java.util.ArrayList;
import java.util.List;

import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin.OnReloadFragmentListener;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.PinnedHeaderListAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedFragment;
import edu.uwi.mona.mobileourvle.app.Classes.CourseContentAdapter;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.CourseSection;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Courses.CourseSectionDescriptor;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetCourseContents;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.CourseListFragment.CourseModuleFactory;

/**
 * @author Aston Hamilton
 */
public class CourseContentsFragment extends AuthenticatedFragment implements
        OnCommunicationResponseListener, OnReloadFragmentListener {

    private MoodleCourse mCourse;

    private static CourseContentAdapter mCourseModuleListAdapter;

    private static List<CourseSection> courseContents;

    private Listener mListener;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<CourseModule> moduleList = new ArrayList<>();

    private DefaultCommunicationModulePlugin mCommunicatioModulePlugin;

    private Activity mActivity;

    private String mEmptyListString;

    private Menu menu;
    private int searchID;

    public static CourseContentsFragment newInstance(
            final UserSession session,
            final MoodleCourse course) {
        final CourseContentsFragment f = new CourseContentsFragment();

        f.setUserSession(session);
        f.setMoodleCourse(course);

        return f;
    }

    public void setMoodleCourse(final MoodleCourse course) {
        getFragmentArguments().putParcelable(ParcelKeys.MOODLE_COURSE,
                new MoodleCourseParcel(course));
        mCourse = course;
    }



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

    }



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
        mCourse = ((MoodleCourseParcel) getFragmentArguments()
                .getParcelable(ParcelKeys.MOODLE_COURSE))
                .getWrappedObejct();

        mCommunicatioModulePlugin = new DefaultCommunicationModulePlugin(this);

        registerPlugin(mCommunicatioModulePlugin);

        //mCourseModuleListAdapter = new CourseContentAdapter(getActivity(),moduleList);

//        setListAdapter(mCourseModuleListAdapter);


        super.onCreate(savedInstanceState);

       // mEmptyListString = getString(R.string.no_course_contents);

    }

    @Override
    public void onStart() {
        super.onStart();

        loadCourseContents();
        addSearchOption();
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
    public void onCommunicationError(final ResponseError response) {
        mCommunicatioModulePlugin.defaultResponseError(response);
    }

    @Override
    public void onCommunicationResponse(final int requestId,
                                        final ResponseObject response) {
        //setEmptyText(mEmptyListString);
        switch (requestId) {
            case Requests.GET_COURSE_CONTENTS:
                    mCommunicatioModulePlugin.turnOffLoadingIcon();

                    final List<CourseSection> courseSectionList = JSONDecoder
                            .getObjectList(new CourseSectionDescriptor(mCourse.getId().toString()),
                                    response.getResponseText());


                    courseContents = courseSectionList;


                //mCourseModuleListAdapter.clearPartitions();

                for (final CourseSection section : courseContents)
                    if (section.getModuleList().size() > 0)
                        moduleList.addAll(section.getModuleList());

                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void loadCourseContents() {
        mCommunicatioModulePlugin.turnOnLoadingIcon();

        //setEmptyText("Loading contents....");
        CommuncationModule.sendAsyncRequest(
                mActivity,
                new GetCourseContents(
                        getUserSession(), mCourse),
                Requests.GET_COURSE_CONTENTS, this);
    }

    @Override
    public void onCommunicationMenuItemTriggered() {
        //addSearchOption();
        loadCourseContents();
    }

    @Override
    public void onStop() {
        CommuncationModule.cancelAllRunningAsyncRequests(this);
        removeSearchOption();
        super.onStop();
    }

    /* ======================== Private CLasses ====================== */
    private class SearchListener implements SearchView.OnQueryTextListener {

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
    private static class CourseModuleListAdapter extends
            PinnedHeaderListAdapter<CourseSection, CourseModule> {

        public CourseModuleListAdapter(final Context context) {

            super(context, R.layout.list_header_simple_pinned,
                    R.layout.list_item_course_module_list);
        }

        class RowViewHolder implements SimpleViewHolder {
            ImageView icon;
            TextView firstLine;
        }

        @Override
        protected SimpleViewHolder getRowViewHolder(
                final View rowView) {
            final RowViewHolder h = new RowViewHolder();
            h.icon = (ImageView) rowView
                    .findViewById(R.id.imageview_indicator);
            h.firstLine = (TextView) rowView
                    .findViewById(R.id.textview_first_line);
            return h;
        }

        @Override
        protected void bindRowView(
                final CourseModule rowData,
                final SimpleViewHolder rowViewHolder) {
            final RowViewHolder h = (RowViewHolder) rowViewHolder;

            if (CourseModuleFactory.getSystemIconResource(rowData) == null) {
                h.icon.setVisibility(View.GONE);
                h.firstLine.setText(":: " + rowData.getLabel().trim());
            } else {
                h.icon.setVisibility(View.VISIBLE);
                h.icon.setImageResource(CourseModuleFactory.getSystemIconResource(rowData));
                h.firstLine.setText(rowData.getLabel().trim());
            }
        }
    }



    /* ========================== Interfaces ======================= */

    public static interface Listener {
        public void onCourseModuleSelected(CourseModule module);
    }

    private static interface Requests {
        int GET_COURSE_CONTENTS = 0;

    }
}
