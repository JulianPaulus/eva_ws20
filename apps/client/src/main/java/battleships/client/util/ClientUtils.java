package battleships.client.util;

import javafx.scene.control.Control;

public class ClientUtils {

	public static void addClassWithoutDuplicate(final Control element, final String clazz) {
		if (!element.getStyleClass().contains(clazz)) {
			element.getStyleClass().add(clazz);
		}
	}

}
