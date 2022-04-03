package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Client extends Application {
	
	PrintWriter pw;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Clients Chat");
		
		BorderPane borderPane = new BorderPane();
		
		Label labelHost = new Label("Host:");
		TextField textfieldHost = new TextField("Localhost");
		
		Label labelPort = new Label("Port:");
		TextField textfieldPort = new TextField("1231");
				
	    Button btnConnecter = new Button("Connecter");
	    
	    HBox hbox = new HBox();
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(10));
		hbox.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
		hbox.getChildren().addAll(labelHost,textfieldHost,labelPort,textfieldPort,btnConnecter);
		
		borderPane.setTop(hbox);
		
		ObservableList<String> listModel = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<String>(listModel);
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10));
		vBox.getChildren().add(listView);
		borderPane.setCenter(vBox);
		
		Label labelMessage = new Label("Message:");
		TextField textfieldMessage = new TextField();
		textfieldMessage.setPrefWidth(250);
		
		Button btnEnvoyer = new Button("Envoyer");
		
		HBox hbox2 = new HBox();
		hbox2.setSpacing(10);
		hbox2.setPadding(new Insets(10));
		hbox2.setBackground(new Background(new BackgroundFill(Color.AZURE, null, null)));
		hbox2.getChildren().addAll(labelMessage,textfieldMessage,btnEnvoyer);
		
		borderPane.setBottom(hbox2);
		
		Scene scene = new Scene(borderPane,500,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		btnConnecter.setOnAction((evt)->{
			
			String host = textfieldHost.getText();
			int port = Integer.parseInt(textfieldPort.getText());
			
			try {
				
				Socket socket = new Socket(host,port);
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is); 
				BufferedReader br = new BufferedReader(isr);
				OutputStream os = socket.getOutputStream();
				pw = new PrintWriter(os,true);
				
				new Thread(()-> {
					
					while(true) {
						try {
							String reponse = br.readLine();
							Platform.runLater(()->{
								listModel.add(reponse);
							});
						} catch(IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}).start();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		});
		
		btnEnvoyer.setOnAction((evt)->{
			String message = textfieldMessage.getText();
			pw.println(message);
		});
		
	}

}
