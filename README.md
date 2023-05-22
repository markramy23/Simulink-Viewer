
# Simulink-Viewer

Application to view Simulink block diagrams

## Team members

| Name          | ID                                                              |
| ----------------- | ------------------------------------------------------------------ |
| Mark Ramy Fathy |  2000923 |
| Ziad Mohammed Hassan | 2000390 |
| George Ihab Farouk | 2001493 |
| Heba Maher Abdelrahman | 2001400 |


## Discribtion
It's a GUI that takes Simulink .mdl files which are represented as group of xml files bundled in one file and parse the **Blocks** and **Lines** date in the file to represent each block as square with it's name written under it and connect between them using the Lines data


## Classes included

- [**Block Class**](https://github.com/markramy23/Simulink-Viewer/blob/main/Project/src/project/Block.java)

         class to contain the data of the blocks
        parsed from the MDL file which are:

            1. Name
            2. Position
            3. SID
            4. Position
            5. Block Mirror
            6. I/O ports

         The Class also contain all the 
         required methods to change the data 
         parsed from the MDL to it's useful 
         data type to be used in  other methods


         It also contain methods to return the 
         square representation of the block 
         and it's Name to be written under it
         and also the I/O points of the Block 


- [**Line Class**](https://github.com/markramy23/Simulink-Viewer/blob/main/Project/src/project/Line.java)

        This class for containing the data of the 
        lines parsed from the MDL file .

        1. points
        2. source
        3. destination
        4. Branches 

        and also all the required methods to deal
        with the data as it is parsed as String
        so it needs to be divided and converted 
        to be useful to use.


- [**Branch Class**](https://github.com/markramy23/Simulink-Viewer/blob/main/Project/src/project/Branch.java) 

        This class for containing the data of the 
        Branches parsed from the MDL file .

        1. points
        2. destination


- [**SimulinkViewer class**](https://github.com/markramy23/Simulink-Viewer/blob/main/Project/src/project/SimulinkViewer.java)

        The public class extending application
        and containing the main method .

        it also contains three essential methods.

            1. ExtractBlock: which takes the mdl 
                file and cut only the needed part 
                needed to be parsed and return it
                as String.

            2. ParseXml: to parse the block 
                data and return a list of
                blocks 

            3. ParseLine: to Parse the Line and
                Branches data   




        
