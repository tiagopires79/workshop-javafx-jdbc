package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	public DepartmentService depService;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableViewColumnID;
	
	@FXML
	private TableColumn<Department, String> tableViewColumnName;
	
	@FXML
	private Button btNew;
	
	@FXML
	private ObservableList<Department> obsList;
	
	public void OnBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department department = new Department();
		createDialogForm(department, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService depService) {
		this.depService = depService;		
	}	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableViewColumnID.setCellValueFactory(new PropertyValueFactory<Department, Integer>("id"));
		tableViewColumnName.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));
		
		// Fazer altura do componente acompanhar a tela do windows
		// Pega uma referencia do palco principal
		Stage stage = (Stage) Main.getMainScene().getWindow();
		// atribui a propriedade de altura do palco principal para a propriedade de altura do componente de tela 
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());		
		
	}	
	
	public void updateTableView() {
		if (depService == null) {
			throw new IllegalStateException("Service was null");
		}		
		//List<Department> list = depService.findAll();
		//obsList = FXCollections.observableArrayList(list);
		//tableViewDepartment.setItems(obsList);	
		tableViewDepartment.setItems(FXCollections.observableArrayList(depService.findAll()));
	}
	
	private void createDialogForm (Department department, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController dFController = loader.getController();
		    dFController.setDepartment(department);
		    dFController.setDepartmentService(new DepartmentService());
		    dFController.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department Data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		}
		catch(IOException e){
			Alerts.showAlert("IO Exception", "Error Loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
