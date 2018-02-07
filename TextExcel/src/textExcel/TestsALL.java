package textExcel;

//*******************************************************
//DO NOT MODIFY THIS FILE!!!
//*******************************************************

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestsALL.Checkpoint1.class,
    TestsALL.Checkpoint2.class,
    TestsALL.Final.class,
    TestsALL.ExtraCreditCommandErrors.class,
    TestsALL.ExtraCreditCommandHistory.class,
})

public class TestsALL
{
    public static class TestLocation implements Location
    {
        // Simple implementation of Location interface for use only by tests.
        private int row;
        private int col;

        public TestLocation(int row, int col)
        {
            this.row = row;
            this.col = col;
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public int getCol() {
            return col;
        }
    }

    public static class Helper
    {
        // For use only by test code, which uses it carefully.
        private String[][] items;

        public Helper()
        {
            items = new String[20][12];
            for (int i = 0; i < 20; i++)
                for (int j = 0; j < 12; j++)
                    items[i][j] = format("");
        }

        public static String format(String s)
        {
            return String.format(String.format("%%-%d.%ds", 10, 10),  s);
        }

        public void setItem(int row, int col, String text)
        {
            items[row][col] = format(text);
        }

        public String getText()
        {
            String ret = "   |";
            for (int j = 0; j < 12; j++)
                ret = ret + format(Character.toString((char)('A' + j))) + "|";
            ret = ret + "\n";
            for (int i = 0; i < 20; i++)
            {
                ret += String.format("%-3d|", i + 1);
                for (int j = 0; j < 12; j++)
                {
                    ret += items[i][j] + "|";
                }
                ret += "\n";
            }
            return ret;
        }
    }
    
    public static class Checkpoint1
    {
        // Tests for checkpoint 1.
        // Pass them all, plus ensure main loop until quit works, for full credit on checkpoint 1.
        // Note these must also pass for all subsequent checkpoints including final project.
        Grid grid;       
        
        @Before
        public void initializeGrid()
        {
            grid = new Spreadsheet();
        }
        
        @Test
        public void testGetRows_1pt()
        {
            assertEquals("getRows", 20, grid.getRows());
        }
        
        @Test
        public void testGetCols_1pt()
        {
            assertEquals("getCols", 12, grid.getCols());
        }
        
        @Test
        public void testProcessCommand_3pt()
        {
            String str = grid.processCommand("");
            assertEquals("output from empty command", "", str);
        }
        
        @Test
        public void testLongShortStringCell_6pt()
        {
            SpreadsheetLocation loc = new SpreadsheetLocation("L20");
            assertEquals("SpreadsheetLocation column", loc.getCol(), 11);
            assertEquals("SpreadsheetLocation row", loc.getRow(), 19);

            loc = new SpreadsheetLocation("D5");
            assertEquals("SpreadsheetLocation column", loc.getCol(), 3);
            assertEquals("SpreadsheetLocation row", loc.getRow(), 4);

            loc = new SpreadsheetLocation("A1");
            assertEquals("SpreadsheetLocation column", loc.getCol(), 0);
            assertEquals("SpreadsheetLocation row", loc.getRow(), 0);
        }
        
        @Test
        public void testProcessCommandNonliteralEmpty_2pt()
        {
            String input = " ".trim();
            String output = grid.processCommand(input);
            assertEquals("output from empty command", "", output);
        }

        @Test
        public void testEmptyCell_2pt() throws Exception
        {
        	Cell c = (Cell) Class.forName("textExcel.EmptyCell").newInstance();
        	assertEquals("Empty Cell", "          ", c.abbreviatedCellText());       	
        	assertEquals("Empty Cell - full text", "", c.fullCellText());
        }
        
    }

    
    public static class Checkpoint2
    {
    	    	
        // Tests for checkpoint 2.
        // Note these must also pass for all subsequent checkpoints including final project.
        Grid grid;
                
        @Before
        public void initializeGrid()
        {
            grid = new Spreadsheet();
        }        
        
        @Test
        public void testEmptyGridCells_1pt()
        {
            for (int i = 0; i < grid.getRows(); i++)
                for (int j = 0; j < grid.getCols(); j++)
                {
                    Cell cell = grid.getCell(new TestLocation(i, j));
                    assertEquals("empty cell text", Helper.format(""), cell.abbreviatedCellText());
                    assertEquals("empty inspection text", "", cell.fullCellText());
                }
        }
        
        @Test
        public void testEmptyGridText_halfpt()
        {
            Helper helper = new Helper();
            assertEquals("empty grid", helper.getText(), grid.getGridText());
        }
        
