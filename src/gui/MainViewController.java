package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class MainViewController implements Initializable{
	int count;
	
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
    	loadView("/gui/DepartmentList.fxml");    	
    }
    
    @FXML
    public void onMenuItemAboutAction() {
    	loadView("/gui/About.fxml");
    }
		
	@Override
	public void initialize(URL uri, ResourceBundle rb) {	
	}
	
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//Lendo view passada via referencia de string como parametro
			VBox newVBox = loader.load();
			//Guardando referencia da cena principal
			Scene mainScene = Main.getMainScene();
			//Guardando informacoes do mainVBox
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			//Guardando informacoes dos filhos "Nodos" do mainMenu
			Node mainMenu = mainVBox.getChildren().get(0);
			//Limpando todos os filhos do mainVBox
			mainVBox.getChildren().clear();
			//Adicionando novamente filhos do mainVBox
			mainVBox.getChildren().add(mainMenu);		
			//Adicionando filhos de newVbox
			mainVBox.getChildren().addAll(newVBox.getChildren());			
		}
		catch(IOException e){
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
