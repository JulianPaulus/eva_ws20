package battleships.server.cli;

public class Argument<T> {

	private final String name;
	private final String description;
	private final Class<T> valueType;
	private final ArgumentRunnable<T> onAssignment;

	private T value;

	public Argument(final String name, final String description, final Class<T> valueType) {
		this(name, description, valueType, null);
	}

	public Argument(final String name, final String description, final Class<T> valueType, final ArgumentRunnable<T> onAssignment) {
		this.name = name;
		this.description = description;
		this.valueType = valueType;
		this.onAssignment = onAssignment;

		if (valueType.isAssignableFrom(Boolean.class)) {
			value = valueType.cast(false);
		}
	}

	public T getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public boolean hasValue() {
		return value != null;
	}

	public void setValue(final T value) {
		this.value = value;
	}

	public Class<T> getValueType() {
		return valueType;
	}

	public String getDescription() {
		return description;
	}

	public void onAssignment() {
		if (onAssignment != null && value != null) {
			onAssignment.run(value);
		}
	}
}
