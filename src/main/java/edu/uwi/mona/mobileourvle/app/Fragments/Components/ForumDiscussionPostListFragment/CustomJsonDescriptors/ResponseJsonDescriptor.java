package edu.uwi.mona.mobileourvle.app.Fragments.Components.ForumDiscussionPostListFragment.CustomJsonDescriptors;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionContext;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.DiscussionPostListJsonDescriptor;

public class ResponseJsonDescriptor extends
	JSONObjectDescriptor<ResponseHolder> {

    @Override
    public JsonElement getJsonElement(final ResponseHolder object) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ResponseHolder getObjectFromJson(final JsonElement json) {
	final JsonObject jsonObject = (JsonObject) json;

	final boolean userCanReply = jsonObject.get("context")
		.getAsJsonObject().get("can_reply").getAsInt() == 1;

	final DiscussionContext context = new DiscussionContext(
		userCanReply);

	final DiscussionPost[] postList = JSONDecoder
		.getObject(new DiscussionPostListJsonDescriptor(),
			jsonObject.get("posts"));

	return new ResponseHolder(context, postList);

    }
}