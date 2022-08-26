# UML
    Not finished diagram !!!!!!
IntelliJ IDEA properties to appear the diagram: <br />
settings / Languages&Frameworks / Markdown / Markdown extensions; check/install Mermaid

```mermaid
classDiagram

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


```



# (UML markers - training)
```mermaid
classDiagram

MedicationIntakeDto o-- MedicationIntake

Class01 <|-- AveryLongClass : Cool
Class03 *-- Class04
Class05 o-- Class06
Class07 .. Class08
Class09 --> C2 : Where am i?
Class09 --* C3
Class09 --|> Class07
Class07 : equals()
Class07 : Object[] elementData
Class01 : size()
Class01 : int chimp
Class01 : int gorilla
Class08 <--> C2: Cool label

```
