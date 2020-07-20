package org.sijbesma.bp.bpbrewingstandlib.datatypes;

public enum SlotType {
	FUEL(4), INGREDIENT(3), BOTTLE(0|1|2);
	
	private int value;
	
	SlotType(int value) {
		this.value = value;
	}

	public static SlotType valueOf(int value) {
		switch(value) {
		case 0:
		case 1:
		case 2:
			return SlotType.BOTTLE;
		case 3:
			return SlotType.INGREDIENT;
		case 4:
			return SlotType.FUEL;
		default:
			return null;
		}
	}
}