        @Test
        public void testShortStringCell_halfpt()
        {
            String hello = "Hello";
            grid.processCommand("A1 = \"" + hello + "\"");
            Cell helloCell = grid.getCell(new TestLocation(0,0));
            assertEquals("hello cell text", Helper.format(hello), helloCell.abbreviatedCellText());
            assertEquals("hello inspection text", "\"" + hello + "\"", helloCell.fullCellText());
        }
        
        @Test
        public void testLongShortStringCell_halfpt()
        {
            String greeting = "Hello, world!";
            grid.processCommand("L20 = \"" + greeting + "\"");
            Cell greetingCell = grid.getCell(new TestLocation(19,11));
            assertEquals("greeting cell text", Helper.format(greeting), greetingCell.abbreviatedCellText());
            assertEquals("greeting inspection text", "\"" + greeting + "\"", greetingCell.fullCellText());
        }
        
        @Test
        public void testEmptyStringCell_halfpt()
        {
            grid.processCommand("B2 = \"\"");
            Cell emptyStringCell = grid.getCell(new TestLocation(1,1));
            assertEquals("empty string cell text", Helper.format(""), emptyStringCell.abbreviatedCellText());
            assertEquals("empty string inspection text", "\"\"", emptyStringCell.fullCellText());
        }
        
        @Test
        public void testDifferentCellTypes_halfpt()
        {
            grid.processCommand("C11 = \"hi\"");
            Cell stringCell = grid.getCell(new TestLocation(10, 2));
            Cell emptyCell = grid.getCell(new TestLocation(0,0));
            assertTrue("string cell implementation class must be different from empty cell",
                    !emptyCell.getClass().equals(stringCell.getClass()));
        }
        
        @Test
        public void testClear_1pt()
        {
        	Cell cellFirst, cellSecond;
            grid.processCommand("A1 = \"first\"");
            grid.processCommand("D8 = \"second\"");
            cellFirst = grid.getCell(new TestLocation(0,0));
            cellSecond = grid.getCell(new TestLocation(7, 3));
            // Make sure they are there
            assertEquals("cellFirst inspection text before clear", "\"first\"", cellFirst.fullCellText());
            assertEquals("cellSecond inspection text before clear", "\"second\"", cellSecond.fullCellText());            
            grid.processCommand("clear");
            // Make sure they have been cleared
            cellFirst = grid.getCell(new TestLocation(0,0));
            cellSecond = grid.getCell(new TestLocation(7, 3));
            assertEquals("cellFirst inspection text after clear", "", cellFirst.fullCellText());
            assertEquals("cellSecond inspection text after clear", "", cellSecond.fullCellText());
        }
        
        @Test
        public void testClearLocation_1pt()
        {
        	Cell cellFirst, cellSecond;
            grid.processCommand("A1 = \"first\"");
            grid.processCommand("D8 = \"second\"");
            // Make sure they are there
            cellFirst = grid.getCell(new TestLocation(0,0));
            cellSecond = grid.getCell(new TestLocation(7, 3));
            assertEquals("cellFirst inspection text before clear", "\"first\"", cellFirst.fullCellText());
            assertEquals("cellSecond inspection text before clear", "\"second\"", cellSecond.fullCellText());  
            
            grid.processCommand("clear A1");
            cellFirst = grid.getCell(new TestLocation(0,0));
            cellSecond = grid.getCell(new TestLocation(7, 3));
            assertEquals("cellFirst inspection text after clear", "", cellFirst.fullCellText());
            assertEquals("cellSecond inspection text after clear", "\"second\"", cellSecond.fullCellText());
        }
        
        @Test
        public void testProcessCommandInspection_1pt()
        {
            String empty = grid.processCommand("A1");
            assertEquals("inspection of empty cell", "", empty);
            grid.processCommand("A1 = \"first\"");
            String first = grid.processCommand("A1");
            assertEquals("inspection of string cell", "\"first\"", first);

            grid.processCommand("H1 = \"This is a big string\"");
            first = grid.processCommand("H1");
            assertEquals("inspection of string cell2", "\"This is a big string\"", first);            
        }
        
        @Test
        public void testProcessCommand_1pt()
        {
            Helper helper = new Helper();
            String gridOne = grid.processCommand("A1 = \"oNe\"");
            helper.setItem(0, 0, "oNe");
            assertEquals("grid with one string cell", helper.getText(), gridOne);
            String accessorOne = grid.getGridText();
            assertEquals("grid from accessor with one string cell", helper.getText(), accessorOne);
            String gridTwo = grid.processCommand("L20 = \"TWo\"");
            helper.setItem(19, 11, "TWo");
            assertEquals("grid from accessor with two string cells", helper.getText(), gridTwo);
            String gridOnlyTwo = grid.processCommand("clear A1");
            helper.setItem(0, 0, "");
            assertEquals("grid with only the second string cell", helper.getText(), gridOnlyTwo);
            String gridEmpty = grid.processCommand("clear");
            helper.setItem(19, 11, "");
            assertEquals("empty grid", helper.getText(), gridEmpty);
        }
        
