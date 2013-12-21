/**
 *
 */
package com.mits.mobile.ourvle.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SiteInfo;

/**
 * @author Aston Hamilton
 */
public class SiteInfoParcel extends
        SimpleParcableWrapper<SiteInfo> {

    public SiteInfoParcel(final SiteInfo siteInfo) {
        super(siteInfo);
    }

    public SiteInfoParcel(final Parcel in) {
        super(in);
    }

    @Override
    protected SiteInfo getObjectFromStream(final Parcel in) {
        final String siteName = in.readString();
        final int fronPageId = in.readInt();
        final CourseForumParcel newsForumParcel = in
                .readParcelable(CourseForumParcel.class.getClassLoader());
        final String siteUrl = in.readString();

        return new SiteInfo(siteName, fronPageId,
                            newsForumParcel.getWrappedObejct(), siteUrl);
    }

    @Override
    protected void writeObjectToParcel(final SiteInfo wrappedObject,
                                       final Parcel parcel) {
        parcel.writeString(wrappedObject.getName());
        parcel.writeInt(wrappedObject.getFrontPageId());
        parcel.writeParcelable(
                new CourseForumParcel(wrappedObject.getNewsForum()), 0);
        parcel.writeString(wrappedObject.getUrl());
    }

    /**
     * The Constant CREATOR.
     */
    public static final Parcelable.Creator<SiteInfoParcel> CREATOR =
            new SimpleParcableWrapperCreator<SiteInfoParcel>(
                    SiteInfoParcel.class);
}
