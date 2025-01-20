Driver class:
game/Catan

Classes with comments (comments may be brief and sparse):
board/Board
board/Building
board/Edge
board/Point
board/Tile
board/Vertex
constants/Colors
constants/Items
constants/Resource

Classes with more comments:
game/BoardPanel
game/Catan
game/Game
game/Player
game/InfoPanel

Comments on game:
All data is fully encapsulated
Testing is extremely time-consuming, limited my ability to complete all features, no known bugs but there could be
hidden bugs on very niche edge cases
Over 1600 lines of code (excluding comments) but still cannot implement all features unfortunately

Description of game:
Game rules and almanac: https://www.catan.com/sites/default/files/2021-06/catan_base_rules_2020_200707.pdf
All features are implemented exactly as according to the almanac unless otherwise specified
Please read the almanac for complete description of rules and procedures implemented

Coordinate system:
The game uses the coordinate system specified by gui/coordinates.jpg
Each vertex of the board is represented by a row and column coordinate
When moving the robber, the tile the robber moves to is specified by their top-most vertex coordinate

Game settings:
Change number of victory points to win: change the final variable WIN in game/Game
Change starting resources: modify the constructor of game/Player to change initial values in resources array
Do not go below the default starting resources (4 brick, 2 wool, 4 lumber, 2 grain), otherwise setup phase will break

Important tip:
Scrutinize the information panel on every move, it (along with the board) contains all the game information
Changes in resources/updates may not be obvious, best to follow each move along in your head

Description of fully implemented features:
1. Initial setup of settlements
    - Each player can place 2 settlements initially in order specified by the game rules
    - Each player starts with the resources that they place their settlements on and starts with 2 VPs
    - Different feature: Each player receives 2 brick and 2 lumber to build up to 2 roads on their turn, not in
    setup phase
2. Rolling dice and distribution of resources to each player
    - Simulation of rolling 2 dice and any tile with their sum will generate resources for the players who own
    settlements (+1 of resource type) or city (+2 of resource type) on any of the tile's vertices
    - The desert tile and the tile occupied by the robber do not generate any resources
3. Robber when a 7 is rolled
    - When a 7 is rolled, the current player must move the robber to a different tile
    - The player can steal a random resource card from any player who has a settlement/city on the tile the robber is
    moved to (if there is any)
    - Players with over 7 resource cards must discard half (rounded down) of their resource cards
    - Different feature: The discarding is done randomly, players do not choose which resources to discard
4. Building a road
    - Players can build a road on any unoccupied edge as long as they have the necessary resources and the road is
    connected to one of their buildings or roads and does not touch another player's building
5. Building a settlement
    - Apart from the two initial settlements, any further settlements built require the necessary resources and must
    be connected to one of their roads
    - All settlements built must adhere to the "distance rule": they must not be adjacent (connected by 1 edge on the
    board) to any other settlement, including settlements they own
    - A player cannot build more than 5 settlements
6. Building a city
    - Players may upgrade their existing settlements to a city using the required resources to gain +1 VP and +2
    resource generation
    - A player cannot directly purchase a city; they must build upon an existing settlement they own
    - A player cannot build more than 4 cities
7. 4:1 Self Trade
    - A player may execute a 4:1 trade on their turn, where they discard 4 identical resources to receive 1 resource of
    another type
8. Winning the game and output final score and rank
    - The game ends when a player reaches 10 victory points (adjustable based on the code)
    - Game outputs each player's ending victory points, sorted descending

Important rules implemented:
1. A new board layout is randomly generated on every play
2. A player must roll first on their turn; this roll is not optional and must occur before other actions

Features to be implemented (in order of priority, some may be half implemented but don't worry about them):
1. Implement development cards (knight, victory point, year of plenty, monopoly, road building)
2. Trading between players and at ports
3. Implement the longest road and largest army objectives