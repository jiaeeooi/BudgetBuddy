package application.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class NewAccountController {
	
	@FXML DatePicker datePicker;
	
	@FXML
	public void initialize() {
		datePicker.setValue(LocalDate.now());
	}
	
	
}