        @Test
        public void testProcessCommandSpecialStrings_1pt()
        {
            String stringSpecial1 = "A1 = ( avg A2-A3 )";
            String stringSpecial2 = "A1 = ( 1 * 2 / 1 + 3 - 5 )";
            Helper helper = new Helper();
            String grid1 = grid.processCommand("B7 = \"" + stringSpecial1 + "\"");
            helper.setItem(6, 1, stringSpecial1);
            assertEquals("grid with one special string", helper.getText(), grid1);
            String grid2 = grid.processCommand("F13 = \"" + stringSpecial2 + "\"");
            helper.setItem(12, 5, stringSpecial2);
            assertEquals("grid with two special strings", helper.getText(), grid2);
            String inspectedSpecial1 = grid.getCell(new TestLocation(6,1)).fullCellText();
            assertEquals("inspected first special string", "\"" + stringSpecial1 + "\"", inspectedSpecial1);
            String inspectedSpecial2 = grid.getCell(new TestLocation(12,5)).fullCellText();
            assertEquals("inspected second special string", "\"" + stringSpecial2 + "\"", inspectedSpecial2);
        }

        @Test
        public void testLongStringCellNoSpaces_halfpt()
        {
            String greeting = "ThisIsALongString";
            grid.processCommand("L2 = \"" + greeting + "\"");
            Cell greetingCell = grid.getCell(new TestLocation(1,11));
            assertEquals("greeting cell text", Helper.format(greeting), greetingCell.abbreviatedCellText());
            assertEquals("greeting inspection text", "\"" + greeting + "\"", greetingCell.fullCellText());
        }

        @Test
        public void testLowerCaseCellAssignment_halfpt()
        {
            String text = "Cell";
            grid.processCommand("b5 = \"" + text + "\"");
            Cell cell = grid.getCell(new TestLocation(4, 1));
            assertEquals("cell text", Helper.format(text), cell.abbreviatedCellText());
            assertEquals("inspection text", "\"" + text + "\"", cell.fullCellText());
            String processText = grid.processCommand("b5");
            assertEquals("processed inspection text", "\"" + text + "\"", processText);
            String processText2 = grid.processCommand("B5");
            assertEquals("processed inspection text 2", "\"" + text + "\"", processText2);
        }
        
        @Test
        public void testLowerCaseCellProcessInspection_halfpt()
        {
            grid.processCommand("B2 = \"\"");
            String processText = grid.processCommand("b2");
            assertEquals("processed inspection text", "\"\"", processText);
            grid.processCommand("c18 = \"3.1410\"");
            String processText2 = grid.processCommand("c18");
            assertEquals("processed inspection text 2", "\"3.1410\"", processText2);
        }
        
        @Test
        public void testMixedCaseClear_1pt()
        {
            grid.processCommand("A1 = \"first\"");
            grid.processCommand("D8 = \"second\"");
            grid.processCommand("CleaR");
            Cell cellFirst = grid.getCell(new TestLocation(0,0));
            Cell cellSecond = grid.getCell(new TestLocation(7, 3));
            assertEquals("cellFirst inspection text after clear", "", cellFirst.fullCellText());
            assertEquals("cellSecond inspection text after clear", "", cellSecond.fullCellText());
        }
        
        @Test
        public void textNonliteralClear_1pt()
        {
            String clear = " clear ".trim();
            grid.processCommand("A1 = \"first\"");
            grid.processCommand("D8 = \"second\"");
            grid.processCommand(clear);
            Cell cellFirst = grid.getCell(new TestLocation(0,0));
            Cell cellSecond = grid.getCell(new TestLocation(7, 3));
            assertEquals("cellFirst inspection text after clear", "", cellFirst.fullCellText());
            assertEquals("cellSecond inspection text after clear", "", cellSecond.fullCellText());
            String finalGrid = grid.getGridText();
            Helper th = new Helper();
            String emptyGrid = th.getText();
            assertEquals("empty grid", emptyGrid, finalGrid);
        }
        
