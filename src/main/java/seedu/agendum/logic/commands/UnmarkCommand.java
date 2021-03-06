package seedu.agendum.logic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import seedu.agendum.commons.core.Messages;
import seedu.agendum.commons.core.UnmodifiableObservableList;
import seedu.agendum.model.task.ReadOnlyTask;
import seedu.agendum.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.agendum.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0133367E
/**
 * Unmark task(s) identified using their last displayed indices in the task listing.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";    
    public static final String COMMAND_FORMAT = "unmark <id> <more ids>";
    public static final String COMMAND_DESCRIPTION = "unmark task(s) from completed";

    public static final String MESSAGE_UNMARK_TASK_SUCCESS = "Unmarked Task(s)!";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " - "
            + COMMAND_DESCRIPTION + "\n"
            + COMMAND_FORMAT + "\n"
            + "(The id must be a positive number)\n"
            + "Example: " + COMMAND_WORD + " 11-13 15";

    private ArrayList<Integer> targetIndexes;
    private ArrayList<ReadOnlyTask> tasksToUnmark;

    //@@author A0133367E    
    public UnmarkCommand(Set<Integer> targetIndexes) {
        this.targetIndexes = new ArrayList<>(targetIndexes);
        Collections.sort(this.targetIndexes);
        this.tasksToUnmark = new ArrayList<>();
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (isAnyIndexInvalid(lastShownList)) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
 
        for (int targetIndex: targetIndexes) {
            ReadOnlyTask taskToUnmark = lastShownList.get(targetIndex - 1);
            tasksToUnmark.add(taskToUnmark);
        }
        
        try {
            model.unmarkTasks(tasksToUnmark);
        } catch (TaskNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_MISSING_TASK);
        } catch (DuplicateTaskException pnfe) {
            model.resetDataToLastSavedList();
            return new CommandResult(Messages.MESSAGE_DUPLICATE_TASK);
        }

        return new CommandResult(MESSAGE_UNMARK_TASK_SUCCESS);
    }

    private boolean isAnyIndexInvalid(UnmodifiableObservableList<ReadOnlyTask> lastShownList) {
        return targetIndexes.stream().anyMatch(index -> index > lastShownList.size());
    }

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
