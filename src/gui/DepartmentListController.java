package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableViewColumnID;
	
	@FXML
	private TableColumn<Department, String> tableViewColumnName;
	
	@FXML
	private Button btNew;
	
	public void OnBtNewAction() {
		System.out.println("OnBtNewAction");
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

}
