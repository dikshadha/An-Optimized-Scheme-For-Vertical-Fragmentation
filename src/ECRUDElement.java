/**
 * @author vivek(16CS07F) and sourabh(16CS21F)
 */

public class ECRUDElement {
	CRUD[] element;

	ECRUDElement(int numberOfApplications) {
		element = new CRUD[numberOfApplications];
		for (int i = 0; i < numberOfApplications; i++)
			element[i] = new CRUD();
	}

	ECRUDElement() {
	}

	public String toString() {

		String r = "";
		for (int i = 0; i < element.length; i++) {
			r += element[i].toString();
		}
		return r;
	}

	public int getManipulateValue() {

		int m = 0;
		for (int i = 0; i < element.length; i++) {
			m += element[i].manipulateCount;
		}
		return m;
	}

	public int getReadValue() {
		int r = 0;
		for (int i = 0; i < element.length; i++) {
			r += element[i].readCount;
		}
		return r;
	}
}
