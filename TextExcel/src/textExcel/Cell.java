package textExcel;

//*******************************************************
// DO NOT MODIFY THIS FILE!!!
//*******************************************************

// Cell interface, must be implemented by your cell classes (ValueCell, PercentCell, etc...)
public interface Cell
{
	public String abbreviatedCellText(); 		// text for spreadsheet cell display, must be exactly length 10
	public String fullCellText(); 				// text for individual cell inspection, not truncated or padded
}
