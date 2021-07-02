package model;

public enum sortType {
	eA_TO_Z, eZ_TO_A, eINSERT_ORDER;

	public static sortType getSortType(String name) {
		sortType st = null;
		switch (name) {
		case "eA_TO_Z":
		case "Ascending Order":
			st = eA_TO_Z;
			break;
		case "eZ_TO_A":
		case "Descending Order":
			st = eZ_TO_A;
			break;

		case "eINSERT_ORDER":
		case"Insert Order":
			st = eINSERT_ORDER;
			break;
		}
		return st;
	}
}
