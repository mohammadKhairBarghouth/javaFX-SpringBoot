package Frontend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import Backend.Kunde;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {
	Stage mainWindow;
	TableView<Kunde> kunden;
	BorderPane gPane;
	Connection con;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/messstellenbetrieb", "postgres", "123");
		}catch(Exception e) {
			
		}
		TabPane tp = new TabPane();
		Tab kundenTab = new Tab("Kundnen");
		Tab zaehlernTab = new Tab("Zählern");
		Tab adressenTab = new Tab("Adressen");
		Tab messlokationenTab = new Tab("Messlokationen");
		tp.getTabs().addAll(kundenTab, zaehlernTab, adressenTab, messlokationenTab);


		generateTableView();
		ScrollPane sp = new ScrollPane();
		sp.setContent(kunden);
		kundenTab.setContent(sp);

		VBox search = new VBox();
		search.setStyle("-fx-padding: 5px");
		search.setSpacing(5);
		
		TextField CostumerfName = new TextField();
		Label CostumerfNameL = new Label("Vorname");
		TextField CostumerlName = new TextField();
		Label CostumerlNameL = new Label("Nachname");
		TextField CostumerMlId = new TextField();
		Label CostumerMlIdL = new Label("Messlokationsid");
		TextField CostumerAdress = new TextField();
		Label CostumerAdressL = new Label("Adresse");

		Button searchButton = new Button("Suchen");
		searchButton.setStyle("-fx-padding: 5px 10px");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				String vorname = CostumerfName.getText();
				if(vorname.matches("\s*")) {
					vorname = null;
				}
				
				String nachname = CostumerlName.getText();
				if(nachname.matches("\s*")) {
					nachname = null;
				}
				
				Kunde k = new Kunde(vorname, nachname);
				System.out.print(1);
				if(!CostumerMlId.getText().equals("") && isInt(CostumerMlId.getText())) {
					k.setMlID(Integer.parseInt( CostumerMlId.getText()));
				}
				System.out.print(2);
				if(!CostumerAdress.getText().equals("") && isInt(CostumerAdress.getText())) {
					k.setAdresseid(Integer.parseInt( CostumerAdress.getText()));
				}
				
				kunden.getItems().clear();
				kunden.setItems(getKunden(k));
			}
		});
		
		Button addButton = new Button("neue Kunde zufügen");
		
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addNewCostumer(false, null);

			}
		});
		
		Button updateButton = new Button("Daten bearbeiten");
		
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Kunde k1 = kunden.getFocusModel().getFocusedItem();
				addNewCostumer(true, k1);
			}

			
		});
		
		updateButton.setTranslateX(11);
		updateButton.setTranslateY(70);
		updateButton.setPrefWidth(126);
		
		addButton.setTranslateX(11);
		addButton.setTranslateY(130);
		
		Button dropButton = new Button("löschen");
		
		dropButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent v) {
				Kunde k1 = kunden.getFocusModel().getFocusedItem();
				if(k1.getAdresseid() == 0) {
					k1.setAdresseid(null);
				}
				if(k1.getMlID() == 0) {
					k1.setMlID(null);
				}
						
				Kunde.Drop(k1, con);
				kunden.setItems(getKunden(new Kunde(null,null)));
			}
		});
		
		dropButton.setTranslateX(11);
		dropButton.setTranslateY(10);
		dropButton.setPrefWidth(126);
		
		search.getChildren().addAll(CostumerfNameL, CostumerfName, CostumerlNameL, CostumerlName, CostumerMlIdL,
				CostumerMlId, CostumerAdressL, CostumerAdress, searchButton,addButton,updateButton,dropButton);

		gPane = new BorderPane();
		gPane.setCenter(tp);
		gPane.setLeft(search);
		Scene scene = new Scene(gPane);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	
	boolean isInt(String s) {
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) < 48 || s.charAt(i) > 57) {
				return false;
			}
		}
		return true;
	}
	
	void addNewCostumer(boolean Update, Kunde k2) {
		Stage subStage = new Stage();
		
		String  c1 ="",  c2 ="",  c3 ="",  c4 ="";
		
		if(Update) {
			c1 = k2.getVorname();
			c2 = k2.getNachname();
			if(k2.getMlID() != null && k2.getMlID() != 0) {
				c3 = k2.getMlID() + "";
			}else {
				c3 = "";
			}
			
			if(k2.getAdresseid() != null && k2.getAdresseid() != 0) {
				c4 = k2.getAdresseid() + "";
			}else {
				c4 = "";
			}
		}
		
		TextField CostumerfName = new TextField(c1);
		Label CostumerfNameL = new Label("Vorname");
		TextField CostumerlName = new TextField(c2);
		Label CostumerlNameL = new Label("Nachname");
		TextField CostumerMlId = new TextField(c3);
		Label CostumerMlIdL = new Label("Messlokationsid");
		TextField CostumerAdress = new TextField(c4);
		Label CostumerAdressL = new Label("Adresse");
		
		String c;
		if(Update) {
			c = "aktualisieren";
		}else {
			c  = "Zufügen";
		}
		
		Button addButton = new Button(c);
		addButton.setStyle("-fx-padding: 5px 10px");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String vorname = CostumerfName.getText();
				String nachname = CostumerlName.getText();
				if(vorname.matches("\\s*") ||nachname.matches("\\s*") ) {
					vorname = null;
					Alert a = new Alert(Alert.AlertType.WARNING,"Vor- oder Nachname sind nicht eingegeben!");
					a.show();
				}else {
					Kunde k = new Kunde(vorname,nachname);
					
					if(!CostumerMlId.getText().equals("") && isInt(CostumerMlId.getText())) {
						k.setMlID(Integer.parseInt( CostumerMlId.getText()));
					}

					if(!CostumerAdress.getText().equals("") && isInt(CostumerAdress.getText())) {
						k.setAdresseid(Integer.parseInt( CostumerAdress.getText()));
					}
					
					CostumerfName.setText("");
					CostumerlName.setText("");
					CostumerMlIdL.setText("");
					CostumerAdress.setText("");
					
					
					
					if(Update) {
						if(k2.getAdresseid() == 0) {
							k2.setAdresseid(null);
						}
						if(k2.getMlID() == 0) {
							k2.setMlID(null);
						}
						Kunde.Update(k2, k, con);
						subStage.close();
					}else {
						k.addToDB(con);
					}
					kunden.setItems(getKunden(new Kunde(null,null)));				
				}
			}			
		});
		
		
		
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10)); 
		gp.setHgap(10); 
        gp.setVgap(10);
		 
		gp.add(CostumerfNameL, 0, 0);
		gp.add(CostumerfName, 0, 1);
		gp.add(CostumerlNameL, 1, 0);
		gp.add(CostumerlName, 1, 1);
		gp.add(CostumerMlIdL, 0, 2);
		gp.add(CostumerMlId, 0, 3);
		gp.add(CostumerAdressL, 1, 2);
		gp.add(CostumerAdress, 1, 3);
		gp.add(addButton, 0, 4);
		
		
		subStage.setScene(new Scene(gp));
		subStage.showAndWait();
	}
	
	

	void generateTableView() {
		TableColumn<Kunde, String> vorname = new TableColumn<>("vorname");
		TableColumn<Kunde, String> nachname = new TableColumn<>("nachname");
		TableColumn<Kunde, Integer> aid = new TableColumn<>("Adresse-ID");
		TableColumn<Kunde, Integer> mlid = new TableColumn<>("Messlokation-ID");

		vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		aid.setCellValueFactory(new PropertyValueFactory<>("adresseid"));
		mlid.setCellValueFactory(new PropertyValueFactory<>("mlID"));

		kunden = new TableView();
		kunden.setItems(getKunden(new Kunde(null, null)));

		kunden.getColumns().addAll(vorname, nachname, aid, mlid);

	}

	public ObservableList<Kunde> getKunden(Kunde k) {
		ObservableList<Kunde> kunden = FXCollections.observableArrayList();

		
		try {
			

			ResultSet r = Kunde.setResultSet(k, con);

			while (r.next()) {
				String vorname = "", nachname = "", adresseid = "", mlid = "";
				if (r.getString("vorname") != null) {
					vorname = r.getString("vorname");
				}
				if (r.getString("nachname") != null) {
					nachname = r.getString("nachname");
				}
				if (r.getString("adresseid") != null) {
					adresseid = r.getString("adresseid");
				} else {
					adresseid = "0";
				}
				if (r.getString("messlokationid") != null) {
					mlid = r.getString("messlokationid");
				} else {
					mlid = "0";
				}

				kunden.add(new Kunde(vorname, nachname, Integer.parseInt(adresseid), Integer.parseInt(mlid)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kunden;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
