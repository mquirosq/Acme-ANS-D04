
package acme.helpers;

import acme.client.helpers.MessageHelper;

public class InternationalisationHelper {

	public static String internationalizeBoolean(final boolean bool) {
		return MessageHelper.getMessage(bool ? "acme.value.boolean.true" : "acme.value.boolean.false");
	}

}
