package com.techmeridian.stockmanagment.testnav;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class PhyInvJournal implements KvmSerializable {

	private String Journal_Template_Name;
	private String Journal_Batch_Name;
	private Integer Line_No;
	private String Posting_Date;
	private String Document_Date;
	private String Entry_Type;
	private String Document_No;
	private String Item_No;
	private String Variant_Code;
	private String Description;
	private String Shortcut_Dimension_1_Code;
	private String Shortcut_Dimension_2_Code;
	private String ShortcutDimCode_x005B_3_x005D_;
	private String ShortcutDimCode_x005B_4_x005D_;
	private String ShortcutDimCode_x005B_5_x005D_;
	private String ShortcutDimCode_x005B_6_x005D_;
	private String ShortcutDimCode_x005B_7_x005D_;
	private String ShortcutDimCode_x005B_8_x005D_;
	private String Location_Code;
	private String Bin_Code;
	private String Salespers_Purch_Code;
	private String Gen_Bus_Posting_Group;
	private String Gen_Prod_Posting_Group;
	private Integer Qty_Calculated;
	private Integer Qty_Phys_Inventory;
	private Integer Quantity;
	private String Unit_of_Measure_Code;
	private Integer Unit_Amount;
	private Integer Amount;
	private Integer Indirect_Cost_Percent;
	private Integer Unit_Cost;
	private Integer Applies_to_Entry;
	private String Reason_Code;
	private String ItemDescription;

	public PhyInvJournal(String Entry_Type, String Document_No, String Item_No, String Description,
			Integer Qty_Calculated, Integer Qty_Phys_Inventory, Integer Quantity, String Unit_of_Measure_Code,
			String ItemDescription, String Journal_Batch_Name, String Journal_Template_Name, Integer Line_No) {
		this.Entry_Type = Entry_Type;
		this.Document_No = Document_No;
		this.Item_No = Item_No;
		this.Description = Description;
		this.Qty_Calculated = Qty_Calculated;
		this.Qty_Phys_Inventory = Qty_Phys_Inventory;
		this.Quantity = Quantity;
		this.Unit_of_Measure_Code = Unit_of_Measure_Code;
		this.ItemDescription = ItemDescription;
		this.Journal_Batch_Name = Journal_Batch_Name;
		this.Journal_Template_Name = Journal_Template_Name;
		this.Line_No = Line_No;
	}

	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return Journal_Template_Name;
		case 1:
			return Journal_Batch_Name;
		case 2:
			return Line_No;
		case 3:
			return Posting_Date;
		case 4:
			return Document_Date;
		case 5:
			return Entry_Type;
		case 6:
			return Document_No;
		case 7:
			return Item_No;
		case 8:
			return Variant_Code;
		case 9:
			return Description;
		case 10:
			return Shortcut_Dimension_1_Code;
		case 11:
			return Shortcut_Dimension_2_Code;
		case 12:
			return ShortcutDimCode_x005B_3_x005D_;
		case 13:
			return ShortcutDimCode_x005B_4_x005D_;
		case 14:
			return ShortcutDimCode_x005B_5_x005D_;
		case 15:
			return ShortcutDimCode_x005B_6_x005D_;
		case 16:
			return ShortcutDimCode_x005B_7_x005D_;
		case 17:
			return ShortcutDimCode_x005B_8_x005D_;
		case 18:
			return Location_Code;
		case 19:
			return Bin_Code;
		case 20:
			return Salespers_Purch_Code;
		case 21:
			return Gen_Bus_Posting_Group;
		case 22:
			return Gen_Prod_Posting_Group;
		case 23:
			return Qty_Calculated;
		case 24:
			return Qty_Phys_Inventory;
		case 25:
			return Quantity;
		case 26:
			return Unit_of_Measure_Code;
		case 27:
			return Unit_Amount;
		case 28:
			return Amount;
		case 29:
			return Indirect_Cost_Percent;
		case 30:
			return Unit_Cost;
		case 31:
			return Applies_to_Entry;
		case 32:
			return Reason_Code;
		case 33:
			return ItemDescription;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 34;
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			Journal_Template_Name = value != null ? value.toString() : null;
			break;
		case 1:
			Journal_Batch_Name = value != null ? value.toString() : null;
			break;
		case 2:
			Line_No = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 3:
			Posting_Date = value != null ? value.toString() : null;
			break;
		case 4:
			Document_Date = value != null ? value.toString() : null;
			break;
		case 5:
			Entry_Type = value != null ? value.toString() : null;
			break;
		case 6:
			Document_No = value != null ? value.toString() : null;
			break;
		case 7:
			Item_No = value != null ? value.toString() : null;
			break;
		case 8:
			Variant_Code = value != null ? value.toString() : null;
			break;
		case 9:
			Description = value != null ? value.toString() : null;
			break;
		case 10:
			Shortcut_Dimension_1_Code = value != null ? value.toString() : null;
			break;
		case 11:
			Shortcut_Dimension_2_Code = value != null ? value.toString() : null;
			break;
		case 12:
			ShortcutDimCode_x005B_3_x005D_ = value != null ? value.toString() : null;
			break;
		case 13:
			ShortcutDimCode_x005B_4_x005D_ = value != null ? value.toString() : null;
			break;
		case 14:
			ShortcutDimCode_x005B_5_x005D_ = value != null ? value.toString() : null;
			break;
		case 15:
			ShortcutDimCode_x005B_6_x005D_ = value != null ? value.toString() : null;
			break;
		case 16:
			ShortcutDimCode_x005B_7_x005D_ = value != null ? value.toString() : null;
			break;
		case 17:
			ShortcutDimCode_x005B_8_x005D_ = value != null ? value.toString() : null;
			break;
		case 18:
			Location_Code = value != null ? value.toString() : null;
			break;
		case 19:
			Bin_Code = value != null ? value.toString() : null;
			break;
		case 20:
			Salespers_Purch_Code = value != null ? value.toString() : null;
			break;
		case 21:
			Gen_Bus_Posting_Group = value != null ? value.toString() : null;
			break;
		case 22:
			Gen_Prod_Posting_Group = value != null ? value.toString() : null;
			break;
		case 23:
			Qty_Calculated = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 24:
			Qty_Phys_Inventory = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 25:
			Quantity = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 26:
			Unit_of_Measure_Code = value != null ? value.toString() : null;
			break;
		case 27:
			Unit_Amount = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 28:
			Amount = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 29:
			Indirect_Cost_Percent = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 30:
			Unit_Cost = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 31:
			Applies_to_Entry = (value != null ? Integer.parseInt(value.toString()) : 0);
			break;
		case 32:
			Reason_Code = value != null ? value.toString() : null;
			break;
		case 33:
			ItemDescription = value != null ? value.toString() : null;
			break;
		default:
			break;
		}
	}

	@Override
	public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Journal_Template_Name";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Journal_Batch_Name";
			break;
		case 2:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Line_No";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Posting_Date";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Document_Date";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Entry_Type";
			break;
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Document_No";
			break;
		case 7:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Item_No";
			break;
		case 8:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Variant_Code";
			break;
		case 9:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Description";
			break;
		case 10:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Shortcut_Dimension_1_Code";
			break;
		case 11:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Shortcut_Dimension_2_Code";
			break;
		case 12:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ShortcutDimCode_x005B_3_x005D_";
			break;
		case 13:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ShortcutDimCode_x005B_4_x005D_";
			break;
		case 14:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ShortcutDimCode_x005B_5_x005D_";
			break;
		case 15:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ShortcutDimCode_x005B_6_x005D_";
			break;
		case 16:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ShortcutDimCode_x005B_7_x005D_";
			break;
		case 17:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ShortcutDimCode_x005B_8_x005D_";
			break;
		case 18:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Location_Code";
			break;
		case 19:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Bin_Code";
			break;
		case 20:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Salespers_Purch_Code";
			break;
		case 21:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Gen_Bus_Posting_Group";
			break;
		case 22:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Gen_Prod_Posting_Group";
			break;
		case 23:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Qty_Calculated";
			break;
		case 24:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Qty_Phys_Inventory";
			break;
		case 25:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Quantity";
			break;
		case 26:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Unit_of_Measure_Code";
			break;
		case 27:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Unit_Amount";
			break;
		case 28:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Amount";
			break;
		case 29:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Indirect_Cost_Percent";
			break;
		case 30:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Unit_Cost";
			break;
		case 31:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Applies_to_Entry";
			break;
		case 32:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Reason_Code";
			break;
		case 33:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ItemDescription";
			break;
		default:
			break;
		}
	}
}
