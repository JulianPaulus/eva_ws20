package battleships.server.cli;

import battleships.server.exception.ArgumentParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArgumentParser {

	private Map<String, Argument<Object>> arguments;

	public ArgumentParser() {
		arguments = new HashMap<>();
	}

	public void parse(final String[] args) throws ArgumentParseException {
		List<String> parseErrors = new LinkedList<>();
		Iterator<String> iterator = Arrays.stream(args).iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();

			if ("-h".equals(name) || "--help".equals(name)) {
				printHelp();
				System.exit(0);
			}

			Argument<Object> argument = arguments.get(name);
			if (argument != null) {
				if (argument.getValueType().isAssignableFrom(Boolean.class)) {
					argument.setValue(true);
				} else if (iterator.hasNext()) {
					String value = iterator.next();
					if (!value.startsWith("-")) {
						if (Number.class.isAssignableFrom(argument.getValueType())) {
							double numberValue = Double.parseDouble(value);
							argument.setValue(numberValue);
						} else {
							argument.setValue(value);
						}
					} else {
						parseErrors.add(String.format("Argument parameter for '%s' cannot start with '-', did you forget to add a parameter?", name));
					}
				} else {
					parseErrors.add(String.format("Argument '%s' is missing it's parameter.", name));
				}
			} else {
				parseErrors.add(String.format("Unknown argument '%s'.", name));
			}
		}

		if (!parseErrors.isEmpty()) {
			throw new ArgumentParseException(String.join("\n", parseErrors));
		}
	}

	public void addArgument(final Argument<?> argument) {
		Argument<Object> objectArgument = (Argument<Object>) argument;
		arguments.put(argument.getName(), objectArgument);
	}

	public void printHelp() {
		System.out.println("Possible arguments:");
		for (final Argument<Object> argument : arguments.values()) {
			System.out.printf("\t%s: %s [%s]%n", argument.getName(), argument.getDescription(), argument.getValueType().getSimpleName());
		}
	}

	public void updateConfig() {
		arguments.forEach((name, argument) -> {
			if (argument.hasValue()) {
				argument.onAssignment();
			}
		});
	}
}