        @Test
        public void testMixedCaseClearLocation_1pt()
        {
            grid.processCommand("A18 = \"first\"");
            grid.processCommand("D8 = \"second\"");
            grid.processCommand("clEAr a18");
            Cell cellFirst = grid.getCell(new TestLocation(17,0));
            Cell cellSecond = grid.getCell(new TestLocation(7, 3));
            assertEquals("cellFirst inspection text after clear", "", cellFirst.fullCellText());
            assertEquals("cellSecond inspection text after clear", "\"second\"", cellSecond.fullCellText());
            String processedCleared = grid.processCommand("A18");
            assertEquals("processed inspection after clear", "", processedCleared);
        }
        
        @Test
        public void testProcessCommandMoreSpecialStrings_1pt()
        {
            String[] specialStrings = new String[] { "clear", "(", " = ", "5", "4.3", "12/28/1998", "A1 = ( 1 / 1 )", "A20 = 1/1/2000", "A9 = 4.3", "abcdefgh", "abcdefghi", "abcdefghijk" };
            
            Helper helper = new Helper();
            for (int col = 0; col < specialStrings.length; col++)
            {
                for (int row = 5; row < 20; row += 10)
                {
                    String cellName = Character.toString((char)('A' + col)) + (row + 1);
                    helper.setItem(row,  col, specialStrings[col]);
                    String sheet = grid.processCommand(cellName + " = \"" + specialStrings[col] + "\"");
                    assertEquals("grid after setting cell " + cellName, helper.getText(), sheet);
                    String inspected = grid.getCell(new TestLocation(row, col)).fullCellText();
                    assertEquals("inspected cell " + cellName, "\"" + specialStrings[col] + "\"", inspected);
                }
            }
            assertEquals("final sheet", helper.getText(), grid.getGridText());
        }
        
        
        @Test
        public void testAreEmptyCells_halfpt()
        {
        	grid = new Spreadsheet();
            for (int i = 0; i < grid.getRows(); i++)
                for (int j = 0; j < grid.getCols(); j++)
                {
                    Cell cell = grid.getCell(new TestLocation(i, j));
                    assertEquals("Spreadsheet should contain all EmptyCells", cell.getClass().getName(), "textExcel.EmptyCell");
                }
        }
        
        @Test
        public void testTextCell_halfpt()
        {
            grid.processCommand("C5 = \"hi\"");
            Cell cell = grid.getCell(new TestLocation(4, 2));
            assertEquals("Should be of type StringCell", cell.getClass().getName(), "textExcel.TextCell");
        }
             
        
    }
    
    public static class Final
    {
        // Tests for final submission for Part A
        // Note these must also pass for all subsequent checkpoints including final project.
        Grid grid;
        
        @Before
        public void initializeGrid()
        {
            grid = new Spreadsheet();
        }
        
        @Test
        public void testPercentCell_1pt()
        {
            String percent = "11.25%";
            grid.processCommand("A1 = " + percent);
            Cell percentCell = grid.getCell(new TestLocation(0,0));
            assertEquals("percent cell text", "11%", percentCell.abbreviatedCellText().trim());
            assertEquals("percent inspection text", "0.1125", percentCell.fullCellText());
        }
                       
        @Test
        public void testBasicRealCell_1pt()
        {
            String real = "3.14";
            grid.processCommand("D18 = " + real);
            Cell realCell = grid.getCell(new TestLocation(17, 3));
            assertEquals("real cell text", Helper.format(real), realCell.abbreviatedCellText());
            assertEquals("real inspection text", real, realCell.fullCellText());
        }
        
        @Test
        public void testMoreRealCells_1pt()
        {
            String zero = "0.0";
            grid.processCommand("A1 = " + zero);
            Cell zeroCell = grid.getCell(new TestLocation(0, 0));
            assertEquals("real cell 0", Helper.format(zero), zeroCell.abbreviatedCellText());
            assertEquals("real inspection 0", zero, zeroCell.fullCellText());
            String negativeTwo = "-2.0";
            grid.processCommand("B1 = " + negativeTwo);
            Cell negativeTwoCell = grid.getCell(new TestLocation(0, 1));
            assertEquals("real cell -2", Helper.format(negativeTwo), negativeTwoCell.abbreviatedCellText());
            assertEquals("real inspection -2", negativeTwo, negativeTwoCell.fullCellText());
        }
        
        @Test
        public void testDifferentCellTypes_3pt()
        {
            grid.processCommand("H4 = 12.281998%");
            grid.processCommand("G3 = \"5\"");
            grid.processCommand("F2 = -123.456");
            Cell dateCell = grid.getCell(new TestLocation(3, 7));
            Cell stringCell = grid.getCell(new TestLocation(2, 6));
            Cell realCell = grid.getCell(new TestLocation(1, 5));
            Cell emptyCell = grid.getCell(new TestLocation(0, 4));
            Cell[] differentCells = { dateCell, stringCell, realCell, emptyCell };
            for (int i = 0; i < differentCells.length - 1; i++)
            {
                for (int j = i + 1; j < differentCells.length; j++)
                {
                    assertTrue("percent, string, real, empty cells must all have different class types",
                            !differentCells[i].getClass().equals(differentCells[j].getClass()));
                }
            }
        }
        
