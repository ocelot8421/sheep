# UML
    Not finished diagram !!!!!!
IntelliJ IDEA properties to appear the diagram: <br />
settings / Languages&Frameworks / Markdown / Markdown extensions; check/install Mermaid

```mermaid
classDiagram

Main --> RoboSheepAccumulator : instantiate

Main ..> GardenMapImporter : import
GardenMapImporter ..> CoordinateHandler : import
GardenMapImporter --> CoordinateDataStore : instantiate

Main ..> ScreenPrinter : import

GardenMapImporter : - Path mapPath
GardenMapImporter : - String nameFile
GardenMapImporter : + importGardenMap() 

CoordinateHandler : + findCoordinateX(String) Long

CoordinateDataStore : - List<Long> coordinates
CoordinateDataStore : + addCoordinate(long)

ScreenPrinter : - long TIME_OUT
ScreenPrinter : + keepDistanceBetweenScreenshots()

RoboSheepAccumulator : - int accumulatorLevel
RoboSheepAccumulator : - int minimalAccumulatorLevel
RoboSheepAccumulator : + needCharge()
RoboSheepAccumulator : + saveChargeLevelAfterMovement()



```
#Coordinates conversion

|     | 1         | 2         |
|-----|-----------|-----------|
| 1   | 1 000 001 | 1 000 002 |
| 2   | 2 000 001 | 2 000 002 |
| 3   | 3 000 001 | 3 000 002 |
