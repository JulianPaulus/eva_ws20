package battleships.model;

public enum CoordinateState {
	EMPTY("-fx-background-color: #ffffff; -fx-border-color: black"),
	SHIP("-fx-background-color: #0004ff; -fx-border-color: black"),
	HIT("-fx-background-color: #ea1313; -fx-border-color: black"),
	MISS("-fx-background-color: #bdbdbd; -fx-border-color: black"),
	TARGETING("-fx-background-color: #e6f54f; -fx-border-color: black");

	private final String style;

	CoordinateState(final String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}
}
