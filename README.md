# REDHEAD REDEMPTION

## Game Manual

### Story

The world ended 2 years ago. Some overfunded bio lab tried to make a new hair dye that would last forever. They used some unknown chemicals. Classic mistake.

Two weeks later, half of the world was eating the other half.

But here's the kicker: the infected wouldn't touch us gingers. Scientists said it was something in the genes - the same thing that gave us red hair and pale skin.

Suddenly, being a ginger was no longer a curse. And while we couldn't turn, we could still die. We've heard of a safe zone up north. A helicopter is waiting on the other side uptown.

**It's time for Redhead Redemption.**

### Description

Redhead Redemption is a top-down pixel multiplayer shooter set in a zombie apocalypse. You and your fellow redheads fight through waves of zombies, complete objectives, upgrade your weapons and try to reach safety. Work together or go solo across different maps, each with new challenges.

## How to Run the Game

### Prerequisites

- Java installed on your system
- Multiple terminal/command prompt windows

### Setup Instructions

1. **Compile the game:**

   ```bash
   javac *.java
   ```

2. **Start the game server** (in first terminal):

   ```bash
   java GameServer
   ```

   - Input the number of players when prompted

3. **Start the game client** (in second terminal):

   ```bash
   java GameStarter
   ```

   - Input the IP address when prompted

4. **Begin the game:**
   - Go back to the GameServer terminal
   - Press Enter to start the game

### Cleanup

To remove compiled class files:

```bash
rm *.class
```

_On Windows:_

```cmd
del *.class
```

## How to Play

### Controls

- **WASD** - Move and aim
- **Left Click** - Shoot
- **Right Click** - Melee
- **Shift** - Sprint
- **F** - Pickup weapon/medkit
- **E** - Interact
- **6** - Use medkit
- **Space** - Next dialogue
- **C** - Control list

### How to Win

- Collect 3 keys to open the gate to the next map
- Kill zombies to level up
- Collect 6 fuel cans and bring them to the helicopter
- Everyone must reach the helipad

## Mechanics and Features

### Leveling

Level up your ammo and life by killing zombies

### Items

- Heal with medkits or area heals
- Switch from shotgun, rifle, or SMG in designated areas
- Refill ammo

### Design

Hand drawn pixel sprites and assets (except map tiles)

### Multiplayer

- 1-5 players through LAN
- Revive teammates on revive pads

### Enemies

Various zombie behaviors: pathfinding, dormant, passive, aggressive, poison projectiles

### Traps

Be careful of spikes!

---

_Survive the apocalypse. Embrace your red hair. Find redemption._
_CSCI 22 FINAL PROJECT_
