package battleships.model;

public enum CoordinateState {
	EMPTY("-fx-background-color: #ffffff; -fx-border-color: black"),
	SHIP("-fx-background-color: #3739ff; -fx-border-color: black"),
	HIT("-fx-background-color: #033F63; -fx-border-color: black"),
	MISS("-fx-background-color: #8d918b; -fx-border-color: black"),
	TARGETING("-fx-background-color: #FDE74C; -fx-border-color: black"),
	INVALID("-fx-background-color: #F3B61F; -fx-border-color: black");

	private final String style;

	CoordinateState(final String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}
}
