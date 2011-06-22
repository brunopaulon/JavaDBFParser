package parser;

import java.util.Calendar;
import java.util.Date;

public class Utils {
	public String getFieldTypeFromByte(int number){
		switch (number) {
		case ((int) 'C'):
			return "Character";
		case ((int) 'Y'):
			return "Currency";
		case ((int) 'N'):
			return "Numeric";
		case ((int) 'F'):
			return "Float";
		case ((int) 'D'):
			return "Date";
		case ((int) 'T'):
			return "DateTime";
		case ((int) 'B'):
			return "Double";
		case ((int) 'I'):
			return "Integer";
		case ((int) 'L'):
			return "Logical";
		case ((int) 'M'):
			return "Memo";
		case ((int) 'G'):
			return "General";
		case ((int) 'P'):
			return "Picture";
		case ((int) '+'):
			return "Autoincrement (dBase Level 7)";
		case ((int) 'O'):
			return "Double (dBase Level 7)";
		case ((int) '@'):
			return "Timestamp (dBase Level 7)";
		default:
			return "Undefined code: " + number + ", in char: " + (new Character((char) number)).toString();
		}
	}
	
	public String getFieldFlagFromByte(byte flag) {
		switch (flag) {
		case 0:
			return "";
		case 1:
			return "System Column (not visible to user).";
		case 2:
			return "Column can store null values";
		case 4:
			return "Binary column (for CHAR and MEMO only)";
		case 6:
			return "When a field is NULL and binary (Integer, Currency, and Character/Memo fields)";
		case 12:
			return "Column is autoincrementing";
		default:
			return "Undefined code: " + flag;
		}
	}
	
	public String getDBFFileType(byte offset_0){
		String filetype = "";
		int number = (int) offset_0;
		switch (number) {
		case 2:
			// 0x02
			filetype = "FoxBASE";
			break;
		case 3:
			// 0x03
			filetype = "FoxBASE+/Dbase III plus, no memo";
			break;
		case 48:
			// 0x30
			filetype = "Visual FoxPro";
			break;
		case 49:
			// 0x31
			filetype = "Visual FoxPro, autoincrement enabled";
			break;
		case 50:
			// 0x32
			filetype = "Visual FoxPro with field type Varchar or Varbinary";
			break;
		case 67:
			// 0x43
			filetype = "dBASE IV SQL table files, no memo";
			break;
		case 99:
			// 0x63
			filetype = "dBASE IV SQL system files, with memo";
			break;
		case 131:
			// 0x83
			filetype = "FoxBASE+/dBASE III PLUS, with memo";
			break;
		case 139:
			// 0x8B
			filetype = "dBASE IV with memo";
			break;
		case 203:
			// 0xCB
			filetype = "dBASE IV SQL table files, with memo";
			break;
		case 245:
			// 0xF5
			filetype = "FoxPro 2.x(or earlier) with memo";
			break;
		case 229:
			// 0xE5
			filetype = "HiPer-Six format with SMT memo file";
			break;
		case 251:
			// 0xFB
			filetype = "FoxBASE";
			break;
		default:
			filetype = "Indeterminado: " + Integer.toString(((int) offset_0));
			break;
		}
		
		return filetype;
	}

	/*
	 * Intuitivamente:
	 * Os anos acima de 100 representam os anos 2000 pra cima...
	 * Os anos abaixo de 20 também representam os anos 2000 para cima...
	 * 
	 * O grande problema é que o byte do ano (byear) deveria representar apenas os dois ultimos digitos
	 * do ano e foram encontrados casos onde ele possuia valores 101 (que deve ser 2001)
	 * e 02 (que deve ser 2002)
	 */
	
	public Date getLastUpdate(byte byear, byte month, byte day){
		Calendar cal = Calendar.getInstance();
		
		int year;
		if(byear < 40)
			year = 2000 + byear;
		else 
			year = 1900 + byear;
		
		cal.set(year, month, day);
		return cal.getTime();
	}
	
	public String getTableFlag(byte flag){
		String flag_string = "";
		if(flag%2 == 1)
			flag_string += "File has a Structural .cdx. ";
		if(flag >= 4){
			flag_string += "File is a Database. ";
			flag = (byte) (flag -  4);
		}
		if(flag == 2 || flag == 3)
			flag_string += "File has a Memo field.";
		
		
		return flag_string;
	}
}
