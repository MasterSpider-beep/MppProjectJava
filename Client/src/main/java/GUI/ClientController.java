package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import domain.Flight;
import domain.User;
import exceptions.AppException;
import exceptions.LogInException;
import service.IService;
import utils.IObserver;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
public class ClientController implements IObserver {

    @FXML
    public TableView<Flight> flightsTable;
    @FXML
    public TableColumn<Flight,String> destCol;
    @FXML
    public TableColumn<Flight, LocalDate> dateCol;
    @FXML
    public TableColumn<Flight, LocalTime> timeCol;
    @FXML
    public TableColumn<Flight, String> airportCol;
    @FXML
    public TableColumn<Flight, Integer> seatsCol;
    private final ObservableList<Flight> allFlightsObservable = FXCollections.observableArrayList();
    @FXML
    public DatePicker datePickDeparture;
    @FXML
    public TextField txtDestination;
    @FXML
    public Button btnSearch;
    @FXML
    public TableView<Flight> tableSearchFlights;
    @FXML
    public TableColumn<Flight,String> destSearchCol;
    @FXML
    public TableColumn<Flight,LocalTime> timeSearchCol;
    @FXML
    public TableColumn<Flight,Integer> seatsSearchCol;
    @FXML
    public Spinner<Integer> spinSeats;
    @FXML
    public TextField txtClient;
    @FXML
    public TextArea txtTourists;
    @FXML
    public Button btnTicket;
    @FXML
    public TextField txtAddress;
    private IService serverImpl;
    private User user;
    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.setOnCloseRequest(e->this.logOut());
        stage.setOnShown(e->this.loadAllFlights());
    }

    private final ObservableList<Flight> filteredFlights = FXCollections.observableArrayList();

    public void setUser(User user) {
        this.user = user;
    }

    public Stage getStage() {
        return stage;
    }

    public void setService(IService serverImpl){
        this.serverImpl = serverImpl;
    }

    private void showErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void reloadFlightData(List<Flight> flights){
        allFlightsObservable.clear();
        allFlightsObservable.setAll(flights);
        filteredFlights.clear();
    }

    public void loadAllFlights(){
        try {
            allFlightsObservable.setAll(serverImpl.getAllAvailableFlights());
        } catch (AppException e) {
            showErrorMessage("Couldn't load flights");
        }
    }

    public void loadFilteredFlights(ActionEvent actionEvent){
        String destination = txtDestination.getText();
        LocalDate departureDate = datePickDeparture.getValue();

        if(destination.isEmpty() || departureDate == null){
            showErrorMessage("Introduce the destination or date!");
            return;
        }
        try {
            filteredFlights.setAll(serverImpl.getAvailableFilteredFlights(destination,departureDate));
        } catch (AppException e) {
            showErrorMessage("Couldn't load flights");
        }
    }

    public void logOut(){
        try {
            serverImpl.logOut(user,this);
        } catch (LogInException e) {
            showErrorMessage("Couldn't complete the logout" + e.getMessage());
        }
    }

    public void onPressBuy(ActionEvent actionEvent){
        String clientName = txtClient.getText();
        String touristsTemp = txtTourists.getText();
        String address = txtAddress.getText();
        int noSeats = spinSeats.getValue();
        Flight flight = tableSearchFlights.getSelectionModel().getSelectedItem();
        if(clientName.isEmpty() || touristsTemp.isEmpty() || address.isEmpty()){
            showErrorMessage("Please fill in all the required fields");
            return;
        }
        if(flight == null){
            showErrorMessage("Please select a flight to book a ticket");
            return;
        }
        try {
            serverImpl.buyTicket(flight.getId(), clientName, touristsTemp, address, noSeats);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Ticket booked successfully");
            alert.showAndWait();
        }catch (AppException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void initialize(){

        btnSearch.setOnAction(this::loadFilteredFlights);
        btnTicket.setOnAction(this::onPressBuy);
        spinSeats.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        txtTourists.setPromptText("Introduce the names separated by ;");

        airportCol.setCellValueFactory(new PropertyValueFactory<>("airport"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        destCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        seatsCol.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        flightsTable.setItems(allFlightsObservable);

        destSearchCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        timeSearchCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        seatsSearchCol.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        tableSearchFlights.setItems(filteredFlights);
    }

    @Override
    public void ticketBought(List<Flight> flights) {
        reloadFlightData(flights);
    }
}
