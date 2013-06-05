package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import java.util.Iterator;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;

public class DiscussionPostListJsonDescriptor extends
	JSONObjectDescriptor<DiscussionPost[]> {

    @Override
    public JsonElement getJsonElement(final DiscussionPost[] object) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public DiscussionPost[] getObjectFromJson(
	    final JsonElement json) {
	final JsonArray discussionsListJson = (JsonArray) json;
	final DiscussionPost[] discussionList = new DiscussionPost[
		discussionsListJson.size()];

	final Iterator<JsonElement> iter = discussionsListJson.iterator();

	int i = 0;
	while (iter.hasNext()) {
	    final JsonObject extendedDiscussionJson = (JsonObject) iter
		    .next();
	    final DiscussionPost discussion = (DiscussionPost) JSONDecoder
		    .getObject(new DiscussionPostDescriptior(),
			    extendedDiscussionJson);

	    discussionList[i] = discussion;
	    i++;
	}

	return discussionList;
    }

}