        @Test
        public void testFormulaAssignment_3pt()
        {
            for (int row = 1; row < 11; row++)
            {
                for (int col = 1; col < 7; col++)
                {
                    String cellName = Character.toString((char)('A' + col)) + (row + 1);
                    grid.processCommand(cellName + " = 1");
                }
            }
            String formula1 = "( 4 * 5.5 / 2 + 1 - -11.5 )";
            String formula2 = "( sUm B6-g11 )";
            String formula3 = "( AvG f8-F9 )";
            grid.processCommand("K9 = " + formula1);
            grid.processCommand("J10 = " + formula2);
            grid.processCommand("I11 = " + formula3);
            Cell cell1 = grid.getCell(new TestLocation(8, 10));
            Cell cell2 = grid.getCell(new TestLocation(9, 9));
            Cell cell3 = grid.getCell(new TestLocation(10, 8));
            assertEquals("cell length 1", 10, cell1.abbreviatedCellText().length());
            assertEquals("inspection 1", formula1, cell1.fullCellText());
            assertEquals("cell length 2", 10, cell2.abbreviatedCellText().length());
            assertEquals("inspection 2", formula2, cell2.fullCellText());
            assertEquals("cell length 3", 10, cell3.abbreviatedCellText().length());
            assertEquals("inspection 3", formula3, cell3.fullCellText());
        }

        @Test
        public void testProcessCommand_1pt()
        {
            Helper helper = new Helper();
            String first = grid.processCommand("A1 = 1.021822%");
            helper.setItem(0, 0, "1%");
            assertEquals("grid with date", helper.getText(), first);
            String second = grid.processCommand("B2 = -5");
            helper.setItem(1, 1, "-5.0");
            assertEquals("grid with date and number", helper.getText(), second);
            String third = grid.processCommand("C3 = 2.718");
            helper.setItem(2, 2, "2.718");
            assertEquals("grid with date and two numbers", helper.getText(), third);
            String fourth = grid.processCommand("D4 = 0");
            helper.setItem(3, 3, "0.0");
            assertEquals("grid with date and three numbers", helper.getText(), fourth);
        }

        @Test
        public void testRealCellFormat_2pt()
        {
            // NOTE spec not totally consistent on inspection format, allow anything that parses to within epsilon of as entered
            String[] realsEntered = { "3.00", "-74.05000", "400", "400.0" };
            String[] realsFormatted = { "3.0       ", "-74.05    ", "400.0     ", "400.0     " };
            Helper helper = new Helper();
            for (int col = 0; col < realsEntered.length; col++)
            {
                for (int row = 6; row < 20; row += 10)
                {
                    String cellName = Character.toString((char)('A' + col)) + (row + 1);
                    String sheet = grid.processCommand(cellName + " = " + realsEntered[col]);
                    helper.setItem(row,  col, realsFormatted[col]);
                    assertEquals("sheet after setting cell " + cellName, helper.getText(), sheet);
                    String inspected = grid.getCell(new TestLocation(row, col)).fullCellText();
                    double expected = Double.parseDouble(realsEntered[col]);
                    double actual = Double.parseDouble(inspected);
                    assertEquals("inspected real value", actual, expected, 1e-6);
                }
            }
            assertEquals("final sheet", helper.getText(), grid.getGridText());
        }

        @Test
        public void testRealCellTruncation_1pt()
        {
            String big = "-9876543212345";
            grid.processCommand("A1 = " + big);
            Cell bigCell = grid.getCell(new TestLocation(0, 0));
            assertEquals("real big cell length", 10, bigCell.abbreviatedCellText().length());
            assertEquals("real big inspection ", Double.parseDouble(big), Double.parseDouble(bigCell.fullCellText()), 1e-6);
            
            String precise = "3.14159265358979";
            grid.processCommand("A2 = " + precise);
            Cell preciseCell = grid.getCell(new TestLocation(1, 0));
            assertEquals("real precise cell length", 10, preciseCell.abbreviatedCellText().length());
            assertEquals("real precise cell", Double.parseDouble(precise), Double.parseDouble(preciseCell.abbreviatedCellText()), 1e-6);
            assertEquals("real precise inspection ", Double.parseDouble(precise), Double.parseDouble(preciseCell.fullCellText()), 1e-6);
            
            String moderate = "123456";
            grid.processCommand("A3 = " + moderate);
            Cell moderateCell = grid.getCell(new TestLocation(2, 0));
            assertEquals("real moderate cell length", 10, moderateCell.abbreviatedCellText().length());
            assertEquals("real moderate cell", moderate + ".0", moderateCell.abbreviatedCellText().trim());
            assertEquals("real moderate inspection", moderate, moderateCell.fullCellText());
            
            String precisePerc = "7.87878%";
            grid.processCommand("A4 = " + precisePerc);
            Cell precisePerCell = grid.getCell(new TestLocation(3, 0));
            assertEquals("real precise percent cell length", 10, precisePerCell.abbreviatedCellText().length());
            assertEquals("real precise percent cell", "7%", precisePerCell.abbreviatedCellText().trim());
            assertEquals("real precise percent inspection", "0.0787878", precisePerCell.fullCellText());
        }        
        
