# CISC191-FALL2020-B
Intermediate Java Programming

# Draft 1: UML Planning

## Class Player extends Thread
- MAXHEALTHPOINTS: final int
- healthPoints: int
- numTreasures: int
- strength: int
- posX: int
- posY: int
- + Player()
- + getHealthPoints(): int
- + setHealthPoints(int): void
- + getNumTreasures(): int
- + setNumTreasures(int): void
- + attack(Monster): void
- + usePotion(): void
- + updateX(): void
- + updateY(): void
- + updatePosition(): void

## Class Grid
- + setBackground(): void
- + setObject(): void
- + actionWithExecutorService(): void

## Class Monster extends Thread
- healthPoints: int
- maxAttackStrength: int
- posX: int
- posY: int
- + Monster()
- + getHealthPoints(): int
- + setHealthPoints(int)
- + attack(Player): void
- + updateX(): void
- + updateY(): void
- + updatePosition(): void