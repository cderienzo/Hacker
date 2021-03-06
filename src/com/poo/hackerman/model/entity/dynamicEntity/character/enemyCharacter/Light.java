package com.poo.hackerman.model.entity.dynamicEntity.character.enemyCharacter;

import com.poo.hackerman.model.entity.Direction;
import com.poo.hackerman.model.entity.Entity;
import com.poo.hackerman.model.entity.Position;
import com.poo.hackerman.model.entity.dynamicEntity.character.PlayerCharacter;
import com.poo.hackerman.model.gameWorld.GameMap;
import com.poo.hackerman.model.gameWorld.Grid;

/**
 * Created by franciscosanguineti on 31/5/17.
 */
public class Light {

    private int range;

    public Light(int range) {
        this.range = range;
    }

    //Devuelve true si hay colision de la luz con el hacker

    public boolean collision(Position guardPosition, Direction guardDirection, Grid grid) {

        int[] guardDir = guardDirection.getDir(); //Me devuelve vector [-1 0 1, -1 0 1] dependiendo de a donde se dirija el guardia

        Position p1 = new Position(guardPosition.getX() + guardDir[0] * GameMap.CELL_SIZE, guardPosition.getY() + guardDir[1] * GameMap.CELL_SIZE);
        Direction dirRight = guardDirection.getRight();
        Direction dirLeft = guardDirection.getLeft();

        boolean detected = false;

        for(int i = 0; i < range; i++) {
            detected = checkDirection(p1, dirRight, range-i, grid) || checkDirection(p1, dirLeft, range-i, grid);
            if(detected) {
                return true;
            }
            p1.incrementPosition(guardDir[0] * GameMap.CELL_SIZE, guardDir[1] * GameMap.CELL_SIZE);
        }
        return false;
    }

    private boolean checkDirection(Position currentPosition, Direction currentDirection, int currentRange, Grid grid) {
        int[] dir = currentDirection.getDir();

        Position posCopy = new Position(currentPosition.getX(), currentPosition.getY());

        for (int i = 0; i < currentRange; i++) {
            //devuelve true si encuentra al hacker, false si se va del mapa o si encuentra un obstaculo para la linterna
            if(!posCopy.withinBoundaries()) {
                return false;
            }
            if(checkPlayer(posCopy, grid)) {
                return true;
            }
            if(!grid.isPossibleAdd(posCopy)) {
                return false;
            }
            posCopy.incrementPosition(dir[0] * GameMap.CELL_SIZE, dir[1] * GameMap.CELL_SIZE);
        }
        return false;
    }

    private boolean checkPlayer(Position position, Grid grid) {
        Entity entity = grid.getCell(position).getEntity();

        if(entity == null) {
            return false;
        }
        return entity.getClass().equals(PlayerCharacter.class);
    }
}