        @Test
        public void testDifferentCellTypeNames_2pt()
        {
        	grid.processCommand("F1 = \"-He/ll()o\"");  //Text
            grid.processCommand("F2 = -5.2%");	   	    //Percent
            grid.processCommand("f3 = -3.141592654");   //Value
            grid.processCommand("F4 = 10");   
            grid.processCommand("F5 = 20");            
            grid.processCommand("F6 = ( AVG F4-F5 )");  //Formula
            
            Cell cell = grid.getCell(new TestLocation(0,5));
            assertEquals("Text should be TextCell", cell.getClass().getName(), "textExcel.TextCell");
            cell = grid.getCell(new TestLocation(1,5));
            assertEquals("% should be PercentCell", cell.getClass().getName(), "textExcel.PercentCell");
            cell = grid.getCell(new TestLocation(2,5));
            assertEquals("Value should be ValueCell", cell.getClass().getName(), "textExcel.ValueCell");
            cell = grid.getCell(new TestLocation(5,5));
            assertEquals("Formula should be FormulaCell", cell.getClass().getName(), "textExcel.FormulaCell");
        }
                
    }
       
    public static class ExtraCreditCommandErrors
    {
        // Tests for command errors extra credit
        Grid grid;
        
        @Before
        public void initializeGrid()
        {
            grid = new Spreadsheet();
        }
        
        @Test
        public void testInvalidCommand()
        {
            String before = grid.processCommand("A1 = \"thrang\"");
            String error = grid.processCommand("lesnerize");
            String after = grid.getGridText();
            assertTrue("error message starts with ERROR: ", error.startsWith("ERROR: "));
            assertEquals("grid contents unchanged", before, after);
        }
        
        @Test
        public void testInvalidCellAssignment_1pt()
        {
            String before = grid.processCommand("A1 = \"hello\"");
            String error1 = grid.processCommand("A37 = 5");
            String error2 = grid.processCommand("M1 = 3");
            String error3 = grid.processCommand("A-5 = 2");
            String error4 = grid.processCommand("A0 = 17");
            String error5 = grid.processCommand("M1 = 18");
            String error6 = grid.processCommand("A21 = 18");
            String error7 = grid.processCommand("A");
            String error8 = grid.processCommand("8");
            String error9 = grid.processCommand("9A");
            String error10 = grid.processCommand("= 234");
            String error11 = grid.processCommand("A2 = %234.3%");
            String error12 = grid.processCommand("A3 = 1.2.3");
            
            String after = grid.getGridText();
            assertTrue("error1 message starts with ERROR: ", error1.startsWith("ERROR: "));
            assertTrue("error2 message starts with ERROR: ", error2.startsWith("ERROR: "));
            assertTrue("error3 message starts with ERROR: ", error3.startsWith("ERROR: "));
            assertTrue("error4 message starts with ERROR: ", error4.startsWith("ERROR: "));
            assertTrue("error5 message starts with ERROR: ", error5.startsWith("ERROR: "));
            assertTrue("error6 message starts with ERROR: ", error6.startsWith("ERROR: "));
            assertTrue("error7 message starts with ERROR: ", error7.startsWith("ERROR: "));
            assertTrue("error8 message starts with ERROR: ", error8.startsWith("ERROR: "));
            assertTrue("error9 message starts with ERROR: ", error9.startsWith("ERROR: "));
            assertTrue("error10 message starts with ERROR: ", error10.startsWith("ERROR: "));
            assertTrue("error11 message starts with ERROR: ", error11.startsWith("ERROR: "));
            assertTrue("error12 message starts with ERROR: ", error12.startsWith("ERROR: "));
            
            assertEquals("grid contents unchanged", before, after);
        }
        
