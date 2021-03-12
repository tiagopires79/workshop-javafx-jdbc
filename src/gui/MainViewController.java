package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
		
    @FXML
	private MenuItem menuItemSeller;
    @FXML
	private MenuItem menuItemDepartment;
    @FXML
	private MenuItem menuItemAbout;
    
    @FXML
    public void onMenuItemSellerAction() {
    	System.out.println("onMenuItemSellerAction");
    }
    
    @FXML
    public void onMenuItemDepartmentAction() {
    	loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
    		controller.setDepartmentService(new DepartmentService());
    		controller.updateTableView();
    	});    	
    }
    
    @FXML
    public void onMenuItemAboutAction() {
    	loadView("/gui/About.fxml", x -> {});
    }
		
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> inicializingAction) {
		try {
			// Carregando nova view
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//Lendo view passada via referencia de string como parametro
			VBox newVBox = loader.load();
			
			
			//Guardando referência da cena principal
			Scene mainScene = Main.getMainScene();			
			//Guardando nodo Vbox da MainView
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();			
			//Guardando Fihos "Nodos/Menus" da MainView 
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//Limpando todos os filhos do mainVBox
			mainVBox.getChildren().clear();
			
			//Adicionando novamente filhos do mainVBox
			mainVBox.getChildren().add(mainMenu);			
			//Adicionando filhos de newVbox
			mainVBox.getChildren().addAll(newVBox.getChildren());			
			
			T controller = loader.getController();
			inicializingAction.accept(controller);
		}
		catch(IOException e){
			//e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);	
		}
	}
}
