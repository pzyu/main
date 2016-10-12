package seedu.agendum.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.agendum.model.task.ReadOnlyTask;

public class UpcomingTaskCard extends UiPart {
    
    private static final String FXML = "UpcomingTaskCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label tags;
    @FXML
    private Label time;

    private ReadOnlyTask task;
    private int displayedIndex;

    public UpcomingTaskCard(){

    }

    public static UpcomingTaskCard load(ReadOnlyTask task, int displayedIndex){
        UpcomingTaskCard card = new UpcomingTaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(task.getName().fullName);
        id.setText(displayedIndex + ". ");
        time.setText(formatTime());
        tags.setText(task.tagsString());
    }
    
    public String formatTime() {
        StringBuilder sb = new StringBuilder();
        Optional<LocalDateTime> start = task.getStartDateTime();
        Optional<LocalDateTime> end = task.getEndDateTime();
        
        DateTimeFormatter startFormat = DateTimeFormatter.ofPattern("EEE, dd MMM");
        
		if(start.isPresent()) {
			sb.append("from ").append(start.get().format(startFormat));
		}
		if(end.isPresent()) {
			sb.append(sb.length()>0 ? " to " : "by ");
			sb.append(end.get().format(startFormat));
		}
        
        return sb.toString();
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}