        @Test
        public void testInvalidConstants_2pt()
        {
            String before = grid.processCommand("A1 = \"hello\"");
            String error1 = grid.processCommand("A2 = 5...");
            String error2 = grid.processCommand("A3 = 4p");
            String error3 = grid.processCommand("A4 = \"he");
            String error4 = grid.processCommand("A5 = 1/2/aughtfour");
            String error5 = grid.processCommand("A6 = *9");
            String after = grid.getGridText();
            assertTrue("error1 message starts with ERROR: ", error1.startsWith("ERROR: "));
            assertTrue("error2 message starts with ERROR: ", error2.startsWith("ERROR: "));
            assertTrue("error3 message starts with ERROR: ", error3.startsWith("ERROR: "));
            assertTrue("error4 message starts with ERROR: ", error4.startsWith("ERROR: "));
            assertTrue("error5 message starts with ERROR: ", error5.startsWith("ERROR: "));
            assertEquals("grid contents unchanged", before, after);
        }
        
        @Test
        public void testInvalidFormulaAssignment_1pt()
        {
            grid.processCommand("A1 = 1");
            String before = grid.processCommand("A2 = 2");
            String error1 = grid.processCommand("A3 = 5 + 2");
            String error2 = grid.processCommand("A4 = ( avs A1-A2 )");
            String error3 = grid.processCommand("A5 = ( sum A0-A2 )");
            String error4 = grid.processCommand("A6 = ( 1 + 2");
            String error5 = grid.processCommand("A7 = ( avg A1-B )");
            String error6 = grid.processCommand("A8 = M80");
            String after = grid.getGridText();
            assertTrue("error1 message starts with ERROR: ", error1.startsWith("ERROR: "));
            assertTrue("error2 message starts with ERROR: ", error2.startsWith("ERROR: "));
            assertTrue("error3 message starts with ERROR: ", error3.startsWith("ERROR: "));
            assertTrue("error4 message starts with ERROR: ", error4.startsWith("ERROR: "));
            assertTrue("error5 message starts with ERROR: ", error5.startsWith("ERROR: "));
            assertTrue("error6 message starts with ERROR: ", error6.startsWith("ERROR: "));
            assertEquals("grid contents unchanged", before, after);
        }
        
        @Test
        public void testWhitespaceTolerance_1pt()
        {
            // OK to either treat as error or as valid, just don't crash
            String before = grid.getGridText();
            grid.processCommand("L20=5");
            grid.processCommand(" A1  =     -14 ");
            grid.processCommand("A1=-14");
            grid.processCommand("A1=(3+5*4/2)");
            grid.processCommand("A1=(sum L20-L20)");
            grid.processCommand("clear    A1");
            String after = grid.processCommand("clear");
            assertEquals("end with empty grid", before, after);
        }
    }
    
    public static class ExtraCreditCommandHistory
    {
        // Tests for command history extra credit
        Grid grid;
        
        @Before
        public void initializeGrid()
        {
            grid = new Spreadsheet();
        }
        
        @Test
        public void testEmptyCommandHistory_halfpt()
        {
        	startHistory(3);
        	
        	checkHistory(new String[]{});

        	stopHistory();
        }
        
        @Test
        public void testPartiallyEmptyCommandHistory_halfpt()
        {
        	startHistory(5);
        	
        	executeCommands(new String[]
        	{
        		"A1 = 5",
        		"A2 = \"Test\""
        	});
        	
        	checkHistory(new String[]
        	{
        		"A2 = \"Test\"",
        		"A1 = 5"
        	});
        	
        	executeCommands(new String[]
        	{
        		"clear A1",
        		"A3 = 10"
        	});
        	
        	checkHistory(new String[]
        	{
            	"A3 = 10",
            	"clear A1",            		
        		"A2 = \"Test\"",
        		"A1 = 5"
        	});
        	
        	stopHistory();   	
        }
        
        @Test
        public void testFullCommandHistory_1pt()
        {
        	startHistory(6);
        	
        	executeCommands(new String[]
        	{
        		"A1 = 5",
        		"A2 = \"Test\"",
        		"clear A1"
        	});
        	
        	checkHistory(new String[]
        	{
               	"clear A1",
               	"A2 = \"Test\"",
            	"A1 = 5",
        	});
        	
        	executeCommands(new String[]
        	{
            	"A1 = 6",
            	"B1 = 7",
            	"C1 = 8"
        	});
        	
        	checkHistory(new String[]
        	{
            	"C1 = 8",
               	"B1 = 7",
               	"A1 = 6",
               	"clear A1",
               	"A2 = \"Test\"",
            	"A1 = 5",
        	});
        	
        	stopHistory();   	
        }
        
