# Description
This is basic implementation for minesweeper game. 
Minesweeper is a single player game. In this game we have a field (grid with squares) with some hidden
bombs and the goal is to open/clean all the squares without a bomb and if the player hits a bomb, then s/he loses the game.
If the player can open all the squares without a bomb, then s/he wins the game!

# API details
There are four end points provided in postman collection. Import this collection and import environment file in postman UI. 

1) POST  /games/minesweeper/createNewGame : You can provide username in the request body with grid size of your choice.
2) GET /games/minesweeper/getUserGame/{{userName}}  : This is to get the current game of user.
3) POST /games/minesweeper/playGame/{{userName}}  : This is to set play moves. You can provide the position of box in the request body.
4) PUT /games/minesweeper/resetGameStatus/{{userName}}  : If the status of game in 3rd API response is blowup, player can reset status
                                                           to active and continue to play using 3rd API 


