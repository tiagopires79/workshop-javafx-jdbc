package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	public SellerService depService;

	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableViewColumnID;
	@FXML
	private TableColumn<Seller, String> tableViewColumnName;	
	@FXML
	private TableColumn<Seller, String> tableViewColumnEmail;
	@FXML
	private TableColumn<Seller, Date> tableViewColumnBirthDate;
	@FXML
	private TableColumn<Seller, Double> tableViewColumnBaseSalary;	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	@FXML
	private Button btNew;
	@FXML
	private ObservableList<Seller> obsList;

	public void OnBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller department = new Seller();
		createDialogForm(department, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService depService) {
		this.depService = depService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableViewColumnID.setCellValueFactory(new PropertyValueFactory<Seller, Integer>("id"));
		tableViewColumnName.setCellValueFactory(new PropertyValueFactory<Seller, String>("name"));
		tableViewColumnEmail.setCellValueFactory(new PropertyValueFactory<Seller, String>("email"));
		tableViewColumnBirthDate.setCellValueFactory(new PropertyValueFactory<Seller, Date>("birthDate"));
		Utils.formatTableColumnDate(tableViewColumnBirthDate, "dd/MM/yyyy");
		tableViewColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<Seller, Double>("baseSalary"));
		Utils.formatTableColumnDouble(tableViewColumnBaseSalary, 2);

		// Fazer altura do componente acompanhar a tela do windows
		// Pega uma referencia do palco principal
		Stage stage = (Stage) Main.getMainScene().getWindow();
		// atribui a propriedade de altura do palco principal para a propriedade de
		// altura do componente de tela
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (depService == null) {
			throw new IllegalStateException("Service was null");
		}
		// List<Seller> list = depService.findAll();
		// obsList = FXCollections.observableArrayList(list);
		// tableViewSeller.setItems(obsList);
		tableViewSeller.setItems(FXCollections.observableArrayList(depService.findAll()));
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller department, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setSeller(department);
			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller Data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error Loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChenged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if(result.get() == ButtonType.OK) {
			Alerts.injectedCorrectly(obj); // testa se objeto foi injetado corretamente e se não é nulo.
			try {
				depService.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Stop ! Error removind object...", null, e.getMessage(), AlertType.ERROR);
			}			
						
		}
		
	}

}