        @Test
        public void testOverflowingCommandHistory_1pt()
        {
        	startHistory(4);
        	
        	executeCommands(new String[]
        	{
        		"A1 = 10",
        		"A2 = \"Test2\"",
        		"clear A2"
        	});
        	
        	checkHistory(new String[]
        	{
               	"clear A2",
               	"A2 = \"Test2\"",
            	"A1 = 10",
        	});
        	
        	executeCommands(new String[]
        	{
            	"A1 = 60",
            	"B1 = 70",
            	"C1 = 80"
        	});
        	
        	checkHistory(new String[]
        	{
            	"C1 = 80",
               	"B1 = 70",
               	"A1 = 60",
               	"clear A2"
        	});

        	executeCommands(new String[]
        	{
            	"clear B1",
            	"clear C1"
        	});

        	
        	checkHistory(new String[]
        	{
        		"clear C1",
        		"clear B1",
            	"C1 = 80",
               	"B1 = 70"
        	});
        	
        	stopHistory();   	
        }
        
        @Test
        public void testClearHistory_1pt()
        {
        	startHistory(5);

        	executeCommands(new String[]
        	{
        		"A1 = 8",
        		"A2 = \"Test\"",
        		"clear A1",
        		"clear A2"
        	});
        	
        	checkHistory(new String[]
			{
            	"clear A2",
        		"clear A1",
        		"A2 = \"Test\"",
        		"A1 = 8"			
			});
        	
        	clearHistory(2);
        	
        	checkHistory(new String[]
			{
                "clear A2",
            	"clear A1",		
			});
        	
        	clearHistory(1);
        	
        	checkHistory(new String[]
			{
                "clear A2"		
			});
        	
        	clearHistory(1);
        	
        	checkHistory(new String[] 
        	{
        	});
        	
        	executeCommands(new String[]
        	{
        		"C1 = 8",
        		"D2 = \"Test\"",
        		"clear C1",
        		"clear D2",
        		"C1 = 20",
        		"E1 = 40",
        		"F3 = 60"
        	});

        	checkHistory(new String[] 
        	{
            	"F3 = 60",
            	"E1 = 40",
            	"C1 = 20",
            	"clear D2",	
        		"clear C1"	
        	});
        	
        	clearHistory(3);

        	checkHistory(new String[] 
        	{
            	"F3 = 60",
            	"E1 = 40"	
        	});
        
        	executeCommands(new String[]
			{
        		"A5 = 6",
        		"A6 = 7",
        		"A7 = 8",
        		"A8 = 9"
			});

        	checkHistory(new String[] 
        	{
            	"A8 = 9",
            	"A7 = 8",
            	"A6 = 7",
            	"A5 = 6",
            	"F3 = 60",
        	});
        	
        	clearHistory(2);


        	checkHistory(new String[] 
        	{
            	"A8 = 9",
            	"A7 = 8",
            	"A6 = 7"
        	});
        	
        	clearHistory(5);
        	checkHistory(new String[] 
        	{
        	});
        	
        	stopHistory();
        }
        
        @Test
        public void testAllOnSameSheet_1pt()
        {
        	testClearHistory_1pt();
        	
        	grid.processCommand("A1 = 1");
        	
        	testEmptyCommandHistory_halfpt();
        	
        	grid.processCommand("B2 = 2");
        	
        	testPartiallyEmptyCommandHistory_halfpt();
        	
        	grid.processCommand("C3 = 3");
        	
        	testFullCommandHistory_1pt();
        	
        	grid.processCommand("D4 = 4");
        	
        	testOverflowingCommandHistory_1pt();
        }
        
        private void startHistory(int historySize)
        {
        	String historyStart = grid.processCommand("history start " + historySize);
        	assertEquals("", historyStart);
        }
        
        private void executeCommands(String[] commands)
        {
        	for (String command : commands)
        	{
        		String output = grid.processCommand(command);
        		if (command.startsWith("history"))
        		{
        			assertEquals("", output);
        		}
        	}
        }
        
        private void checkHistory(String[] expectedHistory)
        {          	
        	String historyDisplay = grid.processCommand("history display");
        	if (historyDisplay.equals(""))
        	{
        		assertEquals(0, expectedHistory.length);
        	}
        	else
        	{
	        	String[] historyCommands = historyDisplay.split("\n");
	        	
	        	assertTrue(Arrays.equals(expectedHistory, historyCommands));
        	}
        }
        
        private void clearHistory(int numToClear)
        {
        	String historyClear = grid.processCommand("history clear " + numToClear);
        	assertEquals("", historyClear);
        }
        
        private void stopHistory()
        {
        	String historyStop = grid.processCommand("history stop");
        	assertEquals("", historyStop);
        }
    }
}
