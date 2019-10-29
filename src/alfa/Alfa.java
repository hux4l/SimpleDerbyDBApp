/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alfa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * 
 * @author Narayan
 */

public class Alfa extends Application {

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    private TableView tableview;

    //MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

    //CONNECTION DATABASE
    public void buildData(){
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dbName = "Alfa";
        String connectionURL = "jdbc:derby:Alfa/Alfa" + dbName;
        Connection connection;
        
          data = FXCollections.observableArrayList();
          try{
            Class.forName(driver);
            connection = DriverManager.getConnection(connectionURL);
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from ZAKAZNIK";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });

                tableview.getColumns().addAll(col); 
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
          }catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");             
          }
      }


      @Override
      public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new VBox(), 800, 600);  
        stage.setTitle("Alfa");
        
        final Menu menu1 = new Menu("Menu");
        final Menu menu2 = new Menu("Nastavenia");
        final Menu menu3 = new Menu("Pomoc");
        
        MenuItem menuOpen = new MenuItem("Otvor");
        menu1.getItems().add(menuOpen);
        MenuItem menuSave = new MenuItem("Ulož");
        menu1.getItems().add(menuSave);
        MenuItem menuExit = new MenuItem("Koniec");
        menu1.getItems().add(menuExit);

        StackPane root = new StackPane();    
                
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3);
        root.getChildren().add(menuBar);
        

          
        //TableView    tabulka
        tableview = new TableView();
        tableview.setEditable(true);
        buildData();

        //Main Scene

        final VBox tabulka = new VBox();    //box kde je tabulka
        tabulka.setSpacing(5);
        tabulka.setPadding(new Insets(10, 10, 10, 10));
        tabulka.getChildren().addAll(tableview);
        
        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, tabulka);        

        stage.setScene(scene);
        stage.show();
      }
}
