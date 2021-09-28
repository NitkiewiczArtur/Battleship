package app.battleship;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Battleship {
    private ArrayList<Ship> playersFleet;
    private ArrayList<Ship> opponentsFleet;
    private Grid playersGrid;
    private Grid opponentsGrid;
    private boolean isPlayersTurn = true;
    private boolean isGameOver;


    Battleship() {
        playersFleet = new ArrayList<Ship>();
        addShipsToTheFleet(playersFleet);
        playersGrid = new Grid();
    }

    public ArrayList<Ship> getPlayersFleet(){
        return playersFleet;
    }

    public Grid getPlayersGrid() {
        return playersGrid;
    }

    public Grid getOpponentsGrid() {
        return opponentsGrid;
    }

    public boolean isPlayersTurn() {
        return isPlayersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        isPlayersTurn = playersTurn;
    }

    public void prepareTheGameWithComputer(){
        opponentsFleet = new ArrayList<Ship>();
        opponentsGrid = new Grid();
        addShipsToTheFleet(opponentsFleet);
        putShipsAtRandom(opponentsFleet, opponentsGrid);
    }

    public void addShipsToTheFleet(ArrayList<Ship> fleet){
        fleet.add(new Ship("Carrier", 5, 'C'));
        fleet.add(new Ship("Battleship", 4, 'B'));
        fleet.add(new Ship("Cruiser", 3, 'R'));
        fleet.add(new Ship("Destroyer", 2, 'D'));
        fleet.add(new Ship("Submarine", 2, 'S'));
    }

    public boolean areAllShipsSetSail(){
        int counter = 0;
        for(Ship ship : playersFleet){
            if(ship.isSetSail())
                counter++;
        }
        return counter == playersFleet.size();
    }

    public void putShipsAtRandom(ArrayList<Ship> fleet, Grid grid){
        Random rand = new Random();
        int x, y;
        for(Ship ship : fleet){
            while(!ship.isSetSail()) {
                boolean direction = rand.nextBoolean();
                ship.setVertical(direction);
                if(ship.isVertical()) {
                    x = rand.nextInt(10 - ship.getLength());
                    y = rand.nextInt(10);
                } else {
                    x = rand.nextInt(10);
                    y = rand.nextInt(10 - ship.getLength());
                }
                grid.placeShip(ship, x, y);
            }
        }
    }

    private void randomShot(Grid grid){
        Random rand = new Random();
        int x = rand.nextInt(10), y = rand.nextInt(10);
        grid.shoot(x, y);
    }

    public char playerMove(int x, int y) {
        char shotResult = opponentsGrid.shoot(x, y);
        if(shotResult == 'C' || shotResult == 'B' || shotResult == 'R' || shotResult == 'D' || shotResult == 'S')
            hitTheShip(shotResult, opponentsFleet);
        return shotResult;
    }

    public int[] computerMove(){
        Random rand = new Random();
        int x, y;
        char shotResult;
        do{
            x = rand.nextInt(10);
            y = rand.nextInt(10);
            shotResult = playersGrid.shoot(x, y);
        } while (shotResult == '2');
        if(shotResult == 'C' || shotResult == 'B' || shotResult == 'R' || shotResult == 'D' || shotResult == 'S')
            hitTheShip(shotResult, playersFleet);
        return new int[]{x, y};
    }

    public String checkIfGameIsOver(){
        int playerCounter = 0, opponentsCounter = 0;
        for(int i = 0; i < playersFleet.size(); i++){
            if(playersFleet.get(i).isDestroyed)
                playerCounter++;
            if(opponentsFleet.get(i).isDestroyed)
                opponentsCounter++;
        }
        if(playerCounter == 5) {
            return "Computer wins";
        }
        else if(opponentsCounter == 5)
            return "You win";
        else
            return "No over";
    }

    public void hitTheShip(char shotResult, ArrayList<Ship> fleet) {
        for(Ship ship : fleet){
            if (ship.getSymbol() == shotResult)
                ship.hit();
        }
    }

}
