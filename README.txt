Driver class:
game/Catan

Main classes to be commented:
game/Catan
game/Game
game/Player
board/Board

Game rules and almanac: https://www.catan.com/sites/default/files/2021-06/catan_base_rules_2020_200707.pdf
All features are implemented exactly as according to the almanac unless otherwise specified
Please read the almanac for complete list of niche rules and procedures implemented
Notes about coordinate system: Tiles are specified by their top-most vertex coordinate when moving the robber
Possible bugs because testing is difficult (probability based game states and time-consuming gameplay)
As of right now, four-player is untested but no bugs or issues are forecasted
How to change win condition (number of victory points to win): Change the final variable WIN in game/Game

Features implemented:
1. Initial setup of settlements
   - Each player can place 2 settlements initially in order specified by the game rules
   - Each player starts with the resources that they place their settlements on and starts with 2 VPs
   - Different feature: Each player receives 2 brick and 2 lumber to build up to 2 roads on their turn, not in 
   setup phase
2. Rolling dice and distribution of resources to each player
   - Simulation of rolling 2 dice and any tile with their sum will generate resources for the playes who own settlements 
   (+1 of resource type) or city (+2 of resource type) on any of the tile's vertices
   - The desert tile and the tile occupied by the robber do not generate any resources
3. Robber when a 7 is rolled  
   - When a 7 is rolled, the current player must move the robber to a different tile
   - The player can steal a random resource card from any player who has a settlement/city on the tile the robber is 
   moved to
   - Players with over 7 resource cards must discard half (rounded down) of their resource cards
   - Different feature: The discarding is done randomly, players do not choose which resources to discard
4. Building a road
5. Building a settlement
6. Building a city
7. Winning the game and output final score and rank

Features to be implemented:
1. Implement trading (generic trade, player trade, trade on ports)
2. Implement development cards (year of plenty, monopoly, road building)
3. Implement longest road and largest army