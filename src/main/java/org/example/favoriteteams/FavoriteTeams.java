package org.example.favoriteteams;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class FavoriteTeams extends Application {
    Connection con;
    PreparedStatement preparedStatement;
    PreparedStatement preparedStatement2;

    private TextField tfID = new TextField();
    private Label lblID = new Label();
    private TextField tfFName = new TextField();
    private TextField tfLName = new TextField();
    private TextField tfTeam = new TextField();

    public void initalizeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/databasedb?";
            con = DriverManager.getConnection(url + "user=student1&password=pass");
            String queryString = "SELECT * FROM fans WHERE ID = ?";
            String queryString2 = "UPDATE fans SET firstname = ?, lastname = ?, favoriteteam = ? WHERE ID = ?";
            preparedStatement = con.prepareStatement(queryString);
            preparedStatement2 = con.prepareStatement(queryString2);

        } catch (Exception e) {
            System.out.println("Error: " + e);
            System.exit(0);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        initalizeDatabase();
        try {
            Button btnUpdate = new Button("Update");
            Button btnDisplay = new Button("Display");
            HBox hbox2 = new HBox(5);
            VBox vbox = new VBox(5);

            HBox hbox1 = new HBox(5, new Label("ID"), tfID);
            hbox1.setAlignment(Pos.CENTER);

            VBox vLabels = new VBox(5, new Label("ID"), new Label("FName"), new Label("LName"), new Label("Team"));
            vLabels.setAlignment(Pos.CENTER);
            vLabels.setSpacing(15);

            VBox vTexts = new VBox(5, lblID, tfFName, tfLName, tfTeam);
            vTexts.setAlignment(Pos.CENTER_LEFT);
            vTexts.setSpacing(10);

            HBox hLabel1 = new HBox(5, vLabels, vTexts);
            hLabel1.setAlignment(Pos.CENTER);
            hLabel1.setPadding(new Insets(20));

            hbox2.getChildren().addAll(btnUpdate, btnDisplay);
            hbox2.setAlignment(Pos.CENTER);

            vbox.getChildren().addAll(hbox1, hLabel1, hbox2);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(20));

            btnDisplay.setOnAction(e -> showInfo());
            btnUpdate.setOnAction(e -> updateInfo());

            Scene scene = new Scene(vbox);
            stage.setScene(scene);
            stage.setTitle("Favorite Teams");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void showInfo() {
        String id = tfID.getText();
        try {
            preparedStatement.setString(1, id);
            ResultSet rset = preparedStatement.executeQuery();

            if (rset.next()) {
                String ID = rset.getString(1);
                String firstname = rset.getString(2);
                String lastname = rset.getString(3);
                String favoriteTeam = rset.getString(4);

                lblID.setText(ID);
                tfFName.setText(firstname);
                tfLName.setText(lastname);
                tfTeam.setText(favoriteTeam);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID not found.");
                alert.showAndWait();
            }

    } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void updateInfo() {
        String id = tfID.getText();

        String firstname = "";
        String lastname = "";
        String favoriteTeam = "";
        boolean test = true;

        try {
            preparedStatement.setString(1, id);
            ResultSet rset = preparedStatement.executeQuery();

            if (rset.next()) {
                String ID = rset.getString(1);
                firstname = rset.getString(2);
                lastname = rset.getString(3);
                favoriteTeam = rset.getString(4);

            } else {
                test = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ID not found.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        if (test) {
            if (tfFName.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("First Name is blank. Please enter a value.");
                alert.showAndWait();
            } else if (tfLName.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Last Name is blank. Please enter a value.");
                alert.showAndWait();
            } else if (tfTeam.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Favorite team is blank. Please enter a value.");
                alert.showAndWait();
            } else if (Objects.equals(tfFName.getText(), firstname) && Objects.equals(tfLName.getText(), lastname) && Objects.equals(tfTeam.getText(), favoriteTeam)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please change a value in one of the fields.");
                alert.showAndWait();
            } else {
                String idtxt = lblID.getText();
                String fname = tfFName.getText();
                String lname = tfLName.getText();
                String team = tfTeam.getText();

                try {
                    preparedStatement2.setString(1, fname);
                    preparedStatement2.setString(2, lname);
                    preparedStatement2.setString(3, team);
                    preparedStatement2.setString(4, idtxt);
                    preparedStatement2.execute();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Update successful.");
                    alert.showAndWait();

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }

        }
    }

    public static void main(String[] args) {
        launch();
    }
}
