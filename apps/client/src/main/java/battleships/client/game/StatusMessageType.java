package battleships.client.game;

public enum StatusMessageType {

	INFO("-fx-font-style: italic;"),
	CRITICAL("-fx-font-style: italic; -fx-text-fill: red; -fx-font-weight: bold;");

	private final String style;

	StatusMessageType(final String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}
}
