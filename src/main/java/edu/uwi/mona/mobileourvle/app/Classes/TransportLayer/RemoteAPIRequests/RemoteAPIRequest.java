/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests;

import org.apache.http.HttpStatus;
import org.sourceforge.ah.android.utilities.Communication.Request.RequestObject;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.APIEndpoints;

/**
 * @author Aston Hamilton
 * 
 */
public class RemoteAPIRequest extends RequestObject {

    protected RemoteAPIRequest(final Object... objects) {
	super(objects);
    }

    /**
     * Remote functions can intercept responses eg. if an error occurs, it will
     * be in JSON since that's how Moodle represents its errors but the function
     * can catch it and clean it to create a ResponseError, which the mobile API
     * sees as an error or for whatever other reason it might want to intercept
     * the response.
     * 
     * This make the functions very powerful and acts as the bridge between
     * moodle login and mobile login
     * 
     * @param responseObject
     *            the response object
     * @return the processed response object
     */
    @Override
    public ResponseObject getProcessedResponseObject(
	    final ResponseObject responseObject) {

	if (responseObject.getStatus() != HttpStatus.SC_OK)
	    return new ResponseError(responseObject.getStatus(),
		    responseObject.getResponseText());

	final JsonElement responseJSONObject;

	try {
	    responseJSONObject = new JsonParser().parse(responseObject
		    .getResponseText());

	    if (responseJSONObject instanceof JsonObject)
		if (((JsonObject) responseJSONObject).has("error")) {
		    final String errorMessage = ((JsonObject) responseJSONObject)
			    .get("error").getAsString();
		    return new ResponseError(
			    HttpStatus.SC_INTERNAL_SERVER_ERROR,
			    errorMessage);
		} else if (((JsonObject) responseJSONObject).has("exception")) {
		    final String errorMessage = ((JsonObject) responseJSONObject)
			    .get("message").getAsString();

		    if (((JsonObject) responseJSONObject).has("debuginfo")) {
			final String debugMessage = ((JsonObject) responseJSONObject)
				.get("debuginfo").getAsString();
			System.out.println(debugMessage);
		    }

		    return new ResponseError(
			    HttpStatus.SC_INTERNAL_SERVER_ERROR, errorMessage);
		}
	} catch (final JsonParseException e) {
	    return new ResponseError(HttpStatus.SC_INTERNAL_SERVER_ERROR,
		    responseObject.getResponseText());
	}

	return responseObject;
    }

    public String getEndpoint() {
	return APIEndpoints.Shared.REST_API_ENDPOINT;
    }

    @Override
    public RequestMethod getRequestMethod() {
	return RequestMethod.Post;
    }

    @Override
    public String getReuqestUrl() {
	return APIEndpoints.Shared.API_HOST + getEndpoint();
    }
}
