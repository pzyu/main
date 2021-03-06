package seedu.agendum.logic.commands;

import seedu.agendum.commons.core.Messages;
import seedu.agendum.commons.exceptions.IllegalValueException;
import seedu.agendum.logic.parser.DateTimeUtils;
import seedu.agendum.model.task.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adds a task to the to do list.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String COMMAND_FORMAT = "add <name>\n"
                                              + "add <name> by <deadline> \n"
                                              + "add <name> from <start time> to <end time>\n"
                                              + "add '<full task name not to be read as time>'";
    public static final String COMMAND_DESCRIPTION = "adds a task to Agendum";
    public static final String MESSAGE_SUCCESS = "Task added: %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " - "
            + COMMAND_DESCRIPTION + "\n"
            + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " Watch Star Wars\n"
            + "from 7pm to 9pm";

    private Task toAdd = null;

    //@@author A0003878Y
    /**
     * Convenience constructor using name
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name)
            throws IllegalValueException {
        this.toAdd = new Task(
                new Name(name)
        );
    }

    /**
     * Convenience constructor using name, end datetime
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, Optional<LocalDateTime> deadlineDate)
            throws IllegalValueException {
        this.toAdd = new Task(
                new Name(name),
                deadlineDate
        );
    }

    /**
     * Convenience constructor using name, start datetime, end datetime
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, Optional<LocalDateTime> startDateTime, Optional<LocalDateTime> endDateTime)
            throws IllegalValueException {
        Optional<LocalDateTime> balancedEndDateTime = endDateTime;
        if (startDateTime.isPresent() && endDateTime.isPresent()) {
            balancedEndDateTime = Optional.of(DateTimeUtils.balanceStartAndEndDateTime(startDateTime.get(), endDateTime.get()));
        }
        this.toAdd = new Task(
                new Name(name),
                startDateTime,
                balancedEndDateTime
        );
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(Messages.MESSAGE_DUPLICATE_TASK);
        }
    }

    //@@author

    public static String getName() {
        return COMMAND_WORD;
    }

    public static String getFormat() {
        return COMMAND_FORMAT;
    }

    public static String getDescription() {
        return COMMAND_DESCRIPTION;
    }

}

