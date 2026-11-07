package proj;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Button btn = new Button("Click me!");
		
		btn.setOnAction(e -> welcome());
		
		StackPane root = new StackPane();
		root.getChildren().add(btn);
		Scene scene = new Scene(root, 500, 300);
		stage.setTitle("TBAA Management Systen");
		stage.setScene(scene);
		
		stage.show();
		
		
		}
	
	public static void welcome() {
		Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Welcome");
        
        ButtonType loginType = new ButtonType("Login");
        dialog.getDialogPane().getButtonTypes().addAll(loginType, ButtonType.CANCEL);
        
        
        dialog.showAndWait();
	}

}
