package app.battleship;

public class Grid {
    public static final int size = 10;
    private char [][] battlemap;

    Grid(){
        battlemap = new char[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++)
                battlemap[i][j] = '0';
        }
    }

    public char[][] getBattlemap() {
        return battlemap;
    }

    //Puts the ship on the grid, but checks if you can before
    public String placeShip(Ship ship, int x, int y){
        String result = isSelectedPlaceFreeAndInBounds(ship.getLength(), x, y, ship.isVertical());
        if(result.equals("Success")) {
            markSquaresAndAdjacentSquares(ship, x, y);
            ship.setSail(true);
        }
        return result;
    }

    //Marks where the ship is placed and
    // adjacent squares to the ship, so you won't be able to put a ship just next to another
    public void markSquaresAndAdjacentSquares(Ship ship, int x, int y){
        int length = ship.getLength();
        if(ship.isVertical()){
            for (int i = 0; i < length + 2; i++) {
                for (int j = 0; j < 3; j++)
                    if(x - 1 + i >= 0 && y - 1 + j >= 0 && x - 1 + i < size && y - 1 + j < size)
                        battlemap[x - 1 + i][y - 1 + j] = 'T';
            }
            for(int i = 0; i < length; i++)
                battlemap[x + i][y] = ship.getSymbol();
        } else {
            for (int i = 0; i < length + 2; i++) {
                for (int j = 0; j < 3; j++)
                    if(y - 1 + i >= 0 && x - 1 + j >= 0 && y - 1 + i < size && x - 1 + j < size)
                        battlemap[x - 1 + j][y - 1 + i] = 'T';
            }
            for (int i = 0; i < length; i++)
                battlemap[x][y + i] = ship.getSymbol();
        }
    }

    //Checks if you can place a ship in given place
    public String isSelectedPlaceFreeAndInBounds(int length, int x, int y, boolean isVertical){
        if(isVertical && !(x + length > size)){
            for(int i = 0; i < length; i++){
                if (battlemap[x + i][y] != '0' && battlemap[x + i][y] != 'T')
                    return "You can't place ship on top of another";
                else if(battlemap[x + i][y] == 'T')
                    return "You've put a ship too close to another. There must be one square gap between ships";
            }
        } else if(!isVertical && !(y + length > size)){
            for(int i = 0; i < length; i++){
                if (battlemap[x][y + i] != '0' && battlemap[x][y + i] != 'T')
                    return "You can't place ship on top of another";
                else if(battlemap[x][y + i] == 'T')
                    return "You've put a ship too close to another. There must be one square gap between ships";
            }
        } else
            return "Out of bounds";
        return "Success";
    }

//    public String isSelectedPlaceFreeAndInBounds2(int length, int x, int y, boolean isVertical){
//        for(int i = 0; i < length; i++){
//            if(isVertical && !(x + length > size)){
//                if (battlemap[x + i][y] != '0' && battlemap[x + i][y] != 'T')
//                    return "You can't place ship on top of another";
//                else if(battlemap[x + i][y] == 'T')
//                    return "You've put a ship too close to another. There must be one square gap between ships";
//            } else if(!isVertical && !(y + length > size)){
//                if (battlemap[x][y + i] != '0' && battlemap[x][y + i] != 'T')
//                    return "You can't place ship on top of another";
//                else if(battlemap[x][y + i] == 'T')
//                    return "You've put a ship too close to another. There must be one square gap between ships";
//            } else
//                return "Out of Bounds";
//        }
//        return "Success";
//    }

    //Returns true if you hit the target and false if you miss
    public char shoot(int x, int y) {
        char field = battlemap[x][y];
        if (field == 'C' || field == 'B' || field == 'R' || field == 'D' || field == 'S') {
            battlemap[x][y] = 'H';
            return field;
        } else if (field == 'H' || field == 'M') {
            return '2';
        } else {
            battlemap[x][y] = 'M';
            return 'M';
        }
    }

    public void printTheGrid(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                System.out.print(battlemap[i][j] + " ");
            }
            System.out.println();
        }
    }

}
