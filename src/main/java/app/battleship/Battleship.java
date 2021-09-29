package app.battleship;

import java.util.*;

public class Battleship {
    private ArrayList<Ship> playersFleet;
    private ArrayList<Ship> opponentsFleet;
    private Grid playersGrid;
    private Grid opponentsGrid;
    private boolean isPlayersTurn = true;
    private boolean isGameOver;
    private boolean huntMode;
    ArrayList<int []> lastHits;
    ArrayList<int []> possibleShots;

    Battleship() {
        playersFleet = new ArrayList<Ship>();
        addShipsToTheFleet(playersFleet);
        playersGrid = new Grid();
    }

    public boolean isHuntMode() {
        return huntMode;
    }

    public void setHuntMode(boolean huntMode) {
        this.huntMode = huntMode;
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
        lastHits = new ArrayList<>();
        possibleShots = new ArrayList<>();
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
        int[] huntResult;
        char shotResult;
        if(isHuntMode()) {
            huntResult = hunt();
            x = huntResult[0];
            y = huntResult[1];
            shotResult = playersGrid.shoot(x, y);
        }
        else {
            do {
                x = rand.nextInt(10);
                y = rand.nextInt(10);
                shotResult = playersGrid.shoot(x, y);
            } while (shotResult == '2');
        }
        if (shotResult == 'C' || shotResult == 'B' || shotResult == 'R' || shotResult == 'D' || shotResult == 'S') {
            setHuntMode(true);
            lastHits.add(new int[]{x, y});
            hitTheShip(shotResult, playersFleet);
        }
        System.out.println("Last hits");
        for(int[] hit : lastHits)
            System.out.println(hit[0] + " " + hit[1]);
        return new int[]{x, y};
    }

    private int[] hunt() {
        Random rand = new Random();
        int x, y;
        int [] result;
        x = lastHits.get(0)[0];
        y = lastHits.get(0)[1];
        if(lastHits.size() == 1) {
            addIfViable(x - 1, y);
            addIfViable(x + 1, y);
            addIfViable(x, y - 1);
            addIfViable(x, y + 1);
            int random = rand.nextInt(possibleShots.size());
            result = possibleShots.get(random);
            System.out.println("HERE");
        } else {
            boolean isVertical = lastHits.get(0)[1] == lastHits.get(1)[1];
            if(isVertical){
                int min = x, max = x;
                for(int[] hit : lastHits){
                    if(min > hit[0])
                        min = hit[0];
                    if(max < hit[0])
                        max = hit[0];
                }
                addIfViable(min - 1, y);
                addIfViable(max + 1, y);
            } else {
                int min = y, max = y;
                for(int[] hit : lastHits){
                    if(min > hit[1])
                        min = hit[1];
                    if(max < hit[1])
                        max = hit[1];
                }
                addIfViable(x, min - 1);
                addIfViable(x, max + 1);
            }
            int random = rand.nextInt(possibleShots.size());
            result = possibleShots.get(random);
        }
        possibleShots.clear();
        return result;
    }

    public void addIfViable(int x, int y){
        if(x > -1 && x < 10 && y > -1 && y < 10){
            char mark = playersGrid.getBattlemap()[x][y];
            if(mark != 'M')
                possibleShots.add(new int[]{x,y});
        }
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
            if (ship.getSymbol() == shotResult) {
                ship.hit();
                if (ship.isDestroyed && fleet.equals(opponentsFleet))
                    System.out.println("Enemy " + ship.getName() + " has been sunk");
                else if (ship.isDestroyed && fleet.equals(playersFleet)) {
                    System.out.println("Your " + ship.getName() + " has been sunk");
                    lastHits.clear();
                    huntMode = false;
                }
            }
        }
    }

}
